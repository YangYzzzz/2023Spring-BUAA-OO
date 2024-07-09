package src;

import java.math.BigInteger;

public abstract class Commodity implements Comparable<Commodity> {
    abstract void printf();

    abstract int getId();

    public Commodity() {
    }

    abstract void usedBy(Adventurer adventurer);

    abstract BigInteger getPrice();

    //int compareTo(Commodity other);
    @Override
    public int compareTo(Commodity other) {
        if (this.getPrice().compareTo(other.getPrice()) == -1) {
            return 1;
        } else if (this.getPrice().compareTo(other.getPrice()) == 1) {
            return -1;
        } else if (this.getPrice().compareTo(other.getPrice()) == 0 &&
                this.getId() > other.getId()) {
            return -1;
        } else {
            return 1;
        }
    }
}
