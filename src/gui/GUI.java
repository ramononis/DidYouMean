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
        GridBagConstraints c = null;

        JPanel manepane = new JPanel();
        manepane.setBackground(Color.blue);
        manepane.setLayout(new GridBagLayout());
        frame.add(manepane);

        JPanel listpane = new JPanel();
        listpane.setBackground(Color.red);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 7;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        manepane.add(listpane, c);

        JPanel terminane = new JPanel();
        terminane.setBackground(Color.BLACK);
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.SOUTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 7;
        manepane.add(terminane, c);

        String[] temporary = {"asdgasdg", "as;dfhas;dlgk", "a;sdlkfhga"};
        JList<String> output = new JList<>(temporary);
        listpane.setLayout(new GridBagLayout());
        c=new GridBagConstraints();
        c.weighty=1;
        c.weightx=1;
        c.fill=GridBagConstraints.BOTH;
        listpane.add(output, c);

        terminane.setLayout(new GridBagLayout());

        JTextField searchbar = new JTextField("Search here..");
        c = new GridBagConstraints();
        c.anchor=GridBagConstraints.WEST;
        c.fill=GridBagConstraints.BOTH;
        c.weightx=1;
        c.weighty=1;
        c.gridwidth=3;
        terminane.add(searchbar, c);

        JButton searchbutton = new JButton("Search");
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        c.gridx=3;
        terminane.add(searchbutton, c);

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


