package didyoumean;

import didyoumean.bktree.BKTree;
import didyoumean.levenstheinautomata.*;
import database.CSVControl;
import database.IDBControl;

import java.util.*;

/**
 *
 * Created by Tim on 3/9/2016.
 */
public class DidYouMean {

    private static final String[] FILENAMES = {"./csv/Data1.csv", "./csv/Data2.csv", "./csv/Data3.csv", "./csv/Data4.csv"};

    private IDBControl databaseController = new CSVControl(FILENAMES);
    private BKTree tree;

    public DidYouMean(){
        tree = new BKTree();
        setup();
    }

    /**
     * Creates a NFA from a given word.
     * @param word The word the NFA should be made from.
     * @param errorRange The error range that the words should be in.
     * @return The NFA made from the {@code word}.
     */
    protected NFA makeNFAFromWord(String word, int errorRange){
        NFA nfa = new NFA(0,0);
        int i = 0;
        for(char letter : word.toCharArray()){
            for(int e = 0; e <= errorRange; e++){
                nfa.addTransition(new Transition(nfa.createState(i, e),
                        Token.LETTER,
                        nfa.createState(i + 1, e), letter));
                if(e < errorRange){
                    //insertion
                    nfa.addTransition(new Transition(nfa.createState(i, e),
                            Token.ANY,
                            nfa.createState(i, e + 1)));
                    //deletion
                    nfa.addTransition(new Transition(nfa.createState(i, e),
                            Token.LAMBDA,
                            nfa.createState(i + 1, e + 1)));
                    //substitution
                    nfa.addTransition(new Transition(nfa.createState(i, e),
                            Token.ANY,
                            nfa.createState(i + 1, e + 1)));
                }
            }
            i++;
        }
        //add final states and transitions between them
        for(int e = 0; e <= errorRange; e++){
            if(e < errorRange){
                nfa.addTransition(new Transition(nfa.createState(word.length(), e),
                        Token.ANY,
                        nfa.createState(word.length(), e + 1)));
            }
            nfa.setFinalState(nfa.createState(word.length(), e));
        }

        return nfa;
    }

    /**
     * Gets the data from the database, and saves it in this Class.
     * Also creates a new tree from the data (takes a few seconds).
     */
    public void setup(){
        List<String> tree = new ArrayList<>(databaseController.getData().keySet());
        getTree().buildTree(tree, databaseController.getData());
    }

    /**
     * Creates a DFA from a given NFA.
     * @param nfa The NFA that should be transformed.
     * @return The DFA that was made from the {@code nfa}.
     */
    protected DFA makeDFAFromNFA(NFA nfa){
        DFA result = new DFA(new State(0,0,0,true));
        Map<State, Set<State>> stateMap = new HashMap<>();
        Set<State> states = new HashSet<>();
        states.add(nfa.getInitState());
        stateMap.put(result.getInitState(), nfa.lambdaClosure(nfa.getInitState()));
        State state;
        while((state = states.iterator().next()) != null) {
            states.remove(state);
            Set<State> closures = stateMap.get(state);
            Set<Character> outs = new HashSet<Character>();
        }
        return result;
    }

    /**
     * Gets the final did-you-mean suggestion, given a search string.
     * @param searchString The user's search string.
     * @return The String the user most likely meant to type. May be equal to the given search string.
     * @throws IllegalArgumentException if {@code searchString} is {@code null}.
     */
    public String getDYMFromString(String searchString){
        if(searchString == null){
            throw new IllegalArgumentException("Search string is null in DidYouMean.getDYMFromString.");
        }
        return getTree().getDYM(searchString);
    }

    public BKTree getTree() {
        return tree;
    }
    public String getDYMFromAutomata(String searchString){
        if(searchString == null){
            throw new IllegalArgumentException("Search string is null in DidYouMean.getDYMFromString.");
        }
        return null;
    }
}
