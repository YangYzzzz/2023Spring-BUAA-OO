import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Dij {
    /*
    private int dijkstra(int id, int target) {
        // 记录出发点到各个节点之间的距离
        HashMap<Integer, Integer> dis = new HashMap<>();
        // 记录某个节点是否被访问过
        HashMap<Integer, Boolean> vis = new HashMap<>();
        // 创建优先队列 重写比较方法 不需要每次再遍历找到下一步选择的节点
        PriorityQueue<Node> heap = new PriorityQueue<>();
        //初始化
        for (Person person : ((MyNetwork) network).getPeople()) { //O(n)所有人
            dis.put(person.getId(), Integer.MAX_VALUE);
            vis.put(person.getId(), false);
        }
        dis.put(id, 0);
        heap.add(new Node(id, 0));
        // 算法核心流程
        while (!heap.isEmpty()) {
            Node cur = heap.poll(); //从堆中取出value最小的 即取出下一个节点
            int curId = cur.getId();

            if (curId == target) {
                return dis.get(target);
            }
            vis.put(curId, true); //设置为已经遍历过
            MyPerson person = (MyPerson) network.getPerson(curId);
            for (Map.Entry<Person, Integer> entry : person.getAcquaintance().entrySet()) {
                int weight = entry.getValue();
                int friendId = entry.getKey().getId();
                if (!vis.get(friendId) && dis.get(curId) + weight < dis.get(friendId) &&
                        !(curId == id && friendId == target)) {
                    dis.put(friendId, dis.get(curId) + weight); //更新此时最短路径
                    heap.add(new Node(friendId, dis.get(friendId))); //加入堆中 用于比较
                }
            }
        }
        return 0;
    }

    public int getMinPathValue(int id) {
        MyPerson person = (MyPerson) network.getPerson(id);
        int minValue = Integer.MAX_VALUE;
        int friendId;
        int friendValue;
        int tmp;
        for (Map.Entry<Person, Integer> entry : person.getAcquaintance().entrySet()) {
            friendId = entry.getKey().getId();
            friendValue = entry.getValue();
            tmp = dijkstra(id, friendId);
            if (tmp != 0 && (tmp + friendValue) < minValue) {
                minValue = tmp + friendValue;
            }
        }
        return minValue;
    }
    */
}
