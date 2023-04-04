/*
 * Copyright (c) 2005-2022 Xceptance Software Technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xceptance.xlt.api.actions;

import java.net.URL;

import org.htmlunit.Page;
import org.htmlunit.xml.XmlPage;
import org.junit.Assert;

/**
 * AbstractXmlPageAction is the base class for all actions that load an arbitrary XML snippet. The loaded page is parsed
 * and stored internally as a tree of elements. This makes it easy to locate and query/manipulate a certain page
 * element.
 * 
 * @author Jörg Werner (Xceptance Software Technologies GmbH)
 */
public abstract class AbstractXmlPageAction extends AbstractWebAction
{
    /**
     * The parsed XML page object generated by this action.
     */
    private XmlPage xmlPage;

    /**
     * Creates a new AbstractXmlPageAction object and gives it the passed timer name. This constructor is typically used
     * for an intermediate action in a sequence of actions, i.e. it has a previous action.
     * 
     * @param previousAction
     *            the action that preceded the current action
     * @param timerName
     *            the name of the timer that is associated with this action
     */
    protected AbstractXmlPageAction(final AbstractWebAction previousAction, final String timerName)
    {
        super(previousAction, timerName);
    }

    /**
     * Creates a new AbstractXmlPageAction object and gives it the passed timer name. This constructor is typically used
     * for the first action in a sequence of actions, i.e. it has no previous action.
     * 
     * @param timerName
     *            the name of the timer that is associated with this action
     */
    protected AbstractXmlPageAction(final String timerName)
    {
        this(null, timerName);
    }

    /**
     * Returns the parsed XML page object generated by this action.
     * 
     * @return the page
     */
    public XmlPage getXmlPage()
    {
        return xmlPage;
    }

    /**
     * Loads the page from the passed URL.
     * 
     * @param url
     *            the target URL
     * @throws Exception
     *             if an error occurred while loading the page
     */
    protected void loadXMLPage(final URL url) throws Exception
    {
        final Page result = getWebClient().getPage(url);

        if (result instanceof XmlPage)
        {
            xmlPage = (XmlPage) result;
        }
        else
        {
            throw new UnexpectedPageTypeException("The page type of '" + url + "' is not XML as expected.");
        }
    }

    /**
     * Validates the HTTP response code.
     * 
     * @param expected
     *            the expected HTTP response code
     * @throws Exception
     *             if the validation against the expected response code failed or no response is available
     */
    protected void validateHttpResponseCode(final int expected) throws Exception
    {
        final XmlPage p = getXmlPage();
        Assert.assertNotNull("No html page available to validate the response code against", p);

        final int responseCode = p.getWebResponse().getStatusCode();
        Assert.assertEquals("Response code does not match", expected, responseCode);
    }
}
