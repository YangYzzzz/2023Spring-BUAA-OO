package Factor;

import java.math.BigInteger;

public class Cos implements Factor {

    private Factor factor;

    public Cos(Factor factor) {
        this.factor = factor;
    }

    public Sin toSin() {
        return new Sin(factor);
    }

    @Override
    public String toString() {
        return "cos(" + factor.toString() + ")";
    }

    @Override
    public Factor derive() {
        Term term = new Term();
        /* TODO 3 */
        term.addFactor(toSin());  //负号
        Term term1 = new Term();
        term1.addFactor(new Number("-1"));
        term = Term.mergeTerm(term1, (Term) factor.derive());
        return term;
    }

    @Override
    public Factor clone() {
        return new Cos(factor.clone());
    }
}


