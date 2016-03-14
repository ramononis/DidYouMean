package autoComplete;

import autoComplete.tree.Element;
import autoComplete.tree.Root;
import autoComplete.tree.Node;
import com.sun.xml.internal.fastinfoset.util.StringArray;
import database.CSVControl;
import database.IDBControl;

import util.TupleStringInt;

import java.util.*;

/**
 * Created by Tim on 23-2-2016.
 */
public class AutoCompleter {
    public final static char TERM = '\0';
    private IDBControl DB;
    private Root tree = new Root();

    public AutoCompleter() {
        this.DB = new CSVControl();
        makeTree();
    }

    public void makeTree() {
        HashMap<String, Integer> data = DB.getData();

        for (String key : data.keySet()) {
            increment(tree, key, data.get(key));
        }
    }

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
        String result = null;
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
        Set<Element> result = new HashSet<Element>();
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
        List<String> result = new ArrayList<String>(c);
        Element n = searchElement(r, p);
        if (n == null) {
            return result;
        }
        Set<Element> searchNodes = new HashSet<Element>();
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
    public List<String> find(int k) {

        return null;
    }
}
