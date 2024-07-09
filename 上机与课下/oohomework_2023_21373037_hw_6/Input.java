import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.MaintainRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.io.IOException;

public class Input implements Runnable {   //输入线程 拥有电梯输入 增添电梯请求 维修电梯如何实现
    private final ElevatorInput elevatorInput;
    private final RequestQueue requestQueue;

    private final Scheduler scheduler;

    public Input(RequestQueue requestQueue, Scheduler scheduler) {
        this.elevatorInput = new ElevatorInput(System.in);
        this.requestQueue = requestQueue;
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {   //终止条件
                requestQueue.setEndFlag();
                //System.out.println("Input Stop");
                break;
            }
            //TimableOutput.println(request.getPersonId() + " is coming!");
            if (request instanceof PersonRequest) {
                requestQueue.setRequestQueue((PersonRequest) request);
            } else if (request instanceof MaintainRequest) {
                scheduler.maintainElevator((MaintainRequest) request);
            } else if (request instanceof ElevatorRequest) {
                scheduler.addElevator((ElevatorRequest) request);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

