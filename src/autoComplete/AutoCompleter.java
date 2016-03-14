package autoComplete;

import autoComplete.tree.Element;
import autoComplete.tree.Node;
import autoComplete.tree.Root;
import database.CSVControl;
import database.IDBControl;

import java.util.*;

/**
 * Main class for auto-completion. The GUI has an instance of this class and requests suggested completions.
 * The AutoCompleter class uses IDBControl, a tree (made of Elements) and several private functions to give the suggested completions.
 *
 * @author Tim
 */
public class AutoCompleter {
    public final static char TERM = '\0';
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
     * Gets data from {@link IDBControl#getData()} and uses {@link #increment(Element, String, int)} to make a tree of this data.
     */
    public void makeTree() {
        HashMap<String, Integer> data = DB.getData();

        tree = new Root();

        for (String key : data.keySet()) {
            increment(tree, key, data.get(key));
        }
    }

    /**
     * The public method for requesting a top N best completions.
     * @param k the number of completions that are requested.
     * @param query the prefix that is currently in the search bar.
     * @return a String Array of length k with the best suggestions for the prefix query.
     */
    public String[] getTopN(int k, String query) {
        String[] r = new String[k];
        List<String> suggestions = getTopKeywords(tree, k, query);
        for (int i = 0; i < suggestions.size(); i++) {
            r[i] = suggestions.get(i);
        }
        return r;
    }

    private String maxNode(Set<Element> ns) {
        int maxWeight = -1;
        Element maxN = null;
        String result;
        for (Element n : ns) {
            if (n.getWeight() > maxWeight) {
                maxWeight = n.getWeight();
                maxN = n;
            }
        }
        if (maxN.getLetter() == TERM) {
            result = maxN.getWord();
        } else {
            result = maxNode(maxN.getChildren());
        }
        return result;
    }

    private Set<Element> excludeKeyword(Element n, String k) {
        Set<Element> result = new HashSet<>();
        for (Element child : n.getChildren()) {
            if (child.getLetter() == k.charAt(0)) {
                result.addAll(excludeKeyword(child, k.substring(1)));
            } else {
                result.add(child);
            }
        }
        return result;
    }

    private Element searchElement(Element n, String p) {
        if (p.isEmpty()) {
            return n;
        }
        if (n.hasChild(p.charAt(0))) {
            return searchElement(n.getChild(p.charAt(0)), p.substring(1));
        } else {
            return null;
        }
    }

    /**
     * Increments the weight of the given keyword, creates the keyword if it doesn't exist yet.
     *
     * @param n the root node.
     * @param k the keyword.
     * @param d the weight to add.
     * @return the new weight of the keyword.
     */
    private int increment(Element n, String k, int d) {
        if (k.equals("")) {
            n.setWeight(n.getWeight() + d);
            return n.getWeight();
        }
        Element child = ((Node) n).addNewChild(k.charAt(0));
        n.setWeight(Math.max(n.getWeight(), increment(n.getChild(k.charAt(0)), k.substring(1), d)));

        return n.getWeight();
    }

    private List<String> getTopKeywords(Root r, int c, String p) {
        p = p.toLowerCase();
        List<String> result = new ArrayList<>(c);
        Element n = searchElement(r, p);
        if (n == null) {
            return result;
        }
        Set<Element> searchNodes = new HashSet<>();
        searchNodes.addAll(n.getChildren());
        int i = 0;
        while (i++ < c && !searchNodes.isEmpty()) {
            String keyword = maxNode(searchNodes);
            result.add(keyword);
            for (Element e : searchNodes) {
                String word = e.getWord();
                if (keyword.startsWith(word)) {
                    searchNodes.remove(e);
                    searchNodes.addAll(excludeKeyword(e, keyword.replaceFirst(word,"")));
                    break; //i'm sorry, don't kill me for using break...
                }
            }
        }
        return result;
    }
}
