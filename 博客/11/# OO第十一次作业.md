# OO第十一次作业

## 新增指令

|指令|输入|简写|含义|
|--|--|--|--|
|add_red_envelope_message| id(int) money(int) type(int) person_id1(int) person_id2(int)\|group_id(int)|arem|添加红包信息|
|add_notice_message| id(int) string(String) type(int)person_id1(int) person_id2(int)\|group_id(int)|anm|添加提示信息|
|clear_notices| id(int)|cn|清除某人提示信息|
|add_emoji_message| id(int) emoji_id(int) type(int)person_id1(int) person_id2(int)\|group_id(int)|aem|添加表情信息|
|store_emoji_id| id(int)|sei|添加表情|
|query_popularity| id(int)|qp|查询表情使用次数|
|delete_cold_emoji| limit(int)|dce|删除使用次数较少表情(及其相应信息)|
|query_money| id(int)|qm|查询某人金钱|
|delete_cold_emoji_ok_test||dceok|oktest|
|query_least_moment| id(int)|qlm|查找路径最短环|

本次作业主要扩展了消息的种类，在普通消息的基础上多出了红包消息，提示消息和表情消息，并且需要完成一个变种的单源最短路径问题的求解，同时要尽可能降低复杂度。

作为本单元最后一次作业，JML的结构更为复杂繁琐，代码量进一步加大，极大的锻炼我们的阅读JML和根据JML写出规格完备代码的能力。然而不得不说，经过前两次的洗礼，大部分代码无非就是对容器加加减减，对对象属性修修改改，沉下心来仔细阅读JML后上述想必不是难事。

本次作业实则是披着JML规格化外套的算法题，下面将针对qlm展开讲解。

## 最小环问题

qlm指令含义为找到一个经历指定结点的权值之和最小的环，环意味着至少要经过三个结点。可以发现这是一个在无向有权图上求解最短路径的问题，我们第一时间想到两个老生常谈的算法，迪杰斯特拉和佛洛依德算法，由于佛洛依德算法是多源最短路径求解，复杂度较高，无用信息过多，本次笔者采取**迪杰斯特拉算法**的改造版本。

我们知道迪算法计算的是单源最短路径，即某一固定点到其余各点的最短路径，对于本问题(从自己到自己)是无法直接解决的，算出来的结果会是0，因此我们要适当改进。
删边法：**针对指定节点的每一邻接结点，删去与指定节点连接的边，并使用迪算法计算指定节点到该邻接结点的最短路径，再加上删去边的权值，得到经过该邻接结点的最小环，如此遍历指定结点的所有邻接结点，并取历次最小值，即得所求**，如图所示，这种方法的依据是，最小环一定要经过指定结点和其中某一邻接结点，迪算法可以保证环的另一半是所有情况中最短的那条，整体类似于动态规划的思想(**用贪心算法替代了下层的动态规划**)，即$minValue = min(dijkstra(target, friend[i]) + edge(target, friend[i]))$。

算法如下：

    private int dijkstra(int id, int target) {
        // 记录出发点到各个节点之间的距离
        HashMap<Integer, Integer> dis = new HashMap<>();
        // 记录某个节点是否被访问过
        HashMap<Integer, Boolean> vis = new HashMap<>();
        // 创建优先队列 重写比较方法 不需要每次再遍历找到下一步选择的节点
        PriorityQueue<Node> heap = new PriorityQueue<>();
        //初始化
        for (Person person : people) {
            dis.put(person.getId(), Integer.MAX_VALUE);
            vis.put(person.getId(), false);
        }
        dis.put(id, 0);
        heap.add(new Node(id, 0));
        // 算法核心流程
        while (!heap.isEmpty()) {
            Node cur = heap.poll(); //从堆中取出value最小的 即取出下一个最短路径节点
            int curId = cur.getId();
            if (curId == target) {
                return dis.get(target);
            }
            vis.put(curId, true); //设置为已经遍历过
            MyPerson person = (MyPerson) getPerson(curId);
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
        MyPerson person = (MyPerson) getPerson(id);
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

迪算法时间复杂度为n方，加之对指定结点的邻接结点遍历(设其度为m)，复杂度为n\*n\*m，需要使用优先队列进一步优化。heap为一个优先队列，存储的是Node，可以从代码中看出其实就是dis容器的内容(每个结点id以及当前指定结点到该结点的距离)，在类中实现了Comparable接口，用于调整堆，**使其每次取出的结点是value值最小的结点**，在迪常规算法中，我们要遍历所有未访问过的结点选取最小值，复杂度为n，而优先队列复杂度为logn(应该不对，有待商榷，算法学的不是很好)，整体时间复杂度降低为n\*logn\*m。

    public class Node implements Comparable<Node> {
        private final int id;
        private final int value;

        public Node(int to, int value) {
            this.id = to;
            this.value = value;
        }

        @Override
        public int compareTo(Node o) { //升序排列
            return this.value - o.value;
        }

        public int getId() {
            return id;
        }

        public int getValue() {
            return value;
        }
    }
## 对容器的遍历删除

本次有指令涉及到对容器内多个元素的删除，直接遍历容器元素一定是不可取的，在第一单元我们也遇到过，**一定要使用迭代器进行遍历与删除**。