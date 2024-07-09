import java.util.PriorityQueue;

public class test {
    public static void main(String[] args) {
        PriorityQueue<Node> heap = new PriorityQueue<>();
        Node node1 = new Node(1, 100);
        Node node2 = new Node(2, 200);
        heap.add(node1);
        heap.add(node2);
        node1.setMinValue(300);
        Node newnode = heap.poll();
        heap.add(newnode);
        while(!heap.isEmpty()) {
            Node node = heap.poll();
            System.out.println(node.getId() + " value : " + node.getMinValue());
        }
    }
}
