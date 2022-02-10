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

import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 */
@XStreamAlias("agent")
public class AgentReport
{
    public DoubleStatisticsReport totalCpuUsage;

    public DoubleStatisticsReport cpuUsage;

    public long fullGcCount;

    public BigDecimal fullGcCpuUsage;

    public long fullGcTime;

    public long minorGcCount;

    public BigDecimal minorGcCpuUsage;

    public long minorGcTime;

    public String name;

    public int transactions;

    public int transactionErrors;
}
