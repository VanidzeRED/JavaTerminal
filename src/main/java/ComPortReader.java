import jssc.*;

public class ComPortReader implements SerialPortEventListener {
    SerialPort serialPort;

    public void setSerialPort(SerialPort hSerial) {
        this.serialPort = hSerial;
    }

    public void serialEvent(SerialPortEvent event) {
        if(event.isRXCHAR() && event.getEventValue() > 0){
            try {
                String receivedData = serialPort.readString(event.getEventValue());
                System.out.println("\nReceived bytes" + receivedData.length());
                System.out.println("\n" + receivedData);
            }
            catch (SerialPortException e) {
                System.out.println(e);
            }
        }
    }
}
