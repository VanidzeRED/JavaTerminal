package ui;

import javax.swing.*;
import main.Terminal;

public class Index extends JFrame {
    static int buttonWidth = 100;
    static int buttonHeight = 30;

    public Index () {
        super("Terminal for receiving data");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(380, 250, buttonWidth, buttonHeight);
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(exitButton);
        this.getContentPane().add(panel);
        this.setVisible(true);
    }
}
