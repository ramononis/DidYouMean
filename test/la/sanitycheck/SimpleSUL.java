package la.sanitycheck;

import de.learnlib.api.SUL;
import de.learnlib.api.SULException;

/**
 * A wrapper class for a Wolf-goat-cabbage implementation
 * See the SUL interface for more info:
 * https://github.com/LearnLib/learnlib/blob/develop/core/src/main/java/de/learnlib/api/SUL.java
 */
public class SimpleSUL implements SUL<String, String> {

    private String state;
    private String result;
    @Override
    public void pre() {
        state = result = "";
    }

    @Override
    public void post() {
        state = result = "";
    }

    @Override
    public String step(String input) throws SULException {
        if(state.contains(input) || result.equals("fail")) {
            return result = "fail";
        }
        state += input;
        if(state.chars().distinct().count() == 3) {
            return "done";
        }
        return "ok";
    }
}

