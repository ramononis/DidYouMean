package gui;

import com.sun.deploy.panel.JavaPanel;
import database.CSVControl;
import autoComplete.AutoCompleter;

import javax.swing.*;
import java.awt.*;

/**
 * Displays a GUI and requests suggestions from AutoCompleter class. Also contains the main function to start the program.
 *
 * @author Tim
 */
public class GUI {
    private AutoCompleter AC;

    /**
     * Initializes new GUI.
     */
    public GUI() {
        AC = new AutoCompleter();

        JFrame frame = new JFrame();
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panelMain = new JPanel();
        JPanel panelOut = new JPanel();
        JPanel panelTerminal = new JPanel();
        JPanel panelSearchbar = new JPanel();
        JPanel panelButtons = new JPanel();

        panelMain.setVisible(true);
        panelOut.setVisible(true);
        panelTerminal.setVisible(true);
        panelSearchbar.setVisible(true);
        panelButtons.setVisible(true);

        GridBagConstraints c = new GridBagConstraints();

        frame.setLayout(new GridLayout(1, 1));
        frame.add(panelMain, c);

        panelMain.setLayout(new GridLayout(8, 1));
        c = new GridBagConstraints();
        c.gridwidth = 1;
        c.gridheight = 7;
        c.gridx = 0;
        c.gridy = 0;
        panelMain.add(panelOut, c);

        c.gridy = 3;
        panelMain.add(panelTerminal, c);

        JTextField textFieldSearch = new JTextField();
        JButton buttonSearch = new JButton("Search");

        panelTerminal.setLayout(new GridLayout(1, 4));
        c = new GridBagConstraints();
        c.gridwidth = 3;
        c.gridheight = 1;
        c.gridx = 0;
        panelTerminal.add(textFieldSearch, c);

        c.gridwidth = 1;
        c.gridx = 3;
        panelTerminal.add(buttonSearch, c);

        frame.setVisible(true);

    }

    /**
     * Initializes the program.
     *
     * @param args should be empty.
     */
    public static void main(String[] args) {
        new GUI();
    }
}


