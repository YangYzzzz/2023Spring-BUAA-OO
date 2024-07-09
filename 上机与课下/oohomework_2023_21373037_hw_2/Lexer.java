public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public String getInput() {
        return input;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getCurToken() {
        return curToken;
    }

    public void setCurToken(String curToken) {
        this.curToken = curToken;
    }

    public Lexer(String input) {
        this.input = input;
        this.next();  //执行了第一次next
    }

    private String getNumber() { //消去前导0
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))
                && input.charAt(pos) == '0') {
            pos += 1;
        }
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            pos = pos + 1;
        }
        if (sb.length() == 0) {
            return "0";
        } else {
            return sb.toString();
        }
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }

        char c = input.charAt(pos); // .charAt
        if (Character.isDigit(c)) {
            curToken = getNumber();
        } else if (c == '+' || c == '-' || c == '(' || c == ')' || c == ',') {   //符号，变量名，函数名
            pos += 1;
            curToken = String.valueOf(c);
        } else if (c == 'x' || c == 'y' || c == 'z' || c == 'f' || c == 'g' || c == 'h') {
            pos += 1;
            curToken = String.valueOf(c);
        } else if (c == '*') {
            pos += 1;
            if (input.charAt(pos) == '*') {
                pos += 1;
                curToken = "**";
            } else {
                curToken = "*";
            }
        } else if (c == 's' && input.charAt(pos + 1) == 'i' && input.charAt(pos + 2) == 'n') {
            pos = pos + 3;
            curToken = "sin";
        } else if (c == 'c' && input.charAt(pos + 1) == 'o' && input.charAt(pos + 2) == 's') {
            pos = pos + 3;
            curToken = "cos";
        }
    }

    public String peek() {
        return this.curToken;
    }
}
