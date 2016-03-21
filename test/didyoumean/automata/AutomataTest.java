package didyoumean.automata;

import didyoumean.levenstheinautomata.*;
import org.junit.Before;

/**
 * Created by Tim on 3/21/2016.
 */
public class AutomataTest {

    State state;
    Transition transition;
    NFA nfa;
    DFA dfa;

    @Before
    public void setup(){
        state = new State();
        transition = new Transition(new State(0,0), Token.ANY, new State(0,1));
        nfa = new NFA(new State(0,0));
        dfa = new DFA(new State(0,0));
    }

    //-----State.java testing-------


}
