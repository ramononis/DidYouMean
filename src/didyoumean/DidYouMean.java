package didyoumean;

import didyoumean.levenstheinautomata.DFA;
import didyoumean.levenstheinautomata.NFA;
import didyoumean.levenstheinautomata.State;
import database.CSVControl;
import database.IDBControl;

import java.util.HashMap;

/**
 * Created by Tim on 3/9/2016.
 */
public class DidYouMean {

    public DidYouMean(){
        fillInData();
    }

    private HashMap<String, Integer> data;
    private IDBControl databaseController = new CSVControl();
    private TreeControlDYM treeControl = new TreeControlDYM(databaseController);

    /**
     * Creates a NFA from a given word.
     * @param word The word the NFA should be made from.
     * @param errorRange The error range that the words should be in.
     * @return The NFA made from the <code>word</code>.
     */
    public NFA makeNFAFromWord(String word, int errorRange){
        return new NFA(0,0);
    }

    /**
     * Gets the data from the database, and saves it in this Class.
     */
    public void fillInData(){
        data = databaseController.getData();
    }

    /**
     * Creates a DFA from a given NFA.
     * @param nfa The NFA that should be transformed.
     * @return The DFA that was made from the <code>nfa</code>.
     */
    public DFA makeDFAFromNFA(NFA nfa){
        return new DFA(new State(0,0));
    }

    /**
     * Gets the final did-you-mean suggestion, given a search string.
     * @param searchString The user's search string.
     * @return The String the user most likely meant to type. May be equal to the given search string.
     */
    public String getDYMFromString(String searchString){
        return null;
    }
}
