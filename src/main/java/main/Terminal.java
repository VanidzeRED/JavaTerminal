package main;

import files.DataFile;
import jssc.*;
import processing.ComPortListener;
import processing.MqttPublisher;
import processing.TerminalService;
import ui.Index;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class Terminal {
    public static String SERIAL_PORT_NAME = "COM7";
    public static String FILE_NAME = "info.txt";
    public static String SERVER_ADDRESS = "tcp://62.77.153.231:1883";
    public static String MQTT_TOPIC = "Received data";
    public static byte PACKAGE_START_LABEL_1 = (byte) 0xAA;
    public static byte PACKAGE_START_LABEL_2 = (byte) 0xBB;
    public static int BAUDRATE = 256000;
    public static int DATABITS = 8;
    public static int STOPBITS = 1 + 2;
    public static int PARITY = 0;
    public static int PACKAGE_LENGTH = 18;
    public static int THREAD_COUNT = 8;

    public static void main(String[] args) {
        JFrame index = new Index();
        SerialPort serialPort = new SerialPort(SERIAL_PORT_NAME);
        DataFile dataFile = new DataFile(FILE_NAME);
        MqttPublisher publisher = new MqttPublisher();
        TerminalService terminalService = new TerminalService();
        Semaphore semaphore = new Semaphore(THREAD_COUNT);
        System.out.println(Arrays.toString(terminalService.findComPorts()));
        dataFile.writeToFile("     accelerometer          gyroscope           magnetometer\n");
        publisher.setConnection();
        //publisher.subscribe();
        while (!terminalService.openSerialPort(serialPort)) {
            try {
                System.out.println("Available serial ports: " + Arrays.toString(terminalService.findComPorts()));
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
            e.printStackTrace();
        }
    }
}
