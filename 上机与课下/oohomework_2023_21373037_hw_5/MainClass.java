
import com.oocourse.elevator1.TimableOutput;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();  //使用TimableOutput.println输出结果
        RequestQueue requestQueue = new RequestQueue();
        Thread input = new Thread(new Input(requestQueue), "input");
        input.start();
        Scheduler scheduler = new Scheduler(requestQueue);
        Elevator elevator1 = new Elevator();
        elevator1.setScheduler(scheduler);
        Elevator elevator2 = new Elevator();
        elevator2.setScheduler(scheduler);
        Elevator elevator3 = new Elevator();
        elevator3.setScheduler(scheduler);
        Elevator elevator4 = new Elevator();
        elevator4.setScheduler(scheduler);
        Elevator elevator5 = new Elevator();
        elevator5.setScheduler(scheduler);
        Elevator elevator6 = new Elevator();
        elevator6.setScheduler(scheduler);
        scheduler.addElevator(elevator1);
        scheduler.addElevator(elevator2);
        scheduler.addElevator(elevator3);
        scheduler.addElevator(elevator4);
        scheduler.addElevator(elevator5);
        scheduler.addElevator(elevator6);
        Thread thread1 = new Thread(elevator1, "1");
        thread1.start();
        Thread thread2 = new Thread(elevator2, "2");
        thread2.start();
        Thread thread3 = new Thread(elevator3, "3");
        thread3.start();
        Thread thread4 = new Thread(elevator4, "4");
        thread4.start();
        Thread thread5 = new Thread(elevator5, "5");
        thread5.start();
        Thread thread6 = new Thread(elevator6, "6");
        thread6.start();
    }
}
