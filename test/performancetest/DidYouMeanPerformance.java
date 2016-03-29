package performancetest;

import api.database.CSVControl;
import api.didyoumean.DYM;
import api.didyoumean.DidYouMean;
import gui.GUI;

import java.util.Arrays;

public class DidYouMeanPerformance {
    public static void main(String[] args) {
        DidYouMean dym = new DidYouMean(new CSVControl(GUI.FILENAMES), DYM.BKTREE, 6);
        String[] testStrings = {"akku 12v", "batterij", "akkuclem", "06345", "9182732874", "rukschakelar", "suka", "asdkjhasdkjhf"};
        Arrays.stream(testStrings).forEach(s -> testString(dym, s, 1000));
    }

    private static void testString(DidYouMean dym, String s, int n) {
        dym.setMethod(DYM.BKTREE);
        String bk = testNTimes(dym, s, n);
        dym.setMethod(DYM.LEVENSHTEIN);
        String lev = testNTimes(dym, s, n);
        if (!bk.equals(lev)) {
            System.out.println("BKTree: " + bk + " , Levenshtein: " + lev);
        } else {
            System.out.println("Suggestion: " + bk);
        }
    }

    private static String testNTimes(DidYouMean dym, String s, int n) {
        long time = System.currentTimeMillis();
        int i = 0;
        String result = "";
        while (i++ < n) {
            result = dym.getDYM(s);
        }
        time -= System.currentTimeMillis();
        time *= -1;
        System.out.println(dym.getMethod() + " " + s + " " + n + " times. " + time + " ms");
        return result;
    }
}
