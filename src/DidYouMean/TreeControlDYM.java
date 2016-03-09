package DidYouMean;

import autoComplete.tree.Node;
import autoComplete.tree.Root;
import database.CSVControl;
import database.IDBControl;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Tim on 3/9/2016.
 */
public class TreeControlDYM {

    Root root = new Root();

    private IDBControl databaseController;

    public TreeControlDYM(IDBControl db){
        databaseController = db;
    }
    public void buildTree(){
        //TODO when building, don't forget to add the scores
    }

    public void addWord(Root root, String word){

    }

    public HashSet<Node> searchTree(Root root, String string, int errorRange){
        return null;
    }
}
