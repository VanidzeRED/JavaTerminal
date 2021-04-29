import jssc.*;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ComPortListener implements SerialPortEventListener {
    SerialPort serialPort;
    DataFile dataFile;
    MqttPublisher publisher;
    byte[] receivedData;

    public void setSerialPort(SerialPort hSerial) {
        this.serialPort = hSerial;
    }

    public void setDataFile(DataFile dataFile) {
        this.dataFile = dataFile;
    }

    public void setPublisher(MqttPublisher publisher) {
        this.publisher = publisher;
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

    public void doTerminalOperation() {
        double[] a = parser(0, 1);
        double[] g = parser(6, 1);
        double[] m = parser(12, 1);
        System.out.println("accelerometer: " + Arrays.toString(a) + "\ngyroscope: " + Arrays.toString(g) + "\nmagnetometer: " + Arrays.toString(m) + "\n");
        dataFile.writeToFile(Arrays.toString(receivedData));
        publisher.sendMessage(receivedData);
    }

    public void serialEvent(SerialPortEvent event) {
        receivedData = null;
        int check;
        if (event.isRXCHAR() && event.getEventValue() > 0) {
            try {
                check = Byte.toUnsignedInt(serialPort.readBytes(1)[0]);
                //System.out.println(check);
                if (check == Terminal.PACKAGE_START_LABEL_1) {
                    check = Byte.toUnsignedInt(serialPort.readBytes(1)[0]);
                    if (check == Terminal.PACKAGE_START_LABEL_2) {
                        receivedData = serialPort.readBytes(18);
                    } else {
                        return;
                    }
                } else {
                    return;
                }
                if (receivedData != null) {
                    doTerminalOperation();
                }
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }
}
