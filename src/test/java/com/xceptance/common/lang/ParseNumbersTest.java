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
package com.xceptance.common.lang;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for parsing longs and ints.
 * 
 * @author René Schwietzke (Xceptance Software Technologies GmbH)
 */
public class ParseNumbersTest
{
    /**
     * Test method for {@link com.xceptance.common.lang.ParseNumbers#parseLong(java.lang.String)}.
     */
    @Test
    public final void testParseLong()
    {
        {
            final String s = "1670036109465868";
            Assert.assertTrue(Long.valueOf(s) == ParseNumbers.parseLong(XltCharBuffer.valueOf(s)));
        }
        {
            final String s = "0";
            Assert.assertTrue(Long.valueOf(s) == ParseNumbers.parseLong(XltCharBuffer.valueOf(s)));
        }
        {
            final String s = "5";
            Assert.assertTrue(Long.valueOf(s) == ParseNumbers.parseLong(XltCharBuffer.valueOf(s)));
        }
        {
            final String s = "12";
            Assert.assertTrue(Long.valueOf(s) == ParseNumbers.parseLong(XltCharBuffer.valueOf(s)));
        }
        {
            final String s = "1670036";
            Assert.assertTrue(Long.valueOf(s) == ParseNumbers.parseLong(XltCharBuffer.valueOf(s)));
        }
    }

    /**
     * Test method for {@link com.xceptance.common.parsenumbers.FastParseNumbers#fastParseLong(java.lang.String)}.
     */
    @Test
    public final void testParseLongFallback()
    {
        {
            final String s = "-1670036109465868";
            Assert.assertTrue(Long.valueOf(s) == ParseNumbers.parseLong(XltCharBuffer.valueOf(s)));
        }
        {
            final String s = "-0";
            Assert.assertTrue(Long.valueOf(s) == ParseNumbers.parseLong(XltCharBuffer.valueOf(s)));
        }
        {
            final String s = "-1670036";
            Assert.assertTrue(Long.valueOf(s) == ParseNumbers.parseLong(XltCharBuffer.valueOf(s)));
        }
        {
            final String s = "+0";
            Assert.assertTrue(Long.valueOf(s) == ParseNumbers.parseLong(XltCharBuffer.valueOf(s)));
        }
    }

    /**
     * Test method for {@link com.xceptance.common.parsenumbers.FastParseNumbers#fastParseInt(java.lang.String)}.
     */
    @Test
    public final void testParseInt()
    {
        {
            final String s = "1670036108";
            Assert.assertTrue(Integer.valueOf(s) == ParseNumbers.parseInt(XltCharBuffer.valueOf(s)));
        }
        {
            final String s = "0";
            Assert.assertTrue(Integer.valueOf(s) == ParseNumbers.parseInt(XltCharBuffer.valueOf(s)));
        }
        {
            final String s = "8";
            Assert.assertTrue(Integer.valueOf(s) == ParseNumbers.parseInt(XltCharBuffer.valueOf(s)));
        }
        {
            final String s = "28";
            Assert.assertTrue(Integer.valueOf(s) == ParseNumbers.parseInt(XltCharBuffer.valueOf(s)));
        }
        {
            final String s = "1670036";
            Assert.assertTrue(Integer.valueOf(s) == ParseNumbers.parseInt(XltCharBuffer.valueOf(s)));
        }
    }

    /**
     * Test method for {@link com.xceptance.common.parsenumbers.FastParseNumbers#fastParseInt(java.lang.String)}.
     */
    @Test
    public final void testParseIntFallback()
    {
        {
            final String s = "-1670036108";
            Assert.assertTrue(Integer.valueOf(s) == ParseNumbers.parseInt(XltCharBuffer.valueOf(s)));
        }
        {
            final String s = "-0";
            Assert.assertTrue(Integer.valueOf(s) == ParseNumbers.parseInt(XltCharBuffer.valueOf(s)));
        }
        {
            final String s = "-1670036";
            Assert.assertTrue(Integer.valueOf(s) == ParseNumbers.parseInt(XltCharBuffer.valueOf(s)));
        }
        {
            final String s = "+9876";
            Assert.assertTrue(Integer.valueOf(s) == ParseNumbers.parseInt(XltCharBuffer.valueOf(s)));
        }
    }

    /**
     * Test method for {@link com.xceptance.common.parsenumbers.FastParseNumbers#fastParseInt(java.lang.String)}.
     */
    @Test(expected = NumberFormatException.class)
    public final void testNumberFormatExceptionInt_Empty()
    {
        final String s = "";
        ParseNumbers.parseInt(XltCharBuffer.valueOf(s));
    }

    /**
     * Test method for {@link com.xceptance.common.parsenumbers.FastParseNumbers#fastParseInt(java.lang.String)}.
     */
    @Test(expected = NumberFormatException.class)
    public final void testNumberFormatExceptionInt_Space()
    {
        final String s = " ";
        ParseNumbers.parseInt(XltCharBuffer.valueOf(s));
    }

