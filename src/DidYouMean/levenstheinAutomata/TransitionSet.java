package DidYouMean.levenstheinAutomata;

import java.util.HashSet;

/**
 * Created by Tim on 3/8/2016.
 */
public class TransitionSet {

    private HashSet<Transition> transitions;
    public TransitionSet(){
        transitions = new HashSet<>();
    }

    /**
     * Gets a set of transitions that have <code>state</code> as its start.
     * @param state The state the transitions are sought from.
     * @return Set of transitions that start from <code>state</code>. May be empty.
     */
    public HashSet<Transition> getTransitionsByState(State state){
       HashSet<Transition> resultSet = new HashSet<>();
        for (Transition trans : getTransitions()) {
            if(trans.getFromState().equals(state)){
                resultSet.add(trans);
            }
        }
        return resultSet;
    }

    /**
     * Gets a set of states that are connected to <code>state</code>, with <code>state</code>
     * being the start state.
     * @param state The State that the connected States are sought from.
     * @return Set of states connected to <code>state</code>. May be empty.
     */
    public HashSet<State> getConnectedStatesByState(State state){
        HashSet<State> resultSet = new HashSet<>();
        for (Transition trans : getTransitionsByState(state)){
            resultSet.add(trans.getToState());
        }
        return resultSet;
    }

    /**
     * Gets the next State of a State <code>state</code> when applying letter <code>letter</code>.
     * @param state The starting State.
     * @param letter The letter that connects <code>state</code> with another State.
     * @return The state that connects with <code>state</code> if there is one, <code>null</code> if there
     * is no such state.
     */
    public State getStateByEdge(State state, char letter){
        //this is the "nextState" function in the pseudo code algorithm of findNextValidString.
        HashSet<Transition> transSet = getTransitionsByState(state);
        State resState = null;
        for(Transition trans : transSet){
            if(trans.hasLetter() && trans.getLetter() == letter){
                resState = trans.getToState();
            }
        }
        return resState;
    }

    /**
     * Adds a Transition to this set of Transitions.
     * @param transition The Transition to be added.
     * @return Whether the addition completed successfully.
     */
    public boolean add(Transition transition){
        return getTransitions().add(transition);
    }

    /**
     * Gets the current set of Transitions.
     * @return The set of Transitions.
     */
    public HashSet<Transition> getTransitions() {
        return transitions;
    }

    /**
     * Sets the current set of Transitions to a given set.
     * @param transitions A set of Transitions this TransitionSet should have.
     */
    public void setTransitions(HashSet<Transition> transitions) {
        this.transitions = transitions;
    }


}
