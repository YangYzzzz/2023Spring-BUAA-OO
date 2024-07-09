package src;

public class HealingPotion extends Bottle {
    private double efficiency;

    public HealingPotion(int id, String name, long price, double capacity,
                         boolean filled, double efficiency) {
        super(id, name, price, capacity, filled);
        this.efficiency = efficiency;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    public void usedBy(Adventurer adventurers) throws Exception {
        if (!isFilled()) {
            throw new Exception("Failed to use " + getName() + " because it is empty.");
        }
        double addHealth = getCapacity() / 10;
        adventurers.setHealth(adventurers.getHealth() + addHealth + getCapacity() * efficiency);
        setPrice(getPrice() / 10);
        setFilled(false);
        System.out.println(adventurers.getName() + " drank " + getName() +
                " and recovered " + addHealth + ".\n" + adventurers.getName() +
                " recovered extra " + getCapacity() * efficiency + ".");
    }

    public void printf() {
        System.out.println("The healingPotion's id is " + getId() + ", name is " + getName() +
                ", capacity is " + getCapacity() + ", filled is " + isFilled() +
                ", efficiency is " + getEfficiency() + ".");
    }
}
