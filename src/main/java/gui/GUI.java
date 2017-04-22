package gui;

import api.controller.Controller;
import api.database.CSVControl;
import api.database.IDBControl;
import api.didyoumean.DYM;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Displays a GUI and requests suggestions from AutoCompleter class. Also contains the main function to start the program.
 *
 * @author Tim Blok, Frans van Dijk, Yannick Mijsters, Ramon Onis, Tim Sonderen; University of Twente
 */
public class GUI {
    public static final String[] FILENAMES = {"./csv/Data1.csv", "./csv/Data2.csv", "./csv/Data3.csv", "./csv/Data4.csv"};
    private static final int NSUGGESTIONS = 5;
    private Controller CT;

    private JFrame frame;
    private String[] outputList;
    private JList<String> output;

    /**
     * Initializes new GUI.
     */
    public GUI() {
        frame = new JFrame("Autocomplete");
        ImageIcon loading = new ImageIcon(getClass().getResource("/ajax-loader.gif"));
        frame.add(new JLabel("loading... ", loading, JLabel.CENTER));

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);

        IDBControl DB = new CSVControl(FILENAMES);
        CT = new Controller(DB);

        frame.dispose();

        // first just make a gui
        frame = new JFrame("Autocomplete");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        GridBagConstraints c;

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
        c.gridy = 1;
        manepane.add(listpane, c);

        JPanel terminane = new JPanel();
        terminane.setBackground(Color.BLACK);
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.SOUTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        manepane.add(terminane, c);

        outputList = new String[0];
        output = new JList<>(outputList);
        listpane.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.weighty = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        listpane.add(output, c);

        JRadioButton BK = new JRadioButton("BK Tree");
        JRadioButton LA = new JRadioButton("Automata");
        ButtonGroup group = new ButtonGroup();
        group.add(BK);
        group.add(LA);
        BK.setSelected(true);
        JPanel radiopane = new JPanel();
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 3;
        listpane.add(radiopane, c);

        radiopane.setLayout(new GridLayout(3, 1));
        radiopane.add(new JLabel("Select DYM method"));
        radiopane.add(BK);
        radiopane.add(LA);

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
                String[] completions = CT.getAdvancedTopN(NSUGGESTIONS, searchbar.getText());
                updateOutput(completions);
                if (!frame.getTitle().equals("Autocomplete")) {
                    frame.setTitle("Autocomplete");
                }
            }
        });

        BK.addActionListener(e -> CT.setDYMMethod(DYM.BKTREE));

        LA.addActionListener(e -> CT.setDYMMethod(DYM.LEVENSHTEIN));

        searchbar.addActionListener(e -> {
            if (e.getID() == 1001) {
                search(searchbar.getText());
            }
        });

        searchbutton.addActionListener(e -> search(searchbar.getText()));

        output.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();

                // Double-click detected
                int index = list.locationToIndex(evt.getPoint());
                if (index >= outputList.length || index == -1) return;

                String clicked = outputList[index];
                if (clicked == null) return;

                searchbar.setText(clicked);
            }
        });

        frame.setVisible(true);
        frame.requestFocusInWindow();
        searchbar.requestFocusInWindow();
    }

    /**
     * Initializes the program.
     *
     * @param args should be empty.
     */
    public static void main(String[] args) {
        new GUI();
    }

    /**
     * Searches for the word that is currently in the searchbar.
     *
     * @param query the {@link String} that is currently in the searchbar
     */
    private void search(String query) {
        updateOutput(new String[]{CT.getDYM(query)});
    }

    /**
     * Updates the output list and the output on the screen
     *
     * @param newoutput the new {@link String[]} that should be displayed.
     */
    private void updateOutput(String[] newoutput) {
        outputList = newoutput;
        output.setListData(outputList);
        frame.setTitle("Did you mean");
    }
}


