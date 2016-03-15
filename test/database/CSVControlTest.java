package database;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by Tim on 15-3-2016.
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
        assertThat(testData.size(), is(6));


    }
}