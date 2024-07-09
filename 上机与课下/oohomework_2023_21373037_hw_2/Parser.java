import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Lexer getLexer() {
        return lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        if (lexer.peek().equals("-")) { //首项正负可有可无
            lexer.next();
            expr.addTerm(parseTerm(1));
        } else if (lexer.peek().equals("+")) {
            lexer.next();
            expr.addTerm(parseTerm(0));
        } else {
            expr.addTerm(parseTerm(0));
        }
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            if (lexer.peek().equals("+")) {
                lexer.next();
                expr.addTerm(parseTerm(0));  //0为正 1为负
            } else {
                lexer.next();
                expr.addTerm(parseTerm(1));
            }
        }
        return expr;
    }

    public Term parseTerm(int pos) {
        Term term = new Term(pos);
        term.addFactor(parseFactor());

        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Expr parseExprFactor() {
        lexer.next();
        Expr expr = parseExpr();
        lexer.next();  // 处理掉 )
        if (lexer.peek().equals("**")) {
            lexer.next();
            if (lexer.peek().equals("+")) {
                lexer.next();
            }
            expr.setIndex(Integer.parseInt(lexer.peek()));
            lexer.next();
        }
        return expr;
    }

    public TriFun parseTriFun() {
        TriFun trifun = new TriFun();
        if (lexer.peek().equals("sin")) {
            trifun.setSin(true);
        }
        lexer.next();
        lexer.next(); //处理掉sin/cos(
        trifun.setFactor(parseFactor());
        lexer.next();  //)
        if (lexer.peek().equals("**")) {
            lexer.next();
            if (lexer.peek().equals("+")) {
                lexer.next();
            }
            trifun.setIndex(Integer.parseInt(lexer.peek()));
            lexer.next();
        }
        return trifun;
    }

    public Unit parseUnit() {
        Unit unit = new Unit();
        int flag = 0;
        int index = 1;
        int xyz;
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            if (lexer.peek().equals("-")) {
                flag = (flag + 1) % 2;
            }
            lexer.next();
        }
        unit.setPos(flag);
        if (lexer.peek().equals("x") || lexer.peek().equals("y") ||
                lexer.peek().equals("z")) { //处理幂函数变量
            xyz = (lexer.peek().equals("x")) ? 1 : (lexer.peek().equals("y")) ? 2 : 3;
            lexer.next();
            if (lexer.peek().equals("**")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                index = Integer.parseInt(lexer.peek());
                lexer.next();
            }
            if (xyz == 1) {
                unit.setIndexX(index);
            } else if (xyz == 2) {
                unit.setIndexY(index);
            } else {
                unit.setIndexZ(index);
            }
        } else {   //常数
            BigInteger constant = new BigInteger(lexer.peek());
            unit.setConstant(constant);
            lexer.next();
        }
        return unit;
    }

    public CustomFun parseCustomFun() {
        CustomFun customFun = new CustomFun();
        customFun.setName(lexer.peek());  // f g h
        lexer.next();
        lexer.next(); // 读掉(
        customFun.setFactor1(parseFactor());
        if (lexer.peek().equals(",")) {
            lexer.next();
            customFun.setFactor2(parseFactor());
            if (lexer.peek().equals(",")) {
                lexer.next();
                customFun.setFactor3(parseFactor());
            }
        }
        lexer.next();  //读掉）
        return customFun;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            return parseExprFactor();
        } else if (lexer.peek().equals("sin") || lexer.peek().equals("cos")) {
            return parseTriFun();
        } else if (lexer.peek().equals("f") || lexer.peek().equals("g")
                || lexer.peek().equals("h")) {
            return parseCustomFun();
        } else {
            return parseUnit();
        }
    }
}
