package DidYouMean;

import DidYouMean.levenstheinAutomata.DFA;
import DidYouMean.levenstheinAutomata.NFA;
import autoComplete.TreeControl;
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

    public NFA makeNFAFromWord(String word, int errorRange){
        return new NFA(0,0);
    }

    public void fillInData(){
        data = databaseController.getData();
    }

    public DFA makeDFAFromNFA(NFA nfa){
        return new DFA(0,0);
    }

    public String getDYMFromString(String searchString){
        return null;
    }
}
