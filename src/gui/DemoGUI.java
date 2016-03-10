package gui;

import javax.swing.*;
import java.awt.event.*;

public class DemoGUI extends JDialog {
    private JPanel contentPane;
    private JButton buttonSearch;
    private JButton buttonCancel;
    private JTextField searchBar;
    private JTextArea textAreaOut;

    public DemoGUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonSearch);

        buttonSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == 10){
                    onOK();
                }
            }
        });
    }

    private void onOK() {
// add your code here
        textAreaOut.append("Search was clicked!\n");
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        DemoGUI dialog = new DemoGUI();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
