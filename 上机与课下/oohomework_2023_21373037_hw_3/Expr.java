import java.util.ArrayList;

public class Expr implements Factor {
    private ArrayList<Term> terms;
    private int index;

    public Expr() {
        this.terms = new ArrayList<>();
        this.index = 1;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public void setTerms(ArrayList<Term> terms) {
        this.terms = terms;
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Poly toPoly() {
        Poly poly = new Poly();
        for (Term it : terms) {
            poly = poly.addPoly(it.toPoly());
        }
        if (this.index != 1) {
            poly = poly.powPoly(this.index);
        }
        return poly;
    }
}

