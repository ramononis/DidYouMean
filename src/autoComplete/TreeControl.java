package autoComplete;

import autoComplete.tree.Element;
import autoComplete.tree.Node;
import autoComplete.tree.Root;
import database.IDBControl;
import util.TupleStringInt;

import java.util.HashMap;
import java.util.List;

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

    public void add(HashMap<String, Integer> elements) {

    }

    public List<String> find(int k) {

        return null;
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

        if(!n.hasChild(k.charAt(0))){
            new Node(k.charAt(0), (Node) n);
        }

        n.setWeight(Math.max(n.getWeight(), increment(n.getChild(k.charAt(0)), k.substring(1), d)));

        return n.getWeight();
    }

}
