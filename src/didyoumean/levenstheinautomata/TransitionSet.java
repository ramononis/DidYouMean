package didyoumean.levenstheinautomata;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Tim on 3/8/2016.
 */
public class TransitionSet {

    private Map<State, Set<Transition>> transitions;

    public TransitionSet() {
        transitions = new HashMap<State, Set<Transition>>();
    }

    /**
     * Gets a set of transitions that have <code>state</code> as its start.
     * @param state The state the transitions are sought from.
     * @return Set of transitions that start from <code>state</code>. May be empty.
     */
    public Set<Transition> getTransitionsByState(State state){
       Set<Transition> result = transitions.get(state);
        if(result == null) {
            result = new HashSet<Transition>();
        }
        return result;
    }

    /**
     * Gets a set of states that are connected to <code>state</code>, with <code>state</code>
     * being the start state.
     * @param state The State that the connected States are sought from.
     * @return Set of states connected to <code>state</code>. May be empty.
     */
    public Set<State> getConnectedStatesByState(State state){
        return getTransitionsByState(state).stream().map(Transition::getToState).collect(Collectors.toSet());
    }

    /**
     * Gets the next State of a State <code>state</code> when applying letter <code>letter</code>.
     * @param state The starting State.
     * @param letter The letter that connects <code>state</code> with another State.
     * @return The state that connects with <code>state</code> if there is one, <code>null</code> if there
     * is no such state.
     */
    public State getStateByEdge(State state, char letter){
        Optional<Transition> result =  getTransitionsByState(state)
                .stream()
                .filter(t -> t.hasLetter() && t.getLetter() == letter)
                .findFirst();
        if(result.isPresent()) {
            return result.get().getToState();
        } else {
            return null;
        }
    }

    /**
     * Gets the next States of a State <code>state</code> when applying letter <code>letter</code>.
     * @param state The starting State.
     * @param letter The letter that connects <code>state</code> with another State.
     * @return The state that connects with <code>state</code> if there is one, <code>null</code> if there
     * is no such state.
     */
    public Set<State> getStatesByEdge(State state, char letter){
        return getTransitionsByState(state)
                .parallelStream()
                .filter(t -> (t.hasLetter() && t.getLetter() == letter) || t.getToken() == Token.ANY)
                .map(Transition::getToState)
                .collect(Collectors.toSet());
    }

    /**
     * Adds a Transition to this set of Transitions.
     * @param transition The Transition to be added.
     * @return Whether the addition completed successfully.
     */
    public boolean add(Transition transition){
        return transitions.get(transition.getFromState()).add(transition);
    }

    /**
     * Gets the current set of Transitions.
     * @return The set of Transitions.
     */
    public Map<State, Set<Transition>> getTransitions() {
        return transitions;
    }

    /**
     * Sets the current set of Transitions to a given set.
     * @param transitions A set of Transitions this TransitionSet should have.
     */
    public void setTransitions(Map<State, Set<Transition>> transitions) {
        this.transitions = transitions;
    }


}
