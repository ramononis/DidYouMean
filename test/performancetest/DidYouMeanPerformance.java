package performancetest;

import api.database.CSVControl;
import api.didyoumean.DYM;
import api.didyoumean.DidYouMean;
import api.didyoumean.bktree.BKTree;
import gui.GUI;

/**
 * Created by ramon on 29-3-2016.
 */
public class DidYouMeanPerformance {
    public static void main(String[] args) {
        DidYouMean dym = new DidYouMean(new CSVControl(GUI.FILENAMES), DYM.BKTREE, 6);
        testString(dym, "akku 12v", 1000);
        testString(dym, "batterij", 1000);
    }
    private static void testString(DidYouMean dym, String s, int n) {
        dym.setMethod(DYM.BKTREE);
        testNTimes(dym, s, n);
        dym.setMethod(DYM.LEVENSHTEIN);
        testNTimes(dym, s, n);
    }
    private static void testNTimes(DidYouMean dym, String s, int n) {
        long time = System.currentTimeMillis();
        int i = 0;
        while(i++ < n) {
            dym.getDYM(s);
        }
        time -= System.currentTimeMillis();
        time *= -1;
        System.out.println(dym.getMethod() + " " + s + " " + n + " times. " + time + " ms");
    }
}
