import java.util.concurrent.Semaphore;

public class SendingThread extends Thread{
    MqttPublisher publisher;
    Semaphore semaphore;
    byte[] sendingFileByteList;

    public SendingThread(Semaphore semaphore, MqttPublisher publisher, byte[] sendingFileByteList) {
        this.semaphore = semaphore;
        this.publisher = publisher;
        this.sendingFileByteList = sendingFileByteList;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            if (sendingFileByteList != null) {
                publisher.sendMessage(sendingFileByteList);
            }
            semaphore.release();
        } catch (InterruptedException e) {
            Index.setNewsAreaText(e.getMessage() + "\n");
        }
    }
}
