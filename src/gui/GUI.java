package gui;

import autoComplete.AutoCompleter;
import com.sun.xml.internal.fastinfoset.util.StringArray;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Displays a GUI and requests suggestions from AutoCompleter class. Also contains the main function to start the program.
 *
 * @author Tim
 */
public class GUI {
    private static final int NSUGGESTIONS = 5;

    private AutoCompleter AC;

    String[] outputList;
    JList<String> output;

    /**
     * Initializes new GUI.
     */
    public GUI() {
        AC = new AutoCompleter();

        // first just make a gui
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

        outputList = new String[0];
        output = new JList<>(outputList);
        listpane.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.weighty = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        listpane.add(output, c);

        terminane.setLayout(new GridBagLayout());

        JTextField searchbar = new JTextField();
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 3;
        terminane.add(searchbar, c);

        JButton searchbutton = new JButton("Search");
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 3;
        terminane.add(searchbutton, c);

        // active part

        searchbar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String[] completions = AC.getTopN(NSUGGESTIONS, searchbar.getText());
                updateOutput(completions);
            }
        });

        searchbar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getID() == 1001) {
                    search(e);
                }
            }
        });

        searchbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search(e);
            }
        });

        output.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) { // TODO: something is clicked, do we do something with this?
                JList list = (JList) evt.getSource();

                // Double-click detected
                int index = list.locationToIndex(evt.getPoint());
                if(index >= outputList.length || index == -1) return;

                String clicked = outputList[index];
                if(clicked == null) return;

                System.out.println(clicked);
            }
        });

        frame.setVisible(true);
        frame.requestFocusInWindow();
        searchbar.requestFocusInWindow();
    }

    private void search(ActionEvent event) {

    }

    private void updateOutput(String[] newoutput) {
        outputList = newoutput;
        output.setListData(outputList);
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


