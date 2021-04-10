import jssc.*;

public class Terminal {
    static String serialPortName = "COM1";
    static int baudRate = 9600;
    static int dataBits = 8;
    static int stopBits = 1+2;
    static int parity = 0;

    public static void main(String[] args) {
        SerialPort serialPort;
        SerialPortFinder.findComPorts();
        serialPort = new SerialPort(serialPortName);
        ComPortListener comPortListener = new ComPortListener();
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
            serialPort.addEventListener(comPortListener, SerialPort.MASK_RXCHAR);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
}
