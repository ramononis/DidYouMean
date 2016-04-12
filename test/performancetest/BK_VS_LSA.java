package performancetest;

import api.database.IDBControl;
import api.didyoumean.DYM;
import api.didyoumean.DidYouMean;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Tim on 11-4-2016.
 */
public class BK_VS_LSA {

    public static void main(String[] args) throws IOException {
        IDBControl dbc = new TestPurposeDBC();
        DidYouMean dymBK = new DidYouMean(dbc, DYM.BKTREE, 6);
        DidYouMean dymLSA = new DidYouMean(dbc, DYM.LEVENSHTEIN, 6);

        Set<String> queries = new HashSet<>();
        Random r = new Random();
        for (String key : dbc.getData().keySet()) {
            int a = r.nextInt(5);
            int b = r.nextInt(5);
            StringBuilder str = new StringBuilder(key);
            if (a < key.length()) str.deleteCharAt(a);
            if (b < key.length() - 1) str.deleteCharAt(b);
            String x = str.toString();
            queries.add(x);
        }

        long start;
        long end;
        long deltaBK;
        long deltaLSA;

        System.out.println("Query, BK, LSA");
        for (String q : queries) {
            start = System.currentTimeMillis();
            dymBK.getDYM(q);
            end = System.currentTimeMillis();
            deltaBK = end - start;

            start = System.currentTimeMillis();
            dymLSA.getDYM(q);
            end = System.currentTimeMillis();
            deltaLSA = end - start;

            System.out.println(q + ", " + deltaBK + ", " + deltaLSA);
        }
    }
}
