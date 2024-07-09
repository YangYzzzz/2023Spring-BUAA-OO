import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.TimableOutput;

import java.util.ArrayList;
import java.util.Iterator;

import static java.lang.Thread.sleep;

public class Elevator implements Runnable {
    private int lastDance = 2; //还能上两层
    private int maintain;
    private static final int UP;
    private static final int DOWN;
    private int id; //电梯ID
    private int currentFloor;  //当前楼层
    private int currentNum; //当前人数
    private int maxNum;     //满载人数
    private double speed; //电梯运行速度
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

    public int getMaintain() {
        return maintain;
    }

    public void setMaintain(int maintain) {
        this.maintain = maintain;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public Elevator(int id, Scheduler scheduler) {
        this.id = id;
        this.currentFloor = 1;
        this.currentNum = 0;
        this.direction = UP;
        this.personRequests = new ArrayList<>();
        this.scheduler = scheduler;
        this.buffer = new ArrayList<>();
        this.maxNum = 6;
        this.speed = 0.4;
        this.maintain = 0;
    }

    public Elevator(int id, Scheduler scheduler, int maxNum, double speed, int currentFloor) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.currentNum = 0;
        this.direction = UP;
        this.personRequests = new ArrayList<>();
        this.scheduler = scheduler;
        this.buffer = new ArrayList<>();
        this.maxNum = maxNum;
        this.speed = speed;
        this.maintain = 0;
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

    public void move() throws InterruptedException {  //认为先移动到指定楼层再休眠
        if (direction == UP) {
            this.currentFloor++;
        } else if (direction == DOWN) {
            this.currentFloor--;
        }
        sleep((long) (1000 * this.speed));  //400毫秒 0.4秒 等待0.4秒
        TimableOutput.println("ARRIVE-" + this.currentFloor + "-" + this.id);
        if (maintain == 1) {
            lastDance--;
        }
    }

    public void open() throws InterruptedException {
        TimableOutput.println("OPEN-" + this.currentFloor + "-" + this.id);  //先开门
        if (personRequests != null) {
            Iterator<PersonRequest> ite = personRequests.iterator();
            while (ite.hasNext()) {  //再下人
                PersonRequest it = ite.next();
                if (it.getToFloor() == this.currentFloor) {
                    TimableOutput.println("OUT-" + it.getPersonId() + "-" + this.currentFloor
                            + "-" + this.id);
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
                        this.currentFloor + "-" + this.id);
                this.personRequests.add(it);
                this.currentNum++;
            }
            this.buffer.clear(); //清空缓冲区
        }
        TimableOutput.println("CLOSE-" + this.currentFloor + "-" + this.id);//最后关门
    }

    public void turn() {   //掉头
        if (this.direction == UP) {
            this.direction = DOWN;
        } else {
            this.direction = UP;
        }
    }

    public void maintain() throws InterruptedException { //至多再上两层楼 再停修 有问题 不上人了只下人 暂停不了
        int i = 0;
        while (i < lastDance) {
            if (scheduler.canOut(this)) {
                open();
            }
            if (personRequests.isEmpty()) {
                //System.out.println("不走了");
                break;
            }
            //System.out.println("再往上上一层");
            move();
        }
        if (!personRequests.isEmpty()) { //都下车
            TimableOutput.println("OPEN-" + this.currentFloor + "-" + this.id);
            sleep(400);
            Iterator<PersonRequest> ite = personRequests.iterator();
            while (ite.hasNext()) {  //再下人
                PersonRequest it = ite.next();
                if (it.getToFloor() != this.currentFloor) {  //在当前层下车 改变起始楼层
                    scheduler.setRequestQueue(it, this.currentFloor);
                }
                ite.remove();
                TimableOutput.println("OUT-" + it.getPersonId() + "-" + this.currentFloor
                        + "-" + this.id);
            }
            TimableOutput.println("CLOSE-" + this.currentFloor + "-" + this.id);//最后关门
        }
        scheduler.removeElevator(this); //调度器移除电梯
        TimableOutput.println("MAINTAIN_ABLE-" + this.id);//最后关门
        synchronized (scheduler.getRequestQueue()) {   //锁上了
            scheduler.getRequestQueue().notifyAll();
        }
    }

    @Override
    public void run() {  //ARRIVE IN OUT OPEN CLOSE TURN 循环调用调度器 执行完一次操作就接着调用
        Operation operation;
        try {
            while (true) {
                //System.out.println(currentThread().getName());
                operation = scheduler.getOperation(this);
                //TimableOutput.println(operation + currentThread().getName());
                if (operation == Operation.OVER) {
                    //System.out.println("Elevator" + currentThread().getName() + " Stop!");
                    break;
                } else if (operation == Operation.WAIT) {
                    continue;
                } else if (operation == Operation.MOVE) {
                    move();
                } else if (operation == Operation.OPEN) {
                    open();
                } else if (operation == Operation.TURN) {
                    turn();
                } else if (operation == Operation.MAINTAIN) { //执行完临终遗言 终止进程
                    maintain();
                    break;
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

