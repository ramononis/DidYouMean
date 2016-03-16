package autocomplete;

import database.CSVCStub;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Tim on 15-3-2016.
 */
public class AutoCompleterTest {
    CSVCStub DD;
    AutoCompleter AC;

    @org.junit.Before
    public void setUp() throws Exception {
        DD = new CSVCStub();
    }

    @org.junit.Test
    public void testGetTopN() throws Exception {
        DD.setData(null);
        AC = new AutoCompleter(DD);
        assertThat(AC.getTopN(5, "").length, is(0));

        DD.setData(generateData1());
        AC = new AutoCompleter(DD);
        assertThat(AC.getTopN(7, ""), is(new String[]{"NR1", "NR2", "NR3", "NR4", "NR5", "NR6", "NR7"}));

        DD.setData(generateData2());
        AC = new AutoCompleter(DD);
        assertThat(AC.getTopN(7, ""), equalTo(new String[]{"NR2", "NR123", "NR3", "NR4", "NR567", "NR6", "NR7"}));
    }

    private HashMap generateData1() { // standard
        HashMap<String,Integer> r = new HashMap();
        r.put("NR1", 500); //1
        r.put("NR2", 454); //2
        r.put("NR3", 354); //3
        r.put("NR4", 298); //4
        r.put("NR5", 238); //5
        r.put("NR6", 117); //6
        r.put("NR7", 2); //7
        return r;
    }

    private HashMap generateData2() { // same values, this is sort of random because it depends on how the children are iterated. children are a set so no order.
        HashMap<String,Integer> r = new HashMap();
        r.put("NR123", 500); //2
        r.put("NR2", 500); //1
        r.put("NR3", 354); //3
        r.put("NR4", 200); //4
        r.put("NR567", 200); //5
        r.put("NR6", 117); //6
        r.put("NR7", 2); //7
        return r;
    }

    private HashMap generateData3() { // more variations in keywords
        HashMap<String,Integer> r = new HashMap();
        r.put("NR12", 500);
        r.put("NR2", 454);
        r.put("NR3", 354);
        r.put("NR4", 298);
        r.put("NR5", 238);
        r.put("NR6", 117);
        r.put("NR7", 2);
        return r;
    }
}