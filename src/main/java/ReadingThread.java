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
        int data_length = Terminal.PACKAGE_LENGTH;
        int indexAcc = 1;
        int indexGyr = indexAcc + 48;
        int indexMag = indexGyr + 48;
        String receivedData = getReceivedData();
        String accselerometerData = receivedData.substring(indexAcc, indexGyr);
        String gyroscopeData = receivedData.substring(indexGyr, indexMag);
        String magnetometerData = receivedData.substring(indexMag);
        return new String[]{accselerometerData, gyroscopeData, magnetometerData};
    }

    @Override
    public void run() {
        try {
            while (receivedData.length() < Terminal.PACKAGE_LENGTH) {
                sleep(100);
            }
            if (receivedData.length() >= Terminal.PACKAGE_LENGTH) {
                receivedData.delete(Terminal.PACKAGE_LENGTH, receivedData.length());
                System.out.println("Bytes received: " + receivedData.length() + "\n" + receivedData.toString() + "\n");
                dataFile.writeToFile(receivedData.toString());
                publisher.sendMessage(receivedData.toString());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
