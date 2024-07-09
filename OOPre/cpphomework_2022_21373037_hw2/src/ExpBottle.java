package src;

public class ExpBottle extends Bottle {
    private double expRatio;

    public ExpBottle(int id, String name, long price, double capacity,
                     boolean filled, double expRatio) {
        super(id, name, price, capacity, filled);
        this.expRatio = expRatio;
    }

    public double getExpRatio() {
        return expRatio;
    }

    public void setExpRatio(double expRatio) {
        this.expRatio = expRatio;
    }

    public void usedBy(Adventurer adventurers) throws Exception {
        if (!isFilled()) {
            throw new Exception("Failed to use " + getName() + " because it is empty.");
        }
        adventurers.setHealth(adventurers.getHealth() + getCapacity() / 10);
        setPrice(getPrice() / 10);
        setFilled(false);
        adventurers.setExp(adventurers.getExp() * expRatio);
        System.out.println(adventurers.getName() + " drank " + getName() +
                " and recovered " + getCapacity() / 10 + ".\n" + adventurers.getName() +
                "'s exp became " + adventurers.getExp() + '.');
    }

    public void printf() {
        System.out.println("The expBottle's id is " + getId() + ", name is " + getName() +
                ", capacity is " + getCapacity() + ", filled is " + isFilled() +
                ", expRatio is " + getExpRatio() + ".");
    }
}
