import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Graph {
    private Network network;

    public Graph(Network network) {
        this.network = network;
    }

    public void update(Node nodeU, Node nodeV, int weight, int dist, PriorityQueue<Pair> heap) {
        if (dist + weight < nodeV.getMinValue() && dist != Integer.MAX_VALUE) {
            //当前v的最短路可被更新
            if (nodeU.getOrigin1() != nodeV.getOrigin1()) {
                nodeV.setOrigin2(nodeV.getOrigin1());
                nodeV.setSecMinValue(nodeV.getMinValue());
            }
            nodeV.setOrigin1(nodeU.getOrigin1());
            nodeV.setMinValue(dist + weight);
            heap.add(new Pair(nodeV.getId(), dist + weight));
        } else if (dist + weight < nodeV.getSecMinValue() && dist != Integer.MAX_VALUE) {
            //当前v的次短路可被更新
            if (nodeU.getOrigin1() != nodeV.getOrigin1()) {
                nodeV.setOrigin2(nodeU.getOrigin1());
                nodeV.setSecMinValue(dist + weight);
            }
        }
    }

    public HashMap<Integer, Node> dijkstra(int id) {
        // 记录出发点到各个节点之间的距离
        HashMap<Integer, Node> dis = new HashMap<>();
        // 记录某个节点是否被访问过
        HashMap<Integer, Boolean> vis = new HashMap<>();
        // 创建优先队列 重写比较方法 不需要每次再遍历找到下一步选择的节点
        PriorityQueue<Pair> heap = new PriorityQueue<>();
        //初始化
        for (Person person : ((MyNetwork) network).getPeople()) { //O(n)所有人
            Node node = new Node(person.getId());
            dis.put(person.getId(), node);
            vis.put(person.getId(), false);
        }
        dis.get(id).setMinValue(0);
        for (Map.Entry<Person, Integer> entry : ((MyPerson) network.getPerson(id))
                .getAcquaintance().entrySet()) {
            dis.get(entry.getKey().getId()).setOrigin1(entry.getKey().getId());
            dis.get(entry.getKey().getId()).setMinValue(entry.getValue());
            heap.add(new Pair(entry.getKey().getId(), entry.getValue()));
        }
        // 算法核心流程
        vis.put(id, true);
        while (!heap.isEmpty()) {
            Pair cur = heap.poll(); //从堆中取出value最小的 即取出下一个节点
            if (vis.get(cur.getId())) {
                continue;
            }
            int curId = cur.getId();
            vis.put(curId, true); //设置为已经遍历过
            MyPerson person = (MyPerson) network.getPerson(curId);
            for (Map.Entry<Person, Integer> entry : person.getAcquaintance().entrySet()) {
                int weight = entry.getValue();
                int friendId = entry.getKey().getId();
                Node nodeU = dis.get(curId);
                Node nodeV = dis.get(friendId);
                if (!vis.get(friendId)) { //未经访问过
                    update(nodeU, nodeV, weight, nodeU.getMinValue(), heap);
                    update(nodeU, nodeV, weight, nodeU.getSecMinValue(), heap);
                    //System.out.println(friendId + " : " + nodeV.getMinValue());
                    //System.out.println(friendId + " : " + nodeV.getSecMinValue());
                }
            }
        }
        return dis;
    }

    public int getMinPathValue(int id) {
        HashMap<Integer, Node> hashMap = dijkstra(id);
        int minValue = Integer.MAX_VALUE;
        for (Map.Entry<Integer, Node> entry : hashMap.entrySet()) {
            if (entry.getValue().getMinValue() + entry.getValue().getSecMinValue() < minValue &&
                    entry.getValue().getMinValue() != Integer.MAX_VALUE &&
                    entry.getValue().getSecMinValue() != Integer.MAX_VALUE) {
                minValue = entry.getValue().getMinValue() + entry.getValue().getSecMinValue();
            }
        }
        return minValue;
    }

    public int queryLeastMoments(int id) throws PersonIdNotFoundException, PathNotFoundException {
        if (network.contains(id)) {
            int value = getMinPathValue(id);
            if (value != Integer.MAX_VALUE) {
                return value;
            } else {
                throw new MyPathNotFoundException(id);
            }
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    public boolean bfs(int start, int end) {  //start end用PersonId即可
        HashMap<Integer, Boolean> flag = new HashMap<>();
        for (Person person : ((MyNetwork) network).getPeople()) {
            flag.put(person.getId(), false);
        }
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        flag.put(start, true);
        while (!queue.isEmpty()) {
            //取出队首元素top
            int s = queue.remove();
            if (s == end) {
                return true;
            }
            for (Person person : ((MyPerson) network.getPerson(s)).getAcquaintance().keySet()) {
                if (!flag.get(person.getId())) {
                    queue.add(person.getId());
                    flag.put(person.getId(), true);
                }
            }
        }
        return false;
    }
}
