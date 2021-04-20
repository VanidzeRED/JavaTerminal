import jssc.*;

public class ComPortListener implements SerialPortEventListener {
    SerialPort serialPort;
    StringBuilder receivedData = new StringBuilder();
    ReadingThread readingThread;
    DataFile dataFile;
    MqttPublisher publisher;

    int countOfBits;

    public void setSerialPort(SerialPort hSerial) {
        this.serialPort = hSerial;
    }

    public void appendToReceivedData(String receivedData) {
        this.receivedData.append(receivedData);
    }

    public void setCountOfBits(int countOfBits) {
        this.countOfBits = countOfBits;
    }

    public int getCountOfBits() {
        return countOfBits;
    }

    public void setDataFile(DataFile dataFile) {
        this.dataFile = dataFile;
    }

    public void setPublisher(MqttPublisher publisher) {
        this.publisher = publisher;
    }

    public void serialEvent(SerialPortEvent event) {
        setCountOfBits(event.getEventValue());
        if(event.isRXCHAR() && event.getEventValue() > 0){
            try {
                String receivedData = serialPort.readString(getCountOfBits());
                appendToReceivedData(receivedData);
                if ((readingThread == null) || (!readingThread.isAlive())){
                    readingThread = new ReadingThread();
                    readingThread.setDataFile(dataFile);
                    readingThread.setPublisher(publisher);
                    readingThread.start();
                }
                if ((readingThread != null)){
                    readingThread.appendToReceivedData(receivedData);
                }
            }
            catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }
}
