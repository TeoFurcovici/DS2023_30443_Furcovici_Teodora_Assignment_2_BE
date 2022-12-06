package Main;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.Date;
import java.util.Timer;

@Component
public class Send {

    @RabbitListener(queues = "dsQueue1")
    public void readWithDelay(ReadFromCsv csv, String deviceId) {
        LoopTask loopTask = new LoopTask();
        Timer timer = new Timer("TaskName");
        long delay = 1000; // delay in milliseconds
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("timestamp", csv.getTimestamp())
                .add("deviceId", Integer.parseInt(deviceId))
                .add("measurementValue", csv.getMeasurementValue()).build();
       csv.connectToQueue(jsonObject);
        //template.convertAndSend(RabbitConfig.EXCHANGE,RabbitConfig.ROUTING_KEY,jsonObject);
        System.out.println(jsonObject);

        while(csv.readCSV()){
            loopTask.setSensorValue(csv.getMeasurementValue());
            loopTask.run();
            jsonObject = Json.createObjectBuilder()
                    .add("timestamp", csv.getTimestamp())
                    .add("deviceId", Integer.parseInt(deviceId))
                    .add("measurementValue", csv.getMeasurementValue()).build();
           csv.connectToQueue(jsonObject);
            //template.convertAndSend(RabbitConfig.EXCHANGE,RabbitConfig.ROUTING_KEY,jsonObject);
            System.out.println(jsonObject);
        }

        timer.cancel();
        timer = new Timer("TaskName");
        Date executionDate = new Date();
        timer.scheduleAtFixedRate(loopTask, executionDate, delay);
    }
}
