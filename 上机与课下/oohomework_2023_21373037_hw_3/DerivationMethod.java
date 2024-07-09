import java.math.BigInteger;
import java.util.HashMap;

import static java.lang.Math.abs;

public class DerivationMethod {   //放置求导相关函数
    private String var;

    public DerivationMethod() {
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public Poly derivationTri(Unit unit, HashMap.Entry<Poly, Integer> entry) {
        Unit unit1 = new Unit();
        unit1.setIndexX(unit.getIndexX());
        unit1.setIndexY(unit.getIndexY());
        unit1.setIndexZ(unit.getIndexZ());  //周到 由于dx sin(y)这种情况会在对内部y求导中考虑 因此在考虑三角函数时统一全部求导即可
        unit1.setConstant(unit.getConstant().multiply(BigInteger.valueOf(abs(entry.getValue()))));
        if (entry.getValue() < 0) { //cos -> -sin
            if (unit.getPos() == 1) {
                unit1.setPos(0);
            } else {
                unit1.setPos(1);
            }
            unit1.getTriFunHashMap().put(entry.getKey().clone(), entry.getValue() + 1);
        } else { //sin -> cos
            unit1.setPos(unit.getPos());
            unit1.getTriFunHashMap().put(entry.getKey().clone(), entry.getValue() - 1);
        }
        for (HashMap.Entry<Poly, Integer> entry1 : unit.getTriFunHashMap().entrySet()) {
            if (!entry1.equals(entry)) {
                unit1.getTriFunHashMap().put(entry1.getKey().clone(), entry1.getValue());
            }
        }
        // sin(x)**2 -> 2sin(x) * cos(x)
        Poly poly1 = this.derivation(entry.getKey()); //index == 0怎么办
        if (entry.getValue() > 0) {
            unit1.getTriFunHashMap().put(entry.getKey(), -1);
        } else {
            unit1.getTriFunHashMap().put(entry.getKey(), 1);
        }
        Poly poly = new Poly();
        poly.addUnit(unit1);
        return poly.mulPoly(poly1);
    }

    public Poly derivationPoly(Unit unit) {   // 对多项式部分进行求导
        Unit unit1 = new Unit();
        unit1.setPos(unit.getPos());
        if (this.var.equals("x")) {
            unit1.setIndexX(unit.getIndexX() - 1);
            unit1.setIndexY(unit.getIndexY());
            unit1.setIndexZ(unit.getIndexZ());
            unit1.setConstant(unit.getConstant().multiply(
                    BigInteger.valueOf(unit.getIndexX())));
        } else if (this.var.equals("y")) {
            unit1.setIndexX(unit.getIndexX());
            unit1.setIndexY(unit.getIndexY() - 1);
            unit1.setIndexZ(unit.getIndexZ());
            unit1.setConstant(unit.getConstant().multiply(
                    BigInteger.valueOf(unit.getIndexY())));
        } else if (this.var.equals("z")) {
            unit1.setIndexX(unit.getIndexX());
            unit1.setIndexY(unit.getIndexY());
            unit1.setIndexZ(unit.getIndexZ() - 1);
            unit1.setConstant(unit.getConstant().multiply(
                    BigInteger.valueOf(unit.getIndexZ())));
        }
        for (HashMap.Entry<Poly, Integer> entry : unit.getTriFunHashMap().entrySet()) {
            unit1.getTriFunHashMap().put(entry.getKey().clone(), entry.getValue());
        }
        Poly poly = new Poly();
        poly.addUnit(unit1);
        return poly;
    }

    public Poly derivation(Poly poly) {
        Poly polyResult = new Poly();
        for (Unit it : poly.getUnits()) {
            polyResult = polyResult.addPoly(derivationPoly(it));  // 先加常数 加法
            for (HashMap.Entry<Poly, Integer> entry : it.getTriFunHashMap().entrySet()) {
                if (entry.getValue() != 0) {
                    polyResult = polyResult.addPoly(derivationTri(it, entry));    // 加法
                }
            }
        }
        return polyResult;
    }
}
