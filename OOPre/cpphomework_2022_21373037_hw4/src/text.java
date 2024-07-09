package src;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class text {
    public static Scanner sc = new Scanner(System.in);
    public static final String regularPattern = "[\\u3000\\u0020\\u00A0]{5}";
    public static void main(String[] args) {
        String string = sc.nextLine();
        System.out.println(string.length());
        string = string.substring(1,string.length()-1);
        System.out.println(string);
    }
}
