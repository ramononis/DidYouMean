package autocomplete;

import tree.Root;
import database.IDBControl;

import java.util.List;
import java.util.Map;

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
     * Initializes new AutoCompleter
     * @param idb the database control.
     */
    public AutoCompleter(IDBControl idb) {
        this.DB = idb;
        makeTree();
    }

    /**
     * Rebuilds the tree from scratch, also putting in any new data from the database control.
     */
    public void resetTree(){
        makeTree();
    }

    /**
     * Gets data from {@link IDBControl#getData()}, creates a new {@link Root}
     * and uses {@link Root#addOrIncrementWord(String, int)} to make a tree of this data.
     */
    private void makeTree() {
        Map<String, Integer> data = DB.getData();
        tree = new Root();

        if (data == null) {
            return;
        }

        data.entrySet().forEach(entry -> tree.addOrIncrementWord(entry.getKey(), entry.getValue()));
    }

    /**
     * Learns a keyword: adds the keyword to the tree if it doesn't exist yet, or increments the weight if it already exists.
     * @param keyword the keyword that should be learned.
     * @param weight the amount the weight of the keyword will be incremented.
     */
    public void learn(String keyword, int weight){
        tree.addOrIncrementWord(keyword, weight);
    }

    /**
     * The public method for requesting a top N best completions.
     *
     * @param k     the number of completions that are requested.
     * @param query the prefix that is currently in the search bar.
     * @return a String Array of length k with the best suggestions for the prefix query.
     */
    public String[] getTopN(int k, String query) {
        List<String> top = getTopKeywords(tree, k, query);
        return top.toArray(new String[top.size()]);
    }
}
