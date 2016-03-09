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

    public HashSet<Transition> getTransitionsByState(State state){
       HashSet<Transition> resultSet = new HashSet<>();
        for (Transition trans : getTransitions()) {
            if(trans.getFromPos().equals(state)){
                resultSet.add(trans);
            }
        }
        return resultSet;
    }

    public HashSet<State> getConnectedStatesByState(State state){
        HashSet<State> resultSet = new HashSet<>();
        // FIXME: 3/9/2016
        return resultSet;
    }

    public State getStateByEdge(State state, char letter){
        //this is the "nextState" function in the pseudo code algorithm of findNextValidString.
        return null;
    }


    public boolean add(Transition transition){
        return getTransitions().add(transition);
    }

    public void setTransitionSet(HashSet<Transition> transitions){
        this.transitions = transitions;
    }

    public HashSet<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(HashSet<Transition> transitions) {
        this.transitions = transitions;
    }
}
