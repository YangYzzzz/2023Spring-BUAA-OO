package src;

import java.math.BigInteger;

public class Bottle extends Equipment {

    private double capacity;
    private boolean filled;

    public Bottle(int id, String name, BigInteger price, double capacity, boolean filled) {
        super(id, name, price);
        this.capacity = capacity;
        this.filled = filled;
    }

    public Bottle() {

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
    public void usedBy(Adventurer adventurer) {
        if (!isFilled()) {
            System.out.println("Failed to use " + getName() + " because it is empty.");
        } else {
            adventurer.setHealth(adventurer.getHealth() + capacity / 10);
            setPrice(getPrice().divide(BigInteger.valueOf(10)));
            setFilled(false);
            System.out.println(adventurer.getName() + " drank " + getName() +
                    " and recovered " + capacity / 10 + ".");
        }
    }

    @Override
    public void printf() {
        System.out.println("The bottle's id is " + getId() + ", name is " + getName() +
                ", capacity is " + getCapacity() + ", filled is " + isFilled() +
                ".");
    }
}
