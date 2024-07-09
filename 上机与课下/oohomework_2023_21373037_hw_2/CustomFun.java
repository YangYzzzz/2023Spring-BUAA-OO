public class CustomFun implements Factor {
    private String name;
    private Factor factor1;
    private Factor factor2;
    private Factor factor3;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Factor getFactor1() {
        return factor1;
    }

    public void setFactor1(Factor factor1) {
        this.factor1 = factor1;
    }

    public Factor getFactor2() {
        return factor2;
    }

    public void setFactor2(Factor factor2) {
        this.factor2 = factor2;
    }

    public Factor getFactor3() {
        return factor3;
    }

    public void setFactor3(Factor factor3) {
        this.factor3 = factor3;
    }

    public CustomFun() {
        this.name = "/0";
        this.factor1 = null;
        this.factor2 = null;
        this.factor3 = null;
    }

    @Override
    public Poly toPoly() {
        //对每个因子 先toPoly 再toString 再整体replace 再解析Expr字符串 再to Poly
        CustomFunSet customFun = null;
        for (CustomFunSet customFunSet : MainClass.getCustomFunList()) {
            if (customFunSet.getName().equals(this.name)) {
                customFun = customFunSet;  //找到对应的自定义函数
                break;
            }
        }
        assert customFun != null;
        String result1 = null;
        String result2 = null;
        String result3 = null;
        result1 = factor1.toPoly().toString();
        //System.out.println(result1);
        if (factor2 != null) {
            result2 = factor2.toPoly().toString();
            //System.out.println(result2);
            if (factor3 != null) {
                result3 = factor3.toPoly().toString();
                //System.out.println(result3);
            }
        }
        /*result = result.replaceAll(customFun.getVar1(), "(" + result1 + ")"); //大问题
        if(result2 != null) {
            result = result.replaceAll(customFun.getVar2(), "(" + result2 + ")");
            if(result3 != null) {
                result = result.replaceAll(customFun.getVar3(), "(" + result3 + ")");
            }
        }*/
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < customFun.getFun().length(); i++) {
            if (customFun.getFun().charAt(i) == customFun.getVar1().charAt(0)) {
                sb.append("(").append(result1).append(")"); //会产生sin((x))的情况
            } else if (customFun.getVar2() != null &&
                    customFun.getFun().charAt(i) == customFun.getVar2().charAt(0)) {
                sb.append("(").append(result2).append(")");
            } else if (customFun.getVar3() != null &&
                    customFun.getFun().charAt(i) == customFun.getVar3().charAt(0)) {
                sb.append("(").append(result3).append(")");
            } else {
                sb.append(customFun.getFun().charAt(i));
            }
        }
        Lexer lexer = new Lexer(MainClass.preExpr(sb.toString())); //correct
        Parser parser = new Parser(lexer);
        return parser.parseExpr().toPoly();
    }
}
