package src;

import java.math.BigInteger;

public class RareSword extends Sword {
    private double extraExpBonus;

    public RareSword(int id, String name, BigInteger price, double sharpness,
                     double extraExpBonus) {
        super(id, name, price, sharpness);
        this.extraExpBonus = extraExpBonus;
    }

    public RareSword() {

    }

    public double getExtraExpBonus() {
        return extraExpBonus;
    }

    public void setExtraExpBonus(double extraExpBonus) {
        this.extraExpBonus = extraExpBonus;
    }

    public void usedBy(Adventurer adventurer) {
        adventurer.setHealth(adventurer.getHealth() - 10.0);
        adventurer.setExp(adventurer.getExp() + 10.0 + extraExpBonus);
        adventurer.setMoney(adventurer.getMoney() + getSharpness());
        System.out.println(adventurer.getName() + " used " + getName() + " and earned " +
                getSharpness() + ".\n" + adventurer.getName() + " got extra exp " +
                extraExpBonus + ".");
    }

    public void printf() {
        System.out.println("The rareSword's id is " + getId() + ", name is " +
                getName() + ", sharpness is " + getSharpness() + ", extraExpBonus is "
                + getExtraExpBonus() + ".");
    }
}
