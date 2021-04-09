import jssc.*;

public class Terminal {
    public static void main(String[] args) {
        SerialPort serialPort;
        SerialPortFinder.findComPorts();
        serialPort = new SerialPort("COM1");
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);
            ComPortReader comPortReader = new ComPortReader();
            comPortReader.setSerialPort(serialPort);
            serialPort.addEventListener(comPortReader, SerialPort.MASK_RXCHAR);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
}
