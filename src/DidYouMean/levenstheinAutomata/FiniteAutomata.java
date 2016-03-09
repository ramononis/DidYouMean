package DidYouMean.levenstheinAutomata;

import java.util.HashSet;

/**
 * Created by Tim on 3/8/2016.
 */
public class FiniteAutomata {


    private State initState;
    private TransitionSet transitions;
    private HashSet<State> acceptingStates;

    public FiniteAutomata(State initState){
        this.initState = initState;
        acceptingStates = new HashSet<>();
    }

    /**
     * Creates a new FiniteAutomata with starting state (n,e).
     * @param n Amount of letters consumed
     * @param e Amount of errors
     */
    public FiniteAutomata(int n, int e){
        this(new State(n, e, 0, true));
    }

    /**
     * Gets a set of states that are accepting states.
     * @return The set of accepting states. May be empty.
     */
    public HashSet<State> getAcceptingStates() {
        return acceptingStates;
    }

    /**
     * Checks whether or not a State is an accepting state.
     * @param state The State to be checked.
     * @return Whether State <code>state</code> is an accepting state.
     */
    public boolean isFinalState(State state){
        return getAcceptingStates().contains(state);
    }

    /**
     * Sets a State to be an accepting State.
     * @param state The State to be an accepting State.
     * @return Whether the change was recorded correctly.
     */
    public boolean setFinalState(State state){
        state.setAcceptingState(true);
        return getAcceptingStates().add(state);
    }

    /**
     * Gets the initial state of this finite automata.
     * @return initState
     */
    public State getInitState() {
        return initState;
    }

    /**
     * Sets the initial State of this Automata.
     * @param initState The new initial State of this Automata.
     */
    public void setInitState(State initState) {
        this.initState = initState;
    }

    /**
     * Returns a TransitionSet of this Automata.
     * @return The Set of Transitions.
     */
    public TransitionSet getTransitions() {
        return transitions;
    }

    /**
     * Changes the current set of Transitions to a new one.
     * @param transitions The new set of Transitions (class TransitionSet).
     */
    public void setTransitions(TransitionSet transitions) {
        this.transitions = transitions;
    }
}
