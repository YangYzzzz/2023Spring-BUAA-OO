package domain;

import java.util.ArrayList;

public class Adventurer {
    private int id;
    private String name;
    private ArrayList<Bottle> bottles;

    public Adventurer(int id, String name, ArrayList<Bottle> bottles) {
        this.id = id;
        this.name = name;
        this.bottles = bottles;
    }

    public Adventurer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Adventurer() {
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

    public ArrayList<Bottle> getBottles() {
        return bottles;
    }

    public void setBottles(ArrayList<Bottle> bottles) {
        this.bottles = bottles;
    }
}
