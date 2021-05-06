import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ReadingThread extends Thread {
    DataFile dataFile;
    MqttPublisher publisher;
    ComPortListener comPortListener;
    SerialPort serialPort;
    byte[] receivedData;

    public ReadingThread(DataFile dataFile, MqttPublisher publisher, byte[] receivedData,
                         ComPortListener comPortListener, SerialPort serialPort) {
        this.receivedData = receivedData;
        this.publisher = publisher;
        this.dataFile = dataFile;
        this.comPortListener = comPortListener;
        this.serialPort = serialPort;
    }

    public double[] parser(int index, int endian) {
        int j = 0;
        byte[] out = new byte[]{receivedData[index], receivedData[index + 1], receivedData[index + 2],
                receivedData[index + 3], receivedData[index + 4], receivedData[index + 5]};
        double[] outDouble = new double[receivedData.length / 6];
        if (endian == 0) {
            for (int k = 2; k < receivedData.length / 2 - 2; k += 2) {
                outDouble[j++] = toInt(out[k - 2], out[k - 1]);
            }
        } else if (endian == 1) {
            for (int k = 2; k < receivedData.length / 2 - 2; k += 2) {
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

    public synchronized void doTerminalOperation() {
        double[] a = parser(0, 1);
        double[] g = parser(6, 1);
        double[] m = parser(12, 1);
        JsonFile file = new JsonFile(a, g, m);
        byte[] jsonFileByteList = JSON.toJSONBytes(file, SerializerFeature.EMPTY);
        dataFile.writeToFile(Arrays.toString(receivedData));
        dataFile.writeToFile("\n");
        publisher.sendMessage(jsonFileByteList);
    }

    @Override
    public void run() {
        //try {
            //serialPort.closePort();
            doTerminalOperation();
            //serialPort.openPort();
            //serialPort.addEventListener(comPortListener, SerialPort.MASK_RXCHAR);
        //} catch (SerialPortException e) {
            //e.printStackTrace();
        //}
    }
}
