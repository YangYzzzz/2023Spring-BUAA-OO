import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MyselfPersonRequest {
    private ArrayList<Integer> moveSequence;
    private int personId;
    private final Scheduler scheduler;
    private final int startFloor;
    private final int endFloor;

    static boolean bfs(int[][] ad, int start, int end, int[] path) { //怎么得到顺序
        int[] flag = new int[12];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        flag[start] = 1;
        while (!queue.isEmpty()) {
            //取出队首元素top
            int s = queue.remove();
            if (s == end) {
                return true;
            }
            //访问队首元素top
            //将队首元素出队
            for (int i = 1; i < 12; i++) {
                //System.out.println(ad[s][i]);
                if (ad[s][i] == 1 && flag[i] == 0) {
                    queue.add(i);
                    flag[i] = 1;
                    path[i] = s;
                }
            }
            //将top的下一层结点中未曾入队的结点全部入队，并设置为已入队
        }
        return false;
    }

    public void setSequence(int start, int end) {  //每当电梯增加，维修时，将所有请求的序列全部重置
        int[][] ad = new int[12][12]; //构建邻接矩阵
        int[] path = new int[12];
        synchronized (scheduler.getElevators()) {
            for (Elevator elevator : scheduler.getElevators()) {
                if (elevator.getMaxNum() != elevator.getCurrentNum()) {
                    //若电梯人员已满 邻接矩阵将不考虑他 so上车下车都要统一修改全部
                    for (int i = 0; i < elevator.getAccess().size(); i++) {
                        for (int j = i + 1; j < elevator.getAccess().size(); j++) {
                            //System.out.println(it.get(i) + " " + it.get(j));
                            ad[elevator.getAccess().get(i)][elevator.getAccess().get(j)] = 1;
                            ad[elevator.getAccess().get(j)][elevator.getAccess().get(i)] = 1;
                        }
                    }
                }
            }
        }
        ArrayList<Integer> tmp = new ArrayList<>();
        if (bfs(ad, start, end, path)) {
            int now = end;
            while (now != start) {
                tmp.add(now);
                //System.out.println("now " + now);
                now = path[now];
            }
            tmp.add(now);
            ArrayList<Integer> result = new ArrayList<>();
            for (int i = tmp.size() - 1; i >= 0; i--) {
                result.add(tmp.get(i));
            }
            this.moveSequence = result;
        } else {
            this.moveSequence = new ArrayList<>();
            moveSequence.add(start);
            moveSequence.add(end);
        }
    }

    public MyselfPersonRequest(PersonRequest personRequest, Scheduler scheduler) {
        this.scheduler = scheduler;
        this.endFloor = personRequest.getToFloor();
        this.startFloor = personRequest.getFromFloor();
        this.personId = personRequest.getPersonId();
        setSequence(startFloor, endFloor);
    }

    public int fromFloor() {
        return moveSequence.get(0);
    }

    public int getEndFloor() {
        return endFloor;
    }

    public int toFloor() {
        return moveSequence.get(1);
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }
}
