public class CustomFunSet {
    private String name;
    private String var1;
    private String var2;
    private String var3;
    private String fun;
    private int num;

    public CustomFunSet(String name, String var1, String var2, String var3, String fun) {
        this.name = name;
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
        this.fun = fun;
    }

    public void setNum() {
        int i = 0;
        if (var1 != null) {
            i++;
        }
        if (var2 != null) {
            i++;
        }
        if (var3 != null) {
            i++;
        }
        this.num = i;
    }

    public CustomFunSet() {
        this.name = "\0";
        this.var1 = "\0";
        this.var2 = "\0";
        this.var3 = "\0";
        this.fun = "\0";
        this.num = 0;
    }

    public int getNum() {
        return num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVar1() {
        return var1;
    }

    public void setVar1(String var1) {
        this.var1 = var1;
    }

    public String getVar2() {
        return var2;
    }

    public void setVar2(String var2) {
        this.var2 = var2;
    }

    public String getVar3() {
        return var3;
    }

    public void setVar3(String var3) {
        this.var3 = var3;
    }

    public String getFun() {
        return fun;
    }

    public void setFun(String fun) {
        this.fun = fun;
    }
}
