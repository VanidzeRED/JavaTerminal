import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;

@XmlRootElement

public class SendingFile {
    double[] accelerometer;
    double[] gyroscope;
    double[] magnetometer;

    public SendingFile(){
        accelerometer = new double[0];
        gyroscope = new double[0];
        magnetometer = new double[0];
    }

    public SendingFile(double[] accelerometer, double[] gyroscope, double[] magnetometer) {
        this.accelerometer = accelerometer;
        this.gyroscope = gyroscope;
        this.magnetometer = magnetometer;
    }

    public double[] getAccelerometer() {
        return accelerometer;
    }

    public void setAccelerometer(double[] accelerometer) {
        this.accelerometer = accelerometer;
    }

    public double[] getGyroscope() {
        return gyroscope;
    }

    public void setGyroscope(double[] gyroscope) {
        this.gyroscope = gyroscope;
    }

    public double[] getMagnetometer() {
        return magnetometer;
    }

    public void setMagnetometer(double[] magnetometer) {
        this.magnetometer = magnetometer;
    }

    public String getData() {
        return "accelerometer: " + Arrays.toString(accelerometer) +
                "\ngyroscope: " + Arrays.toString(gyroscope) +
                "\nmagnetometer: " + Arrays.toString(magnetometer) + "\n";
    }
}
