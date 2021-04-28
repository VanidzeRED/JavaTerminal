import jssc.*;

public class Terminal {
    static String SERIAL_PORT_NAME = "COM1";
    static String FILE_NAME = "info.txt";
    static String SERVER_ADDRESS = "tcp://84.201.135.43:1883";
    static String MQTT_TOPIC = "Received data";
    static char PACKAGE_START_LABEL_1 = 170;
    static char PACKAGE_START_LABEL_2 = 187;
    static int BAUDRATE = 9600;
    static int DATABITS = 8;
    static int STOPBITS = 1 + 2;
    static int PARITY = 0;
    static int PACKAGE_LENGTH = 18;

    public static void main(String[] args) {
        SerialPortFinder.findComPorts();
        SerialPort serialPort = new SerialPort(SERIAL_PORT_NAME);
        ComPortListener comPortListener = new ComPortListener();
        DataFile dataFile = new DataFile(FILE_NAME);
        MqttPublisher publisher = new MqttPublisher();
        //publisher.setConnection();
        //publisher.subscribe();
        try {
            serialPort.openPort();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        System.out.println("Serial port was successfully opened");
        try {
            serialPort.setParams(BAUDRATE, DATABITS, STOPBITS, PARITY, true, true);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);
            comPortListener.setSerialPort(serialPort);
            comPortListener.setDataFile(dataFile);
            comPortListener.setPublisher(publisher);
            serialPort.addEventListener(comPortListener, SerialPort.MASK_RXCHAR);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
}
