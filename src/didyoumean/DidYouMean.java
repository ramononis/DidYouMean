package didyoumean;

import didyoumean.bktree.BKTree;
import didyoumean.levenstheinautomata.*;
import database.CSVControl;
import database.IDBControl;
import tree.Root;
import utils.DYM;

import java.util.*;

/**
 * Created by Tim on 3/9/2016.
 */
public class DidYouMean {

    public static final String[] FILENAMES = {"./csv/Data1.csv", "./csv/Data2.csv", "./csv/Data3.csv", "./csv/Data4.csv"};

    private IDBControl databaseController = new CSVControl(FILENAMES);
    private BKTree tree;

    private DYM method;
    private Root root;
    private static final int MAX_DISTANCE = 3;
    private LevenshteinAutomataFactory laf;
    public DidYouMean(DYM method) {
        this.method = method;
        tree = new BKTree();
        setup();
    }

    /**
     * Creates a NFA from a given word.
     *
     * @param word       The word the NFA should be made from.
     * @param errorRange The error range that the words should be in.
     * @return The NFA made from the {@code word}.
     */
    protected NFA makeNFAFromWord(String word, int errorRange, DYM method) {
        this.method = method;
        NFA nfa = new NFA(0, 0);
        int i = 0;
        for (char letter : word.toCharArray()) {
            for (int e = 0; e <= errorRange; e++) {
                nfa.addTransition(new Transition(nfa.createState(i, e),
                        Token.LETTER,
                        nfa.createState(i + 1, e), letter));
                if (e < errorRange) {
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
        for (int e = 0; e <= errorRange; e++) {
            if (e < errorRange) {
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
    public void setup() {
        switch(method) {
            case LEVENSHTEIN:
                root = new Root();
                databaseController.getData().entrySet().forEach(
                        entry -> root.addOrIncrementWord(entry.getKey(), entry.getValue())
                );
                laf = new LevenshteinAutomataFactory(MAX_DISTANCE);
                break;
            case BKTREE:
                List<String> tree = new ArrayList<>(databaseController.getData().keySet());
                getTree().buildTree(tree, databaseController.getData());
                break;
        }
    }

    /**
     * Creates a DFA from a given NFA.
     *
     * @param nfa The NFA that should be transformed.
     * @return The DFA that was made from the {@code nfa}.
     */
    protected DFA makeDFAFromNFA(NFA nfa) {
        DFA result = new DFA(new State(0, 0, 0, true));
        Map<State, Set<State>> stateMap = new HashMap<>();
        Set<State> states = new HashSet<>();
        states.add(nfa.getInitState());
        stateMap.put(result.getInitState(), nfa.lambdaClosure(nfa.getInitState()));
        State state;
        while ((state = states.iterator().next()) != null) {
            states.remove(state);
            Set<State> closures = stateMap.get(state);
            Set<Character> outs = new HashSet<Character>();
        }
        return result;
    }

    /**
     * Gets the final did-you-mean suggestion, given a search string.
     *
     * @param searchString The user's search string.
     * @return The String the user most likely meant to type. May be equal to the given search string.
     * @throws IllegalArgumentException if {@code searchString} is {@code null}.
     */
    public String getDYM(String searchString) {
        if (searchString == null) {
            throw new IllegalArgumentException("Search string is null in DidYouMean.getDYMFromString.");
        } else if (method == DYM.BKTREE) {
            return getTree().getDYM(searchString);
        } else if (method == DYM.LEVENSHTEIN) {
            return DFA.intersect(root, laf, searchString, 1).get(0);
        } else {
            return null;
        }
    }

    public BKTree getTree() {
        return tree;
    }


}
