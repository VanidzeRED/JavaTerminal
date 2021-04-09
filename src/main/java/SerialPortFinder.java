import jssc.SerialPortList;

public class SerialPortFinder {
    public static void findComPorts() {
        String[] portNames = SerialPortList.getPortNames();
        for (String portName : portNames) {
            System.out.println(portName);
        }
    }
}
