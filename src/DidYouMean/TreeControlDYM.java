package DidYouMean;

import autoComplete.tree.Node;
import autoComplete.tree.Root;
import database.IDBControl;

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

    /**
     * Creates a tree given the data from the database.
     */
    public void buildTree(){
        //TODO when building, don't forget to add the scores
    }

    /**
     * Adds a word to the current tree.
     * @param root The root of the current tree.
     * @param word The word that should be added to the current tree.
     */
    public void addWord(Root root, String word){

    }

    /**
     * Searches the tree for a set of Nodes that correspond to the parameters.
     * @param root The root of the tree.
     * @param string The string that was searched for by the user.
     * @param errorRange The maximum error range when searching for words.
     * @return A set of Nodes that are within error range of the user's search term.
     */
    public HashSet<Node> searchTree(Root root, String string, int errorRange){
        return null;
    }
}
