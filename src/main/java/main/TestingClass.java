package main;

import jssc.SerialPort;
import jssc.SerialPortException;

public class TestingClass {
    public static void main(String[] args) {

        //this class is not using now

        /*int a = 170;
        int b = 187;
        int c = 110;
        System.out.println((byte)a);
        System.out.println((byte)b);
        System.out.println((byte)c);
        SerialPort serialPort = new SerialPort(Terminal.SERIAL_PORT_NAME);
        DataFile dataFile = new DataFile(Terminal.FILE_NAME);
        MqttPublisher publisher = new MqttPublisher();
        publisher.setConnection();
        publisher.subscribe();
        byte check;
        boolean flag;
        byte[] receivedData;
        try {
            serialPort.openPort();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        System.out.println("Serial port was successfully opened");
        try {
            serialPort.setParams(Terminal.BAUDRATE, Terminal.DATABITS, Terminal.STOPBITS, Terminal.PARITY);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);
            ComPortListener comPortListener = new ComPortListener(serialPort, dataFile, publisher);
            while (true) {
                receivedData = null;
                flag = true;
                while (flag) {
                    check = serialPort.readBytes(1)[0];
                    if (check == Terminal.PACKAGE_START_LABEL_1) {
                        check = serialPort.readBytes(1)[0];
                        if (check == Terminal.PACKAGE_START_LABEL_2) {
                            flag = false;
                            receivedData = serialPort.readBytes(18);
                            comPortListener.setReceivedData(receivedData);
                        }
                    }
                }
                if (receivedData != null) {
                    comPortListener.doTerminalOperation();
                }
                if (serialPort.getEventsMask() == SerialPort.MASK_ERR) {
                    System.out.println("Error!\n");
                }
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        }*/
    }
}
