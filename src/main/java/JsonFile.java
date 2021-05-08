import java.util.Arrays;

public class JsonFile {
    double[] accelerometer;
    double[] gyroscope;
    double[] magnetometer;

    public JsonFile(double[] accelerometer, double[] gyroscope, double[] magnetometer) {
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

    public void getData() {
        System.out.println("accelerometer: " + Arrays.toString(accelerometer) +
                "\ngyroscope: " + Arrays.toString(gyroscope) +
                "\nmagnetometer: " + Arrays.toString(magnetometer) + "\n");
    }
}
