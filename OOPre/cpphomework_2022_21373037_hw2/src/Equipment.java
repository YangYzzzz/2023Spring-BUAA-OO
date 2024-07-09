package src;

public abstract class Equipment {
    private int id;
    private String name;
    private long price;
    private int flag = 0;

    public Equipment(int id, String name, long price) {
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public abstract void usedBy(Adventurer adventurers) throws Exception;

    public abstract void printf();
}

