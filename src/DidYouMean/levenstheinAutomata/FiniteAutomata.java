package DidYouMean.levenstheinAutomata;

/**
 * Created by Tim on 3/8/2016.
 */
public class FiniteAutomata {


    private State initState;
    private TransitionSet transitions;

    public FiniteAutomata(State initState){
        this.initState = initState;
    }

    /**
     * Creates a new FiniteAutomata with starting state (n,e).
     * @param n Amount of letters consumed
     * @param e Amount of errors
     */
    public FiniteAutomata(int n, int e){
        this(new State(n, e, 0, true));
    }

    public State getInitState() {
        return initState;
    }

    public void setInitState(State initState) {
        this.initState = initState;
    }

    public TransitionSet getTransitions() {
        return transitions;
    }

    public void setTransitions(TransitionSet transitions) {
        this.transitions = transitions;
    }
}
