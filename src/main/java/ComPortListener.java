import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import jssc.*;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ComPortListener implements SerialPortEventListener {
    SerialPort serialPort;
    DataFile dataFile;
    MqttPublisher publisher;
    ReadingThread readingThread;
    byte[] receivedData;

    public ComPortListener(SerialPort serialPort, DataFile dataFile, MqttPublisher publisher) {
        this.serialPort = serialPort;
        this.dataFile = dataFile;
        this.publisher = publisher;
    }

    public void setReceivedData(byte[] receivedData) {
        this.receivedData = receivedData;
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

    public Integer[] arrayToInt(byte[] array) {
        Integer[] newInt = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            Byte current = array[i];
            newInt[i] = Integer.parseUnsignedInt(current.toString());
        }
        return newInt;
    }

    public void doTerminalOperation() {
        double[] a = parser(0, 1);
        double[] g = parser(6, 1);
        double[] m = parser(12, 1);
        JsonFile file = new JsonFile(a, g, m);
        byte[] jsonFileByteList = JSON.toJSONBytes(file, SerializerFeature.EMPTY);
        //System.out.println("accelerometer: " + Arrays.toString(a) + "\ngyroscope: " + Arrays.toString(g) + "\nmagnetometer: " + Arrays.toString(m) + "\n");
        dataFile.writeToFile(Arrays.toString(receivedData));
        dataFile.writeToFile("\n");
        publisher.sendMessage(jsonFileByteList);
    }

    public void serialEvent(SerialPortEvent event) {
        byte check;
        if (event.isRXCHAR() && event.getEventValue() > 0) {
            try {
                check = serialPort.readBytes(1)[0];
                if (check == Terminal.PACKAGE_START_LABEL_1) {
                    check = serialPort.readBytes(1)[0];
                    if (check == Terminal.PACKAGE_START_LABEL_2) {
                        receivedData = serialPort.readBytes(Terminal.PACKAGE_LENGTH);
                    } else {
                        return;
                    }
                } else {
                    return;
                }
                readingThread = new ReadingThread(dataFile, publisher, receivedData, this, serialPort);
                readingThread.start();
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }
}
