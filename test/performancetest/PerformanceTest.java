package performancetest;

import api.controller.Controller;
import api.database.IDBControl;

import java.io.IOException;

/**
 * Created by Tim on 30-3-2016.
 */
public class PerformanceTest {

    public static void speedTest() {
        System.out.println("Creating the test purpose DBC...");
        IDBControl DB = new TestPurposeDBC();
        System.out.println("Done");
        int n = 100;
        double percentage;
        long startTime = System.currentTimeMillis();
        long time = System.currentTimeMillis();

        System.out.println("Creating " + n + " controllers...");
        for (int i = 0; i < n; i++) {
            new Controller(DB);
            percentage = i * 100 / n;
            if (System.currentTimeMillis() - time > 1000) {
                System.out.println(percentage + "%");
            }
        }
        System.out.println("Done in " + (double) ((System.currentTimeMillis() - startTime) / 1000) + " seconds!");
    }

    public static void main(String[] args) throws IOException {
//        speedTest();
        System.in.read();
        TestPurposeDBC tdb = new TestPurposeDBC();
        System.in.read();
        Object o = new Controller(tdb);
        System.in.read();
        tdb.empty();
        System.out.println("object made");
        System.in.read();
    }
}
