import jssc.*;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class Terminal {
    static String SERIAL_PORT_NAME = "COM7";
    static String FILE_NAME = "info.txt";
    static String SERVER_ADDRESS = "tcp://62.77.153.231:1883";
    static Index index;
    static FileTypes fileType = FileTypes.JSON;
    static DataFile dataFile;
    static SerialPort serialPort;
    static MqttPublisher publisher;
    public static boolean startingFlag;

    //current address: tcp://62.77.153.231:1883

    static String MQTT_TOPIC = "Received data";
    static byte PACKAGE_START_LABEL_1 = (byte) 0xAA;
    static byte PACKAGE_START_LABEL_2 = (byte) 0xBB;
    static int BAUDRATE = 256000;
    static int DATABITS = 8;
    static int STOPBITS = 3;
    static int PARITY = 0;
    static int PACKAGE_LENGTH = 18;
    static int THREAD_COUNT = 8;

    public static void start() {
        startingFlag = false;
        serialPort = new SerialPort(SERIAL_PORT_NAME);
        publisher = new MqttPublisher();
        TerminalService terminalService = new TerminalService();
        Semaphore semaphore = new Semaphore(THREAD_COUNT);
        terminalService.setIndex(index);
        dataFile.writeToFile("     accelerometer          gyroscope           magnetometer\n");
        publisher.setConnection();
        publisher.subscribe();
        while (!terminalService.openSerialPort(serialPort)) {
            try {
                Index.setNewsAreaText(Arrays.toString(TerminalService.findComPorts()));
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {}
        }
        Index.setNewsAreaText("Serial port was successfully opened" + "\n");
        try {
            serialPort.setParams(BAUDRATE, DATABITS, STOPBITS, PARITY);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);

            ComPortListener comPortListener = new ComPortListener();
            comPortListener.setSerialPort(serialPort);

            terminalService.setSerialPort(serialPort);
            terminalService.setDataFile(dataFile);
            terminalService.setPublisher(publisher);
            terminalService.setSemaphore(semaphore);
            serialPort.addEventListener(comPortListener, SerialPort.MASK_RXCHAR);

            comPortListener.setTerminalService(terminalService);
        } catch (SerialPortException e) {
            Index.setNewsAreaText(e.getMessage() + "\n");
        }
        //terminalService.startTimer();
    }

    public static void stop() {
        try {
            serialPort.removeEventListener();
            serialPort.closePort();
            Index.setNewsAreaText("Terminal has stopped receiving");
        } catch (SerialPortException ignored) {}
    }

    public static void restart() {
        TerminalService terminalService = new TerminalService();
        Semaphore semaphore = new Semaphore(THREAD_COUNT);
        terminalService.setIndex(index);
        while (!terminalService.openSerialPort(serialPort)) {
            try {
                Index.setNewsAreaText(Arrays.toString(TerminalService.findComPorts()));
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {}
        }
        Index.setNewsAreaText("Serial port was successfully opened" + "\n");
        try {
            serialPort.setParams(BAUDRATE, DATABITS, STOPBITS, PARITY);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);

            ComPortListener comPortListener = new ComPortListener();
            comPortListener.setSerialPort(serialPort);

            terminalService.setSerialPort(serialPort);
            terminalService.setDataFile(dataFile);
            terminalService.setPublisher(publisher);
            terminalService.setSemaphore(semaphore);
            serialPort.addEventListener(comPortListener, SerialPort.MASK_RXCHAR);

            comPortListener.setTerminalService(terminalService);
        } catch (SerialPortException e) {
            Index.setNewsAreaText(e.getMessage() + "\n");
        }
        //terminalService.startTimer();
    }

    public static void main(String[] args) {
        startingFlag = true;
        dataFile = new DataFile(FILE_NAME);
        index = new Index();
    }
}
