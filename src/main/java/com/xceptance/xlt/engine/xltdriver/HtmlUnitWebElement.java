// Licensed to the Software Freedom Conservancy (SFC) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The SFC licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
//
// Copyright (c) 2005-2022 Xceptance Software Technologies GmbH

package com.xceptance.xlt.engine.xltdriver;

import static com.gargoylesoftware.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;
import static com.gargoylesoftware.htmlunit.html.DomElement.ATTRIBUTE_VALUE_EMPTY;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.Colors;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.DisabledElement;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInputElement;
import com.xceptance.xlt.engine.scripting.htmlunit.HtmlUnitElementUtils;

import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 *
 * @author Alexei Barantsev
 * @author Ahmed Ashour
 * @author Javier Neira
 * @author Ronald Brill
 * @author Andrei Solntsev
 * @author Martin Bartoš
 */
public class HtmlUnitWebElement implements WrapsDriver, WebElement, Coordinates, Locatable {

    private static final String[] booleanAttributes = {"async", "autofocus", "autoplay", "checked", "compact",
        "complete", "controls", "declare", "defaultchecked", "defaultselected", "defer", "disabled", "draggable",
        "ended", "formnovalidate", "hidden", "indeterminate", "iscontenteditable", "ismap", "itemscope", "loop",
        "multiple", "muted", "nohref", "noresize", "noshade", "novalidate", "nowrap", "open", "paused", "pubdate",
        "readonly", "required", "reversed", "scoped", "seamless", "seeking", "selected", "spellcheck", "truespeed",
        "willvalidate"};

    private final HtmlUnitDriver driver_;
    private final int id_;
    private final DomElement element_;

    private String toString_;

    public HtmlUnitWebElement(final HtmlUnitDriver driver, final int id, final DomElement element) {
        driver_ = driver;
        id_ = id;
        element_ = element;
    }

    @Override
    public void click() {
        // #2897 start
        /*
        verifyCanInteractWithElement(true);
        */
        verifyCanInteractWithElement(false);
        // #2897 end
        driver_.click(element_, true);
    }

    @Override
    public void submit() {
        driver_.submit(this);
    }

    void submitImpl() {
        try {
            if (element_ instanceof HtmlForm) {
                submitForm((HtmlForm) element_);
            }
            else if ((element_ instanceof HtmlSubmitInput) || (element_ instanceof HtmlImageInput)) {
                element_.click();
            }
            else if (element_ instanceof HtmlInput) {
                final HtmlForm form = ((HtmlElement) element_).getEnclosingForm();
                if (form == null) {
                    throw new JavascriptException("Unable to find the containing form");
                }
                submitForm(form);
            }
            else {
                final HtmlUnitWebElement form = findParentForm();
                if (form == null) {
                    throw new JavascriptException("Unable to find the containing form");
                }
                form.submitImpl();
            }
        }
        catch (final IOException e) {
            throw new WebDriverException(e);
        }
    }

    private void submitForm(final HtmlForm form) {
        assertElementNotStale();

        final List<HtmlElement> allElements = new ArrayList<>();
        allElements.addAll(form.getElementsByTagName("input"));
        allElements.addAll(form.getElementsByTagName("button"));

        HtmlElement submit = null;
        for (final HtmlElement e : allElements) {
            if (!isSubmitElement(e)) {
                continue;
            }

            if (submit == null) {
                submit = e;
            }
        }

        if (submit == null) {
            if (driver_.isJavascriptEnabled()) {
                final ScriptResult eventResult = form.fireEvent("submit");
                if (!ScriptResult.isFalse(eventResult)) {
                    driver_.executeScript("arguments[0].submit()", form);
                }
                return;
            }
            throw new WebDriverException("Cannot locate element used to submit form");
        }
        try {
            // this has to ignore the visibility like browsers are doing
            submit.click(false, false, false, true, true, true, false);
        }
        catch (final IOException e) {
            throw new WebDriverException(e);
        }
    }

    private static boolean isSubmitElement(final HtmlElement element) {
        HtmlElement candidate = null;

        if (element instanceof HtmlSubmitInput && !((HtmlSubmitInput) element).isDisabled()) {
            candidate = element;
        }
        else if (element instanceof HtmlImageInput && !((HtmlImageInput) element).isDisabled()) {
            candidate = element;
        }
        else if (element instanceof HtmlButton) {
            final HtmlButton button = (HtmlButton) element;
            if ("submit".equalsIgnoreCase(button.getTypeAttribute()) && !button.isDisabled()) {
                candidate = element;
            }
        }

        return candidate != null;
    }

