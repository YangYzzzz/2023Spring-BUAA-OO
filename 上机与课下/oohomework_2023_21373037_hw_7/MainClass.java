
import com.oocourse.elevator3.TimableOutput;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();  //使用TimableOutput.println输出结果 线程叫啥名不重要
        RequestQueue requestQueue = new RequestQueue();
        Scheduler scheduler = new Scheduler(requestQueue);
        Thread input = new Thread(new Input(requestQueue, scheduler));
        input.start();
        Elevator elevator1 = new Elevator(1, scheduler);
        Elevator elevator2 = new Elevator(2, scheduler);
        Elevator elevator3 = new Elevator(3, scheduler);
        Elevator elevator4 = new Elevator(4, scheduler);
        Elevator elevator5 = new Elevator(5, scheduler);
        Elevator elevator6 = new Elevator(6, scheduler);
        scheduler.addElevator(elevator1);
        scheduler.addElevator(elevator2);
        scheduler.addElevator(elevator3);
        scheduler.addElevator(elevator4);
        scheduler.addElevator(elevator5);
        scheduler.addElevator(elevator6);
        Thread thread1 = new Thread(elevator1);
        thread1.start();
        Thread thread2 = new Thread(elevator2);
        thread2.start();
        Thread thread3 = new Thread(elevator3);
        thread3.start();
        Thread thread4 = new Thread(elevator4);
        thread4.start();
        Thread thread5 = new Thread(elevator5);
        thread5.start();
        Thread thread6 = new Thread(elevator6);
        thread6.start();
    }
}
