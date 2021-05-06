import jssc.SerialPortList;

public class SerialPortFinder {
    public static String[] findComPorts() {
        return SerialPortList.getPortNames();
    }
}
