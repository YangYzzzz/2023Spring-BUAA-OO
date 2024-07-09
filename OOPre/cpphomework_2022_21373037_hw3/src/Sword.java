package src;

import java.math.BigInteger;

public class Sword extends Equipment {
    private double sharpness;

    public Sword(int id, String name, BigInteger price, double sharpness) {
        super(id, name, price);
        this.sharpness = sharpness;
    }

    public Sword() {
        super();
    }

    public double getSharpness() {
        return sharpness;
    }

    public void setSharpness(double sharpness) {
        this.sharpness = sharpness;
    }

    @Override
    public void usedBy(Adventurer adventurer) {
        adventurer.setHealth(adventurer.getHealth() - 10.0);
        adventurer.setExp(adventurer.getExp() + 10.0);
        adventurer.setMoney(adventurer.getMoney() + this.sharpness);
        System.out.println(adventurer.getName() + " used " + this.getName() +
                " and earned " + sharpness + ".");
    }

    @Override
    public void printf() {
        System.out.println("The sword's id is " + getId() + ", name is " + getName() +
                ", sharpness is " + getSharpness() + ".");
    }
}
