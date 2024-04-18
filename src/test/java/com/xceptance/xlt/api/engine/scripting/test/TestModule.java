/*
 * Copyright (c) 2005-2024 Xceptance Software Technologies GmbH
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
package com.xceptance.xlt.api.engine.scripting.test;

import com.xceptance.xlt.api.engine.scripting.AbstractHtmlUnitScriptModule;

/**
 * TODO: Add class description.
 *
 * @author Hartmut Arlt (Xceptance Software Technologies GmbH)
 */
public class TestModule extends AbstractHtmlUnitScriptModule
{
    public String doResolve(String resolvable)
    {
        return resolve(resolvable);
    }
}
