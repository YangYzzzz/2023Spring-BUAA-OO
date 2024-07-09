import java.util.HashMap;

public class FloorMonitor {
    private static final int M_X = 4; //一楼层的服务中电梯

    private static final int N_X = 2; //一楼层的只接人电梯

    private final HashMap<Integer, Integer> servicingElevators; //楼层 当前电梯数

    private final HashMap<Integer, Integer> onlyInElevators;

    public FloorMonitor() {
        servicingElevators = new HashMap<>();
        onlyInElevators = new HashMap<>();
        for (int i = 1; i < 12; i++) {
            servicingElevators.put(i, 0);
            onlyInElevators.put(i, 0);
        }
    }

    public synchronized boolean isServicingMax(int floor) {
        return servicingElevators.get(floor) == M_X;
    }

    public synchronized boolean isOnlyInMax(int floor) {
        return onlyInElevators.get(floor) == N_X;
    }

    public synchronized void servicingAdd(int floor) {
        servicingElevators.put(floor, servicingElevators.get(floor) + 1);
        //System.out.println("floor: " + floor + ", servicing : " + servicingElevators.get(floor));
    }

    public synchronized void onlyInAdd(int floor) {
        onlyInElevators.put(floor, onlyInElevators.get(floor) + 1);
        //System.out.println("floor: " + floor + ", only in : " + onlyInElevators.get(floor));
    }

    public synchronized void servicingRemove(int floor) {  //在移除时唤醒其他正在等待开门的线程
        servicingElevators.put(floor, servicingElevators.get(floor) - 1);
        //System.out.println("floor: " + floor + ", servicing : " + servicingElevators.get(floor));
        notifyAll();
    }

    public synchronized void onlyInRemove(int floor) {
        onlyInElevators.put(floor, onlyInElevators.get(floor) - 1);
        //System.out.println("floor: " + floor + ", only in : " + onlyInElevators.get(floor));
        notifyAll();
    }
}
