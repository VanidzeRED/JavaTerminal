import jssc.*;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class Terminal {
    static String SERIAL_PORT_NAME = "COM7";
    static String FILE_NAME = "info.txt";
    static String SERVER_ADDRESS = "tcp://62.77.153.231:1883";
    static Index index;

    //current address: tcp://62.77.153.231:1883

    static String MQTT_TOPIC = "Received data";
    static byte PACKAGE_START_LABEL_1 = (byte) 0xAA;
    static byte PACKAGE_START_LABEL_2 = (byte) 0xBB;
    static int BAUDRATE = 256000;
    static int DATABITS = 8;
    static int STOPBITS = 1 + 2;
    static int PARITY = 0;
    static int PACKAGE_LENGTH = 18;
    static int THREAD_COUNT = 8;

    public static void start() {
        SerialPort serialPort = new SerialPort(SERIAL_PORT_NAME);
        DataFile dataFile = new DataFile(FILE_NAME);
        MqttPublisher publisher = new MqttPublisher();
        TerminalService terminalService = new TerminalService();
        Semaphore semaphore = new Semaphore(THREAD_COUNT);
        terminalService.setIndex(index);
        System.out.println(Arrays.toString(TerminalService.findComPorts()));
        dataFile.writeToFile("     accelerometer          gyroscope           magnetometer\n");
        publisher.setConnection();
        publisher.subscribe();
        while (!terminalService.openSerialPort(serialPort)) {
            try {
                Index.setNewsAreaText(Arrays.toString(TerminalService.findComPorts()));
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {}
        }
        System.out.println("Available serial ports: " + Arrays.toString(terminalService.findComPorts()));
        System.out.println("Serial port was successfully opened");
        try {
            serialPort.setParams(BAUDRATE, DATABITS, STOPBITS, PARITY);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);

            //ComPortListener comPortListener = new ComPortListener(serialPort, dataFile, publisher);

            ComPortListener comPortListener = new ComPortListener();
            comPortListener.setSerialPort(serialPort);

            terminalService.setSerialPort(serialPort);
            terminalService.setDataFile(dataFile);
            terminalService.setPublisher(publisher);
            terminalService.setSemaphore(semaphore);
            serialPort.addEventListener(comPortListener, SerialPort.MASK_RXCHAR);

            comPortListener.setTerminalService(terminalService);
        } catch (SerialPortException e) {
            Index.setNewsAreaText(e.getMessage());
        }
    }

    public static void reconnect(SerialPort serialPort) {
        TerminalService newService = new TerminalService();
        try {
            serialPort.closePort();
            serialPort = new SerialPort(SERIAL_PORT_NAME);
        } catch (SerialPortException e) {
            Index.setNewsAreaText(e.getMessage());
        }
        while (!newService.openSerialPort(serialPort)) {
            try {
                System.out.println("Available serial ports: " + Arrays.toString(newService.findComPorts()));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Index.setNewsAreaText(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        index = new Index();
    }
}
