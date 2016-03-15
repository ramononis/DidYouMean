package database;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Tests the CSVControl using the blackbox bethod.
 *
 * @author Tim
 */
public class CSVControlTest {
    private CSVControl c;

    @Before
    public void setUp() throws Exception {
        c = new CSVControl(new String[]{"./csv/DataTest.csv"});
    }

    @Test
    public void testGetData() throws Exception {
        HashMap<String,Integer> testData = c.getData();

        System.out.println(testData.toString());

        assertThat(testData.size(), is(7)); // skips everything it shouldn't/can't parse

        assertThat(testData.get("1"), is((int)(2*(100-5))));
        assertThat(testData.get("one"), is((int)(2*(100-100))));
        assertThat(testData.get("two"), is((int)(1337*(100-80.4))));
        assertThat(testData.get("three"), is((int)(2048*(100-64.64))));
        assertThat(testData.get("four"), is((int)(2296222*(100-43.98))));
        assertThat(testData.get("five,six"), is((int)(2296546*(100-1.88))));
        assertThat(testData.get("inch\""), is((int)(2296*(100-0))));
    }
}