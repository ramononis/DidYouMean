package database;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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

        assertThat(testData.get("1"), is(190));
        assertThat(testData.get("one"), is(0));
        assertThat(testData.get("two"), is(26205));
        assertThat(testData.get("three"), is(72417));
        assertThat(testData.get("four"), is(128634356));
        assertThat(testData.get("five,six"), is(225337093));
        assertThat(testData.get("inch\""), is(229600));
    }
}