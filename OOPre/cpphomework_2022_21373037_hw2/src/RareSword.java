package src;

public class RareSword extends Sword {
    private double extraExpBonus;

    public RareSword(int id, String name, long price, double sharpness, double extraExpBonus) {
        super(id, name, price, sharpness);
        this.extraExpBonus = extraExpBonus;
    }

    public double getExtraExpBonus() {
        return extraExpBonus;
    }

    public void setExtraExpBonus(double extraExpBonus) {
        this.extraExpBonus = extraExpBonus;
    }

    public void usedBy(Adventurer adventurers) {
        adventurers.setHealth(adventurers.getHealth() - 10.0);
        adventurers.setExp(adventurers.getExp() + 10.0 + extraExpBonus);
        adventurers.setMoney(adventurers.getMoney() + getSharpness());
        System.out.println(adventurers.getName() + " used " + getName() + " and earned " +
                getSharpness() + ".\n" + adventurers.getName() + " got extra exp " +
                extraExpBonus + ".");
    }

    public void printf() {
        System.out.println("The rareSword's id is " + getId() + ", name is " +
                getName() + ", sharpness is " + getSharpness() + ", extraExpBonus is "
                + getExtraExpBonus() + ".");
    }
}
