package com.ReynDev.JNote;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Main extends Component implements Runnable, ActionListener {
    private JFrame frame;
    private JPanel panel;
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu, helpMenu;
    private JMenuItem fileOpen, fileSave, fileExit;
    private JMenuItem editFind;
    private JMenuItem helpAbout;
    private JTextArea textArea;

    // Resolutions
    private final int width = 640;
    private final int height = 480;

    // Action commands
    private String openCmd = "OpenCommand";
    private String saveCmd = "SaveCommand";
    private String exitCmd = "ExitCommand";
    private String findCmd = "FindCommand";
    private String aboutCmd = "AboutCommand";

    // Vars
    private String filepath;
    private String filename;

    @Override
    public void run() {
        // Get screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Frame
        frame = new JFrame("New Document - JNote");
        frame.setMinimumSize(new Dimension(width, height));
        frame.setLocation((screenWidth - width) / 2, (screenHeight - height) / 2);

        // MenuBar
        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        // File menu
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        // File menu items
        fileOpen = new JMenuItem("Open...");
        fileOpen.setActionCommand(openCmd);
        fileMenu.add(fileOpen);

        fileSave = new JMenuItem("Save");
        fileSave.setActionCommand(saveCmd);
        fileMenu.add(fileSave);

        fileMenu.addSeparator();

        fileExit = new JMenuItem("Exit");
        fileExit.setActionCommand(exitCmd);
        fileMenu.add(fileExit);

        // Edit menu
        editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        // Edit menu items
        editFind = new JMenuItem("Find");
        editFind.setActionCommand(findCmd);
        editMenu.add(editFind);

        // Help menu
        helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        // Help menu items
        helpAbout = new JMenuItem("About");
        helpAbout.setActionCommand(aboutCmd);
        helpMenu.add(helpAbout);

        // Panel
        GridBagConstraints ctr = new GridBagConstraints();
        panel = new JPanel(new GridBagLayout());
        frame.add(panel);

        // Main text area
        textArea = new JTextArea();

        ctr.fill = GridBagConstraints.BOTH;
        ctr.weightx = 1;
        ctr.weighty = 1;
        ctr.gridx = 0;
        ctr.gridy = 0;
        panel.add(textArea, ctr);

        // Action listeners
        fileOpen.addActionListener(this);
        fileSave.addActionListener(this);
        fileExit.addActionListener(this);
        editFind.addActionListener(this);
        helpAbout.addActionListener(this);

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(openCmd)) {
            // Create an instance of JFileChooser
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(this);

            // Skip if the user cancelled the operation
            if (returnVal == JFileChooser.CANCEL_OPTION) {
                System.out.println("Open command cancelled by user.");
                return;
            }

            // Read texts from file
            try {
                /**
                 * Create an instance of FileReader and BufferedReader
                 * to read from file we choose from JFileChooser
                 */
                FileReader file = new FileReader(fc.getSelectedFile());
                BufferedReader br = new BufferedReader(file);

                // Write the text to textArea
                textArea.read(br, file);

                // Get data
                filepath = fc.getSelectedFile().getPath();
                filename = fc.getSelectedFile().getName() + " - JNote";
                frame.setTitle(filename);

                // Close the BufferedReader
                br.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Cannot open the file: " + ex.getMessage());
                ex.printStackTrace();
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                System.out.println("Cannot read the file: " + ex.getMessage());
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }

        if (e.getActionCommand().equals(saveCmd)) {

        }

        if (e.getActionCommand().equals(exitCmd)) {

        }

        if (e.getActionCommand().equals(findCmd)) {

        }

        if (e.getActionCommand().equals(aboutCmd)) {

        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Main());
    }
}