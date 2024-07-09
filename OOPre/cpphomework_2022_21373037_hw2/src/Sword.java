package src;

public class Sword extends Equipment {
    private double sharpness;

    public Sword(int id, String name, long price, double sharpness) {
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
    public void usedBy(Adventurer adventurers) {
        adventurers.setHealth(adventurers.getHealth() - 10.0);
        adventurers.setExp(adventurers.getExp() + 10.0);
        adventurers.setMoney(adventurers.getMoney() + this.sharpness);
        System.out.println(adventurers.getName() + " used " + this.getName() +
                " and earned " + sharpness + ".");
    }

    @Override
    public void printf() {
        System.out.println("The sword's id is " + getId() + ", name is " + getName() +
                ", sharpness is " + getSharpness() + ".");
    }
}
