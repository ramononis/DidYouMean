package didyoumean;

import didyoumean.bktree.BKTree;
import didyoumean.levenstheinautomata.*;
import database.CSVControl;
import database.IDBControl;

import java.util.*;

/**
 * Created by Tim on 3/9/2016.
 */
public class DidYouMean {

    private static final String[] FILENAMES = {"./csv/Data1.csv", "./csv/Data2.csv", "./csv/Data3.csv", "./csv/Data4.csv"};

    private IDBControl databaseController = new CSVControl(FILENAMES);

    public DidYouMean(){
        setup();
    }

    /**
     * Creates a NFA from a given word.
     * @param word The word the NFA should be made from.
     * @param errorRange The error range that the words should be in.
     * @return The NFA made from the {@code word}.
     */
    public NFA makeNFAFromWord(String word, int errorRange){
        return new NFA(0,0);
    }

    /**
     * Gets the data from the database, and saves it in this Class.
     */
    public void setup(){
        List<String> tree = new ArrayList<>(databaseController.getData().keySet());
        BKTree.buildTree(tree, databaseController.getData());
    }

    /**
     * Creates a DFA from a given NFA.
     * @param nfa The NFA that should be transformed.
     * @return The DFA that was made from the {@code nfa}.
     */
    public DFA makeDFAFromNFA(NFA nfa){
        DFA result = new DFA(new State(0,0,0,true));
        Map<State, Set<State>> stateMap = new HashMap<State, Set<State>>();
        Set<State> states = new HashSet<State>();
        states.add(nfa.getInitState());
        stateMap.put(result.getInitState(), nfa.lambdaClosure(nfa.getInitState()));
        State state = null;
        while((state = states.iterator().next()) != null) {
            states.remove(state);
            Set<State> closures = stateMap.get(state);
            Set<Character> outs = new HashSet<Character>();
        }
        return result;
    }

    /**
     *
     */

    /**
     * Gets the final did-you-mean suggestion, given a search string.
     * @param searchString The user's search string.
     * @return The String the user most likely meant to type. May be equal to the given search string.
     */
    public String getDYMFromString(String searchString){
        return BKTree.getDYM(searchString);
    }
}
