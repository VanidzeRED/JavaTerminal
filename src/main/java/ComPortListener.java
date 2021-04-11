import jssc.*;

public class ComPortListener implements SerialPortEventListener {
    SerialPort serialPort;
    StringBuilder receivedData = new StringBuilder();
    DataFile dataFile;
    int countOfBits;

    public void setDataFile(DataFile dataFile) {
        this.dataFile = dataFile;
    }

    public void setSerialPort(SerialPort hSerial) {
        this.serialPort = hSerial;
    }

    public void appendToReceivedData(String receivedData) {
        this.receivedData.append(receivedData);
    }

    public String getReceivedData() {
        return receivedData.toString();
    }

    public void setCountOfBits(int countOfBits) {
        this.countOfBits = countOfBits;
    }

    public int getCountOfBits() {
        return countOfBits;
    }

    public void serialEvent(SerialPortEvent event) {
        setCountOfBits(event.getEventValue());
        if(event.isRXCHAR() && event.getEventValue() > 0){
            try {
                String receivedData = serialPort.readString(getCountOfBits());
                System.out.println("Received bits" + getCountOfBits());
                appendToReceivedData(receivedData);
                System.out.println(getReceivedData());
                dataFile.writeToFile(receivedData);
            }
            catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }
}
