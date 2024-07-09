public class Node {
    private final int id;
    private int minValue;
    private int secMinValue;
    private int origin1;//最短路出点
    private int origin2;//次短路出点

    public Node(int to) {
        this.id = to;
        this.minValue = Integer.MAX_VALUE;
        this.secMinValue = Integer.MAX_VALUE;
        this.origin1 = -1;
        this.origin2 = -1;
    }

    public Node(int to, int minValue) {
        this.id = to;
        this.minValue = minValue;
        this.secMinValue = Integer.MAX_VALUE;
        this.origin1 = -1;
        this.origin2 = -1;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public void setSecMinValue(int secMinValue) {
        this.secMinValue = secMinValue;
    }

    public int getId() {
        return id;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getSecMinValue() {
        return secMinValue;
    }

    public int getOrigin1() {
        return origin1;
    }

    public void setOrigin1(int origin1) {
        this.origin1 = origin1;
    }

    public int getOrigin2() {
        return origin2;
    }

    public void setOrigin2(int origin2) {
        this.origin2 = origin2;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Node && this.id == ((Node) o).getId();
    }
}
