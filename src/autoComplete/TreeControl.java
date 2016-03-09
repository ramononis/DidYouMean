package autoComplete;

import autoComplete.tree.Element;
import autoComplete.tree.Root;
import autoComplete.tree.Node;
import database.IDBControl;

import util.TupleStringInt;

import java.util.*;

/**
 * Created by Tim on 23-2-2016.
 */
public class TreeControl {
    private IDBControl DB;
    private Root tree = null;

    public TreeControl(IDBControl db) {
        this.DB = db;
        makeTree();
    }

    public void makeTree() {
        HashMap<String,Integer> data = DB.getData();

        for (String key : data.keySet()){
            increment(tree, key, data.get(key));
        }
    }

    private String maxNode1(Set<Element> ns, String p) {
        int maxWeight = -1;
        Element maxN = null;
        String result = null;
        for(Element n : ns) {
            if(n.getWeight() > maxWeight) {
                maxWeight = n.getWeight();
                maxN = n;
            }
        }
        if(maxN.getLetter() == 0) {
            result = p + 0;
        } else {
            result = maxNode1(maxN.getChildren(), p + maxN.getLetter());
        }
        return result;
    }

    private String maxNode2(Map<Element, String> ns) {
        int maxWeight = -1;
        Element maxN = null;
        String result = null;
        for(Element n : ns.keySet()) {
            if(n.getWeight() > maxWeight) {
                maxWeight = n.getWeight();
                maxN = n;
            }
        }
        if(maxN.getLetter() == 0) {
            result = ns.get(maxN) + 0;
        } else {
            result = maxNode1(maxN.getChildren(), ns.get(maxN) + maxN.getLetter());
        }
        return result;
    }
    private Map<Element, String> excludeKeyword(Element n, String k, String p) {
        Map<Element, String> result = new HashMap<Element, String>();
        for(Element child : n.getChildren()) {
            if(child.getLetter() == k.charAt(0)) {
                result.putAll(excludeKeyword(child, k.substring(1), p + child.getLetter()));
            } else {
                result.put(child, p);
            }
        }
        return result;
    }

    private Element searchElement(Element n, String p) {
        if(p.isEmpty()) {
            return n;
        }
        if(n.hasChild(p.charAt(0))) {
            return searchElement(n.getChild(p.charAt(0)), p.substring(1));
        } else {
            return null;
        }
    }
    /**
     * Increments the weight of the given keyword, creates the keyword if it doesn't exist yet.
     * @param n the root node.
     * @param k the keyword.
     * @param d the weight to add.
     * @return the new weight of the keyword.
     */
    private int increment(Element n, String k, int d){
        if(k.equals("")){
            n.setWeight(n.getWeight()+d);
            return n.getWeight();
        }
        Element child = ((Node) n).addNewChild(k.charAt(0));
        n.setWeight(Math.max(n.getWeight(), increment(n.getChild(k.charAt(0)), k.substring(1), d)));

        return n.getWeight();
    }
    private List<String> getTopKeywords(Root r, int c, String p) {
        List<String> result = new ArrayList<String>(c);
        Element n = searchElement(r, p);
        if(n == null) {
            return result;
        }
        Map<Element, String> searchNodes = new HashMap<Element, String>();
        searchNodes.put(n,p);
        int i = 0;
        while(i++ < c && !searchNodes.isEmpty()) {
            String keyword = maxNode2(searchNodes);
            result.add(keyword);
            for(Map.Entry<Element, String> entry : searchNodes.entrySet()) {
                if(keyword.startsWith(entry.getValue())) {
                    searchNodes.remove(entry.getKey());
                    searchNodes.putAll(excludeKeyword(entry.getKey(), keyword.replace(entry.getValue(), ""), entry.getValue()));
                    break; //i'm sorry, don't kill me for using break...
                }
            }
        }
        return result;
    }


    public void add(HashMap<String, Integer> elements) {

    }

    public List<String> find(int k) {

        return null;
    }
}
