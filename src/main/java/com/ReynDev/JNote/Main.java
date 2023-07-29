package com.ReynDev.JNote;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Main extends Component implements Runnable, ActionListener {
    private JFrame frame;
    private JPanel panel;
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu, helpMenu;
    private JMenuItem fileOpen, fileSave, fileSaveAs, fileExit;
    private JMenuItem editFind;
    private JMenuItem helpAbout;
    private JTextArea textArea;

    // Resolutions
    private final int width = 640;
    private final int height = 480;

    // Action commands
    private String openCmd = "OpenCommand";
    private String saveCmd = "SaveCommand";
    private String saveAsCmd = "SaveAsCommand";
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

        fileMenu.addSeparator();

        fileSave = new JMenuItem("Save");
        fileSave.setActionCommand(saveCmd);
        fileMenu.add(fileSave);

        fileSaveAs = new JMenuItem("Save As...");
        fileSaveAs.setActionCommand(saveAsCmd);
        fileMenu.add(fileSaveAs);

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
        fileSaveAs.addActionListener(this);
        fileExit.addActionListener(this);
        editFind.addActionListener(this);
        helpAbout.addActionListener(this);

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Open Command
        if (e.getActionCommand().equals(openCmd)) {
            // Create an instance of JFileChooser
            JFileChooser fc = new JFileChooser();

            // Set extension filter
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Text Document", "txt"
            );
            fc.addChoosableFileFilter(filter);
            fc.setFileFilter(filter);

            int returnVal = fc.showOpenDialog(this);

            // Skip if the user cancelled the operation
            if (returnVal == JFileChooser.CANCEL_OPTION) {
                System.out.println("Open command cancelled by user.");
                return;
            }

            // Skip if the file is not exist
            if (!fc.getSelectedFile().exists()) {
                JOptionPane.showMessageDialog(
                        null, "Cannot find or open the file.",
                        "Error", JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Read texts from file
            try {
                /*
                 * Create an instance of FileReader and BufferedReader
                 * to read from file we choose from JFileChooser
                 */
                File file = fc.getSelectedFile();
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                // Write the text to textArea
                textArea.read(br, fr);

                // Get data
                filepath = file.getPath();
                filename = file.getName();

                String title = filename + " - JNote";
                frame.setTitle(title);

                // Close the reader
                br.close();
                fr.close();
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

        // Save command
        if (e.getActionCommand().equals(saveCmd)) {
            // Check if filepath is null or empty
            if (filepath == null || filepath.isEmpty()) {
                // Create an instance of JFileChooser
                JFileChooser fc = new JFileChooser();

                // Set extension filter
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Text Document", ".txt"
                );
                fc.addChoosableFileFilter(filter);
                fc.setFileFilter(filter);

                // Skip if user cancelled the operation
                int returnVal = fc.showSaveDialog(this);
                if (returnVal == JFileChooser.CANCEL_OPTION) {
                    System.out.println("Save command cancelled.");
                    return;
                }

                // Save data
                String[] exts = filter.getExtensions();
                filepath = fc.getSelectedFile().getPath() + exts[0];
                filename = fc.getSelectedFile().getName();

                // Set application title
                String title = filename + " - JNote";
                frame.setTitle(title);
            }

            try {
                /*
                 * Create an instance of File, FileWriter, and BufferedWriter
                 * so we can write the file from the defined filepath
                 */
                File file = new File(filepath);
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);

                // Write text from the textArea
                bw.write(textArea.getText());

                // Close
                bw.close();
                fw.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Cannot find the file: " + ex.getMessage());
                ex.printStackTrace();
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                System.out.println("Cannot save the file: " + ex.getMessage());
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }

        // Save As command
        if (e.getActionCommand().equals(saveAsCmd)) {
            // Create an instance of JFileChooser
            JFileChooser fc = new JFileChooser();

            // Set extension filter
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Text Document", ".txt"
            );
            fc.addChoosableFileFilter(filter);
            fc.setFileFilter(filter);

            // Skip if user cancelled the operation
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.CANCEL_OPTION) {
                System.out.println("Save command cancelled.");
                return;
            }

            // Save data
            String[] exts = filter.getExtensions();
            filepath = fc.getSelectedFile().getPath() + exts[0];
            filename = fc.getSelectedFile().getName();

            // Set application title
            String title = filename + " - JNote";
            frame.setTitle(title);

            try {
                /*
                 * Create an instance of File, FileWriter, and BufferedWriter,
                 * so we can write the file from the defined filepath
                 */
                File file = new File(filepath);
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);

                // Write text from the textArea
                bw.write(textArea.getText());

                // Close
                bw.close();
                fw.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Cannot find the file: " + ex.getMessage());
                ex.printStackTrace();
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                System.out.println("Cannot save the file: " + ex.getMessage());
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }

        // Exit command
        if (e.getActionCommand().equals(exitCmd)) {
            System.exit(0);
        }

        // Find command
        if (e.getActionCommand().equals(findCmd)) {

        }

        // About command
        if (e.getActionCommand().equals(aboutCmd)) {
            JOptionPane.showMessageDialog(
                    null, "JNote app is made by ReynDev",
                    "About JNote", JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Main());
    }
}