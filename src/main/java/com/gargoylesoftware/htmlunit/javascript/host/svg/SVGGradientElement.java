/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host.svg;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF68;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

/**
 * A JavaScript object for {@code SVGGradientElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class SVGGradientElement extends SVGElement {

    /** The constant {@code SVG_SPREADMETHOD_UNKNOWN}. */
    @JsxConstant
    public static final int SVG_SPREADMETHOD_UNKNOWN = 0;
    /** The constant {@code SVG_SPREADMETHOD_PAD}. */
    @JsxConstant
    public static final int SVG_SPREADMETHOD_PAD = 1;
    /** The constant {@code SVG_SPREADMETHOD_REFLECT}. */
    @JsxConstant
    public static final int SVG_SPREADMETHOD_REFLECT = 2;
    /** The constant {@code SVG_SPREADMETHOD_REPEAT}. */
    @JsxConstant
    public static final int SVG_SPREADMETHOD_REPEAT = 3;
    /** Constant for {@code SVG_UNIT_TYPE_UNKNOWN}. */
    @JsxConstant(IE)
    public static final int SVG_UNIT_TYPE_UNKNOWN = 0;
    /** Constant for {@code SVG_UNIT_TYPE_USERSPACEONUSE}. */
    @JsxConstant(IE)
    public static final int SVG_UNIT_TYPE_USERSPACEONUSE = 1;
    /** Constant for {@code SVG_UNIT_TYPE_OBJECTBOUNDINGBOX}. */
    @JsxConstant(IE)
    public static final int SVG_UNIT_TYPE_OBJECTBOUNDINGBOX = 2;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, FF, FF68})
    public SVGGradientElement() {
    }
}
