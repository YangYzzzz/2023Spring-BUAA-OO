package src;

import java.math.BigInteger;

public class EpicSword extends Sword {
    private double evolveRatio;

    public EpicSword(int id, String name, BigInteger price, double sharpness, double evolveRatio) {
        super(id, name, price, sharpness);
        this.evolveRatio = evolveRatio;
    }

    public EpicSword() {

    }

    public double getEvolveRatio() {
        return evolveRatio;
    }

    public void setEvolveRatio(double evolveRatio) {
        this.evolveRatio = evolveRatio;
    }

    public void usedBy(Adventurer adventurer) {
        final double addMoney = getSharpness();
        adventurer.setHealth(adventurer.getHealth() - 10.0);
        adventurer.setExp(adventurer.getExp() + 10.0);
        adventurer.setMoney(adventurer.getMoney() + getSharpness());
        setSharpness(getSharpness() * evolveRatio);
        System.out.println(adventurer.getName() + " used " + getName() +
                " and earned " + addMoney + ".\n" + getName() + "'s sharpness became "
                + getSharpness() + ".");
    }

    public void printf() {
        System.out.println("The epicSword's id is " + getId() + ", name is " + getName() +
                ", sharpness is " + getSharpness() + ", evolveRatio is " + getEvolveRatio() + ".");
    }
}
