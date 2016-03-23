package didyoumean.levenstheinautomata;


import didyoumean.DidYouMean;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Created by Tim on 3/9/2016.
 */
public class DFA extends FiniteAutomata{
    private Set<State> states;

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
    public void setScores(Map<String, Integer> data){
        //TODO
    }

    /**
     * Gets the next State according to a given {@code state} and {@code letter}.
     * @param state The state of the starting State.
     * @param letter The letter that is on the Transition from {@code state} to possibly another State.
     * @return The State {@code state} connects to via {@code letter}. If there is no such State,
     * returns {@code null}.
     */
    public State nextState(State state, char letter){
        State nextState = null;
        if(getTransitions().getStateByEdge(state,letter)!=null){
            nextState = getTransitions().getStateByEdge(state,letter);
        }
        else{
            for(Transition t : this.getTransitions().getTransitionsByState(state)){
                if(t.getToken()==Token.ANY){
                    nextState = t.getToState();
                }
            }
        }
        return nextState;
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
     * @param state The current state.
     * @param x The letter which is up next.
     * @return The String most likely to be searched for, given previous conditions.
     */
    public String findNextEdge(State state, char x){
        return null;
    }

    /**
     * Gets the current set of states.
     * @return The current set of states. Is never empty, as a DFA is initialized with a starting state.
     */
    public Set<State> getStates() {
        return states;
    }


    public ArrayList<String> intersect(DFA dfa){
        ArrayList<String> list = new ArrayList<String>();
        Stack<String> path = new Stack<String>();
        Stack<State> DFA1 = new Stack<State>();
        Stack<State> DFA2 = new Stack<State>();
        DFA1.add(getInitState());
        DFA2.add(dfa.getInitState());
        String pathString = "";
        String pathNext;
        State current1;
        State current2;
        State next1;
        State next2;
        while(!DFA1.isEmpty()||!DFA2.isEmpty()||!path.isEmpty()){
            if(!path.isEmpty()) {
                pathString = path.pop();
            }
            current1 = DFA1.pop();
            current2 = DFA2.pop();
            for(Transition t1: getTransitions().getTransitionsByState(current1)){
                for(Transition t2: dfa.getTransitions().getTransitionsByState(current2) ){
                    if(t1.getLetter() == t2.getLetter()||t1.getToken()==Token.ANY){
                        next1 = nextState(current1,t1.getLetter());
                        next2 = dfa.nextState(current2,t2.getLetter());
                        if(current1 != null && current2!=null){
                            pathNext = String.valueOf(t2.getLetter());
                            path.add(pathString + pathNext);
                            DFA1.add(next1);
                            DFA2.add(next2);
                            if(isFinalState(next1)&& dfa.isFinalState(next2)){
                                if(!list.contains(pathString + pathNext)) {
                                    list.add(pathString + pathNext);
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }
}
