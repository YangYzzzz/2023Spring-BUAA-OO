package src;

import java.math.BigInteger;

public class ExpBottle extends Bottle {
    private double expRatio;

    public ExpBottle(int id, String name, BigInteger price, double capacity,
                     boolean filled, double expRatio) {
        super(id, name, price, capacity, filled);
        this.expRatio = expRatio;
    }

    public ExpBottle() {

    }

    public double getExpRatio() {
        return expRatio;
    }

    public void setExpRatio(double expRatio) {
        this.expRatio = expRatio;
    }

    public void usedBy(Adventurer adventurer) {
        if (!isFilled()) {
            System.out.println("Failed to use " + getName() + " because it is empty.");
        } else {
            adventurer.setHealth(adventurer.getHealth() + getCapacity() / 10);
            setPrice(getPrice().divide(BigInteger.valueOf(10)));
            setFilled(false);
            adventurer.setExp(adventurer.getExp() * expRatio);
            System.out.println(adventurer.getName() + " drank " + getName() +
                    " and recovered " + getCapacity() / 10 + ".\n" + adventurer.getName() +
                    "'s exp became " + adventurer.getExp() + '.');
        }
    }

    public void printf() {
        System.out.println("The expBottle's id is " + getId() + ", name is " + getName() +
                ", capacity is " + getCapacity() + ", filled is " + isFilled() +
                ", expRatio is " + getExpRatio() + ".");
    }
}
