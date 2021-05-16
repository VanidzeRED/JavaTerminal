import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class TerminalService {
    SerialPort serialPort;
    MqttPublisher publisher;
    DataFile dataFile;
    Semaphore semaphore;
    byte[] receivedData;
    Index index;

    public void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public void setPublisher(MqttPublisher publisher) {
        this.publisher = publisher;
    }

    public void setDataFile(DataFile dataFile) {
        this.dataFile = dataFile;
    }

    public void setReceivedData(byte[] receivedData) {
        this.receivedData = receivedData;
    }

    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public void prepareNewPublisher() {
        publisher = new MqttPublisher();
        publisher.setConnection();
    }

    public static String[] findComPorts() {
        return SerialPortList.getPortNames();
    }

    public boolean openSerialPort(SerialPort serialPort) {
        try {
            serialPort.openPort();
            return true;
        } catch (SerialPortException e) {
            Index.setNewsAreaText(e.getMessage() + "\n");
            return false;
        }
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

    public void doTerminalOperation(byte[] receivedData) {
        //prepareNewPublisher();
        setReceivedData(receivedData);
        double[] a = parser(0, 1);
        double[] g = parser(6, 1);
        double[] m = parser(12, 1);
        SendingFile file = new SendingFile(a, g, m);
        byte[] sendingFileByteList = {};
        if (Terminal.fileType == FileTypes.JSON) {
            sendingFileByteList = JSON.toJSONBytes(file, SerializerFeature.EMPTY);
        }
        if (Terminal.fileType == FileTypes.XML) {
            try {
                StringWriter fileWriter = new StringWriter();
                JAXBContext context = JAXBContext.newInstance(SendingFile.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(file, fileWriter);
                sendingFileByteList = fileWriter.toString().getBytes();
            } catch (JAXBException ignored) {}
        }
        if (Terminal.fileType == FileTypes.ProtocolBuffers) {
            file = null;
        }
        dataFile.writeToFile(Arrays.toString(a));
        dataFile.writeToFile(Arrays.toString(g));
        dataFile.writeToFile(Arrays.toString(m));
        dataFile.writeToFile("\n");
        index.setReceivedAreaText(file.getData());
        SendingThread sendingThread = new SendingThread(semaphore, publisher, sendingFileByteList);
        sendingThread.start();
    }
}
