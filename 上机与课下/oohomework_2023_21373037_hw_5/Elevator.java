import com.oocourse.elevator1.PersonRequest;
import com.oocourse.elevator1.TimableOutput;

import java.util.ArrayList;
import java.util.Iterator;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class Elevator implements Runnable {
    private static final int UP;
    private static final int DOWN;
    private int currentFloor;  //当前楼层
    private int currentNum; //当前人数
    private int direction;  //当前方向
    private Scheduler scheduler;
    private ArrayList<PersonRequest> personRequests; //当前电梯内每个人信息 存储方式为PersonRequest形式 包含id to from
    private ArrayList<PersonRequest> buffer;

    public ArrayList<PersonRequest> getBuffer() {
        return buffer;
    }

    public void setBuffer(ArrayList<PersonRequest> buffer) {
        this.buffer = buffer;
    }

    public int remainPosition() {
        return 6 - this.currentNum;
    }

    public Elevator() {
        this.currentFloor = 1;
        this.currentNum = 0;
        this.direction = UP;
        this.personRequests = new ArrayList<>();
        this.scheduler = null;
        this.buffer = new ArrayList<>();
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }

    public int getForward() {
        return direction;
    }

    public void setForward(int forward) {
        this.direction = forward;
    }

    public ArrayList<PersonRequest> getPersonRequests() {
        return personRequests;
    }

    public void setPersonRequests(ArrayList<PersonRequest> personRequests) {
        this.personRequests = personRequests;
    }

    public void move(int direction) throws InterruptedException {  //认为先移动到指定楼层再休眠
        if (direction == UP) {
            this.currentFloor++;
        } else if (direction == DOWN) {
            this.currentFloor--;
        }
        sleep(400);  //400毫秒 0.4秒 等待0.4秒
        TimableOutput.println("ARRIVE-" + this.currentFloor + "-" + currentThread().getName());
    }

    public void open() throws InterruptedException {
        TimableOutput.println("OPEN-" + this.currentFloor + "-" + currentThread().getName());  //先开门
        if (personRequests != null) {
            Iterator<PersonRequest> ite = personRequests.iterator();
            while (ite.hasNext()) {  //再下人
                PersonRequest it = ite.next();
                if (it.getToFloor() == this.currentFloor) {
                    TimableOutput.println("OUT-" + it.getPersonId() + "-" + this.currentFloor
                            + "-" + currentThread().getName());
                    ite.remove();
                    this.currentNum--;
                }
            }
        }
        sleep(400);  //休眠开关门所有时间
        //后进人 缓冲区
        this.scheduler.getRequestQueue(this);
        if (this.buffer != null) {
            for (PersonRequest it : this.buffer) {
                TimableOutput.println("IN-" + it.getPersonId() + "-" +
                        this.currentFloor + "-" + currentThread().getName());
                this.personRequests.add(it);
                this.currentNum++;
            }
            this.buffer.clear(); //清空缓冲区
        }
        TimableOutput.println("CLOSE-" + this.currentFloor + "-" + currentThread().getName());//最后关门
    }

    public void turn() {   //掉头
        if (this.direction == UP) {
            this.direction = DOWN;
        } else {
            this.direction = UP;
        }
    }

    @Override
    public void run() {  //ARRIVE IN OUT OPEN CLOSE TURN 循环调用调度器 执行完一次操作就接着调用
        Operation operation;
        try {
            while (true) {
                //System.out.println(currentThread().getName());
                operation = scheduler.getOperation(currentThread().getName());
                TimableOutput.println(operation + currentThread().getName());
                if (operation == Operation.OVER) {
                    //System.out.println("Elevator" + currentThread().getName() + " Stop!");
                    break;
                } else if (operation == Operation.WAIT) {
                    continue;
                } else if (operation == Operation.MOVE) {
                    move(this.direction);
                } else if (operation == Operation.OPEN) {
                    open();
                } else if (operation == Operation.TURN) {
                    turn();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        UP = 0;
        DOWN = 1;
    }
}
