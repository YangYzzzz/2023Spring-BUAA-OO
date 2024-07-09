package domain;

public class Bottle {
    private int id;
    private String name;
    private long price;
    private double capacity;
    private boolean filled;

    public Bottle(int id, String name, long price, double capacity, boolean filled) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.capacity = capacity;
        this.filled = filled;
    }

    public Bottle() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }
}
