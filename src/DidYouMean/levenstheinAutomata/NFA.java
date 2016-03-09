package DidYouMean.levenstheinAutomata;

import java.util.HashSet;

/**
 * Created by Tim on 3/9/2016.
 */
public class NFA extends FiniteAutomata{

    private HashSet<State> acceptingStates;

    public NFA(State initState){
        super(initState);
    }

    public NFA(int n, int e){
        super(n, e);
    }

    public boolean addTransition(Transition transition){
        return getTransitions().add(transition);
    }

    public void addFinalState(State state){
        state.setAcceptingState(true);
        getAcceptingStates().add(state);
    }

    public HashSet<State> getAcceptingStates() {
        return acceptingStates;
    }

    public void setAcceptingStates(HashSet<State> acceptingStates) {
        this.acceptingStates = acceptingStates;
    }
}
