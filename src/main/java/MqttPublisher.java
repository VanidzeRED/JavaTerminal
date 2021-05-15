import com.alibaba.fastjson.JSON;
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
            Index.setNewsAreaText(e.getMessage() + "Can't create client");
        }
    }

    public void setConnection() {
        try {
            this.client.connect();
            System.out.println("Connected to host\n");
        } catch (MqttException e) {
            Index.setNewsAreaText(e.getMessage() + "Can't connect to host");
        }
    }

    public void sendMessage(byte[] bytes) {
        message.setPayload(bytes);
        try {
            client.publish(Terminal.MQTT_TOPIC, message);
        } catch (MqttException e) {
            e.printStackTrace();
            Index.setNewsAreaText(e.getMessage() + "Can't send message");
        }
    }

    public void subscribe() {
        try {
            IMqttMessageListener messageListener = (topic, message) -> {
                JsonFile newFile = JSON.parseObject(String.valueOf(message), JsonFile.class);
                //System.out.println("Message from server:");
                Index.setServerAreaText(newFile.getData());
            };
            client.subscribe(Terminal.MQTT_TOPIC, messageListener);
        } catch (MqttException e) {
            Index.setNewsAreaText(e.getMessage());
        }
    }

    public void closeClient() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            Index.setNewsAreaText(e.getMessage());
        }
    }
}
