import jssc.SerialPort;

import javax.swing.*;
import java.awt.*;

public class Index extends JFrame {
    static int buttonWidth = 100;
    static int buttonHeight = 30;

    public Index () {
        super("Terminal for receiving data");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel label1 = new JLabel("Serial port settings");
        label1.setFont(new Font(null, Font.BOLD, 14));
        label1.setBounds(10, 10, 200, 30);

        JLabel portnameLabel = new JLabel("Baudrate");
        portnameLabel.setBounds(10, 40, 200, 30);

        JTextField portnameField = new JTextField(5);
        portnameField.setText("COM7");
        portnameField.setBounds(10, 70, 200, 30);
        portnameField.addActionListener(e -> {
            Terminal.SERIAL_PORT_NAME = portnameField.getText();
            System.out.println(Terminal.SERIAL_PORT_NAME);
        });

        JLabel baudrateLabel = new JLabel("Baudrate");
        baudrateLabel.setBounds(10, 100, 200, 30);

        Integer[] baudrateArray = {SerialPort.BAUDRATE_110,
                SerialPort.BAUDRATE_300,
                SerialPort.BAUDRATE_600,
                SerialPort.BAUDRATE_1200,
                SerialPort.BAUDRATE_2400,
                SerialPort.BAUDRATE_9600,
                SerialPort.BAUDRATE_14400,
                SerialPort.BAUDRATE_19200,
                SerialPort.BAUDRATE_38400,
                SerialPort.BAUDRATE_57600,
                SerialPort.BAUDRATE_115200,
                SerialPort.BAUDRATE_128000,
                SerialPort.BAUDRATE_256000};

        JComboBox<Integer> baudrateComboBox = new JComboBox<>(baudrateArray);
        baudrateComboBox.setBounds(10, 130, 200, 30);
        baudrateComboBox.setSelectedIndex(12);
        baudrateComboBox.addActionListener(e -> {
            Terminal.BAUDRATE = (int) baudrateComboBox.getSelectedItem();
            System.out.println(Terminal.BAUDRATE);
        });

        JLabel databitsLabel = new JLabel("Data bits");
        databitsLabel.setBounds(10, 160, 200, 30);

        Integer[] databitsArray = {SerialPort.DATABITS_5,
                SerialPort.DATABITS_6,
                SerialPort.DATABITS_7,
                SerialPort.DATABITS_8
        };

        JComboBox<Integer> databitsComboBox = new JComboBox<>(databitsArray);
        databitsComboBox.setBounds(10, 190, 200, 30);
        databitsComboBox.setSelectedIndex(3);
        databitsComboBox.addActionListener(e -> {
            Terminal.DATABITS = (int) databitsComboBox.getSelectedItem();
            System.out.println(Terminal.DATABITS);
        });

        JLabel stopbitsLabel = new JLabel("Stop bits");
        stopbitsLabel.setBounds(10, 220, 200, 30);

        int[] stopbitsIntArray = {SerialPort.STOPBITS_1,
                SerialPort.STOPBITS_1_5
        };

        String[] stopbitsArray = {"0", "1"};

        JComboBox<String> stopbitsComboBox = new JComboBox<>(stopbitsArray);
        stopbitsComboBox.setBounds(10, 250, 200, 30);
        stopbitsComboBox.setSelectedIndex(0);
        stopbitsComboBox.addActionListener(e -> {
            Terminal.DATABITS = stopbitsIntArray[stopbitsComboBox.getSelectedIndex()];
            System.out.println(Terminal.DATABITS);
        });

        JLabel parityLabel = new JLabel("Parity");
        parityLabel.setBounds(10, 280, 200, 30);

        Integer[] parityIntArray = {SerialPort.PARITY_NONE,
                SerialPort.PARITY_ODD,
                SerialPort.PARITY_EVEN,
                SerialPort.PARITY_MARK,
                SerialPort.PARITY_SPACE
        };

        String[] parityArray = {"NONE", "ODD", "EVEN", "MARK", "SPACE"};

        JComboBox<String> parityComboBox = new JComboBox<>(parityArray);
        parityComboBox.setBounds(10, 310, 200, 30);
        parityComboBox.setSelectedIndex(0);
        parityComboBox.addActionListener(e -> {
            Terminal.PARITY = parityIntArray[parityComboBox.getSelectedIndex()];
            System.out.println(Terminal.PARITY);
        });

        JLabel lengthLabel = new JLabel("Package length");
        lengthLabel.setBounds(10, 340, 200, 30);

        JTextField lengthField = new JTextField(3);
        lengthField.setText("18");
        lengthField.setBounds(10, 370, 200, 30);
        lengthField.addActionListener(e -> {
            Terminal.PACKAGE_LENGTH = Integer.parseInt(lengthField.getText());
            System.out.println(Terminal.PACKAGE_LENGTH);
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(380, 250, buttonWidth, buttonHeight);
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(exitButton);
        panel.add(label1);
        panel.add(portnameLabel);
        panel.add(baudrateLabel);
        panel.add(databitsLabel);
        panel.add(stopbitsLabel);
        panel.add(parityLabel);
        panel.add(lengthLabel);
        panel.add(portnameField);
        panel.add(baudrateComboBox);
        panel.add(databitsComboBox);
        panel.add(stopbitsComboBox);
        panel.add(parityComboBox);
        panel.add(lengthField);
        this.getContentPane().add(panel);
        this.setVisible(true);
    }
}