package DidYouMean.levenstheinAutomata;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Tim on 3/9/2016.
 */
public class DFA extends FiniteAutomata{
    private HashSet<State> states;

    /**
     * Creates a new DFA with a given initial state.
     * @param initState The initial state of this DFA.
     */
    public DFA(State initState){
        super(initState);
    }

    /**
     * Assigns scores to the current accepting states (possible words).
     * @param data The data the scores are currently stored in.
     */
    public void setScores(HashMap<String, Integer> data){
        //TODO
    }

    /**
     * Gets the next State according to a given <code>state</code> and <code>letter</code>.
     * @param state The state of the starting State.
     * @param letter The letter that is on the Transition from <code>state</code> to possibly another State.
     * @return The State <code>state</code> connects to via <code>letter</code>. If there is no such State,
     * returns <code>null</code>.
     */
    public State nextState(State state, char letter){
        return null;
    }

    /**
     * Similar to nextState?
     * @param state See nextState
     * @param letter See nextState
     * @return See nextState
     */
    public State getStateByEdge(State state, char letter){
        //HashSet<State> states = getTransitions().getConnectedStatesByState(state);

        return null;
    }

    /**
     * Gets the String that is most likely to be the meant String, given a current state and a character.
     * @param dfa The DFA we are currently in.
     * @param state The current state.
     * @param x The letter which is up next.
     * @return The String most likely to be searched for, given previous conditions.
     */
    public String findNextEdge(DFA dfa, State state, char x){
        return null;
    }

    /**
     * Gets the current set of states.
     * @return The current set of states. Is never empty, as a DFA is initialized with a starting state.
     */
    public HashSet<State> getStates() {
        return states;
    }
}
