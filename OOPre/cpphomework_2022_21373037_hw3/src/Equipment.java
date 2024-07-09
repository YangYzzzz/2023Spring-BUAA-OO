package src;

import java.math.BigInteger;

public abstract class Equipment extends Commodity {
    private int id;
    private String name;
    private BigInteger price;

    public Equipment(int id, String name, BigInteger price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Equipment() {
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

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    public abstract void printf();

}

