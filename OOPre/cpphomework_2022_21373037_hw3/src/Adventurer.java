package src;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class Adventurer extends Commodity { //实现两个接口
    private int id;
    private String name;
    private ArrayList<Commodity> commodities;
    private double health;
    private double exp;
    private double money;

    public Adventurer(int id, String name, ArrayList<Commodity>
            commodities, double health, double exp, double money) {
        this.id = id;
        this.name = name;
        this.commodities = commodities;
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

    public ArrayList<Commodity> getCommodities() {
        return commodities;
    }

    public void setCommodities(ArrayList<Commodity> commodities) {
        this.commodities = commodities;
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

    public void use(Adventurer adventurer) {
        //try{
        Collections.sort(getCommodities());
        //System.out.println(commodities.size());
        for (int i = 0; i < commodities.size(); i++) {
            //System.out.println(commodities.get(3).getId());
            commodities.get(i).usedBy(adventurer);
        }
        //} catch (Exception e) {
        //    System.out.println(e.getMessage());
        //}
    }

    public void printf() {
        System.out.println("The adventurer's id is " + getId() + ", name is " + getName() +
                ", health is " + getHealth() + ", exp is " + getExp() + ", money is " +
                getMoney() + ".");
    }

    @Override
    public void usedBy(Adventurer adventurer) {   //价值体是冒险
        this.use(adventurer);
    }

    public Adventurer() {
    }

    @Override
    public BigInteger getPrice() {
        BigInteger totalPrice = new BigInteger("0");
        for (Commodity item : this.getCommodities()) {
            totalPrice = totalPrice.add(item.getPrice());
        }
        return totalPrice;
    }

}
