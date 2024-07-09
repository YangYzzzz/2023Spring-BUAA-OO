import java.math.BigInteger;

public class Unit implements Factor {  // a * x ** b
    private BigInteger constant;
    private int indexX;
    private int indexY;
    private int indexZ;
    private int pos;  // + / -

    public Unit() {
        this.constant = BigInteger.valueOf(1);
        this.indexX = 0;
        this.indexY = 0;
        this.indexZ = 0;
        //this.pow = '0';  //根据pow判断是常数还是变量
        this.pos = 0;
    }

    public Unit(BigInteger constant, int indexX, int indexY, int indexZ, int pos) {
        this.constant = constant;
        this.indexX = indexX;
        this.indexY = indexY;
        this.indexZ = indexZ;
        this.pos = pos;
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

    public Poly toPoly() {
        Poly poly = new Poly();
        poly.addUnit(this);
        return poly;
    }
}
