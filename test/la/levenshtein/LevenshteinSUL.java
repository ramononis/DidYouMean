package la.levenshtein;

import api.didyoumean.levenshteinautomata.LevenshteinAutomataFactory;
import api.didyoumean.levenshteinautomata.State;
import api.utils.Pair;
import de.learnlib.api.SUL;
import de.learnlib.api.SULException;

/**
 * A wrapper class for a Wolf-goat-cabbage implementation
 * See the SUL interface for more info:
 * https://github.com/LearnLib/learnlib/blob/develop/core/src/main/java/de/learnlib/api/SUL.java
 */
public class LevenshteinSUL implements SUL<String, String> {


    private final int distance;
    private final String word;
    private LevenshteinAutomataFactory factory;
    private State state;

    public LevenshteinSUL(String w, int d) {
        word = w;
        distance = d;
    }

    @Override
    public void pre() {
        factory = new LevenshteinAutomataFactory(distance);
        state = factory.getInit();
    }

    @Override
    public void post() {
        factory = null;
    }

    @Override
    public String step(String input) throws SULException {
        if (state == null) {
            return "fail";
        }
        state = state.outState(input.charAt(0), word);
        if (state == null) {
            return "fail";
        } else if (state.isAcceptingState(word.length())) {
            return "success";
        } else {
            return "ok";
        }
    }
}

