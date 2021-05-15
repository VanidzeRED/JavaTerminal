import jssc.SerialPort;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Index extends JFrame {
    static int buttonWidth = 130;
    static int buttonHeight = 30;
    static JTextArea newsArea;
    static JTextArea receivedArea;
    static JTextArea serverArea;

    public static void setNewsAreaText(String message) {
        newsArea.setText(message);
    }

    public static void setReceivedAreaText(String receivedData) {
        receivedArea.setText(receivedData);
    }

    public static void setServerAreaText(String message) {
        serverArea.setText(message);
    }

    public Index () {
        super("Terminal for receiving data");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(470, 670);

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

        JLabel label2 = new JLabel("MQTT settings");
        label2.setFont(new Font(null, Font.BOLD, 14));
        label2.setBounds(230, 10, 200, 30);

        JLabel addressLabel = new JLabel("Server address");
        addressLabel.setBounds(230, 40, 200, 30);

        JTextField addressField = new JTextField(24);
        addressField.setText("enter address here");
        addressField.setBounds(230, 70, 200, 30);
        addressField.addActionListener(e -> {
            Terminal.SERVER_ADDRESS = addressField.getText();
            System.out.println(Terminal.SERVER_ADDRESS);
        });

        JLabel topicLabel = new JLabel("Message topic");
        topicLabel.setBounds(230, 100, 200, 30);

        JTextField topicField = new JTextField(24);
        topicField.setText("Received data");
        topicField.setBounds(230, 130, 200, 30);
        topicField.addActionListener(e -> {
            Terminal.MQTT_TOPIC = topicField.getText();
            System.out.println(Terminal.MQTT_TOPIC);
        });

        JLabel label3 = new JLabel("Other settings");
        label3.setFont(new Font(null, Font.BOLD, 14));
        label3.setBounds(230, 160, 200, 30);

        JLabel filenameLabel = new JLabel("Reserve data file");
        filenameLabel.setBounds(230, 190, 200, 30);

        JTextField filenameField = new JTextField(24);
        filenameField.setText("info.txt");
        filenameField.setBounds(230, 220, 200, 30);
        filenameField.addActionListener(e -> {
            Terminal.FILE_NAME = filenameField.getText();
            System.out.println(Terminal.FILE_NAME);
        });

        JLabel threadLabel = new JLabel("Count of sending threads");
        threadLabel.setBounds(230, 250, 200, 30);

        JTextField threadField = new JTextField(24);
        threadField.setText("8 (optimal)");
        threadField.setBounds(230, 280, 200, 30);
        threadField.addActionListener(e -> {
            Terminal.THREAD_COUNT = Integer.parseInt(threadField.getText());
            System.out.println(Terminal.THREAD_COUNT);
        });

        newsArea = new JTextArea("News of terminal working process");
        newsArea.setBounds(230, 320, 200, 80);
        newsArea.setEditable(false);
        newsArea.setLineWrap(true);
        newsArea.setWrapStyleWord(true);

        JLabel label4 = new JLabel("Received data");
        label4.setFont(new Font(null, Font.BOLD, 14));
        label4.setBounds(10, 400, 420, 30);

        receivedArea = new JTextArea();
        receivedArea.setBounds(10, 430, 420, 50);
        receivedArea.setEditable(false);
        receivedArea.setLineWrap(true);
        receivedArea.setText("accelerometer:\ngyroscope:\nmagnetometer:");

        JLabel label5 = new JLabel("Data returned from the server");
        label5.setFont(new Font(null, Font.BOLD, 14));
        label5.setBounds(10, 490, 420, 30);

        serverArea = new JTextArea();
        serverArea.setBounds(10, 520, 420, 50);
        serverArea.setEditable(false);
        serverArea.setLineWrap(true);
        serverArea.setText("accelerometer:\ngyroscope:\nmagnetometer:");

        JButton startButton = new JButton("Start terminal");
        startButton.setBounds(10, 580, buttonWidth, buttonHeight);
        startButton.addActionListener(e -> Terminal.start());

        JButton checkButton = new JButton("Check COM's");
        checkButton.setBounds(150, 580, buttonWidth, buttonHeight);
        checkButton.addActionListener(e -> newsArea.setText("Available serial ports: " +
                Arrays.toString(TerminalService.findComPorts())));

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(290, 580, buttonWidth, buttonHeight);
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(startButton);
        panel.add(checkButton);
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
        panel.add(label2);
        panel.add(addressLabel);
        panel.add(addressField);
        panel.add(topicLabel);
        panel.add(topicField);
        panel.add(label3);
        panel.add(filenameLabel);
        panel.add(filenameField);
        panel.add(threadLabel);
        panel.add(threadField);
        panel.add(newsArea);
        panel.add(label4);
        panel.add(receivedArea);
        panel.add(label5);
        panel.add(serverArea);
        this.getContentPane().add(panel);
        this.setVisible(true);
    }
}