package src;

public class Bottle extends Equipment {

    private double capacity;
    private boolean filled;

    public Bottle(int id, String name, long price, double capacity, boolean filled) {
        super(id, name, price);
        this.capacity = capacity;
        this.filled = filled;
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

    @Override
    public void usedBy(Adventurer adventurers) throws Exception {
        if (!isFilled()) {
            throw new Exception("Failed to use " + getName() + " because it is empty.");
        }
        adventurers.setHealth(adventurers.getHealth() + capacity / 10);
        setPrice(getPrice() / 10);
        setFilled(false);
        System.out.println(adventurers.getName() + " drank " + getName() +
                " and recovered " + capacity / 10 + ".");
    }

    @Override
    public void printf() {
        System.out.println("The bottle's id is " + getId() + ", name is " + getName() +
                ", capacity is " + getCapacity() + ", filled is " + isFilled() +
                ".");
    }
}
