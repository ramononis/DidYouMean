package autocomplete;

import autocomplete.tree.Root;
import database.CSVControl;
import database.IDBControl;

import java.util.*;

import static autocomplete.Algorithm.getTopKeywords;

/**
 * Main class for auto-completion. The GUI has an instance of this class and requests suggested completions.
 * The AutoCompleter class uses IDBControl, a tree (made of Elements) and several private functions to give the suggested completions.
 *
 * @author Tim
 */
public class AutoCompleter {
    private IDBControl DB;
    private Root tree;

    /**
     * Initializes a new AutoCompleter.
     */
    public AutoCompleter() {
        this.DB = new CSVControl();
        makeTree();
    }

    /**
     * Gets data from {@link IDBControl#getData()}, creates a new {@link Root}
     * and uses {@link Root#addOrIncrementWord(String, int)} to make a tree of this data.
     */
    private void makeTree() {
        HashMap<String, Integer> data = DB.getData();

        tree = new Root();

        data.entrySet().forEach(entry -> tree.addOrIncrementWord(entry.getKey() + Algorithm.TERM, entry.getValue()));
    }

    /**
     * The public method for requesting a top N best completions.
     *
     * @param k     the number of completions that are requested.
     * @param query the prefix that is currently in the search bar.
     * @return a String Array of length k with the best suggestions for the prefix query.
     */
    public String[] getTopN(int k, String query) {
        return getTopKeywords(tree, k, query).toArray(new String[k]);
    }
}
