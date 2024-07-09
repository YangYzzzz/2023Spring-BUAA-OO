import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;
import java.io.IOException;

public class Input implements Runnable {   //输入线程 拥有电梯输入
    private final ElevatorInput elevatorInput;
    private final RequestQueue requestQueue;

    public Input(RequestQueue requestQueue) {
        this.elevatorInput = new ElevatorInput(System.in);
        this.requestQueue = requestQueue;
    }

    @Override
    public void run() {
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {   //终止条件
                requestQueue.setEndFlag();
                //System.out.println("Input Stop");
                break;
            }
            //TimableOutput.println(request.getPersonId() + " is coming!");
            requestQueue.setRequestQueue(request);
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
