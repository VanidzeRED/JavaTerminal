public class ReadingThread extends Thread{
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

    @Override
    public void run() {
        try {
            sleep(100);
            System.out.println("Bytes received: " + receivedData.length() + "\n" + receivedData.toString() + "\n");
            dataFile.writeToFile(receivedData.toString());
            publisher.sendMessage(receivedData.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
