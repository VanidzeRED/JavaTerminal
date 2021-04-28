import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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

    public double[] parser(int index, int endian) {
        int j = 0;
        byte[] message = receivedData.toString().getBytes();
        byte[] out = new byte[]{message[index], message[index + 1], message[index + 2],
                message[index + 3], message[index + 4], message[index + 5]};
        double outDouble[] = new double[message.length / 6];
        if (endian == 0) {
            for (int k = 2; k < message.length / 2 - 2; k += 2) {
                outDouble[j++] = toInt(out[k - 2], out[k - 1]);
            }
        } else if (endian == 1) {
            for (int k = 2; k < message.length / 2 - 2; k += 2) {
                outDouble[j++] = toInt(out[k - 1], out[k - 2]);
            }
        } else {
            return null;
        }
        return outDouble;
    }

    public int toInt(byte hb, byte lb) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[]{hb, lb});
        short ans = bb.getShort();
        return ans;
    }

    public void doThreadOperation() {
        receivedData.delete(Terminal.PACKAGE_LENGTH, receivedData.length());
        double[] a = parser(0, 1);
        double[] g = parser(6, 1);
        double[] m = parser(12, 1);
        //System.out.println("Bytes received: " + receivedData.length() + "\n" + receivedData.toString() + "\n");
        //System.out.println(Arrays.toString(divideData()));
        System.out.println("accelerometer: " + Arrays.toString(a) + "\ngyroscope: " + Arrays.toString(g) + "\nmagnetometer: " + Arrays.toString(m) + "\n");
        dataFile.writeToFile(receivedData.toString());
        //publisher.sendMessage(receivedData.toString());
    }

    @Override
    public void run() {
        try {
            while (true) {
                sleep(100);
                if (receivedData.length() >= Terminal.PACKAGE_LENGTH) {
                    doThreadOperation();
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
