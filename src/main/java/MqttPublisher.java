import org.eclipse.paho.client.mqttv3.*;

public class MqttPublisher {
    MqttClient client;
    MqttMessage message;

    public MqttPublisher() {
        this.message = new MqttMessage();
        try {
            this.client = new MqttClient(Terminal.SERVER_ADDRESS, MqttClient.generateClientId());
            System.out.println("Client created\n");
        } catch (MqttException e) {
            e.printStackTrace();
            System.out.println("Can't create client\n");
        }
    }

    public void setConnection() {
        try {
            this.client.connect();
            System.out.println("Connected to host\n");
        } catch (MqttException e) {
            e.printStackTrace();
            System.out.println("Can't connect to host\n");
        }
    }

    public void sendMessage(String string) {
        message.setPayload(string.getBytes());
        try {
            client.publish(Terminal.MQTT_TOPIC, message);
        } catch (MqttException e) {
            e.printStackTrace();
            System.out.println("Can't send message\n");
        }
    }

    public void subscribe() {
        try {
            IMqttMessageListener messageListener = (topic, message) -> System.out.println("Message from server: " + message);
            client.subscribe(Terminal.MQTT_TOPIC, messageListener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
