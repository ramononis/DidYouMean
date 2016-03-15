package database;

import java.util.HashMap;

/**
 * Created by Tim on 15-3-2016.
 */
public class CSVCStub {
    private HashMap<String, Integer> data;

    public CSVCStub(int t) { // t should be 1, 2 or 3
        switch (t) {
            case 1:
                data = generateData1();
                break;
            case 2:
                data = generateData2();
                break;
            case 3:
                data = generateData3();
                break;
            default:
                data = null; // we dont take that
        }
    }

    private HashMap generateData1() { // standard
        HashMap r = new HashMap();
        r.put("NR1", 500);
        r.put("NR2", 454);
        r.put("NR3", 354);
        r.put("NR4", 298);
        r.put("NR5", 238);
        r.put("NR6", 117);
        r.put("NR7", 2);
        return r;
    }

    private HashMap generateData2() { // same values -> position in tree matters
        HashMap r = new HashMap();
        r.put("NR1", 500);
        r.put("NR2", 500);
        r.put("NR3", 354);
        r.put("NR4", 298);
        r.put("NR5", 238);
        r.put("NR6", 117);
        r.put("NR7", 2);
        return r;
    }

    private HashMap generateData3() {
        HashMap r = new HashMap();
        r.put("NR1", 500);
        r.put("NR2", 454);
        r.put("NR3", 354);
        r.put("NR4", 298);
        r.put("NR5", 238);
        r.put("NR6", 117);
        r.put("NR7", 2);
        return r;
    }
}
