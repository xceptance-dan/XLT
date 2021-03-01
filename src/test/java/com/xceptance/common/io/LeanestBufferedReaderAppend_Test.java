package com.xceptance.common.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.common.util.XltCharBuffer;


public class LeanestBufferedReaderAppend_Test
{
    private String compose(String[] s, String sep)
    {
        return Arrays.stream(s).collect(Collectors.joining(sep));
    }
    
    private String compose(String[] s)
    {
        return Arrays.stream(s).collect(Collectors.joining());
    }
    
    private void test(String[] s, String sep, int bufferSize)
    {
        test(compose(s, sep), bufferSize);
    }
    
    private void test(String[] s, String sep)
    {
        test(compose(s, sep), 8192);
    }
    
    private void test(String[] s)
    {
        test(compose(s), 8192);
    }
    
    private void test(String src, int bufferSize)
    {
        final List<String> newBR = new ArrayList<>();
        try (final LeanestBufferedReaderAppend r = new LeanestBufferedReaderAppend(new StringReader(src), bufferSize))
        {
        	XltCharBuffer cb = null;
            while ((cb = r.readLine()) != null)
            {
                newBR.add(cb.toString());
            }
        }
        catch (IOException e)
        {
            Assert.fail();
        }

        final List<String> original = new ArrayList<>();
        try (final BufferedReader r = new BufferedReader(new StringReader(src), bufferSize))
        {
            String osb = null;
            while ((osb = r.readLine()) != null)
            {
                original.add(osb.toString());
            }
        }
        catch (IOException e)
        {
            Assert.fail();
        }
        
        // verify
        Assert.assertEquals(original.size(), newBR.size());
        Assert.assertArrayEquals(original.toArray(), newBR.toArray());
    }
    
	@Test
	public void oneLine_NoEnding()
	{
		test("T", 100);
		test("Test Test", 100);
		test("Test TestTest TestTest TestTest TestTest TestTest TestTest TestTest TestTest TestTest TestTest Test", 1000);
		test("Test TestTest TestTest TestTest TestTest TestTest TestTest TestTest TestTest TestTest TestTest Test", 10);
	}
	
	@Test
	public void oneLine_And_Ending()
	{
		test("T\r\n", 20);
		test("T\r", 10);
		test("T\n", 5);    
	}

    @Test
    public void happyPathThreeLines()
    {
        final String[] data = new String[] {
                "Test",
                "Foobar",
                "Mario and the Gang"
        };
       
        test(data, "\r");
        test(data, "\n");
        test(data, "\r\n");
    }
	
    @Test
    public void happyPathEmptyLineMiddle()
    {
        final String[] data = new String[] {
                "T",
                "",
                "A"
        };
       
        test(data, "\r");
        test(data, "\n");
        test(data, "\r\n");
    }
    
    @Test
    public void happyPathEmpty()
    {
        test("\r", 100);
        test("\n", 100);
        test("\r\n", 100);
    }
    
    @Test
    public void twoEmptyLine()
    {
        final String[] data = new String[] {
                "", ""
        };
       
        test(data, "\r");
        test(data, "\n");    
        test(data, "\r\n");
    }

    @Test
    public void happyPathEmptyOnly3Full()
    {
        final String[] data = new String[] {
                "T", "a", "B"
        };
       
        test(data, "\r");
        test(data, "\n");
        test(data, "\r\n");
    }
    
    @Test
    public void bufferSmallerThanLine()
    {
        final String[] data = new String[] {
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789", // 100
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789", 
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789", 
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789", 
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" 
        };
       
        test(data, "\r", 25);
        test(data, "\n", 99);
        test(data, "\r\n", 50);
    }

    @Test
    public void hugeBuffer()
    {
        final String[] data = new String[] {
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789", // 100
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789", 
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789", 
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789", 
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" 
        };
       
        test(data, "\r", 1500);
        test(data, "\n", 2000);
        test(data, "\r\n", 10000);
    }
    
    @Test
    public void bufferLargerThanLine()
    {
        final String[] data = new String[] {
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789", // 100
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789", 
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789", 
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789", 
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" 
        };
       
        test(data, "\r", 250);
        test(data, "\n", 250);
        test(data, "\r\n", 250);
    }
    
    @Test
    public void lastLineEmpty()
    {
        final String data =
                "A12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678E\n" + 
                "A12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678E\n" +
                "";
       
        test(data, 10);
        test(data, 99);
        test(data, 100);
        test(data, 101);
        test(data, 1000);
    }
    
    public List<XltCharBuffer> readViaNonStringBufferedReader(InputStream in)
    {
        final List<XltCharBuffer> result = new ArrayList<>();
        XltCharBuffer osb = null;
        
        try (final LeanestBufferedReaderAppend r = new LeanestBufferedReaderAppend(new InputStreamReader(in)))
        {
            while ((osb = r.readLine()) != null)
            {
                result.add(osb);
            }
        }
        catch (IOException e)
        {
            Assert.fail();
        }
        catch (Exception e)
        {
        	System.err.printf("[%s] %s", result.size(), osb.toString());
            throw e;
        }
        
        return result;
    }
    
    public List<String> readViaBufferedReader(InputStream in)
    {
        final List<String> result = new ArrayList<>();
        try (final BufferedReader r = new BufferedReader(new InputStreamReader(in)))
        {
            String osb = null;
            while ((osb = r.readLine()) != null)
            {
                result.add(osb);
            }
        }
        catch (IOException e)
        {
            Assert.fail();
        }

        return result;
    }
    
    @Test
    public void compare() throws FileNotFoundException
    {
        final String file = "//home/rschwietzke/projects/loadtest/test-results/20191109-054302-michaels/ac001_us-central1-c_00/TCouponAddToCart_MIK/356/test.csv";
        
        final List<String> regular = readViaBufferedReader(new FileInputStream(new File(file)));
        final List<XltCharBuffer> fancy = readViaNonStringBufferedReader(new FileInputStream(new File(file)));
        
        Assert.assertEquals(regular.size(), fancy.size());
        
        for (int i = 0; i < regular.size(); i++)
        {
            String r = regular.get(i);
            XltCharBuffer f = fancy.get(i);
            
            Arrays.equals(r.toCharArray(), f.toCharArray());
        }
    }
}
