import com.oocourse.elevator1.PersonRequest;
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
        /*
        PersonRequest test1 = new PersonRequest(1, 7, 1);
        PersonRequest test2 = new PersonRequest(1, 8, 2);
        PersonRequest test3 = new PersonRequest(1, 7, 3);
        PersonRequest test4 = new PersonRequest(1, 8, 4);
        PersonRequest test5 = new PersonRequest(1, 7, 5);
        PersonRequest test6 = new PersonRequest(1, 8, 6);
        PersonRequest test7 = new PersonRequest(1, 7, 7);
        PersonRequest test8 = new PersonRequest(1, 8, 8);
        requestQueue.get(1).add(test1);
        requestQueue.get(1).add(test2);
        requestQueue.get(1).add(test3);
        requestQueue.get(1).add(test4);
        requestQueue.get(1).add(test5);
        requestQueue.get(1).add(test6);
        requestQueue.get(1).add(test7);
        requestQueue.get(1).add(test8);
        */
        //PersonRequest test1 = new PersonRequest(1, 7, 1);
        //PersonRequest test2 = new PersonRequest(1, 8, 2);
        //requestQueue.get(1).add(test1);
        //requestQueue.get(1).add(test2);
        endFlag = 0;
    }

    public void setEndFlag() {
        this.endFlag = 1;
    }

    public synchronized void setRequestQueue(PersonRequest personRequest) { //输入来临时往请求队列内加入请求信息
        requestQueue.get(personRequest.getFromFloor()).add(personRequest);
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
        if (endFlag == 1) {
            this.notifyAll();
        }
        return endFlag == 1;
    }
}
