
public class Derivation extends DerivationMethod implements Factor {  //求导因子类
    private Expr expr;

    public Derivation() {
        super();
        this.expr = null;
    }

    public String getVar() {
        return super.getVar();
    }

    public void setVar(String var) {
        super.setVar(var);
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    @Override
    public Poly toPoly() {  //进行求导相关工作
        Poly polyExpr = this.expr.toPoly();
        return this.derivation(polyExpr);
    }
}
