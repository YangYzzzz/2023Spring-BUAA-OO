import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.MaintainRequest;
import java.util.ArrayList;
import java.util.Iterator;

import static java.lang.Math.abs;

public class Scheduler {
    private final RequestQueue requestQueue;
    private final ArrayList<Elevator> elevators;
    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int MAX_FLOOR = 11;
    private static final int MIN_FLOOR = 1;

    private FloorMonitor floorMonitor;

    public Scheduler(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
        this.elevators = new ArrayList<>();
        this.floorMonitor = new FloorMonitor();
    }

    public FloorMonitor getFloorMonitor() {
        return floorMonitor;
    }

    public void setFloorMonitor(FloorMonitor floorMonitor) {
        this.floorMonitor = floorMonitor;
    }

    public void addElevator(ElevatorRequest request) {  //增加电梯请求 开新的线程
        synchronized (elevators) {
            Elevator elevator = new Elevator(request.getElevatorId(), this, request.getCapacity(),
                    request.getSpeed(), request.getFloor(), request.getAccess());
            elevators.add(elevator);
            Thread newThread = new Thread(elevator);
            newThread.start();
        }
        requestQueue.updatePath();
    }

    public void addElevator(Elevator elevator) {
        synchronized (elevators) {
            this.elevators.add(elevator);
        }
    }

    public void setRequestQueue(MyselfPersonRequest personRequest, int floor) {
        requestQueue.setRequestQueue(personRequest, floor);
    }

    public void normalOutPerson(MyselfPersonRequest personRequest) {  //正常下人 下来的人重置方法
        if (personRequest.toFloor() == personRequest.getEndFloor()) {
            requestQueue.getOverControl().hasDoneRequest();
            return;
        }
        personRequest.setSequence(personRequest.toFloor(), personRequest.getEndFloor());
        setRequestQueue(personRequest, personRequest.fromFloor()); //一旦下车没到 就重置路线
    }

    public void maintainOutPerson(MyselfPersonRequest personRequest, int currentFloor) { //维修时下人
        personRequest.setSequence(currentFloor, personRequest.getEndFloor());
        setRequestQueue(personRequest, personRequest.fromFloor());  //从当前楼看作是起点
    }

    public void maintainElevator(MaintainRequest request) { //锁后唤醒全部电梯
        //若不唤醒可能该请求无法响应
        synchronized (elevators) {
            for (Elevator elevator : elevators) {
                if (elevator.getId() == request.getElevatorId()) {
                    elevator.setMaintain(1);
                }
            }
        }
        synchronized (requestQueue) {
            requestQueue.updatePath();  //增加电梯 维修电梯时对所有仍在队列的请求更新路径 下车时对单个请求维护路径
            requestQueue.notifyAll();
        }
    }

    public void removeElevator(Elevator elevator) {  //电梯停工 删除调度器内的电梯
        synchronized (elevators) {
            elevators.remove(elevator);
        }
    }

    public ArrayList<Elevator> getElevators() {
        synchronized (elevators) {
            return elevators;
        }
    }

