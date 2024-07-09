package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    private static Scanner sc = new Scanner(System.in);
    private static HashMap<String, ArrayList<String>> resultDate = new HashMap<>();
    private static HashMap<String, ArrayList<String>> resultSender = new HashMap<>();
    private static HashMap<String, ArrayList<String>> resultRev = new HashMap<>();
    private static String regularPattern =
            "(\\d+/\\d+/\\d+)-([A-Za-z0-9@ ]+):\"([A-Za-z0-9@,?!. ]+)\";";

    public static void main(String[] args) {
        String string = sc.nextLine();
        String date;
        String stringSender;
        String stringRev = null;
        String tem;
        String[] senderAndRev;
        String[] yearMonthDay;
        int start;
        int end;
        int flag;
        Pattern pattern = Pattern.compile(regularPattern);
        while (!string.equals("END_OF_MESSAGE")) {
            Matcher matcher = pattern.matcher(string);
            while (matcher.find()) {
                flag = 0;
                yearMonthDay = matcher.group(1).split("/");
                date = getDate(yearMonthDay);
                tem = matcher.group(2);
                if (tem.contains("@")) {
                    senderAndRev = tem.split("@");
                    stringSender = senderAndRev[0];
                    stringRev = senderAndRev[1].trim();
                    flag = 1;
                } else {
                    stringSender = tem;
                    if (matcher.group(3).contains("@")) {
                        start = matcher.group(3).indexOf("@");
                        end = matcher.group(3).indexOf(" ", start);
                        stringRev = matcher.group(3).substring(start + 1, end);
                        flag = 1;
                    }
                }
                storeDate(date, matcher.group(0));
                storeSender(stringSender, matcher.group(0));
                storeRev(stringRev, matcher.group(0), flag);
            }
            string = sc.nextLine();
        }
        printf();
    }

    private static String getDate(String[] yearMonthDay) {
        String year = Integer.toString(Integer.parseInt(yearMonthDay[0]));
        String month = Integer.toString(Integer.parseInt(yearMonthDay[1]));
        String day = Integer.toString(Integer.parseInt(yearMonthDay[2]));
        return year + "/" + month + "/" + day;
    }

    private static void storeRev(String stringRev, String sentence, int flag) {
        int flagRev = 0;
        for (HashMap.Entry<String, ArrayList<String>> entry : resultRev.entrySet()) {
            if (flag == 1 && entry.getKey().equals(stringRev)) { //这个句子有rev
                entry.getValue().add(sentence);
                flagRev = 1;
            }
        }
        if (flag == 1 && flagRev == 0) {
            ArrayList<String> newRevString = new ArrayList<>();
            newRevString.add(sentence);
            resultRev.put(stringRev, newRevString);
        }
    }

    private static void storeSender(String stringSender, String sentence) {
        int flagSender = 0;
        for (HashMap.Entry<String, ArrayList<String>> entry : resultSender.entrySet()) {
            if (entry.getKey().equals(stringSender)) {
                entry.getValue().add(sentence);
                flagSender = 1;
            }
        }
        if (flagSender == 0) {
            ArrayList<String> newSenderString = new ArrayList<>();
            newSenderString.add(sentence);
            resultSender.put(stringSender, newSenderString);
        }
    }

    public static void storeDate(String date, String sentence) {
        int flagDate = 0;
        for (HashMap.Entry<String, ArrayList<String>> entry : resultDate.entrySet()) {
            if (entry.getKey().equals(date)) {
                entry.getValue().add(sentence);
                flagDate = 1;
            }
        }
        if (flagDate == 0) {
            ArrayList<String> newDateString = new ArrayList<>();
            newDateString.add(sentence);
            resultDate.put(date, newDateString);
        }
    }

    public static void printf() {
        String[] commandAndReq;
        String[] yearMonthDay;
        while (sc.hasNext()) {
            String command = sc.nextLine();
            commandAndReq = command.split(" ");    //可能出错
            command = commandAndReq[0];
            if (command.equals("qdate")) {
                for (HashMap.Entry<String, ArrayList<String>> entry : resultDate.entrySet()) {
                    yearMonthDay = commandAndReq[1].split("/");
                    if (getDate(yearMonthDay).equals(entry.getKey())) {
                        for (String item : entry.getValue()) {
                            System.out.println(item);
                        }
                    }
                }
            } else if (command.equals("qsend")) {
                commandAndReq[1] = commandAndReq[1].substring(1, commandAndReq[1].length() - 1);
                for (HashMap.Entry<String, ArrayList<String>> entry : resultSender.entrySet()) {
                    if (commandAndReq[1].equals(entry.getKey())) {
                        for (String item : entry.getValue()) {
                            System.out.println(item);
                        }
                    }
                }
            } else if (command.equals("qrecv")) {
                commandAndReq[1] = commandAndReq[1].substring(1, commandAndReq[1].length() - 1);
                for (HashMap.Entry<String, ArrayList<String>> entry : resultRev.entrySet()) {
                    if (commandAndReq[1].equals(entry.getKey())) {
                        for (String item : entry.getValue()) {
                            System.out.println(item);
                        }
                    }
                }
            }
        }
    }
    //思路 date比较时拆成三个数 若有的数都相等 便打印
    //sen rev 先看有无-v 设flag 模糊查询用string.contains方法返回true 便打印
}

