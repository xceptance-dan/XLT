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
package com.xceptance.xlt.report.providers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EventReportTest
{
    /**
     * Constr
     */
    @Test
    public void ctr()
    {
        var e = new EventReport("TEvent", "event");
        assertEquals("TEvent", e.testCaseName);
        assertEquals("event", e.name);
        assertEquals(0, e.droppedCount);
        assertEquals(0, e.totalCount);
        assertEquals(0, e.messageMap.size());
        assertNull(e.messages);
    }

    /**
     * Check that we coount correctly
     */
    @Test
    public void addMessage()
    {
        var e = new EventReport("TEvent", "event");

        e.addMessage("msg1", 10);
        assertEquals(0, e.droppedCount);
        assertEquals(1, e.totalCount);
        assertEquals(1, e.messageMap.size());
        assertEquals(1, e.messageMap.get("msg1").count);
        assertEquals("msg1", e.messageMap.get("msg1").info);

        e.addMessage("msg1", 10);
        assertEquals(0, e.droppedCount);
        assertEquals(2, e.totalCount);
        assertEquals(1, e.messageMap.size());
        assertEquals(2, e.messageMap.get("msg1").count);
        assertEquals("msg1", e.messageMap.get("msg1").info);

        e.addMessage("msg2", 10);
        assertEquals(0, e.droppedCount);
        assertEquals(3, e.totalCount);
        assertEquals(2, e.messageMap.size());
        assertEquals(2, e.messageMap.get("msg1").count);
        assertEquals("msg1", e.messageMap.get("msg1").info);

        assertEquals(1, e.messageMap.get("msg2").count);
        assertEquals("msg2", e.messageMap.get("msg2").info);
    }

    /**
     * Check that the limit is obeyed and counted
     */
    @Test
    public void addMessageAndLimit()
    {
        var e = new EventReport("TEvent", "event");

        e.addMessage("msg1", 3);
        e.addMessage("msg2", 3);
        e.addMessage("msg3", 3);
        assertEquals(0, e.droppedCount);
        assertEquals(3, e.totalCount);
        assertEquals(3, e.messageMap.size());
        assertEquals(1, e.messageMap.get("msg1").count);
        assertEquals(1, e.messageMap.get("msg2").count);
        assertEquals(1, e.messageMap.get("msg3").count);

        e.addMessage("msg1", 3);
        assertEquals(0, e.droppedCount);
        assertEquals(4, e.totalCount);
        assertEquals(3, e.messageMap.size());
        assertEquals(2, e.messageMap.get("msg1").count);
        assertEquals(1, e.messageMap.get("msg2").count);
        assertEquals(1, e.messageMap.get("msg3").count);

        e.addMessage("msg4", 3);
        assertEquals(1, e.droppedCount);
        assertEquals(5, e.totalCount);
        assertEquals(3, e.messageMap.size());
        assertEquals(2, e.messageMap.get("msg1").count);
        assertEquals(1, e.messageMap.get("msg2").count);
        assertEquals(1, e.messageMap.get("msg3").count);

        e.addMessage("msg2", 3);
        assertEquals(1, e.droppedCount);
        assertEquals(6, e.totalCount);
        assertEquals(3, e.messageMap.size());
        assertEquals(2, e.messageMap.get("msg1").count);
        assertEquals(2, e.messageMap.get("msg2").count);
        assertEquals(1, e.messageMap.get("msg3").count);

        e.addMessage("msg4", 3);
        assertEquals(2, e.droppedCount);
        assertEquals(7, e.totalCount);
        assertEquals(3, e.messageMap.size());
        assertEquals(2, e.messageMap.get("msg1").count);
        assertEquals(2, e.messageMap.get("msg2").count);
        assertEquals(1, e.messageMap.get("msg3").count);
    }

    /**
     * Check serialization helper
     */
    @Test
    public void prepareSerialization()
    {
        var e = new EventReport("TEvent", "event");
        e.addMessage("Msg1", 10);
        assertEquals(1, e.messageMap.size());
        assertNull(e.messages);

        e.prepareSerialization();

        assertNull(e.messageMap);
        assertEquals(1, e.messages.size());
        assertEquals(1, e.messages.get(0).count);
        assertEquals("Msg1", e.messages.get(0).info);
    }

    /**
     * Check the merging
     */
    @Test
    public void addSimple()
    {
        var e1 = new EventReport("TEvent", "event");
        e1.addMessage("msg1", 10);

        var e2 = new EventReport("TEvent", "event");
        e2.addMessage("msg1", 10);

        e1.add(e2);

        assertEquals(0, e1.droppedCount);
        assertEquals(2, e1.totalCount);
        assertEquals(1, e1.messageMap.size());
        assertEquals(2, e1.messageMap.get("msg1").count);
    }

    /**
     * Check the merging with drop count
     */
    @Test
    public void addAndLimit()
    {
        var e1 = new EventReport("TEvent", "event");
        e1.addMessage("msg1", 2);
        e1.addMessage("msg2", 2);
        e1.addMessage("msg3", 2);

        var e2 = new EventReport("TEvent", "event");
        e2.addMessage("msg1", 2);
        e2.addMessage("msg2", 2);

        e1.add(e2);

        assertEquals(1, e1.droppedCount);
        assertEquals(5, e1.totalCount);
        assertEquals(2, e1.messageMap.size());
        assertEquals(2, e1.messageMap.get("msg1").count);
        assertEquals(2, e1.messageMap.get("msg2").count);
        assertNull(e1.messageMap.get("msg3"));
    }

    /**
     * Check the merging with drop count
     */
    @Test
    public void addDisjunct()
    {
        var e1 = new EventReport("TEvent", "event");
        e1.addMessage("msg1", 2);
        e1.addMessage("msg2", 2);

        var e2 = new EventReport("TEvent", "event");
        e2.addMessage("msg4", 2);
        e2.addMessage("msg5", 2);

        e1.add(e2);

        assertEquals(0, e1.droppedCount);
        assertEquals(4, e1.totalCount);
        assertEquals(4, e1.messageMap.size());
        assertEquals(1, e1.messageMap.get("msg1").count);
        assertEquals(1, e1.messageMap.get("msg2").count);
        assertEquals(1, e1.messageMap.get("msg4").count);
        assertEquals(1, e1.messageMap.get("msg5").count);
    }

    /**
     * Set new test case name
     */
    @Test
    public void setTestCaseName()
    {
        var e = new EventReport("TEvent", "event");
        assertEquals("TEvent", e.testCaseName);

        e.setTestCaseName("TNew");
        assertEquals("TNew", e.testCaseName);
        assertEquals("event", e.name);
    }
}
