package threads;

import java.util.concurrent.Semaphore;
import processing.MqttPublisher;

public class SendingThread extends Thread{
    MqttPublisher publisher;
    Semaphore semaphore;
    byte[] jsonFileByteList;

    public SendingThread(Semaphore semaphore, MqttPublisher publisher, byte[] jsonFileByteList) {
        this.semaphore = semaphore;
        this.publisher = publisher;
        this.jsonFileByteList = jsonFileByteList;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            publisher.sendMessage(jsonFileByteList);
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
