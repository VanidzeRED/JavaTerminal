import java.util.Arrays;

public class TimeoutThread extends Thread{

    @Override
    public void run() {
        while (true){
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Arrays.toString(SerialPortFinder.findComPorts()));
        }
    }
}
