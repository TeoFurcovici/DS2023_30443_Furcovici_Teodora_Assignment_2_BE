package Main;

import java.util.TimerTask;

public class LoopTask extends TimerTask {
    private Double sensorValue;

    public void setSensorValue(Double sensorValue) {
        this.sensorValue = sensorValue;
    }
    @Override
    public void run() {
        System.out.println(this.sensorValue);
        try {
            //assuming it takes 10 secs to complete the task
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