    /**
     * Test method for {@link com.xceptance.common.parsenumbers.FastParseNumbers#fastParseInt(java.lang.String)}.
     */
    @Test(expected = NumberFormatException.class)
    public final void testNumberFormatExceptionInt_WrongCharacter()
    {
        final String s = "aaa";
        ParseNumbers.parseInt(XltCharBuffer.valueOf(s));
    }

    /**
     * Test method for {@link com.xceptance.common.parsenumbers.FastParseNumbers#fastParseInt(java.lang.String)}.
     */
    @Test(expected = NumberFormatException.class)
    public final void testNumberFormatExceptionInt_Null()
    {
        ParseNumbers.parseInt(null);
    }

    /**
     * Test method for {@link com.xceptance.common.parsenumbers.FastParseNumbers#fastParseInt(java.lang.String)}.
     */
    @Test(expected = NumberFormatException.class)
    public final void testNumberFormatExceptionLong_Empty()
    {
        final String s = "";
        ParseNumbers.parseLong(XltCharBuffer.valueOf(s));
    }

    /**
     * Test method for {@link com.xceptance.common.parsenumbers.FastParseNumbers#fastParseInt(java.lang.String)}.
     */
    @Test(expected = NumberFormatException.class)
    public final void testNumberFormatExceptionLong_Space()
    {
        final String s = " ";
        ParseNumbers.parseLong(XltCharBuffer.valueOf(s));
    }

    /**
     * Test method for {@link com.xceptance.common.parsenumbers.FastParseNumbers#fastParseInt(java.lang.String)}.
     */
    @Test(expected = NumberFormatException.class)
    public final void testNumberFormatExceptionLong_WrongCharacter()
    {
        final String s = "aaa";
        ParseNumbers.parseLong(XltCharBuffer.valueOf(s));
    }

    /**
     * Test method for {@link com.xceptance.common.parsenumbers.FastParseNumbers#fastParseInt(java.lang.String)}.
     */
    @Test(expected = NumberFormatException.class)
    public final void testNumberFormatExceptionLong_Null()
    {
        ParseNumbers.parseLong(null);
    }
    
    // ================================================================
    // Double
    
    @Test
    public void doubleHappyPath()
    {
        String s = "";
        
        s = "0"; Assert.assertTrue(Double.parseDouble(s) == ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)));
        s = "0.0"; Assert.assertTrue(Double.parseDouble(s) == ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)));
        s = "0.000008765"; Assert.assertTrue(Double.parseDouble(s) == ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)));
        s = "1"; Assert.assertTrue(Double.parseDouble(s) == ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)));
        s = "1.0000087171"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)), 0.00000000001);

        s = "2"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)), 0.00000000001);
        s = "32"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)), 0.00000000001);
        s = "423"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)), 0.00000000001);
        s = "5234"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)), 0.00000000001);
        s = "12345"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)), 0.00000000001);
        s = "223456"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)), 0.00000000001);
        s = "5234567"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)), 0.00000000001);

        s = "1.1"; Assert.assertTrue(Double.parseDouble(s) == ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)));
        s = "12.1"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)),      0.0000000001);
        s = "123.1"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)),     0.0000000001);
        s = "1234.2"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)),    0.0000000001);
        s = "12345.3"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)),   0.0000000001);
        s = "123456.4"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)),  0.0000000001);
        s = "1234567.5"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)), 0.0000000001);

        s = "1"; Assert.assertTrue(Double.parseDouble(s) == ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)));
        s = "1.143"; Assert.assertTrue(Double.parseDouble(s) == ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)));
        s = "12.111"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)), 0.00000000001);
        s = "123.144"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)), 0.00000000001);
        s = "1234.255"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)), 0.00000000001);
        s = "12345.322"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)), 0.00000000001);
        s = "123456.433"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)), 0.00000000001);
        s = "1234567.533"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)), 0.00000000001);

        s = "1.0"; Assert.assertTrue(Double.parseDouble(s) == ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)));
        s = "1.001"; Assert.assertEquals(Double.parseDouble(s), ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)), 0.00000000001);
        s = "0.25"; Assert.assertTrue(Double.parseDouble(s) == ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)));
        s = "2.50"; Assert.assertTrue(Double.parseDouble(s) == ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)));
        s = "25.0"; Assert.assertTrue(Double.parseDouble(s) == ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)));
        s = "25.25"; Assert.assertTrue(Double.parseDouble(s) == ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)));
        s = "25.00025"; Assert.assertTrue(Double.parseDouble(s) == ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)));
        s = "0.6811"; Assert.assertTrue(Double.parseDouble(s) == ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)));
        s = "141.001"; Assert.assertTrue(Double.parseDouble(s) == ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)));

        s = "10.100000000000001"; Assert.assertTrue(Double.parseDouble(s) == ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)));
        s = "-141.001"; Assert.assertTrue(Double.parseDouble(s) == ParseNumbers.parseDouble(XltCharBuffer.valueOf(s)));
    }
}
