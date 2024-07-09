import java.util.ArrayList;
import java.util.HashMap;

public class RequestQueue { //请求队列类
    private final HashMap<Integer, ArrayList<MyselfPersonRequest>> requestQueue;  //拆分请求
    //Integer代表起始楼层，后面跟着同为一个起始楼层的人们
    private OverControl overControl;

    public RequestQueue() {
        this.requestQueue = new HashMap<>();
        for (int i = 1; i <= 11; i++) {  //把数组提前开好
            ArrayList<MyselfPersonRequest> personRequests = new ArrayList<>();
            requestQueue.put(i, personRequests);
        }
        overControl = new OverControl();
    }

    public OverControl getOverControl() {
        return overControl;
    }

    public void setOverControl(OverControl overControl) {
        this.overControl = overControl;
    }

    public synchronized void setEndFlag() {
        //TimableOutput.println("end coming");
        notifyAll();
        overControl.setEndFlag();
    }

    public synchronized void setRequestQueue(MyselfPersonRequest personRequest) {
        requestQueue.get(personRequest.fromFloor()).add(personRequest);
        overControl.addRequest();
        this.notifyAll();
    }

    public synchronized void setRequestQueue(MyselfPersonRequest personRequest, int floor) {
        requestQueue.get(floor).add(personRequest);
        this.notifyAll();
    }

    public synchronized ArrayList<MyselfPersonRequest> getFloorRequestQueue(int floor) {
        //调度器根据当前楼层获取请求信息 在调度器内对请求队列进行删除
        if (!requestQueue.get(floor).isEmpty()) {
            return requestQueue.get(floor);
        } else {
            return null;
        }
    }

    public synchronized boolean isEmpty() {
        for (HashMap.Entry<Integer, ArrayList<MyselfPersonRequest>> entry :
                this.requestQueue.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public synchronized void updatePath() {  //更新请求队列中全部的路径
        for (HashMap.Entry<Integer, ArrayList<MyselfPersonRequest>> entry :
                requestQueue.entrySet()) {
            for (MyselfPersonRequest it : entry.getValue()) {
                it.setSequence(it.fromFloor(), it.getEndFloor());
            }
        }
    }

    public synchronized boolean isOver() {
        return overControl.canOver();
    }
}

