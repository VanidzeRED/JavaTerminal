import java.io.*;

public class DataFile {

    String fileName;
    FileWriter fileWriter;
    FileReader fileReader;
    int strLen;

    public void setStrLen(int strLen) {
        this.strLen = strLen;
    }

    public int getStrLen() {
        return strLen;
    }

    public DataFile(String fileName) {
        this.fileName = fileName;
        try {
            fileWriter = new FileWriter(fileName, false);
            fileReader = new FileReader(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(String string) {
        try {
            fileWriter.append(string);
            fileWriter.flush();
            setStrLen(string.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFromFile() {
        char[] data = new char[getStrLen()];
        StringBuilder stringBuilder = new StringBuilder();
        try {
            fileReader.read(data);
            for (char currentByte : data) {
                stringBuilder.append(currentByte);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
