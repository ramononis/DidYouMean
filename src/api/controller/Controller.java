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
    private final AutoCompleter ac;
    private final DidYouMean dym;

    /**
     * Initializes a new Controller
     *
     * @param dbController the dbController that should be used.
     */
    public Controller(IDBControl dbController) {
        dym = new DidYouMean(dbController, api.didyoumean.DYM.BKTREE);
        ac = new AutoCompleter(dbController);
    }

    // ac functions

    /**
     * Gets the top n suggestions for a given prefix. If it can't find anything, it uses did-you-mean on the searchterm and then tries again.
     * Uses {@link AutoCompleter#getTopN(int, String)} and {@link DidYouMean#getDYM(String)}
     *
     * @param n          the number of suggestions it should give.
     * @param searchterm the prefix so far.
     * @return a list of length n with suggested completions.
     */
    public String[] getAdvancedTopN(int n, String searchterm) {
        String[] r = getTopN(n, searchterm);
        if (r.length == 0) {
            r = getTopN(n, getDYM(searchterm));
        }
        return r;
    }

    /**
     * Calls {@link AutoCompleter#getTopN(int, String)}
     *
     * @param n     the number of completions that are requested.
     * @param query the prefix that is currently in the search bar.
     * @return a String Array of length k with the best suggestions for the prefix query.
     */
    public String[] getTopN(int n, String query) {
        return ac.getTopN(n, query);
    }

    /**
     * Calls {@link AutoCompleter#resetTree()}
     */
    public void resetTree() {
        ac.resetTree();
    }

    /**
     * Calls {@link AutoCompleter#learn(String, int)}
     *
     * @param keyword the keyword that should be learned.
     * @param weight  the amount the weight of the keyword will be incremented.
     */
    public void learn(String keyword, int weight) {
        ac.learn(keyword, weight);
    }

    // dym functions

    /**
     * Calls {@link DidYouMean#getDYM(String)}
     *
     * @param searchstring The user's search string.
     * @return The String the user most likely meant to type. May be equal to the given search string.
     * @throws IllegalArgumentException if {@code searchString} is {@code null}.
     */
    public String getDYM(String searchstring) {
        return dym.getDYM(searchstring);
    }

    /**
     * Calls {@link DidYouMean#setMethod(DYM)}
     *
     * @param method The new DYM method.
     */
    public void setDYMMethod(DYM method) {
        dym.setMethod(method);
    }
}
