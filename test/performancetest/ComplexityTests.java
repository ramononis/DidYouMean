package performancetest;

import api.autocomplete.AutoCompleter;
import api.database.IDBControl;
import api.didyoumean.DYM;
import api.didyoumean.DidYouMean;
import api.didyoumean.bktree.BKTree;
import api.didyoumean.levenshteinautomata.LevenshteinAutomataFactory;
import api.tree.Root;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by Tim on 31/03/2016.
 */
public class ComplexityTests {

    public static void main(String[] args) throws IOException {
        System.out.println("Creating necessary objects..");
        IDBControl dbc = new TestPurposeDBC();
        DidYouMean dym0BK = new DidYouMean(dbc, DYM.BKTREE, 6);
        DidYouMean dym0LS = new DidYouMean(dbc, DYM.LEVENSHTEIN, 6);
        System.out.println("Done");

        System.out.println("============================================");

        System.out.println("Time complexity tests"); // ######## Time complexity tests

        Long start;
        Long end;
        long delta;

        System.out.println("\tInitializations"); // ######## Initializations

        start = System.currentTimeMillis();
        AutoCompleter ac = new AutoCompleter(dbc);
        end = System.currentTimeMillis();
        delta = end - start;
        System.out.println("\t\tAC: " + delta + " milliseconds");

        start = System.currentTimeMillis();
        BKTree dymBK = new BKTree();
        dymBK.buildTree(dbc.getData());
        end = System.currentTimeMillis();
        delta = end - start;
        System.out.println("\t\tDYM BKTree: " + delta + " milliseconds");

        start = System.currentTimeMillis();
        LevenshteinAutomataFactory laf = new LevenshteinAutomataFactory(3);
        end = System.currentTimeMillis();
        delta = end - start;
        System.out.println("\t\tLSA Factory (3): " + delta + " milliseconds");

        start = System.currentTimeMillis();
        Root dymLS = new Root();
        for (Map.Entry e : dbc.getData().entrySet()) {
            dymLS.addOrIncrementWord(String.valueOf(e.getKey()), (int) e.getValue());
        }
        end = System.currentTimeMillis();
        delta = end - start;
        System.out.println("\t\tDYM LSA: " + delta + " milliseconds");

        System.out.println("\tSearches"); // ######## Searches

        start = System.currentTimeMillis();
        ac.getTopN(5, "");
        end = System.currentTimeMillis();
        delta = end - start;
        System.out.println("\t\tAC (5 suggestions, empty string): " + delta + " milliseconds");

        start = System.currentTimeMillis();
        dym0BK.getDYM("4634773");
        end = System.currentTimeMillis();
        delta = end - start;
        System.out.println("\t\tDYM BKTree (\"4634773\"): " + delta + " milliseconds");

        start = System.currentTimeMillis();
        dym0LS.getDYM("4634773");
        end = System.currentTimeMillis();
        delta = end - start;
        System.out.println("\t\tDYM LSA (\"4634773\"): " + delta + " milliseconds");

        ac = null;
        dymBK = null;
        dymLS = null;
        laf = null;
        System.gc();

        System.out.println("============================================");

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Space complexity tests"); // ######## Space complexity tests;

        System.out.println("Open java mission control and keep track of the heap");
        int startsize;
        int endsize;
        int deltasize;

        System.out.println("\tInitializations"); // ######## Initializations

        System.out.println("\tPlease enter the size of the heap in MB as start value.");
        startsize = Integer.parseInt(String.valueOf(in.readLine()));
        ac = new AutoCompleter(dbc);
        System.out.println("\t\tPlease enter the size of the heap in MB as end value.");
        endsize = Integer.parseInt(String.valueOf(in.readLine()));
        ac = null;
        System.gc();
        deltasize = endsize - startsize;
        System.out.println("\t\tAC: " + deltasize + " MB");

        System.out.println("\tPlease enter the size of the heap in MB as start value.");
        startsize = Integer.parseInt(String.valueOf(in.readLine()));
        dymBK = new BKTree();
        dymBK.buildTree(dbc.getData());
        System.out.println("\t\tPlease enter the size of the heap in MB as end value.");
        endsize = Integer.parseInt(String.valueOf(in.readLine()));
        dymBK = null;
        System.gc();
        deltasize = endsize - startsize;
        System.out.println("\t\tBK Tree: " + deltasize + " MB");

        System.out.println("\tPlease enter the size of the heap in MB as start value.");
        startsize = Integer.parseInt(String.valueOf(in.readLine()));
        laf = new LevenshteinAutomataFactory(3);
        System.out.println("\t\tPlease enter the size of the heap in MB as end value.");
        endsize = Integer.parseInt(String.valueOf(in.readLine()));
        laf = null;
        System.gc();
        deltasize = endsize - startsize;
        System.out.println("\t\tLevenshtein automata factory: " + deltasize + " MB");

        System.out.println("\tPlease enter the size of the heap in MB as start value.");
        startsize = Integer.parseInt(String.valueOf(in.readLine()));
        dymLS = new Root();
        for (Map.Entry e : dbc.getData().entrySet()) {
            dymLS.addOrIncrementWord(String.valueOf(e.getKey()), (int) e.getValue());
        }
        System.out.println("\t\tPlease enter the size of the heap in MB as end value.");
        endsize = Integer.parseInt(String.valueOf(in.readLine()));
        dymLS = null;
        System.gc();
        deltasize = endsize - startsize;
        System.out.println("\t\tDYM LSA: " + deltasize + " MB");

        System.out.println("============================================");
    }
}
