package api.didyoumean;

import api.didyoumean.bktree.BKTree;
import api.didyoumean.levenshteinautomata.*;
import api.database.IDBControl;
import api.tree.Root;

/**
 * The main class that holds all the information about the BK-trees and Levenshtein Automata.
 * Created by Tim on 3/9/2016.
 */
public class DidYouMean {
    private IDBControl databaseController;
    private BKTree tree;

    private DYM method;
    private Root root;
    private static final int MAX_DISTANCE = 3;
    private LevenshteinAutomataFactory laf;

    /**
     * Creates a new DidYouMean, with a given database and did-you-mean data structure.
     * @param idbControl The controller which connects to the database.
     * @param method The method the DYM should be getting its values with.
     */
    public DidYouMean(IDBControl idbControl, DYM method) {
        this.databaseController = idbControl;
        this.method = method;
        tree = new BKTree();
        setup();
    }

    /**
     * Gets the data from the api.database, and saves it in this Class.
     * Also creates a new api.tree from the data (takes a few seconds).
     */
    public void setup() {
        switch(method) {
            case LEVENSHTEIN:
                root = new Root();
                databaseController.getData().entrySet().forEach(
                        entry -> root.addOrIncrementWord(entry.getKey(), entry.getValue())
                );
                laf = new LevenshteinAutomataFactory(MAX_DISTANCE);
                break;
            case BKTREE:
                getTree().buildTree(databaseController.getData());
                break;
        }
    }

    /**
     * Gets the final did-you-mean suggestion, given a search string.
     *
     * @param searchString The user's search string.
     * @return The String the user most likely meant to type. May be equal to the given search string.
     * @throws IllegalArgumentException if {@code searchString} is {@code null}.
     */
    public String getDYM(String searchString) {
        if (searchString == null) {
            throw new IllegalArgumentException("Search string is null in DidYouMean.getDYMFromString.");
        } else if (method == DYM.BKTREE) {
            return getTree().getDYM(searchString);
        } else if (method == DYM.LEVENSHTEIN) {
            return LevenshteinAutomata.intersect(root, laf, searchString, 1).get(0);
        } else {
            return null;
        }
    }

    /**
     * Sets the current DYM method of this class.
     * @param method The new DYM method of this class.
     */
    public void setMethod(DYM method) {
        this.method = method;
    }

    /**
     * Returns the current main tree of this class.
     * @return The current tree of this class.
     */
    public BKTree getTree() {
        return tree;
    }


}
