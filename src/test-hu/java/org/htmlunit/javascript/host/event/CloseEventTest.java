/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.javascript.host.event;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.HtmlPageTest;
import org.htmlunit.javascript.host.event.CloseEvent;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.junit.BrowserRunner.BuggyWebDriver;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link CloseEvent}.
 *
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class CloseEventTest extends WebDriverTestCase {

    private static final String DUMP_EVENT_FUNCTION = "  function dump(event) {\n"
        + "    if (event) {\n"
        + "      log(event);\n"
        + "      log(event.type);\n"
        + "      log(event.bubbles);\n"
        + "      log(event.cancelable);\n"
        + "      log(event.composed);\n"
        + "      log(event.code);\n"
        + "      log(event.reason);\n"
        + "      log(event.wasClean);\n"
        + "    } else {\n"
        + "      log('no event');\n"
        + "    }\n"
        + "  }\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object CloseEvent]", "type-close", "false", "false", "false", "0", "", "false"},
            IE = "exception")
    public void create_ctor() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new CloseEvent('type-close');\n"
            + "      dump(event);\n"
            + "    } catch (e) { log('exception') }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object CloseEvent]", "type-close", "true", "false", "false", "42", "test-reason", "true"},
            IE = "exception")
    public void create_ctorWithDetails() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = new CloseEvent('type-close', {\n"
            + "        'bubbles': true,\n"
            + "        'reason': 'test-reason',\n"
            + "        'code': 42,\n"
            + "        'wasClean': true\n"
            + "      });\n"
            + "      dump(event);\n"
            + "    } catch (e) { log('exception') }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object CloseEvent]", "", "false", "false", "false", "0", "", "false"},
            FF = "exception",
            FF_ESR = "exception",
            IE = {"[object CloseEvent]", "", "false", "false", "undefined", "0", "", "false"})
    @BuggyWebDriver(IE = {"[object CloseEvent]", "", "false", "false", "undefined", "0", "exception"})
    public void create_createEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('CloseEvent');\n"
            + "      dump(event);\n"
            + "    } catch (e) { log('exception') }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no initCloseEvent",
            FF = "exception",
            FF_ESR = "exception",
            IE = {"[object CloseEvent]", "close", "true", "false", "undefined", "42", "time to close", "true"})
    public void initCloseEvent() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var event = document.createEvent('CloseEvent');\n"
            + "      if (event.initCloseEvent) {\n"
            + "        event.initCloseEvent('close', true, false, true, 42, 'time to close');\n"
            + "        dump(event);\n"
            + "      } else { log('no initCloseEvent'); }\n"
            + "    } catch (e) { log('exception') }\n"
            + "  }\n"
            + DUMP_EVENT_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
