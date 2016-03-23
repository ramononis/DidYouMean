package api.controller;

import api.autocomplete.AutoCompleter;
import api.database.IDBControl;
import api.didyoumean.DYM;
import api.didyoumean.DidYouMean;

/**
 * Main api.controller of the API.
 * Contains all accessable functions of AutoCompleter and DidYouMean.
 *
 * @author Tim
 */
public class Controller {
    private AutoCompleter AC;
    private DidYouMean DYM;
    private IDBControl DB;

    /**
     * Initializes a new Controller
     * @param dbController the dbController that should be used.
     */
    public Controller(IDBControl dbController) {
        DB = dbController;
        DYM = new DidYouMean(DB, api.didyoumean.DYM.BKTREE);
        AC = new AutoCompleter(DB);
    }

    // AC functions

    /**
     * Gets the top n suggestions for a given prefix. If it can't find anything, it uses did-you-mean on the searchterm and then tries again.
     * Uses {@link AutoCompleter#getTopN(int, String)} and {@link DidYouMean#getDYM(String)}
     * @param n the number of suggestions it should give.
     * @param searchterm the prefix so far.
     * @return a list of length n with suggested completions.
     */
    public String[] getAdvancedTopN(int n, String searchterm) {
        String[] r = AC.getTopN(n, searchterm);
        if (r.length == 0) {
            r = AC.getTopN(n, DYM.getDYM(searchterm));
        }
        return r;
    }

    public String[] getTopN(int n, String searchterm) {
        return AC.getTopN(n, searchterm);
    }

    public void resetTree() {
        AC.resetTree();
    }

    public void learn(String keyword, int weight) {
        AC.learn(keyword, weight);
    }

    // DYM functions
    public String getDYM(String searchstring) {
        return DYM.getDYM(searchstring);
    }

    public void setDYMMethod(api.didyoumean.DYM method) {
        DYM.setMethod(method);
    }
}
