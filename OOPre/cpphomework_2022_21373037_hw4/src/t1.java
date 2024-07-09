package src;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class t1 {
    public static final String PACKAEG_PATTERN = "^package \\w+(\\.\\w+)+";
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        String string = sc.nextLine();
        string = getSecondDomain(string);
        System.out.println(string);
    }
    public static String getSecondDomain(String javaFileContent) {
        Pattern pattern = Pattern.compile(PACKAEG_PATTERN);         //将定义好的正则表达式字符串转换为正则表达式对象
        Matcher matcher = pattern.matcher(javaFileContent);         //将正则表达式结合到输入数据上
        if (!matcher.find()) {                                      //尝试进行匹配
            throw new RuntimeException("Invalid input!");           //失配，报错
        }
        String[] urlParts = matcher.group()                         //匹配成功，获得匹配到的字符串
                .split("\\.");                          //对匹配进行切分。注意split方法的参数也是正则表达式
        if (urlParts.length < 2) {
            System.err.println("package name's too simple!");
        }
        return urlParts[1];
    }
}
