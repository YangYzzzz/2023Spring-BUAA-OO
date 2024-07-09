import java.math.BigInteger;
import java.util.HashMap;

public class Unit implements Factor {
    private BigInteger constant;
    private int indexX;
    private int indexY;
    private int indexZ;
    private int pos;  // + / -
    private HashMap<Poly, Integer> triFunHashMap;  // 指数正号代表sin 负号代表cos

    public Unit() {
        this.constant = BigInteger.valueOf(1);
        this.indexX = 0;
        this.indexY = 0;
        this.indexZ = 0;
        //this.pow = '0';  //根据pow判断是常数还是变量
        this.pos = 0;
        this.triFunHashMap = new HashMap<>();
    }

    public Unit(BigInteger constant, int indexX, int indexY, int indexZ, int pos) {
        this.constant = constant;
        this.indexX = indexX;
        this.indexY = indexY;
        this.indexZ = indexZ;
        this.pos = pos;
        this.triFunHashMap = new HashMap<>();
    }

    public boolean notExpr() {
        if (this.indexY == 0 && this.getConstant().compareTo(BigInteger.valueOf(1)) == 0
                && this.indexX == 0 && this.triFunHashMap.size() == 0) {
            return true;
        } else if (this.indexZ == 0 && this.getConstant().compareTo(BigInteger.valueOf(1)) == 0
                && this.indexX == 0 && this.triFunHashMap.size() == 0) {
            return true;
        } else if (this.indexY == 0 && this.getConstant().compareTo(BigInteger.valueOf(1)) == 0
                && this.indexZ == 0 && this.triFunHashMap.size() == 0) {
            return true;
        } else if (this.triFunHashMap.size() == 0 && this.indexZ == 0
                && this.indexY == 0 && this.indexX == 0) {
            return true;
        } else {
            return this.indexX == 0 && this.getConstant().compareTo(BigInteger.valueOf(1)) == 0
                && this.indexY == 0 && this.indexZ == 0 && this.triFunHashMap.size() == 1;
        }
    }

    public int getIndexX() {
        return indexX;
    }

    public void setIndexX(int indexX) {
        this.indexX = indexX;
    }

    public int getIndexY() {
        return indexY;
    }

    public void setIndexY(int indexY) {
        this.indexY = indexY;
    }

    public int getIndexZ() {
        return indexZ;
    }

    public void setIndexZ(int indexZ) {
        this.indexZ = indexZ;
    }

    public BigInteger getConstant() {
        return constant;
    }

    public void setConstant(BigInteger constant) {
        this.constant = constant;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public HashMap<Poly, Integer> getTriFunHashMap() {
        return triFunHashMap;
    }

    public void setTriFunHashMap(HashMap<Poly, Integer> triFunHashMap) {
        this.triFunHashMap = triFunHashMap;
    }

    public void addTriFun(int index, Poly poly) {
        this.triFunHashMap.put(poly, index);
    }

    public Poly toPoly() {
        Poly poly = new Poly();
        poly.addUnit(this);
        return poly;
    }

    public Unit clone() {
        Unit unit = new Unit();
        unit.setConstant(this.constant);
        unit.setPos(this.pos);
        unit.setIndexX(this.indexX);
        unit.setIndexY(this.indexY);
        unit.setIndexZ(this.indexZ);
        for (HashMap.Entry<Poly, Integer> entry : this.getTriFunHashMap().entrySet()) {
            unit.getTriFunHashMap().put(entry.getKey().clone(), entry.getValue());
        }
        return unit;
    }
}
