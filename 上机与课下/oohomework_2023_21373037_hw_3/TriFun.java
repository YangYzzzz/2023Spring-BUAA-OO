import java.math.BigInteger;

public class TriFun implements Factor {
    private int index;
    private boolean isSin;
    private Factor factor;

    public TriFun() {
        this.index = 1;
        this.isSin = false;
        this.factor = null;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isSin() {
        return isSin;
    }

    public void setSin(boolean sin) {
        isSin = sin;
    }

    public Factor getFactor() {
        return factor;
    }

    public void setFactor(Factor factor) {
        this.factor = factor;
    }

    @Override
    public Poly toPoly() {
        Unit unit = new Unit();
        Poly poly = new Poly();
        Poly factorPoly = factor.toPoly();
        String str = factorPoly.toString();
        if (str.equals("0") && isSin && index != 0) {   // 将sin0 cos0化简为多项式形式
            unit.setConstant(BigInteger.valueOf(0));
        } else if ((str.equals("0") && !isSin) || index == 0) {
            unit.setConstant(BigInteger.valueOf(1));
        } else if (!isSin && factorPoly.getUnits().size() == 1
                && factorPoly.getUnits().get(0).notExpr()) { //cos((-x))
            factorPoly.getUnits().get(0).setPos(0);
            unit.addTriFun(-index, factorPoly);
        } else if (isSin && factorPoly.getUnits().size() == 1
                && factorPoly.getUnits().get(0).notExpr()
                && factorPoly.getUnits().get(0).getPos() == 1) {
            unit.setPos(index % 2);
            factorPoly.getUnits().get(0).setPos(0);
            unit.addTriFun(index, factorPoly);
        } else if (isSin) {
            unit.addTriFun(index, factorPoly);
        } else {
            unit.addTriFun(-index, factorPoly);
        }
        poly.addUnit(unit);
        return poly;
    }
}
