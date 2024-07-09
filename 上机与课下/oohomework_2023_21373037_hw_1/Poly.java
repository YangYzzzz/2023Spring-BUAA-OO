import java.math.BigInteger;
import java.util.ArrayList;

public class Poly {
    private ArrayList<Unit> units;

    public Poly() {
        this.units = new ArrayList<>();
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }

    public void addUnit(Unit unit) {
        this.units.add(unit);
    }

    public Poly addPoly(Poly other) {
        Poly poly = new Poly();
        int pos;
        int flag;
        BigInteger constant1;
        BigInteger constant2;
        BigInteger constant;
        for (Unit unit : this.units) {
            poly.addUnit(unit);
        }
        for (Unit unit1 : other.units) {
            flag = 0;
            for (Unit unit2 : poly.units) {
                if (unit1.getIndexX() == unit2.getIndexX() && unit1.getIndexY() ==
                        unit2.getIndexY() && unit1.getIndexZ() == unit2.getIndexZ()) {
                    flag = 1;
                    constant1 = (unit1.getPos() == 0) ? unit1.getConstant()
                            : unit1.getConstant().negate();
                    constant2 = (unit2.getPos() == 0) ? unit2.getConstant()
                            : unit2.getConstant().negate();
                    constant = constant1.add(constant2);
                    pos = (constant.compareTo(BigInteger.valueOf(0)) >= 0) ? 0 : 1;
                    constant = constant.abs();
                    unit2.setConstant(constant);
                    unit2.setPos(pos);
                    break;
                }
            }
            if (flag == 0) {
                poly.addUnit(unit1);
            }
        }
        return poly;
    }

    public Poly mulPoly(Poly other) {
        Poly poly = new Poly();
        int pos;
        BigInteger constant;
        for (Unit unit1 : other.units) {
            if (units.size() == 0) {
                return other;
            }
            for (Unit unit2 : this.units) { //乘法
                constant = unit1.getConstant().multiply(unit2.getConstant());
                pos = ((unit1.getPos() + unit2.getPos()) == 1) ? 1 : 0;
                Unit unit = new Unit(constant, unit1.getIndexX() + unit2.getIndexX(),
                        unit1.getIndexY() + unit2.getIndexY(),
                        unit1.getIndexZ() + unit2.getIndexZ(), pos);
                Poly newPoly = unit.toPoly();
                poly = poly.addPoly(newPoly);
            }
        }
        return poly;
    }

    public Poly powPoly(int index) {
        Poly poly = this;
        int i;
        if (index == 0) {
            Unit unit = new Unit();
            poly = unit.toPoly();
            return poly;
        }
        for (i = 0; i < index - 1; i++) {
            poly = this.mulPoly(poly);
        }
        return poly;
    }

    public void inverse() {
        for (Unit it : units) {
            if (it.getPos() == 1) {
                it.setPos(0);
            } else {
                it.setPos(1);
            }
        }
    }

    public StringBuilder getMain(Unit it, StringBuilder sb) {
        sb.append(it.getConstant());
        if (it.getIndexX() != 0) {
            sb.append("*x");
            if (it.getIndexX() > 2) {
                sb.append("**").append(it.getIndexX());
            } else if (it.getIndexX() == 2) {
                sb.append("*x");
            }
        }
        if (it.getIndexY() != 0) {
            sb.append("*y");
            if (it.getIndexY() > 2) {
                sb.append("**").append(it.getIndexY());
            } else if (it.getIndexY() == 2) {
                sb.append("*y");
            }
        }
        if (it.getIndexZ() != 0) {
            sb.append("*z");
            if (it.getIndexZ() > 2) {
                sb.append("**").append(it.getIndexZ());
            } else if (it.getIndexZ() == 2) {
                sb.append("*z");
            }
        }
        return sb;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < units.size(); i++) {  // 找到第一个正项输出
            if (units.get(i).getPos() == 0 &&
                    units.get(i).getConstant().compareTo(BigInteger.valueOf(0)) != 0) {
                sb.append("+");
                sb = getMain(units.get(i), sb);
                units.remove(i);
                break;
            }
        }
        for (Unit it : units) {
            if (it.getConstant().compareTo(BigInteger.valueOf(0)) == 0) {
                continue;
            }
            if (it.getPos() == 1) {
                sb.append("-");
            } else if (it.getPos() == 0) {
                sb.append("+");
            }
            sb = getMain(it, sb);
        }
        if (sb.length() == 0) {
            return "0";
        }
        String result;
        result = sb.toString().replaceAll("\\+1\\*", "+").replaceAll("-1\\*", "-");
        if (result.charAt(0) == '+') {
            result = result.substring(1);
        }
        return result;
    }
}
