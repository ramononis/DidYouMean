package gui;

import DidYouMean.DidYouMean;
import database.CSVControl;
import autoComplete.AutoCompleter;

/**
 * Displays a GUI and requests suggestions from AutoCompleter class. Also contains the main function to start the program.
 * @author Tim
 */
public class GUI {
    private AutoCompleter AC;
    private DidYouMean DYM;

    /**
     * Initializes new GUI.
     */
    public GUI(){
        AC = new AutoCompleter();
    }

    /**
     * Initializes the program.
     * @param args should be empty.
     */
    public static void main(String[] args) {
        new GUI();
    }
}
