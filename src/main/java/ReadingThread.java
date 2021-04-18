public class ReadingThread extends Thread{
    StringBuilder receivedData = new StringBuilder();

    public ReadingThread(ComPortListener comPortListener) {
        super();
    }

    public void appendToReceivedData(String receivedData) {
        this.receivedData.append(receivedData);
    }

    public String getReceivedData() {
        return receivedData.toString();
    }

    @Override
    public void run() {
        try {
            sleep(100);
            System.out.println("Bytes received: " + receivedData.length() + "\n" + receivedData.toString() + "\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
