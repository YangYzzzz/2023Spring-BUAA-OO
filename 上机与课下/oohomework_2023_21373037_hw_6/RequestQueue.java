import com.oocourse.elevator2.PersonRequest;
import java.util.ArrayList;
import java.util.HashMap;

public class RequestQueue { //请求队列类
    private final HashMap<Integer, ArrayList<PersonRequest>> requestQueue;
    //Integer代表起始楼层，后面跟着同为一个起始楼层的人们
    private int endFlag;

    public RequestQueue() {
        this.requestQueue = new HashMap<>();
        for (int i = 1; i <= 11; i++) {  //把数组提前开好
            ArrayList<PersonRequest> personRequests = new ArrayList<>();
            requestQueue.put(i, personRequests);
        }
        endFlag = 0;
    }

    public synchronized void setEndFlag() {
        //TimableOutput.println("end coming");
        notifyAll();
        this.endFlag = 1;
    }

    public synchronized void setRequestQueue(PersonRequest personRequest) { //输入来临时往请求队列内加入请求信息
        requestQueue.get(personRequest.getFromFloor()).add(personRequest);
        this.notifyAll();
    }

    public synchronized void setRequestQueue(PersonRequest personRequest, int floor) {
        requestQueue.get(floor).add(personRequest);
        this.notifyAll();
    }

    public synchronized ArrayList<PersonRequest> getFloorRequestQueue(int floor) {
        //调度器根据当前楼层获取请求信息 在调度器内对请求队列进行删除
        if (!requestQueue.get(floor).isEmpty()) {
            return requestQueue.get(floor);
        } else {
            return null;
        }
    }

    public int getSize() {
        int size = 0;
        for (int i = 1; i < 12; i++) {
            ArrayList<PersonRequest> personRequests = this.requestQueue.get(i);
            size += personRequests.size();
        }
        return size;
    }

    public synchronized boolean isEmpty() {
        for (HashMap.Entry<Integer, ArrayList<PersonRequest>> entry :
                this.requestQueue.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public synchronized boolean isOver() {
        return endFlag == 1;
    }
}

