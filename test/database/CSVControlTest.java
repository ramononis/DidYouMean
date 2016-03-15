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
    private CSVControl deadc;

    @Before
    public void setUp() throws Exception {
        c = new CSVControl(new String[]{"./csv/DataTest.csv"});
        deadc = new CSVControl(new String[]{"je moeder"});
    }

    @Test
    public void testGetData() throws Exception {
        deadc.getData(); // should print file not found.

        HashMap<String,Integer> testData = c.getData();

        assertThat(testData.size(), is(8)); // skips everything it shouldn't/can't parse

        assertThat(testData.get("1"), is(190));
        assertThat(testData.get("one"), is(0));
        assertThat(testData.get("two"), is(26205));
        assertThat(testData.get("three"), is(72417));
        assertThat(testData.get("four"), is(128634356));
        assertThat(testData.get("five,six"), is(225337093));
        assertThat(testData.get("inch\""), is(229600));
        assertThat(testData.get("double"), is(400));
    }
}