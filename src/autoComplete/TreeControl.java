package autoComplete;

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
        makeTree(DB.getData());
    }

    public void makeTree(HashMap<String, Integer> elements) {

    }

    public void add(HashMap<String, Integer> elements) {

    }

    public List<String> find(int k) {

        return null;
    }
}
