package src;

public class EpicSword extends Sword {
    private double evolveRatio;

    public EpicSword(int id, String name, long price, double sharpness, double evolveRatio) {
        super(id, name, price, sharpness);
        this.evolveRatio = evolveRatio;
    }

    public double getEvolveRatio() {
        return evolveRatio;
    }

    public void setEvolveRatio(double evolveRatio) {
        this.evolveRatio = evolveRatio;
    }

    public void usedBy(Adventurer adventurers) {
        final double addMoney = getSharpness();
        adventurers.setHealth(adventurers.getHealth() - 10.0);
        adventurers.setExp(adventurers.getExp() + 10.0);
        adventurers.setMoney(adventurers.getMoney() + getSharpness());
        setSharpness(getSharpness() * evolveRatio);
        System.out.println(adventurers.getName() + " used " + getName() +
                " and earned " + addMoney + ".\n" + getName() + "'s sharpness became "
                + getSharpness() + ".");
    }

    public void printf() {
        System.out.println("The epicSword's id is " + getId() + ", name is " + getName() +
                ", sharpness is " + getSharpness() + ", evolveRatio is " + getEvolveRatio() + ".");
    }
}
