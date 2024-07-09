import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    private static Pattern patternFun =
            Pattern.compile("(f|g|h)\\((\\w+)(?:,(\\w+)){0,1}(?:,(\\w+)){0,1}\\)");
    private static ArrayList<CustomFunSet> customFunList = new ArrayList<>();

    public static ArrayList<CustomFunSet> getCustomFunList() {
        return customFunList;
    }

    public static void setCustomFunList(ArrayList<CustomFunSet> customFunList) {
        MainClass.customFunList = customFunList;
    }

    public static String preExpr(String input) {
        StringBuilder expression = new StringBuilder();
        String preInput = input.replaceAll("\\s", "");
        int len = preInput.length();
        int flag = 0;
        for (int i = 0; i < len; i++) {
            if (preInput.charAt(i) == '+' || preInput.charAt(i) == '-') {
                while (preInput.charAt(i) == '+' || preInput.charAt(i) == '-') {
                    if (preInput.charAt(i) == '-') {
                        flag++;
                    }
                    i++;
                }
                i--;
                if (flag % 2 == 1) {
                    expression.append("-");
                } else {
                    expression.append("+");
                }
                flag = 0;
            } else {
                expression.append(preInput.charAt(i));
            }
        }
        return expression.toString();
    }

    public static String parseExpr(String input) {
        Lexer lexer = new Lexer(preExpr(input)); //correct
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        Poly poly = expr.toPoly();
        return poly.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String function;
        for (int i = 0; i < n; i++) {
            function = scanner.nextLine();
            customFunList.add(preCustomFunSet(function));
        }
        String input = scanner.nextLine();
        System.out.println(parseExpr(input));
    }

    public static CustomFunSet preCustomFunSet(String function1) {
        String function = function1.replaceAll("\\s", "");
        String[] tmp = function.split("=");
        String fun = tmp[0];
        Matcher matcher = patternFun.matcher(fun);
        CustomFunSet customFunSet = new CustomFunSet();
        if (matcher.find()) {
            customFunSet.setName(matcher.group(1));
            customFunSet.setVar1(matcher.group(2));
            customFunSet.setVar2(matcher.group(3));
            customFunSet.setVar3(matcher.group(4));
            customFunSet.setFun(parseExpr(tmp[1]));
            customFunSet.setNum();
        }
        return customFunSet;
    }
}