    public boolean isUp(MyselfPersonRequest it) {
        return it.fromFloor() <= it.toFloor();
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void getRequestQueue(Elevator elevator) {
        //ArrayList<MyselfPersonRequest> personRequests = new ArrayList<>();
        synchronized (requestQueue) {
            ArrayList<MyselfPersonRequest> personRequestsAll = requestQueue.getFloorRequestQueue(
                    elevator.getCurrentFloor()); //获得当前层等待的人
            int num = elevator.getCurrentNum() + elevator.getBuffer().size();
            //寻找到当前层可以上的人
            if (personRequestsAll != null) {
                Iterator<MyselfPersonRequest> ite = personRequestsAll.iterator();
                while (ite.hasNext()) {
                    MyselfPersonRequest it = ite.next();
                    if (((isUp(it) && elevator.getDirection() == UP) ||
                            (!isUp(it) && elevator.getDirection() == DOWN)) && num <
                            elevator.getMaxNum() && elevator.getAccess().contains(it.toFloor())) {
                        elevator.getBuffer().add(it);   //缓冲区添人 当前电梯的可达楼层应包含该请求当前要去的楼层
                        //System.out.println("Buffer ++");
                        num++;
                        ite.remove();  //提前删人 第二次再扫描一遍
                    }
                }
            }
        }
    }

    public boolean canOut(Elevator elevator) {  //判断此刻需不需要下人
        for (MyselfPersonRequest personRequest : elevator.getPersonRequests()) {
            if (personRequest.toFloor() == elevator.getCurrentFloor()) {
                return true;
            }
        }
        return false;
    }

    public boolean canIn(Elevator currentElevator) {  //判断此刻需不需要上人 关键 调用 getFloorRequestQueue
        getRequestQueue(currentElevator);
        return !currentElevator.getBuffer().isEmpty();
    }

    public boolean noMorePerson(Elevator elevator) {  //判断电梯在到该层时所有人都将下车
        for (MyselfPersonRequest it : elevator.getPersonRequests()) {
            if (it.toFloor() != elevator.getCurrentFloor()) {
                return false;
            }
        }
        return true;
    }

    public boolean noMoreRequire(Elevator elevator) {     //纯用look算法 不考虑其他电梯
        if (elevator.getDirection() == UP) {
            for (int i = elevator.getCurrentFloor(); i <= MAX_FLOOR; i++) {
                ArrayList<MyselfPersonRequest> personRequests =
                        requestQueue.getFloorRequestQueue(i);
                if (i == elevator.getCurrentFloor() && personRequests != null) { //若在当前楼层 则判断有没有往上走的
                    for (MyselfPersonRequest it : personRequests) {
                        if (isUp(it)) {
                            return false;
                        }
                    }
                } else if (i != elevator.getCurrentFloor() && personRequests != null) {
                    return false;
                }
            }
        } else {
            for (int i = elevator.getCurrentFloor(); i >= MIN_FLOOR; i--) {
                ArrayList<MyselfPersonRequest> personRequests =
                        requestQueue.getFloorRequestQueue(i);
                if (i == elevator.getCurrentFloor() && personRequests != null) { //若在当前楼层 则判断有没有往上走的
                    for (MyselfPersonRequest it : personRequests) {
                        if (!isUp(it)) {
                            return false;
                        }
                    }
                } else if (i != elevator.getCurrentFloor() && personRequests != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean canOpen(Elevator it) { //当前层能否停靠
        return it.getAccess().contains(it.getCurrentFloor());
    }

    //比较两台电梯的时间之差
    public boolean timeCompare(Elevator it, Elevator other, MyselfPersonRequest person) {
        return it.getSpeed() * abs(person.fromFloor() - it.getCurrentFloor()) >
                other.getSpeed() * abs(person.fromFloor() - other.getCurrentFloor());
    }

    public boolean thisMoreSatisfy(MyselfPersonRequest it, Elevator elevator) {  //当前电梯为空 都认为彼此会接
        synchronized (elevators) {
            for (Elevator elevatorAll : elevators) {
                //不是同一台电梯 且不在维修状态
                if (!elevatorAll.equals(elevator) && elevatorAll.getMaintain() == 0 &&
                        elevatorAll.getAccess().contains(it.toFloor()) &&
                        elevatorAll.getAccess().contains(it.fromFloor())) {
                    if (elevatorAll.getCurrentNum() == 0 && ((elevatorAll.getDirection() == UP &&
                            elevatorAll.getCurrentFloor() <= it.fromFloor()) ||
                            (elevatorAll.getDirection() == DOWN &&
                                    elevatorAll.getCurrentFloor() >= it.fromFloor())) &&
                            timeCompare(elevator, elevatorAll, it)) {
                        return false;
                    }
                    if (elevatorAll.getCurrentNum() > 0 &&
                            elevatorAll.getCurrentNum() < elevator.getCurrentNum()) {
                        if (((elevatorAll.getDirection() == UP && elevatorAll.getCurrentFloor()
                                <= it.fromFloor() && isUp(it)) ||
                                (elevatorAll.getDirection() == DOWN && elevatorAll.getCurrentFloor()
                                        >= it.fromFloor() && !isUp(it))) &&
                                timeCompare(elevator, elevatorAll, it)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    public boolean needGoToCarryPerson(Elevator elevator, int direction) { //当前电梯内无人需要去接人 与上下无关 从远处接
        int totalNum = 0;
        if (direction == UP) {
            for (int i = MAX_FLOOR; i > elevator.getCurrentFloor(); i--) {
                ArrayList<MyselfPersonRequest> personRequests =
                        requestQueue.getFloorRequestQueue(i);
                if (personRequests != null) {
                    //遍历每一层的每一个请求 不考虑其他电梯的超载问题 看其他电梯接客会不会更快
                    for (MyselfPersonRequest it : personRequests) {
                        if (elevator.getAccess().contains(it.fromFloor()) &&
                                elevator.getAccess().contains(it.toFloor())) {
                            totalNum++;
                            if (thisMoreSatisfy(it, elevator)) {
                                return true;
                            }
                        }
                    }
                }
            }
        } else {
            for (int i = MIN_FLOOR; i < elevator.getCurrentFloor(); i++) {
                ArrayList<MyselfPersonRequest> personRequests =
                        requestQueue.getFloorRequestQueue(i);
                if (personRequests != null) {
                    for (MyselfPersonRequest it : personRequests) {
                        if (elevator.getAccess().contains(it.fromFloor()) &&
                                elevator.getAccess().contains(it.toFloor())) {
                            totalNum++;
                            if (thisMoreSatisfy(it, elevator)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return totalNum >= 13;   //有待讨论
    }

    public boolean currentFloorReverse(Elevator elevator) {
        int flag = 0;
        ArrayList<MyselfPersonRequest> requests =
                requestQueue.getFloorRequestQueue(elevator.getCurrentFloor());
        if (requests != null) {
            for (MyselfPersonRequest it : requests) {
                if (((elevator.getDirection() == UP && isUp(it))
                        || (elevator.getDirection() == DOWN && !isUp(it)))
                        && elevator.getAccess().contains(it.fromFloor())
                    && elevator.getAccess().contains(it.toFloor())) {
                    return false;
                }
                if (((elevator.getDirection() == UP && !isUp(it))
                        || (elevator.getDirection() == DOWN && isUp(it)))
                        && elevator.getAccess().contains(it.fromFloor())
                        && elevator.getAccess().contains(it.toFloor())) {
                    flag = 1;
                }
            }
            return flag == 1;
        }
        return false;
    }

    /*public boolean isMaintain(Elevator elevator) {  //在电梯Move后判断是否被维修 若被维修则lastDance加1
        synchronized (elevators) {
            for (Elevator elevator1 : elevators) {
                if (it == elevator.getId()) {
                    return true;
                }
            }
            return false;
        }
    } */

    public boolean hasMaintain() {
        synchronized (elevators) {
            for (Elevator it : elevators) {
                if (it.getMaintain() == 1) {
                    return true;
                }
            }
            return false;
        }
    }

    public Operation getOperation(Elevator elevator) throws InterruptedException {
        //是否一直会掉头 转向方法放在哪 开两次门如何处理 在第二次OPEN时判断可不可以掉头
        synchronized (requestQueue) {  //主要是用wait方法
            if (elevator.getMaintain() == 1) {
                return Operation.MAINTAIN;
            }
            if (canOpen(elevator)) {
                if (canIn(elevator) || canOut(elevator)) {
                    return Operation.OPEN;
                }
            }
            if (elevator.getCurrentNum() != 0) {
                return Operation.MOVE;
            } else {
                if (requestQueue.isEmpty()) {
                    //TimableOutput.println(elevator.getId() + " is empty");
                    if (requestQueue.isOver()) {
                        //System.out.println(elevator.getId() + " Over!!");
                        requestQueue.notifyAll();
                        return Operation.OVER;
                    } else {
                        //System.out.println(elevator.getId() + " wait");
                        requestQueue.wait();
                        //System.out.println(elevator.getId() + " wait Over");
                        return Operation.WAIT;
                    }
                } else {
                    if (needGoToCarryPerson(elevator, elevator.getDirection())) {  //有问题
                        return Operation.MOVE;
                    } else if (needGoToCarryPerson(elevator, 1 - elevator.getDirection()) ||
                            currentFloorReverse(elevator)) {
                        return Operation.TURN;
                    } else {
                        //System.out.println(Thread.currentThread().getName());
                        requestQueue.wait(400);
                        return Operation.WAIT;
                    }
                }
            }
        }
    }
}

