
public class Pair implements Comparable<Pair> {
    private int id;
    private int value;

    public Pair(int id, int value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public int compareTo(Pair o) { //升序排列
        //System.out.println(this.id + " " + o.id);
        return this.value - o.value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
