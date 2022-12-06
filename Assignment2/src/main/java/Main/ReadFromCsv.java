package Main;

import com.opencsv.CSVReader;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import javax.json.JsonObject;
import java.io.FileReader;
import java.sql.Timestamp;
import java.time.Instant;

public class ReadFromCsv {
    private final String fileName = "./sensor.csv";
    private String timestamp;
    private Integer deviceId;
    private Double measurementValue;
    private final String QUEUE_NAME = "dsQueue1";
    private Channel channel;
    private CSVReader csvReader;
    private FileReader filereader;
    private ConnectionFactory factory;
    private  Connection connection;

    public ReadFromCsv(){
        try {
            this.filereader = new FileReader(fileName);
            this.csvReader = new CSVReader(this.filereader);
            this.factory = new ConnectionFactory();
            this.factory.setHost("localhost");
            this.connection=factory.newConnection();

            channel = connection.createChannel();
            channel.queueDeclare(RabbitConfig.QUEUE, false, false, false, null);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getTimestamp() {
        return timestamp;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public Double getMeasurementValue() {
        return measurementValue;
    }

    public void connectToQueue(JsonObject jsonObject) {
        try {
            channel.basicPublish("", RabbitConfig.QUEUE, null, jsonObject.toString().getBytes());
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public boolean readCSV() {
        try {
            String[] nextRecord;

            if ((nextRecord = csvReader.readNext()) != null) {
                timestamp = Timestamp.from(Instant.now()).toString();
                deviceId = 1;
                measurementValue = Double.parseDouble(nextRecord[0]);

                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }

    }
}


