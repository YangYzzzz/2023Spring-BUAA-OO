package src;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    private static Scanner sc = new Scanner(System.in);
    private static ArrayList<Message> messages = new ArrayList<>();
    private static String regularPattern =
            "((\\d+/\\d+/\\d+)-([A-Za-z0-9@ ]+):)(\"[A-Za-z0-9@,?!. ]+\";)";
    private static String virtualDate = "(\\d*)/(\\d*)/(\\d*)";

    //private static String requireForm =
    // "(qsend|qrecv)\s*(-v)*\s*(-pos|-pre|-ssq|-ssr)*\s*(\"\\w+\")\s*(-c)*\s*(\"\\w+\")";
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
        int year;
        int month;
        int day;
        Pattern pattern = Pattern.compile(regularPattern);
        while (!string.equals("END_OF_MESSAGE")) {
            Matcher matcher = pattern.matcher(string);
            while (matcher.find()) {
                flag = 0;
                date = matcher.group(2);
                yearMonthDay = date.split("/");
                year = getYear(yearMonthDay[0]);
                month = getMonth(yearMonthDay[1]);
                day = getDay(yearMonthDay[2]);
                tem = matcher.group(3);
                if (tem.contains("@")) {
                    senderAndRev = tem.split("@");
                    stringSender = senderAndRev[0];
                    stringRev = senderAndRev[1].trim();
                    flag = 1;
                } else {
                    stringSender = tem;
                    if (matcher.group(4).contains("@")) {
                        start = matcher.group(4).indexOf("@");
                        end = matcher.group(4).indexOf(" ", start);
                        stringRev = matcher.group(4).substring(start + 1, end);
                        flag = 1;
                    }
                }
                Message message = new Message(year, month, day, stringSender,
                        stringRev, matcher.group(1), flag, matcher.group(4));
                messages.add(message);
            }
            string = sc.nextLine();
        }
        printf();
    }

    public static int getYear(String year) {
        return Integer.parseInt(year);
    }

    public static int getMonth(String month) {
        return Integer.parseInt(month);
    }

    public static int getDay(String day) {
        return Integer.parseInt(day);
    }

    public static int isRun(int year) {
        if ((year == -1) || (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))) {
            return 1;
        } else {
            return 0;
        }
    }

    public static void isDate(int year, int month, int day) throws Exception {
        int flag = 0;
        int isRun = isRun(year);
        if (((month == 1 || month == 3 || month == 5 || month == 7 ||
                month == 8 || month == 10) && day > 31)) {
            flag = 1;
        }
        if ((month == 2 && isRun == 1 && day > 29) || (month == 2 && isRun == 0 && day > 28)) {
            flag = 1;
        }
        if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
            flag = 1;
        }
        if (month == 0 || day == 0 || month > 12 || (month == 12 && day > 31)) {
            flag = 1;
        }
        if (month == -1 && day > 31) {
            flag = 1;
        }
        if (flag == 1) {
            throw new Exception();
        }
    }

    public static void printf() {
        String[] commandAndReq;
        int yearVirtual = 0;
        int monthVirtual = 0;
        int dayVirtual = 0;
        while (sc.hasNext()) {
            String command = sc.nextLine();
            commandAndReq = command.split(" ");
            try {
                if (commandAndReq[1].equals("-ssq") || commandAndReq[1].equals("-ssr") ||
                        commandAndReq[1].equals("-pre") || commandAndReq[1].equals("-pos")) {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("Command Error!: Not Vague Query! \"" + command + "\"");
                continue;
            }
            if (commandAndReq[0].equals("qdate")) {
                try {
                    Pattern pattern = Pattern.compile(virtualDate);
                    Matcher matcher = pattern.matcher(commandAndReq[1]);
                    matcher.find();
                    if (matcher.group(1).equals("")) {
                        yearVirtual = -1;
                    } else {
                        yearVirtual = getYear(matcher.group(1));
                    }
                    if (matcher.group(2).equals("")) {
                        monthVirtual = -1;
                    } else {
                        monthVirtual = getMonth(matcher.group(2));
                    }
                    if (matcher.group(3).equals("")) {
                        dayVirtual = -1;
                    } else {
                        dayVirtual = getDay(matcher.group(3));
                    }
                    isDate(yearVirtual, monthVirtual, dayVirtual);
                } catch (Exception e) {
                    System.out.println("Command Error!: Wrong Date Format! \"" + command + "\"");
                    continue;
                }
            }
            split(commandAndReq, command, yearVirtual, monthVirtual, dayVirtual);
        }
    }

    public static void datePrint(String clean, int yearVirtual, int monthVirtual, int dayVirtual) {
        StringBuilder replace = new StringBuilder();
        int num;
        if (clean != null) {
            num = clean.length();
            for (int i = 0; i < num; i++) {
                replace.append("*");
            }
        }
        int year;
        int month;
        int day;
        for (Message item : messages) {
            year = item.getYear();
            month = item.getMonth();
            day = item.getDay();
            if ((yearVirtual == -1 || year == yearVirtual) &&
                    (monthVirtual == -1 || month == monthVirtual) &&
                    (dayVirtual == -1 || day == dayVirtual)) {
                print(item, clean, replace);
            }
        }
    }

    public static void split(String[] string, String command,
                             int yearVirtual, int monthVirtual, int dayVirtual) {
        String search;
        String clean = null;
        int index;
        int start;
        int end;
        int op = 0; //0为精确查找
        index = command.indexOf("-c");
        if (index != -1) {
            start = command.indexOf("\"", index);
            end = command.indexOf("\"", start + 1);
            clean = command.substring(start + 1, end);
        }
        if (string[1].equals("-v")) { //
            if (string[2].equals("-ssq")) {
                op = 1;
            }
            if (string[2].equals("-ssr")) {
                op = 2;
            }
            if (string[2].equals("-pre")) {
                op = 3;
            }
            if (string[2].equals("-pos")) {
                op = 4;
            }
            if (op == 0) {
                search = string[2].substring(1, string[2].length() - 1);
                op = 2;
            } else {
                search = string[3].substring(1, string[3].length() - 1);
            }
        } else {
            search = string[1].substring(1, string[1].length() - 1);
        }
        if (string[0].equals("qsend")) {
            sendPrint(search, op, clean);
        } else if (string[0].equals("qrecv")) {
            recPrint(search, op, clean);
        } else if (string[0].equals("qdate")) {
            datePrint(clean, yearVirtual, monthVirtual, dayVirtual);
        }
    }

    public static void recPrint(String search, int op, String clean) {
        StringBuilder replace = new StringBuilder();
        int num;
        if (clean != null) {
            num = clean.length();
            for (int i = 0; i < num; i++) {
                replace.append("*");
            }
        }
        switch (op) {
            case 0:
                for (Message item : messages) {
                    if (item.getFlag() == 1 && search.equals(item.getRev())) {
                        print(item, clean, replace);
                    }
                }
                break;
            case 2:
                for (Message item : messages) {
                    if (item.getFlag() == 1 && item.getRev().contains(search)) {
                        print(item, clean, replace);
                    }
                }
                break;
            case 1:
                for (Message item : messages) {
                    if (item.getFlag() == 1 && isSubsequence(item.getRev(), search)) {
                        print(item, clean, replace);
                    }
                }
                break;
            case 3: //前缀
                for (Message item : messages) {
                    if (item.getFlag() == 1 && item.getRev().startsWith(search)) {
                        print(item, clean, replace);
                    }
                }
                break;
            case 4:
                for (Message item : messages) {
                    if (item.getFlag() == 1 && item.getRev().endsWith(search)) {
                        print(item, clean, replace);
                    }
                }
                break;
            default:
                break;
        }
    }

    public static void sendPrint(String search, int op, String clean) {
        StringBuilder replace = new StringBuilder();
        int num;
        if (clean != null) {
            num = clean.length();
            for (int i = 0; i < num; i++) {
                replace.append("*");
            }
        }
        switch (op) {
            case 0:
                for (Message item : messages) {
                    if (search.equals(item.getSender())) {
                        print(item, clean, replace);
                    }
                }
                break;
            case 2: //ssr
                for (Message item : messages) {
                    if (item.getSender().contains(search)) {
                        print(item, clean, replace);
                    }
                }
                break;
            case 1: //ssq
                for (Message item : messages) {
                    if (isSubsequence(item.getSender(), search)) {
                        print(item, clean, replace);
                    }
                }
                break;
            case 3: //前缀
                for (Message item : messages) {
                    if (item.getSender().startsWith(search)) {
                        print(item, clean, replace);
                    }
                }
                break;
            case 4:
                for (Message item : messages) {
                    if (item.getSender().endsWith(search)) {
                        print(item, clean, replace);
                    }
                }
                break;
            default:
                break;
        }
    }

    public static void print(Message item, String clean, StringBuilder replace) {
        if (clean != null) {
            System.out.println(item.getOthers() + item.getDialogue().replace(clean, replace));
        } else {
            System.out.println(item.getOthers() + item.getDialogue());
        }
    }

    public static Boolean isSubsequence(String item, String search) {
        int index = -1;
        for (char a : search.toCharArray()) {
            index = item.indexOf(a, index + 1);
            if (index == -1) {
                return false;
            }
        }
        return true;
    }
}
//空格的问题