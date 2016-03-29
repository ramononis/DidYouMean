package api.controller;

import api.database.CSVControl;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Tests the Controller
 *
 * @author Tim Blok, Frans van Dijk, Yannick Mijsters, Ramon Onis, Tim Sonderen; University of Twente
 */
public class ControllerTest {
    private Controller ct;

    @Before
    public void setUp() throws Exception {
        ct = new Controller(new CSVControl(new String[]{"./test_res/DataTest.csv"}));
    }

    @Test
    public void getAdvancedTopN() throws Exception {
        assertThat(ct.getAdvancedTopN(5, ""), is(new String[]{"five,six", "four", "inch\"", "three", "two"}));
        assertThat(ct.getAdvancedTopN(2, "foir"), is(new String[]{"four"}));
    }
}
