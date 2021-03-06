import com.alibaba.fastjson.JSON;
import org.eclipse.paho.client.mqttv3.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class MqttPublisher {
    MqttClient client;
    MqttMessage message;

    public MqttPublisher() {
        this.message = new MqttMessage();
        try {
            this.client = new MqttClient(Terminal.SERVER_ADDRESS, MqttClient.generateClientId());
            Index.setNewsAreaText("Client created\n");
        } catch (MqttException e) {
            Index.setNewsAreaText(e.getMessage() + "\nCan't create client");
        }
    }

    public void setConnection() {
        try {
            this.client.connect();
            Index.setNewsAreaText("Connected to host\n");
        } catch (MqttException e) {
            Index.setNewsAreaText(e.getMessage() + "\nCan't connect to host");
        }
    }

    public void sendMessage(byte[] bytes) {
        message.setPayload(bytes);
        try {
            client.publish(Terminal.MQTT_TOPIC, message);
        } catch (MqttException e) {
            e.printStackTrace();
            Index.setNewsAreaText(e.getMessage() + "\nCan't send message");
        }
    }

    public void subscribe() {
        try {
            IMqttMessageListener messageListener = (topic, message) -> {
                SendingFile newFile = null;
                if(Terminal.fileType == FileTypes.JSON) {
                    newFile = JSON.parseObject(String.valueOf(message), SendingFile.class);
                    System.out.println("JSON");
                }
                if(Terminal.fileType == FileTypes.XML) {
                    JAXBContext context = JAXBContext.newInstance(SendingFile.class);
                    Unmarshaller unmarshaller = context.createUnmarshaller();
                    StringReader reader = new StringReader(String.valueOf(message));
                    newFile = (SendingFile) unmarshaller.unmarshal(reader);
                    System.out.println("XML");
                }
                if(Terminal.fileType == FileTypes.ProtocolBuffers) {
                    newFile = null;
                }
                System.out.println(newFile.getData());
            };
            client.subscribe(Terminal.MQTT_TOPIC, messageListener);
        } catch (MqttException e) {
            Index.setNewsAreaText(e.getMessage() + "\n");
        }
    }

    public void closeClient() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            Index.setNewsAreaText(e.getMessage() + "\n");
        }
    }
}
