package textproc;

import java.awt.*;
import javax.swing.*;
import java.util.Map;
import java.util.Map.Entry;
import java.awt.event.*;  

public class BookReaderController {

    JRadioButton alphabetRadioButton, frequencyRadioButton;
    JButton exitButton;

    public BookReaderController(GeneralWordCounter counter) {
        SwingUtilities.invokeLater(() -> createWindow(counter, "BookReader", 1000, 500));
    }

    private void createWindow(GeneralWordCounter counter, String title, int width, int height) {
        // Skapa en modell för sorterad lista med ord och frekvenser från räknaren
        SortedListModel<Map.Entry<String, Integer>> listModel = new SortedListModel<>(counter.getWordList());
        JList<Map.Entry<String, Integer>> wordList = new JList<>(listModel);

        // Skapa huvudfönstret
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = frame.getContentPane();

        JScrollPane scrollPane = new JScrollPane(wordList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(width, height));

        // Skapa radioknappar för sorteringsalternativ
        alphabetRadioButton = new JRadioButton("Alphabetic");
        frequencyRadioButton = new JRadioButton("Frequency");
        ButtonGroup group = new ButtonGroup();
        group.add(alphabetRadioButton);
        group.add(frequencyRadioButton);

        // Lyssnare för radioknappar som ändrar sorteringsordning
        alphabetRadioButton.addActionListener(event -> {
            listModel.sort((x, y) -> ((Entry<String, Integer>) x).getKey().compareTo(((Entry<String, Integer>) y).getKey()));
        });

        frequencyRadioButton.addActionListener(event -> {
            listModel.sort((x, y) -> -((x).getValue() - (y).getValue()));
        });

        // Skapa en panel för radioknappar
        JPanel sortPanel = new JPanel();
        sortPanel.add(alphabetRadioButton);
        sortPanel.add(frequencyRadioButton);

        // Skapa en panel för sökfält och sökknapp
        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Find");
        searchPanel.add(searchButton);
        searchPanel.add(searchField);
        frame.getRootPane().setDefaultButton(searchButton);

        // Lyssnare för sökknapp
        searchButton.addActionListener(e -> {
            String searchedKey = searchField.getText().toLowerCase().trim();
            boolean found = false;

            for (int i = 0; i < listModel.getSize(); i++) {
                String currentKey = ((Entry<String, Integer>) listModel.getElementAt(i)).getKey();

                if (currentKey.equals(searchedKey)) {
                    wordList.setSelectedIndex(i);
                    wordList.ensureIndexIsVisible(i);
                    found = true;
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(frame, "Word not found");
            }
        });

        // Lägg till komponenter i huvudpanelen
        pane.add(scrollPane, BorderLayout.NORTH);
        pane.add(sortPanel, BorderLayout.CENTER);
        pane.add(searchPanel, BorderLayout.SOUTH);

        // Skapa en knapp för att avsluta programmet
        exitButton = new JButton("Exit");

        // Lägg till exit-knapp i panelen
        sortPanel.add(exitButton);

        // Lyssnare för exit-knapp
        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        frame.pack();
        frame.setVisible(true);
    }
}