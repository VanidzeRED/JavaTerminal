import jssc.*;

public class ComPortListener implements SerialPortEventListener {
    DataFile dataFile;
    MqttPublisher publisher;
    SerialPort serialPort;
    TerminalService terminalService;
    byte[] receivedData;

    public ComPortListener() {}

    public ComPortListener(SerialPort serialPort, DataFile dataFile, MqttPublisher publisher) {
            this.serialPort = serialPort;
            this.dataFile = dataFile;
            this.publisher = publisher;
    }

    public void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public void setTerminalService(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    public void serialEvent(SerialPortEvent event) {
        byte check;
        if (event.isRXCHAR() && event.getEventValue() > 0) {
            try {
                check = serialPort.readBytes(1)[0];
                if (check == Terminal.PACKAGE_START_LABEL_1) {
                    check = serialPort.readBytes(1)[0];
                    if (check == Terminal.PACKAGE_START_LABEL_2) {
                        receivedData = serialPort.readBytes(Terminal.PACKAGE_LENGTH);
                    } else {
                        return;
                    }
                } else {
                    return;
                }
                //ReadingThread readingThread = new ReadingThread(dataFile, publisher, receivedData, this, serialPort);
                //receivedData = serialPort.readBytes(Terminal.PACKAGE_LENGTH);

                ReadingThread readingThread = new ReadingThread(terminalService, receivedData);
                readingThread.start();
            } catch (SerialPortException e) {
                Index.setNewsAreaText(e.getMessage() + "\n");
            }
        }
    }
}
