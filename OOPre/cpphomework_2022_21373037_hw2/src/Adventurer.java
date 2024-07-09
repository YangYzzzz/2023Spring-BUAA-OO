package src;

import java.util.HashMap;

public class Adventurer {
    private int id;
    private String name;
    private HashMap<Integer, Equipment> equipments;
    private double health;
    private double exp;
    private double money;

    public Adventurer(int id, String name, HashMap<Integer, Equipment>
            equipments, double health, double exp, double money) {
        this.id = id;
        this.name = name;
        this.equipments = equipments;
        this.health = health;
        this.exp = exp;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Integer, Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(HashMap<Integer, Equipment> equipments) {
        this.equipments = equipments;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void use(Equipment equipment) {
        try {
            equipment.usedBy(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
