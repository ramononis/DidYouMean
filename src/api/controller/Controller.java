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

    private static final int DEFAULT_LD_WEIGHT = 6;

    /**
     * Initializes a new Controller
     *
     * @param dbController the dbController that should be used.
     */
    public Controller(IDBControl dbController) {
        dym = new DidYouMean(dbController, api.didyoumean.DYM.BKTREE, DEFAULT_LD_WEIGHT);
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
        if (r.length < n) {
            String d = getDYM(searchterm);
            if(!d.equals("")) {
                String[] rnew = new String[n];
                System.arraycopy(r, 0, rnew, 0, r.length);
                rnew[r.length] = d;

                int j = r.length + 1; //number of items in rnew
                if (j < n) {
                    String[] r2 = getTopN(n - (r.length), d);
                    for (int i = 0; i < r2.length; i++) {
                        if (j < n && !r2[i].equals(d)) {
                            rnew[j] = r2[i];
                            j++;
                        }
                    }
                }
                r = new String[j];
                System.arraycopy(rnew, 0, r, 0, j);
            }
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
        dym.learn(keyword, weight);
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
     * Calls {@link DidYouMean#getDYM_N(String, int)}
     *
     * @param searchstring The user's search string.
     * @param n            The maximum number of suggestions to return.
     * @return A list with at most n words the user probably meant when searching for {@code word}.
     * Sorted from most likely to least likely. May include {@code word}, if so this will be the first one.
     * @throws IllegalArgumentException if {@code searchString} is {@code null}.
     */
    public String[] getDYM_N(String searchstring, int n) {
        return dym.getDYM_N(searchstring, n).toArray(new String[]{});
    }

    /**
     * Calls {@link DidYouMean#setMethod(DYM)}
     *
     * @param method The new DYM method.
     */
    public void setDYMMethod(DYM method) {
        dym.setMethod(method);
    }

    /**
     * Calls {@link DidYouMean#setLdWeight(int)}
     *
     * @param weight The new LD weight.
     * @throws IllegalArgumentException if weight is negative.
     */
    public void setDYMWeight(int weight) {
        dym.setLdWeight(weight);
    }
}
