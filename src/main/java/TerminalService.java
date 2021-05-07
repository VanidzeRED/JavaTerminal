import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class TerminalService {

    SerialPort serialPort;
    MqttPublisher publisher;
    DataFile dataFile;
    byte[] receivedData;

    public TerminalService(SerialPort serialPort, MqttPublisher publisher, DataFile dataFile) {
        this.serialPort = serialPort;
        this.publisher = publisher;
        this.dataFile = dataFile;
    }

    public String[] findComPorts() {
        return SerialPortList.getPortNames();
    }

    public boolean openSerialPort(SerialPort serialPort) {
        try {
            serialPort.openPort();
            return true;
        } catch (SerialPortException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
