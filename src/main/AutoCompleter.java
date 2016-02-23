package main;

import autoComplete.TreeControl;
import database.IDBControl;
import gui.GUI;
import database.DBControl;

/**
 * Created by Tim on 02/02/2016.
 */
public class AutoCompleter {
    private IDBControl DB;
    private TreeControl TC;

    private static final int NUMSUGGESTIONS = 5;

    public AutoCompleter() {
    }

    public String[] suggest (String prefix){
        return null;
    }

    public void refresh(){

    }
}
