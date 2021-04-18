import jssc.*;

public class Terminal {
    static String serialPortName = "COM1";
    static String fileName = "info.txt";
    static int baudRate = 9600;
    static int dataBits = 8;
    static int stopBits = 1+2;
    static int parity = 0;

    String receivedData;

    public void setReceivedData(String receivedData) {
        this.receivedData = receivedData;
    }

    public String getReceivedData() {
        return receivedData;
    }

    public static void main(String[] args) {
        SerialPortFinder.findComPorts();
        SerialPort serialPort = new SerialPort(serialPortName);
        ComPortListener comPortListener = new ComPortListener();
        DataFile dataFile = new DataFile(fileName);
        try {
            serialPort.openPort();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        System.out.println("Serial port was successfully opened");
        try {
            serialPort.setParams(baudRate, dataBits, stopBits, parity, true, true);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);
            comPortListener.setSerialPort(serialPort);
            comPortListener.setDataFile(dataFile);
            serialPort.addEventListener(comPortListener, SerialPort.MASK_RXCHAR);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
}
