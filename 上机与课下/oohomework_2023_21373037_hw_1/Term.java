import java.util.ArrayList;
import java.util.Iterator;

public class Term {
    private ArrayList<Factor> factors;
    private final int pos; //项的正负

    public ArrayList<Factor> getFactors() {
        return factors;
    }

    public void setFactors(ArrayList<Factor> factors) {
        this.factors = factors;
    }

    public int getPos() {
        return pos;
    }

    public Term(int pos) {
        this.factors = new ArrayList<>();
        this.pos = pos;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public String toString() {
        Iterator<Factor> iter = factors.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString());
        while (iter.hasNext()) {
            sb.append(" ");
            sb.append(iter.next().toString());
            sb.append(" *");
        }
        return sb.toString();
    }

    public Poly toPoly() {
        Poly poly = new Poly();
        for (Factor it : factors) {
            Poly temp = poly.mulPoly(it.toPoly());
            poly = temp;
        }
        if (this.pos == 1) {
            poly.inverse();
        }
        return poly;
    }
}
