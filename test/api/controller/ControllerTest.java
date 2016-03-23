package api.controller;

import api.database.CSVControl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Tim on 23-3-2016.
 */
public class ControllerTest {
    Controller ct;

    @Before
    public void setUp() throws Exception {
        ct = new Controller(new CSVControl(new String[]{"./test_res/DataTest.csv"}));
    }

    @Test
    public void getAdvancedTopN() throws Exception {

    }
}