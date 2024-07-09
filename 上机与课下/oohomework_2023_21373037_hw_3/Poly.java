import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

    //public
    public boolean equals(Poly poly) {  //比较两个Poly是否相同 深克隆 默认化简到最简
        ArrayList<Unit> unitList1 = new ArrayList<>();
        ArrayList<Unit> unitList2 = new ArrayList<>();
        //深克隆
        for (Unit unit : this.getUnits()) {
            if (unit.getConstant().compareTo(BigInteger.valueOf(0)) != 0) {
                unitList1.add(unit.clone());
            }
        }
        for (Unit unit : poly.getUnits()) {
            if (unit.getConstant().compareTo(BigInteger.valueOf(0)) != 0) {
                unitList2.add(unit.clone());
            }
        }
        Iterator<Unit> unitIt1 = unitList1.iterator();
        Iterator<Unit> unitIt2 = unitList2.iterator();
        while (unitIt1.hasNext()) {
            Unit unit1 = unitIt1.next();
            while (unitIt2.hasNext()) {
                Unit unit2 = unitIt2.next();
                if ((unit1.getConstant().compareTo(unit2.getConstant()) == 0 &&
                        unit1.getIndexX() == unit2.getIndexX() &&
                        unit1.getIndexY() == unit2.getIndexY() &&
                        unit1.getIndexZ() == unit2.getIndexZ() &&
                        unit1.getTriFunHashMap().size() == unit2.getTriFunHashMap().size())) {
                    int triPos = isHashMapSame(unit1.getTriFunHashMap(), unit2.getTriFunHashMap());
                    if (triPos == 0 && unit1.getPos() == unit2.getPos()) {  //三角哈希图的正负
                        unitIt1.remove();
                        unitIt2.remove();
                        break;
                    } else if (triPos == 1 && unit1.getPos() == -unit2.getPos()) {
                        unitIt1.remove();
                        unitIt2.remove();
                        break;
                    }
                }
            }
            unitIt2 = unitList2.iterator();
        }
        return unitList1.size() == 0 && unitList2.size() == 0;
    }

    public int isHashMapSame(HashMap<Poly, Integer> map1, HashMap<Poly, Integer> map2) {
        int pos = 0; //0代表正 1代表负号
        HashMap<Poly, Integer> polyHashMap1 = new HashMap<>();
        HashMap<Poly, Integer> polyHashMap2 = new HashMap<>();
        for (HashMap.Entry<Poly, Integer> entry : map1.entrySet()) {
            if (entry.getValue() != 0) {
                polyHashMap1.put(entry.getKey().clone(), entry.getValue());
            }
        }
        for (HashMap.Entry<Poly, Integer> entry : map2.entrySet()) {
            if (entry.getValue() != 0) {
                polyHashMap2.put(entry.getKey().clone(), entry.getValue());
            }
        }
        Iterator<HashMap.Entry<Poly, Integer>> polyIt1 = polyHashMap1.entrySet().iterator();
        Iterator<HashMap.Entry<Poly, Integer>> polyIt2 = polyHashMap2.entrySet().iterator();
        while (polyIt1.hasNext()) {
            HashMap.Entry<Poly, Integer> entry = polyIt1.next();
            int index1 = entry.getValue();
            Poly poly1 = entry.getKey();
            while (polyIt2.hasNext()) {
                HashMap.Entry<Poly, Integer> entry1 = polyIt2.next();
                int index2 = entry1.getValue();
                Poly poly2 = entry1.getKey();
                if (index1 == index2 && index1 < 0 && (poly1.equals(poly2)
                        || poly1.inverseEqual(poly2))) {  //cos的合并 以及sin偶数平方项
                    polyIt1.remove();
                    polyIt2.remove();
                    break;
                }
                if (index1 == index2 && index1 > 0 && index1 % 2 == 0
                        && (poly1.equals(poly2) || poly1.inverseEqual(poly2))) {
                    polyIt1.remove();
                    polyIt2.remove();
                    break;
                }
                if (index1 == index2 && index1 % 2 == 1 && poly1.equals(poly2)) {
                    polyIt1.remove();
                    polyIt2.remove();
                    break;
                }
                if (index1 == index2 && index1 % 2 == 1 &&
                        poly1.inverseEqual(poly2)) { //sin的奇数项的合并 在对应的Unit中负号应颠倒 传递符号
                    pos++;
                    pos = pos % 2;
                    polyIt1.remove();
                    polyIt2.remove();
                    break;
                }
            }
            polyIt2 = polyHashMap2.entrySet().iterator();
        }
        if (polyHashMap1.size() == 0 && polyHashMap2.size() == 0) {
            return pos;  // 0 / 1
        } else {
            return -1;
        }
    }

    private boolean inverseEqual(Poly poly) {
        ArrayList<Unit> unitList1 = new ArrayList<>();
        ArrayList<Unit> unitList2 = new ArrayList<>();
        //深克隆
        for (Unit unit : this.getUnits()) {
            unitList1.add(unit.clone());
        }
        for (Unit unit : poly.getUnits()) {
            unitList2.add(unit.clone());
        }
        Poly poly1 = new Poly();
        poly1.setUnits(unitList1);
        Poly poly2 = new Poly();
        poly2.setUnits(unitList2);
        poly1.inverse();
        return poly1.equals(poly2);
    }

    public void squareMergeModify(Unit unit1, Unit unit2) {
        for (HashMap.Entry<Poly, Integer> entry1 : unit1.getTriFunHashMap().entrySet()) {
            for (HashMap.Entry<Poly, Integer> entry2 : unit2.getTriFunHashMap().entrySet()) {
                if (((entry1.getValue() >= 2 && entry2.getValue() <= -2) || (entry1.getValue() <= -2
                        && entry2.getValue() >= 2)) && (entry1.getKey().equals(entry2.getKey()) ||
                        entry1.getKey().inverseEqual((entry2.getKey())))) {
                    //System.out.println("++");
                    HashMap<Poly, Integer> polyHashMap1 = new HashMap<>();
                    HashMap<Poly, Integer> polyHashMap2 = new HashMap<>();
                    for (HashMap.Entry<Poly, Integer> entry :
                            unit1.getTriFunHashMap().entrySet()) {  //去除平方项
                        if (!entry.equals(entry1)) {
                            polyHashMap1.put(entry.getKey().clone(), entry.getValue());
                        } else {
                            polyHashMap1.put(entry.getKey().clone(), entry.getValue() > 0 ?
                                    entry.getValue() - 2 : entry.getValue() + 2);
                        }
                    }
                    for (HashMap.Entry<Poly, Integer> entry :
                            unit2.getTriFunHashMap().entrySet()) {
                        if (!entry.equals(entry2)) {
                            polyHashMap2.put(entry.getKey().clone(), entry.getValue());
                        } else {
                            polyHashMap2.put(entry.getKey().clone(), entry.getValue() > 0 ?
                                    entry.getValue() - 2 : entry.getValue() + 2);
                        }
                    }
                    int triPos = isHashMapSame(polyHashMap1, polyHashMap2);
                    if ((triPos == 0 && unit1.getPos() == unit2.getPos()) ||
                            (triPos == 1 && unit1.getPos() + unit2.getPos() == 1)) {
                        if (unit1.getConstant().compareTo(unit2.getConstant()) > 0) {
                            entry2.setValue(entry2.getValue() > 0 ? entry2.getValue() - 2
                                    : entry2.getValue() + 2);
                            unit1.setConstant(unit1.getConstant().subtract(unit2.getConstant()));
                        } else {
                            unit2.setConstant(unit2.getConstant().subtract(unit1.getConstant()));
                            entry1.setValue(entry1.getValue() > 0 ? entry1.getValue() - 2
                                    : entry1.getValue() + 2);
                        }
                        return;
                    }
                }
            }
        }
    }

    public void squareMerge(Unit unit1, Unit unit2) {
        for (HashMap.Entry<Poly, Integer> entry1 : unit1.getTriFunHashMap().entrySet()) {
            for (HashMap.Entry<Poly, Integer> entry2 : unit2.getTriFunHashMap().entrySet()) {
                if (((entry1.getValue() == 2 && entry2.getValue() == -2) || (entry1.getValue() == -2
                        && entry2.getValue() == 2)) && (entry1.getKey().equals(entry2.getKey()) ||
                        entry1.getKey().inverseEqual((entry2.getKey())))) {
                    HashMap<Poly, Integer> polyHashMap1 = new HashMap<>();
                    HashMap<Poly, Integer> polyHashMap2 = new HashMap<>();
                    for (HashMap.Entry<Poly, Integer> entry :
                            unit1.getTriFunHashMap().entrySet()) {  //去除平方项
                        if (!entry.equals(entry1)) {
                            polyHashMap1.put(entry.getKey().clone(), entry.getValue());
                        }
                    }
                    for (HashMap.Entry<Poly, Integer> entry : unit2.getTriFunHashMap().entrySet()) {
                        if (!entry.equals(entry2)) {
                            polyHashMap2.put(entry.getKey().clone(), entry.getValue());
                        }
                    }
                    int triPos = isHashMapSame(polyHashMap1, polyHashMap2);
                    if ((triPos == 0 && unit1.getPos() == unit2.getPos()) ||
                            (triPos == 1 && unit1.getPos() + unit2.getPos() == 1)) {
                        if (unit1.getConstant().compareTo(unit2.getConstant()) > 0) {
                            unit2.getTriFunHashMap().remove(entry2.getKey());
                            unit1.setConstant(unit1.getConstant().subtract(unit2.getConstant()));
                        } else {
                            unit2.setConstant(unit2.getConstant().subtract(unit1.getConstant()));
                            unit1.getTriFunHashMap().remove(entry1.getKey());
                        }
                        return;
                    }
                }
            }
        }
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
                int triPos = isHashMapSame(unit1.getTriFunHashMap(), unit2.getTriFunHashMap());
                if (unit1.getIndexX() == unit2.getIndexX() && unit1.getIndexY() ==
                        unit2.getIndexY() && unit1.getIndexZ() == unit2.getIndexZ() &&
                        triPos != -1) {
                    flag = 1;
                    constant1 = (unit1.getPos() == 0) ? unit1.getConstant()
                            : unit1.getConstant().negate();
                    constant2 = (unit2.getPos() == 0) ? unit2.getConstant()
                            : unit2.getConstant().negate();
                    constant = (triPos == 0) ? constant2.add(constant1) :
                            constant2.subtract(constant1);
                    pos = (constant.compareTo(BigInteger.valueOf(0)) >= 0) ? 0 : 1;
                    constant = constant.abs();
                    unit2.setConstant(constant);
                    unit2.setPos(pos);
                    break;
                } else if (unit1.getIndexY() == unit2.getIndexY()
                        && unit1.getIndexZ() == unit2.getIndexZ()
                        && unit1.getIndexX() == unit2.getIndexX()) {
                    squareMergeModify(unit1, unit2);
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
                pos = ((unit1.getPos() + unit2.getPos()) == 1) ? 1 : 0; //
                Unit unit = new Unit(constant, unit1.getIndexX() + unit2.getIndexX(),
                        unit1.getIndexY() + unit2.getIndexY(),
                        unit1.getIndexZ() + unit2.getIndexZ(), pos);  //new HashMap

                for (HashMap.Entry<Poly, Integer> entry :
                        unit1.getTriFunHashMap().entrySet()) { //默认在Map中此刻无重复
                    unit.addTriFun(entry.getValue(), entry.getKey());
                }
                int flag;
                for (HashMap.Entry<Poly, Integer> entry1 : unit2.getTriFunHashMap().entrySet()) {
                    flag = 0;
                    for (HashMap.Entry<Poly, Integer> entry2 : unit.getTriFunHashMap().entrySet()) {
                        if (entry1.getKey().equals(entry2.getKey()) &&
                                entry1.getValue() * entry2.getValue() > 0) {
                            flag = 1;
                            entry2.setValue(entry2.getValue() + entry1.getValue());
                            break;
                        } else if (entry1.getKey().inverseEqual(entry2.getKey())
                                && entry1.getValue() * entry2.getValue() > 0
                                && entry1.getValue() < 0) {  //cos(x)*cos(-x)
                            flag = 1;
                            entry2.setValue(entry2.getValue() + entry1.getValue());
                            break;
                        } else if (entry1.getKey().inverseEqual(entry2.getKey())
                                && entry1.getValue() * entry2.getValue() > 0
                                && entry1.getValue() > 0) { // sin(x) * sin(-x)
                            flag = 1;
                            pos = (entry1.getValue() % 2 + unit.getPos()) % 2;  //判断此时正负号
                            unit.setPos(pos);
                            entry2.setValue(entry2.getValue() + entry1.getValue());
                            break;
                        }
                    }
                    if (flag == 0) {
                        unit.getTriFunHashMap().put(entry1.getKey(), entry1.getValue());
                    }
                }
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

    public StringBuilder getTri(Unit it, StringBuilder sb) { //输出三角函数部分
        String str;
        for (HashMap.Entry<Poly, Integer> entry : it.getTriFunHashMap().entrySet()) {
            if (entry.getValue() == 0) {
                continue;
            }
            str = entry.getKey().toString();
            if (entry.getValue() > 0) {
                sb.append("*sin(");
            } else {
                sb.append("*cos(");
            }
            Unit unit = entry.getKey().getUnits().get(0);
            if (entry.getKey().getUnits().size() == 1 && unit.notExpr()) {  //找到非表达式类型， 化简
                sb.append(str);
            } else {
                sb.append("(").append(str).append(")");
            }
            sb.append(")");
            if (entry.getValue() > 1) {
                sb.append("**").append(entry.getValue());
            } else if (entry.getValue() < -1) {
                sb.append("**").append(-entry.getValue());
            }
        }
        return sb;
    }

    public StringBuilder getPoly(Unit it, StringBuilder sb) {  // 输出多项式部分
        sb.append(it.getConstant());
        if (it.getIndexX() != 0) {
            sb.append("*x");
            if (it.getIndexX() > 1) {
                sb.append("**").append(it.getIndexX());
            } //else if (it.getIndexX() == 2) {
            //    sb.append("*x");
            //}
        }
        if (it.getIndexY() != 0) {
            sb.append("*y");
            if (it.getIndexY() > 1) {
                sb.append("**").append(it.getIndexY());
            } //else if (it.getIndexY() == 2) {
            //    sb.append("*y");
            //}
        }
        if (it.getIndexZ() != 0) {
            sb.append("*z");
            if (it.getIndexZ() > 1) {
                sb.append("**").append(it.getIndexZ());
            } //else if (it.getIndexZ() == 2) {
            //    sb.append("*z");
            //}
        }
        getTri(it, sb);
        return sb;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        ArrayList<Unit> unitsClone = new ArrayList<>(); //深拷贝
        for (Unit it : units) {
            unitsClone.add(it.clone());
        }
        for (int i = 0; i < unitsClone.size(); i++) {  // 找到第一个正项输出
            if (unitsClone.get(i).getPos() == 0 &&
                    unitsClone.get(i).getConstant().compareTo(BigInteger.valueOf(0)) != 0) {
                sb.append("+");
                getPoly(unitsClone.get(i), sb);
                unitsClone.remove(i);
                break;
            }
        }
        for (Unit it : unitsClone) {
            if (it.getConstant().compareTo(BigInteger.valueOf(0)) == 0) {
                continue;
            }
            if (it.getPos() == 1) {
                sb.append("-");
            } else if (it.getPos() == 0) {
                sb.append("+");
            }
            getPoly(it, sb);
        }
        if (sb.length() == 0) {
            return "0";
        }
        String resultStr;
        resultStr = sb.toString().replaceAll("\\+1\\*", "+").replaceAll("-1\\*", "-");
        if (resultStr.charAt(0) == '+') {
            resultStr = resultStr.substring(1);
        }
        return resultStr;
    }

    public Poly clone() {
        Poly poly = new Poly();
        for (Unit unit : this.units) {
            poly.addUnit(unit.clone());
        }
        return poly;
    }
}

