import java.io.*;

public class DataFile {

    String fileName;
    FileWriter fileWriter;
    FileReader fileReader;

    public DataFile (String fileName){
        this.fileName = fileName;
        try{
            fileWriter = new FileWriter(fileName, false);
            fileReader = new FileReader(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile (String string) {
        try {
            fileWriter.append(string);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
    public String  readFromFile () {
        try {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
