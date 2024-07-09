package src;

import java.math.BigInteger;

public class HealingPotion extends Bottle {
    private double efficiency;

    public HealingPotion(int id, String name, BigInteger price, double capacity,
                         boolean filled, double efficiency) {
        super(id, name, price, capacity, filled);
        this.efficiency = efficiency;
    }

    public HealingPotion() {

    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    public void usedBy(Adventurer adventurer) {
        if (!isFilled()) {
            System.out.println("Failed to use " + getName() + " because it is empty.");
        } else {
            double addHealth = getCapacity() / 10;
            adventurer.setHealth(adventurer.getHealth() + addHealth + getCapacity() * efficiency);
            setPrice(getPrice().divide(BigInteger.valueOf(10)));
            setFilled(false);
            System.out.println(adventurer.getName() + " drank " + getName() +
                    " and recovered " + addHealth + ".\n" + adventurer.getName() +
                    " recovered extra " + getCapacity() * efficiency + ".");
        }
    }

    public void printf() {
        System.out.println("The healingPotion's id is " + getId() + ", name is " + getName() +
                ", capacity is " + getCapacity() + ", filled is " + isFilled() +
                ", efficiency is " + getEfficiency() + ".");
    }
}
