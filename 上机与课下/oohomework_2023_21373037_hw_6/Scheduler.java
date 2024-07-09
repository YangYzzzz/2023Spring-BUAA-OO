import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.MaintainRequest;
import com.oocourse.elevator2.PersonRequest;
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

    public Scheduler(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
        this.elevators = new ArrayList<>();
    }

    public void addElevator(ElevatorRequest request) {  //增加电梯请求 开新的线程
        synchronized (elevators) {
            Elevator elevator = new Elevator(request.getElevatorId(), this,
                    request.getCapacity(), request.getSpeed(), request.getFloor());
            elevators.add(elevator);
            Thread newThread = new Thread(elevator);
            newThread.start();
        }
    }

    public void addElevator(Elevator elevator) {
        synchronized (elevators) {
            this.elevators.add(elevator);
        }
    }

    public void maintainElevator(MaintainRequest request) { //锁 待移除电梯的列表 锁后唤醒全部电梯
        //若不唤醒可能该请求无法响应
        synchronized (elevators) {
            for (Elevator elevator : elevators) {
                if (elevator.getId() == request.getElevatorId()) {
                    elevator.setMaintain(1);
                }
            }
        }
        synchronized (requestQueue) {
            requestQueue.notifyAll();
        }
    }

    public void removeElevator(Elevator elevator) {  //电梯停工 删除调度器内的电梯
        synchronized (elevators) {
            elevators.remove(elevator);
        }
    }

    public boolean isUp(PersonRequest it) {
        return it.getFromFloor() <= it.getToFloor();
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void getRequestQueue(Elevator elevator) {
        //ArrayList<PersonRequest> personRequests = new ArrayList<>();
        synchronized (requestQueue) {
            ArrayList<PersonRequest> personRequestsAll = requestQueue.getFloorRequestQueue(
                    elevator.getCurrentFloor()); //获得当前层等待的人
            int num = elevator.getCurrentNum() + elevator.getBuffer().size();
            //寻找到当前层可以上的人
            if (personRequestsAll != null) {
                Iterator<PersonRequest> ite = personRequestsAll.iterator();
                while (ite.hasNext()) {
                    PersonRequest it = ite.next();
                    if (((isUp(it) && elevator.getDirection() == UP) ||
                            (!isUp(it) && elevator.getDirection() == DOWN))
                            && num < elevator.getMaxNum()) {   //设置缓冲，下
                        elevator.getBuffer().add(it);   //缓冲区添人
                        //System.out.println("Buffer ++");
                        num++;
                        ite.remove();  //提前删人 第二次再扫描一遍
                    }
                }
            }
        }
    }

    public boolean canOut(Elevator elevator) {  //判断此刻需不需要下人
        for (PersonRequest personRequest : elevator.getPersonRequests()) {
            if (personRequest.getToFloor() == elevator.getCurrentFloor()) {
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
        for (PersonRequest it : elevator.getPersonRequests()) {
            if (it.getToFloor() != elevator.getCurrentFloor()) {
                return false;
            }
        }
        return true;
    }

    public boolean noMoreRequire(Elevator elevator) {     //纯用look算法 不考虑其他电梯
        if (elevator.getDirection() == UP) {
            for (int i = elevator.getCurrentFloor(); i <= MAX_FLOOR; i++) {
                ArrayList<PersonRequest> personRequests = requestQueue.getFloorRequestQueue(i);
                if (i == elevator.getCurrentFloor() && personRequests != null) { //若在当前楼层 则判断有没有往上走的
                    for (PersonRequest it : personRequests) {
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
                ArrayList<PersonRequest> personRequests = requestQueue.getFloorRequestQueue(i);
                if (i == elevator.getCurrentFloor() && personRequests != null) { //若在当前楼层 则判断有没有往上走的
                    for (PersonRequest it : personRequests) {
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

    public boolean canTurn(Elevator currentElevator) { //若电梯里有人则不存在转向 1 电梯里此刻无人且当前层无人往上 2 当前方向无其他需求
        return noMoreRequire(currentElevator) && noMorePerson(currentElevator);
    }

    public boolean existInMidReverse(Elevator elevator, PersonRequest personRequest) { //在中途是否会掉头
        int flag = 0;
        if (elevator.getDirection() == UP) {
            for (int i = elevator.getCurrentFloor(); i < personRequest.getFromFloor(); i++) {
                ArrayList<PersonRequest> queues = this.requestQueue.getFloorRequestQueue(i);
                if (queues != null) {  //找到最近的第一个不为空的队列
                    for (PersonRequest queue : queues) {
                        if (isUp(queue)) {
                            flag = 1;
                        }
                    }
                    return flag == 0;  //最近的只要有一个是走同侧的返回false 不同侧返回true
                }
            }
        } else {
            for (int i = elevator.getCurrentFloor(); i > personRequest.getFromFloor(); i--) {
                ArrayList<PersonRequest> queues = this.requestQueue.getFloorRequestQueue(i);
                if (queues != null) {  //找到最近的第一个不为空的队列
                    for (PersonRequest queue : queues) {
                        if (!isUp(queue)) {
                            flag = 1;
                        }
                    }
                    return flag == 0;
                }
            }
        }
        return false;  //中间没东西返回false
    }

    public boolean timeCompare(Elevator it, Elevator other, PersonRequest person) {  //比较两台电梯的时间之差
        return it.getSpeed() * abs(person.getFromFloor() - it.getCurrentFloor()) >
                other.getSpeed() * abs(person.getFromFloor() - other.getCurrentFloor());
    }

    public boolean thisMoreSatisfy(PersonRequest it, Elevator elevator) {  //当前电梯为空 都认为彼此会接
        synchronized (elevators) {
            for (Elevator elevatorAll : elevators) {
                //不是同一台电梯 且不在维修状态
                if (!elevatorAll.equals(elevator) && elevatorAll.getMaintain() == 0) {
                    if (elevatorAll.getCurrentNum() == 0 && ((elevatorAll.getDirection() == UP &&
                            elevatorAll.getCurrentFloor() <= it.getFromFloor()) ||
                            (elevatorAll.getDirection() == DOWN &&
                                    elevatorAll.getCurrentFloor() >= it.getFromFloor())) &&
                            timeCompare(elevator, elevatorAll, it)) {
                        return false;
                    }
                    if (elevatorAll.getCurrentNum() > 0 &&
                            elevatorAll.getCurrentNum() < elevator.getCurrentNum()) {
                        if (((elevatorAll.getDirection() == UP && elevatorAll.getCurrentFloor()
                                <= it.getFromFloor() && isUp(it)) ||
                                (elevatorAll.getDirection() == DOWN && elevatorAll.getCurrentFloor()
                                        >= it.getFromFloor() && !isUp(it))) &&
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
                ArrayList<PersonRequest> personRequests = requestQueue.getFloorRequestQueue(i);
                if (personRequests != null) {
                    for (PersonRequest it : personRequests) {  //每一层的每一个请求 不考虑其他电梯的超载问题 看其他电梯接客会不会更快
                        if (thisMoreSatisfy(it, elevator)) {
                            return true;
                        }
                        totalNum++;
                    }
                }
            }
        } else {
            for (int i = MIN_FLOOR; i < elevator.getCurrentFloor(); i++) {
                ArrayList<PersonRequest> personRequests = requestQueue.getFloorRequestQueue(i);
                if (personRequests != null) {
                    for (PersonRequest it : personRequests) {  //每一层的每一个请求 不考虑其他电梯的超载问题 看其他电梯接客会不会更快
                        if (thisMoreSatisfy(it, elevator)) {
                            return true;
                        }
                        totalNum++;
                    }
                }
            }
        }
        return totalNum >= 13;
    }

    public boolean currentFloorReverse(Elevator elevator) {
        ArrayList<PersonRequest> requests =
                requestQueue.getFloorRequestQueue(elevator.getCurrentFloor());
        if (requests != null) {
            for (PersonRequest it : requests) {
                if ((elevator.getDirection() == UP && isUp(it))
                        || (elevator.getDirection() == DOWN && !isUp(it))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void setRequestQueue(PersonRequest personRequest, int floor) {
        requestQueue.setRequestQueue(personRequest, floor);
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
            if (canIn(elevator) || canOut(elevator)) {
                return Operation.OPEN;
            }
            if (elevator.getCurrentNum() != 0) {
                return Operation.MOVE;
            } else {
                if (requestQueue.isEmpty()) {
                    //TimableOutput.println(elevator.getId() + " is empty");
                    if (requestQueue.isOver() && !hasMaintain()) {
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

