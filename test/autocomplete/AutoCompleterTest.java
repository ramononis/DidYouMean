package autocomplete;

import autocomplete.AutoCompleter;
import database.CSVControl;

import static org.junit.Assert.*;

/**
 * Created by Tim on 15-3-2016.
 */
public class AutoCompleterTest {
    AutoCompleter AC0;
    AutoCompleter AC1;
    AutoCompleter AC2;
    AutoCompleter AC3;

    @org.junit.Before
    public void setUp() throws Exception {
        AC0 = new AutoCompleter(new CSVControl(new String[]{"wrong path"}));
        AC1 = new AutoCompleter(new CSVControl(new String[]{"./csv/DataTest1.csv"}));
        AC2 = new AutoCompleter(new CSVControl(new String[]{"./csv/DataTest2.csv"}));
        AC3 = new AutoCompleter(new CSVControl(new String[]{"./csv/DataTest3.csv"}));
    }

    @org.junit.Test
    public void testGetTopN() throws Exception {

    }
}