import java.util.Arrays;

public class ReadingThread extends Thread {
    StringBuilder receivedData = new StringBuilder();
    DataFile dataFile;
    MqttPublisher publisher;

    public void appendToReceivedData(String receivedData) {
        this.receivedData.append(receivedData);
    }

    public String getReceivedData() {
        return receivedData.toString();
    }

    public void setDataFile(DataFile dataFile) {
        this.dataFile = dataFile;
    }

    public void setPublisher(MqttPublisher publisher) {
        this.publisher = publisher;
    }

    public String[] divideData() {
        int indexAcc = 0;
        int indexGyr = indexAcc + 6;
        int indexMag = indexGyr + 6;
        String receivedData = getReceivedData();
        String accelerometerData = receivedData.substring(indexAcc, indexGyr);
        String gyroscopeData = receivedData.substring(indexGyr, indexMag);
        String magnetometerData = receivedData.substring(indexMag);
        return new String[]{accelerometerData, gyroscopeData, magnetometerData};
    }

    public void doThreadOperation() {
        receivedData.delete(Terminal.PACKAGE_LENGTH, receivedData.length());
        System.out.println("Bytes received: " + receivedData.length() + "\n" + receivedData.toString() + "\n");
        System.out.println(Arrays.toString(divideData()));
        dataFile.writeToFile(receivedData.toString());
        publisher.sendMessage(receivedData.toString());
    }

    @Override
    public void run() {
        while (true) {
            if (receivedData.length() >= Terminal.PACKAGE_LENGTH) {
                doThreadOperation();
                break;
            }
        }
    }
}
