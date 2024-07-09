package src;

import com.sun.source.tree.NewArrayTree;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
    public static String test = "(\\d*)/(\\d*)/(\\d*)";

    public static void main(String[] args) {
        StringBuilder s = new StringBuilder();
        s.append("ass" + " ");
        s.delete(s.length()-1,s.length());
        System.out.println(s.length());
        /*Pattern pattern = Pattern.compile(test);
        Matcher matcher = pattern.matcher(s);
        matcher.find();
        System.out.println(matcher.group(0));
        System.out.println(matcher.group(1));
        System.out.println(matcher.group(2).equals("2")); */

        //System.out.println(matcher.group(3).equals(""));

    }
}
