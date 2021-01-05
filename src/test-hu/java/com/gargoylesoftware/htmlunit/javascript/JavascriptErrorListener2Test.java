/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link JavaScriptErrorListener}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class JavascriptErrorListener2Test extends SimpleWebTestCase {

    /**
     * Test for running with a JavaScript error listener.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void parsingError() throws Exception {
        final StringBuilder scriptExceptions = new StringBuilder();

        final WebClient webClient = getWebClientWithMockWebConnection();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.setJavaScriptErrorListener(new SilentJavaScriptErrorListener() {
            @Override
            public void scriptException(final HtmlPage page, final ScriptException scriptException) {
                scriptExceptions.append(scriptException.getCause() + "\n");
            }
        });

        final String html = "<html><body><script>while (</script></body></html>";
        getMockWebConnection().setDefaultResponse(html);
        webClient.getPage(URL_FIRST);

        assertEquals("net.sourceforge.htmlunit.corejs.javascript.EvaluatorException: "
            + "Unexpected end of file (script in " + URL_FIRST + " from (1, 21) to (1, 37)#1)\n",
                scriptExceptions.toString());
    }
}
