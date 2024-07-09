import java.util.ArrayList;

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