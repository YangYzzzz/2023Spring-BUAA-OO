import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        StringBuilder expression = new StringBuilder();
        input = input.replaceAll("\\s", "");
        int len = input.length();
        int flag = 0;
        for (int i = 0; i < len; i++) {
            if (input.charAt(i) == '+' || input.charAt(i) == '-') {
                while (input.charAt(i) == '+' || input.charAt(i) == '-') {
                    if (input.charAt(i) == '-') {
                        flag++;
                    }
                    i++;
                }
                i--;
                if (flag % 2 == 1) {
                    expression.append("-");
                }
                else {
                    expression.append("+");
                }
                flag = 0;
            } else {
                expression.append(input.charAt(i));
            }
        }
        //int pos = expression.indexOf("(");
        //if(pos != -1 && expression.charAt(pos+1) == '-') expression.insert(pos+1, '0');
        //System.out.println(expression);
        Lexer lexer = new Lexer(expression.toString()); //correct
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        /* String result = expr.toString();
        System.out.println(result);
        String[] suffix = result.split(" ");
        System.out.println(suffix.length); */
        Poly poly = expr.toPoly();
        String result = poly.toString();
        System.out.println(result);
    }
}
