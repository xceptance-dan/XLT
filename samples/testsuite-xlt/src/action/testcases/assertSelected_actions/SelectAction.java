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
package action.testcases.assertSelected_actions;

import org.junit.Assert;

import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.api.engine.scripting.AbstractHtmlUnitScriptAction;
import org.htmlunit.html.HtmlPage;

import action.modules.assertNotSelected;
import action.modules.assertSelected;

/**
 * TODO: Add class description
 */
public class SelectAction extends AbstractHtmlUnitScriptAction
{

    /**
     * Constructor.
     * @param prevAction The previous action.
     */
    public SelectAction(final AbstractHtmlPageAction prevAction)
    {
        super(prevAction);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void preValidate() throws Exception
    {
        final HtmlPage page = getPreviousAction().getHtmlPage();
        Assert.assertNotNull("Failed to get page from previous action", page);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute() throws Exception
    {
        HtmlPage page = getPreviousAction().getHtmlPage();
        page = select("id=select_1", "id=select_1_c");
        page = addSelection("id=select_9", "id=select_9_b");
        page = addSelection("id=select_9", "id=select_9_c");
        final assertNotSelected assertNotSelected = new assertNotSelected("id=select_1", "select_1_a", "0");
        page = assertNotSelected.run(page);

        final assertSelected assertSelected = new assertSelected("id=select_1", "select_1_c", "2");
        page = assertSelected.run(page);

        final assertNotSelected assertNotSelected0 = new assertNotSelected("id=select_9", "select_9_a", "0");
        page = assertNotSelected0.run(page);

        final assertSelected assertSelected0 = new assertSelected("id=select_9", "select_9_b", "1");
        page = assertSelected0.run(page);

        final assertSelected assertSelected1 = new assertSelected("id=select_9", "select_9_c", "2");
        page = assertSelected1.run(page);


        setHtmlPage(page);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void postValidate() throws Exception
    {
        final HtmlPage page = getHtmlPage();
        Assert.assertNotNull("Failed to load page", page);


    }
}