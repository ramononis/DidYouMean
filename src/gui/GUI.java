package gui;

import database.CSVControl;
import main.AutoCompleter;

/**
 * Displays a GUI and requests suggestions from AutoCompleter class. Also contains the main function to start the program.
 * @author Tim
 */
public class GUI {
    private AutoCompleter AC;

    /**
     * Initializes new GUI.
     */
    public GUI(){

    }

    /**
     * Initializes the program.
     * @param args should be empty.
     */
    public static void main(String[] args) {
        CSVControl x = new CSVControl();
        System.out.println(x.getData());
    }
}
