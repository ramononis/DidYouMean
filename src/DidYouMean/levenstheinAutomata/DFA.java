package DidYouMean.levenstheinAutomata;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Tim on 3/9/2016.
 */
public class DFA extends FiniteAutomata{
    private HashSet<State> states;
    private HashSet<State> acceptingStates;

    public DFA(State initState){
        super(initState);
        acceptingStates = new HashSet<>();
    }

    public void setScores(HashMap<String, Integer> data){
        //TODO
    }

    public DFA(int n, int e){
        super(n, e);
    }

    public State nextState(State state, char letter){
        return null;
    }

    public State getStateByEdge(State state, char letter){
        //HashSet<State> states = getTransitions().getConnectedStatesByState(state);

        return null;
    }

    public void setFinalState(State state){
        state.setAcceptingState(true);
        acceptingStates.add(state);
    }

    public HashSet<State> getAcceptingStates() {
        return acceptingStates;
    }

    public boolean isFinalState(State state){
        return getAcceptingStates().contains(state);
    }

    public String findNextEdge(DFA dfa, State state, char x){
        return null;
    }

    public HashSet<State> getStates() {
        return states;
    }
}
