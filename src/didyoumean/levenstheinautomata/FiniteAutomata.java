package didyoumean.levenstheinautomata;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tim on 3/8/2016.
 */
public class FiniteAutomata {


    private State initState;
    private TransitionSet transitions;
    private Set<State> acceptingStates;
    private Set<State> states;

    /**
     * Creates a new Finite Automata with a given initial State.
     * @param initState The initial state this Finite Automata should have.
     */
    public FiniteAutomata(State initState){
        this.initState = initState;
        this.initState.setAcceptingState(true);
        acceptingStates = new HashSet<>();
        states = new HashSet<>();
        transitions = new TransitionSet();
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
    public Set<State> getAcceptingStates() {
        return acceptingStates;
    }

    /**
     * Checks whether or not a State is an accepting state.
     * @param state The State to be checked.
     * @return Whether State {@code state} is an accepting state.
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

    /**
     * Returns the list of States this FA has.
     * @return The list of States of this FA.
     */
    public Set<State> getStates() {
        return states;
    }

    /**
     * Creates a new State in this FA. If there already is such a state in position (n, e), returns
     * the state that already exists.
     * @param n The consumed letters so far.
     * @param e The errors encountered so far.
     * @return A new (n, e, 0, false) State if there was no such state, else the existing State corresponding to (n,e)
     */
    public State createState(int n, int e){
        State state = new State(n, e);
        boolean isNew = true;
        for(State s : getStates()){
            if(s.equals(state)){
                state = s;
                isNew = false;
            }
        }
        if(isNew){
            getStates().add(state);
        }
        return state;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("Initial state: ");
        sb.append(getInitState());
        sb.append("\nAccepting states");
        for(State s : getAcceptingStates()){
            sb.append(", ").append(s);
        }
        sb.append("\nAll states");
        for(State s : getStates()){
            sb.append(", ").append(s);
        }
        sb.append("\nTransitions");
        for(State s : getTransitions().getTransitions().keySet()){
            for(Transition t : getTransitions().getTransitions().get(s)){
                sb.append(", ").append(t);
            }
        }
        return sb.toString();
    }
}