    @Override
    public void clear() {
        assertElementNotStale();

        if (element_ instanceof HtmlInput) {
            final HtmlInput htmlInput = (HtmlInput) element_;
            if (htmlInput.isReadOnly()) {
                throw new InvalidElementStateException("You may only edit editable elements");
            }
            if (htmlInput.isDisabled()) {
                throw new InvalidElementStateException("You may only interact with enabled elements");
            }
            htmlInput.setValue("");
            if (htmlInput instanceof SelectableTextInput) {
                ((SelectableTextInput) htmlInput).setSelectionEnd(0);
            }
            htmlInput.fireEvent("change");
        }
        else if (element_ instanceof HtmlTextArea) {
            final HtmlTextArea htmlTextArea = (HtmlTextArea) element_;
            if (htmlTextArea.isReadOnly()) {
                throw new InvalidElementStateException("You may only edit editable elements");
            }
            if (htmlTextArea.isDisabled()) {
                throw new InvalidElementStateException("You may only interact with enabled elements");
            }
            htmlTextArea.setText("");
        }
        else if (!element_.getAttribute("contenteditable").equals(ATTRIBUTE_NOT_DEFINED)) {
            element_.setTextContent("");
        }
    }

    void verifyCanInteractWithElement(final boolean ignoreDisabled) {
        assertElementNotStale();

        final Boolean displayed = driver_.implicitlyWaitFor(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return isDisplayed();
            }
        });

        if (displayed == null || !displayed) {
            throw new ElementNotInteractableException("You may only interact with visible elements");
        }

        if (!ignoreDisabled && !isEnabled()) {
            throw new InvalidElementStateException("You may only interact with enabled elements");
        }
    }

    void switchFocusToThisIfNeeded() {
        // TODO: HA start (XLT#1227 / SEL#1521)
        /*
        final HtmlUnitWebElement oldActiveElement = (HtmlUnitWebElement) driver_.switchTo().activeElement();

        final boolean jsEnabled = driver_.isJavascriptEnabled();
        final boolean oldActiveEqualsCurrent = oldActiveElement.equals(this);
        try {
            final boolean isBody = oldActiveElement.getTagName().toLowerCase().equals("body");
            if (jsEnabled && !oldActiveEqualsCurrent && !isBody) {
                oldActiveElement.element_.blur();
            }
        }
        catch (final StaleElementReferenceException ex) {
            // old element has gone, do nothing
        }
        element_.focus();
        */
        // HA end (XLT#1227 / SEL#1521)
    }

    @Override
    public void sendKeys(final CharSequence... value) {
        if (value == null) {
            throw new IllegalArgumentException("Keys to send should nor be null");
        }
        driver_.sendKeys(this, value);
    }

    @Override
    public String getTagName() {
        assertElementNotStale();
        return element_.getNodeName();
    }

    @Override
    public String getAttribute(final String name) {
        assertElementNotStale();

        final String lowerName = name.toLowerCase();

        final String value = element_.getAttribute(name);

        if (element_ instanceof HtmlInput && ("selected".equals(lowerName) || "checked".equals(lowerName))) {
            return trueOrNull(((HtmlInput) element_).isChecked());
        }

        if ("href".equals(lowerName) || "src".equals(lowerName)) {
            final String link = element_.getAttribute(name);
            if (ATTRIBUTE_NOT_DEFINED == link) {
                return null;
            }
            final HtmlPage page = (HtmlPage) element_.getPage();
            try {
                return page.getFullyQualifiedUrl(link.trim()).toString();
            }
            catch (final MalformedURLException e) {
                return null;
            }
        }

        if ("disabled".equals(lowerName)) {
            if (element_ instanceof DisabledElement) {
                return trueOrNull(((DisabledElement) element_).isDisabled());
            }
            return "true";
        }

        if ("multiple".equals(lowerName) && element_ instanceof HtmlSelect) {
            final String multipleAttribute = ((HtmlSelect) element_).getMultipleAttribute();
            if ("".equals(multipleAttribute)) {
                return trueOrNull(element_.hasAttribute("multiple"));
            }
            return "true";
        }

        for (final String booleanAttribute : booleanAttributes) {
            if (booleanAttribute.equals(lowerName)) {
                return trueOrNull(element_.hasAttribute(lowerName));
            }
        }
        if ("index".equals(lowerName) && element_ instanceof HtmlOption) {
            final HtmlSelect select = ((HtmlOption) element_).getEnclosingSelect();
            final List<HtmlOption> allOptions = select.getOptions();
            for (int i = 0; i < allOptions.size(); i++) {
                final HtmlOption option = select.getOption(i);
                if (element_.equals(option)) {
                    return String.valueOf(i);
                }
            }

            return null;
        }

        if ("value".equals(lowerName)) {
            if (element_ instanceof HtmlFileInput) {
                return ((HTMLInputElement) element_.getScriptableObject()).getValue();
            }
            if (element_ instanceof HtmlTextArea) {
                return ((HtmlTextArea) element_).getText();
            }

            // According to
            // http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-value-OPTION
            // if the value attribute doesn't exist, getting the "value" attribute defers to
            // the
            // option's content.
            if (element_ instanceof HtmlOption && !element_.hasAttribute("value")) {
                return getText();
            }

            return value == null ? "" : value;
        }

        if (!value.isEmpty()) {
            return value;
        }

        if (element_.hasAttribute(name)) {
            return "";
        }

        final Object scriptable = element_.getScriptableObject();
        if (scriptable instanceof Scriptable) {
            final Object slotVal = ScriptableObject.getProperty((Scriptable) scriptable, name);
            if (slotVal instanceof String) {
                return (String) slotVal;
            }
        }

        return null;
    }

    @Override
    public String getDomProperty(final String name) {
        assertElementNotStale();

        final String lowerName = name.toLowerCase();
        final String value = element_.getAttribute(lowerName);
        if (ATTRIBUTE_NOT_DEFINED == value) {
            return null;
        }

        if ("disabled".equals(lowerName)) {
            if (element_ instanceof DisabledElement) {
                return trueOrNull(((DisabledElement) element_).isDisabled());
            }
        }

        if (ATTRIBUTE_VALUE_EMPTY == value) {
            return null;
        }

        return value;
    }

    @Override
    public String getDomAttribute(final String name) {
        assertElementNotStale();

        final String lowerName = name.toLowerCase();
        final String value = element_.getAttribute(lowerName);
        if (ATTRIBUTE_NOT_DEFINED == value) {
            return null;
        }

        if ("disabled".equals(lowerName)) {
            if (element_ instanceof DisabledElement) {
                return trueOrNull(((DisabledElement) element_).isDisabled());
            }
        }

        return value;
    }

    private static String trueOrNull(final boolean condition) {
        return condition ? "true" : null;
    }

    @Override
    public boolean isSelected() {
        assertElementNotStale();

        if (element_ instanceof HtmlInput) {
            return ((HtmlInput) element_).isChecked();
        }
        else if (element_ instanceof HtmlOption) {
            return ((HtmlOption) element_).isSelected();
        }

        throw new UnsupportedOperationException(
                "Unable to determine if element is selected. Tag name is: " + element_.getTagName());
    }

    @Override
    public boolean isEnabled() {
        assertElementNotStale();

        if (element_ instanceof DisabledElement) {
            return !((DisabledElement) element_).isDisabled();
        }
        return true;
    }

    @Override
    public boolean isDisplayed() {
        assertElementNotStale();

        // TODO HA #2018 start
        /*
        return element_.isDisplayed();
        */
        return HtmlUnitElementUtils.isVisible(element_);
        // HA #2018 end
    }

    @Override
    public Point getLocation() {
        assertElementNotStale();

        // TODO HA #2039 start
        /*
        try {
            return new Point(readAndRound("left"), readAndRound("top"));
        }
        catch (final Exception e) {
            throw new WebDriverException("Cannot determine size of element", e);
        }
        */
        final int[] position = HtmlUnitElementUtils.getPosition(getElement());
        return new Point(position[0], position[1]);
        // HA #2039 end
    }

    @Override
    public Dimension getSize() {
        assertElementNotStale();

        try {
            final int width = readAndRound("width");
            final int height = readAndRound("height");
            return new Dimension(width, height);
        }
        catch (final Exception e) {
            throw new WebDriverException("Cannot determine size of element", e);
        }
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle(getLocation(), getSize());
    }

    private int readAndRound(final String property) {
        final String cssValue = getCssValue(property).replaceAll("[^0-9\\.]", "");
        if (cssValue.isEmpty()) {
            return 5; // wrong... but better than nothing
        }
        return Math.round(Float.parseFloat(cssValue));
    }

    @Override
    public String getText() {
        assertElementNotStale();
        // TODO: HA start (#1292)
        /*
        return element_.getVisibleText();
        */
        return HtmlUnitElementUtils.computeText(element_);
        // HA end (#1292)
    }

    protected HtmlUnitDriver getDriver() {
        return driver_;
    }

    public DomElement getElement() {
        return element_;
    }

    @Deprecated // It's not a part of WebDriver API
    public List<WebElement> getElementsByTagName(final String tagName) {
        assertElementNotStale();

        final List<?> allChildren = element_.getByXPath(".//" + tagName);
        final List<WebElement> elements = new ArrayList<>();
        for (final Object o : allChildren) {
            if (!(o instanceof HtmlElement)) {
                continue;
            }

            final HtmlElement child = (HtmlElement) o;
            elements.add(getDriver().toWebElement(child));
        }
        return elements;
    }

    @Override
    public WebElement findElement(final By by) {
        driver_.getAlert().ensureUnlocked();
        return driver_.implicitlyWaitFor(() -> {
            assertElementNotStale();
            return driver_.findElement(this, by);
        });
    }

    @Override
    public List<WebElement> findElements(final By by) {
        driver_.getAlert().ensureUnlocked();
        return driver_.implicitlyWaitFor(() -> {
            assertElementNotStale();
            return driver_.findElements(this, by);
        });
    }

    private HtmlUnitWebElement findParentForm() {
        DomNode current = element_;
        while (!(current == null || current instanceof HtmlForm)) {
            current = current.getParentNode();
        }
        return getDriver().toWebElement((HtmlForm) current);
    }

    @Override
    public String toString() {
        if (toString_ == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append('<').append(element_.getTagName());
            final NamedNodeMap attributes = element_.getAttributes();
            final int n = attributes.getLength();
            for (int i = 0; i < n; ++i) {
                final Attr a = (Attr) attributes.item(i);
                sb.append(' ').append(a.getName()).append("=\"").append(a.getValue().replace("\"", "&quot;"))
                        .append("\"");
            }
            if (element_.hasChildNodes()) {
                sb.append('>');
            }
            else {
                sb.append(" />");
            }
            toString_ = sb.toString();
        }
        return toString_;
    }

    protected void assertElementNotStale() {
        driver_.assertElementNotStale(element_);
    }

    @Override
    public String getCssValue(final String propertyName) {
        assertElementNotStale();

        final HTMLElement elem = element_.getScriptableObject();

        final String style = elem.getWindow().getComputedStyle(elem, null).getPropertyValue(propertyName);

        return getColor(style);
    }

    private static String getColor(final String name) {
        if ("null".equals(name)) {
            return "transparent";
        }
        if (name.startsWith("rgb(")) {
            return Color.fromString(name).asRgba();
        }

        final Colors colors = getColorsOf(name);
        if (colors != null) {
            return colors.getColorValue().asRgba();
        }
        return name;
    }

    private static Colors getColorsOf(String name) {
        name = name.toUpperCase();
        for (final Colors colors : Colors.values()) {
            if (colors.name().equals(name)) {
                return colors;
            }
        }
        return null;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof WebElement)) {
            return false;
        }

        WebElement other = (WebElement) obj;
        if (other instanceof WrapsElement) {
            other = ((WrapsElement) obj).getWrappedElement();
        }

        return other instanceof HtmlUnitWebElement && element_.equals(((HtmlUnitWebElement) other).element_);
    }

    @Override
    public int hashCode() {
        return element_.hashCode();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.WrapsDriver#getContainingDriver()
     */
    @Override
    public WebDriver getWrappedDriver() {
        return driver_;
    }

    public Coordinates getCoordinates() {
        return this;
    }

    public Point onScreen() {
        throw new UnsupportedOperationException("Not displayed, no screen location.");
    }

    public Point inViewPort() {
        return getLocation();
    }

    public Point onPage() {
        return getLocation();
    }

    public Object getAuxiliary() {
        return element_;
    }

    @Override
    public <X> X getScreenshotAs(final OutputType<X> outputType) throws WebDriverException {
        throw new UnsupportedOperationException("Screenshots are not enabled for HtmlUnitDriver");
    }

    public int getId() {
        return id_;
    }

}
