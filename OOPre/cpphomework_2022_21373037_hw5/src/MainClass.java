package src;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    private static Scanner sc = new Scanner(System.in);
    private static ArrayList<Message> messages = new ArrayList<>();
    private static String regularPattern =
            "(\\d+/\\d+/\\d+)-([A-Za-z0-9@ ]+):\"([A-Za-z0-9@,?!. ]+)\";";
    private static String virtualDate = "(\\d*)/(\\d*)/(\\d*)";

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
        String year;
        String month;
        String day;
        Pattern pattern = Pattern.compile(regularPattern);
        while (!string.equals("END_OF_MESSAGE")) {
            Matcher matcher = pattern.matcher(string);
            while (matcher.find()) {
                flag = 0;
                date = matcher.group(1);
                yearMonthDay = date.split("/");
                year = getYear(yearMonthDay[0]);
                month = getMonth(yearMonthDay[1]);
                day = getDay(yearMonthDay[2]);
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
                Message message = new Message(year, month, day, stringSender,
                        stringRev, matcher.group(0), matcher.group(3), flag);
                messages.add(message);
            }
            string = sc.nextLine();
        }
        printf();
    }

    public static String getYear(String year) {
        String newYear;
        newYear = Integer.toString(Integer.parseInt(year));
        return newYear;
    }

    public static String getMonth(String month) {
        String newMonth;
        newMonth = Integer.toString(Integer.parseInt(month));
        return newMonth;
    }

    public static String getDay(String day) {
        String newDay;
        newDay = Integer.toString(Integer.parseInt(day));
        return newDay;
    }

    public static void dateOp(String date) {
        String yearVirtual;
        String monthVirtual;
        Pattern pattern = Pattern.compile(virtualDate);
        Matcher matcher = pattern.matcher(date);
        matcher.find();
        if (matcher.group(1).equals("")) {
            yearVirtual = null;
        } else {
            yearVirtual = getYear(matcher.group(1));
        }
        if (matcher.group(2).equals("")) {
            monthVirtual = null;
        } else {
            monthVirtual = getMonth(matcher.group(2));
        }
        String dayVirtual;
        if (matcher.group(3).equals("")) {
            dayVirtual = null;
        } else {
            dayVirtual = getDay(matcher.group(3));
        }
        String year;
        String month;
        String day;
        for (Message item : messages) {
            year = item.getYear();
            month = item.getMonth();
            day = item.getDay();
            if ((yearVirtual == null || year.equals(yearVirtual)) &&
                    (monthVirtual == null || month.equals(monthVirtual)) &&
                        (dayVirtual == null || day.equals(dayVirtual))) {
                System.out.println(item.getContent());
            }
        }
    }

    public static void printf() {
        String[] commandAndReq;
        int flagVirtual;
        while (sc.hasNext()) {
            flagVirtual = 0;
            String command = sc.nextLine();
            commandAndReq = command.split(" ");    //可能出错
            command = commandAndReq[0];
            if (commandAndReq[1].equals("-v")) {
                flagVirtual = 1;
            }
            if (command.equals("qdate")) {
                dateOp(commandAndReq[1]);
            } else if (command.equals("qsend")) {
                if (flagVirtual == 0) {
                    commandAndReq[1] = commandAndReq[1].substring(1, commandAndReq[1].length() - 1);
                    for (Message item : messages) {
                        if (commandAndReq[1].equals(item.getSender())) {
                            System.out.println(item.getContent());
                        }
                    }
                } else {
                    commandAndReq[2] = commandAndReq[2].substring(1, commandAndReq[2].length() - 1);
                    for (Message item : messages) {
                        if (item.getSender().contains(commandAndReq[2])) {
                            System.out.println(item.getContent());
                        }
                    }
                }
            } else if (command.equals("qrecv")) {
                if (flagVirtual == 0) {
                    commandAndReq[1] = commandAndReq[1].substring(1, commandAndReq[1].length() - 1);
                    for (Message item : messages) {
                        if (item.getFlag() == 1 && commandAndReq[1].equals(item.getRev())) {
                            System.out.println(item.getContent());
                        }
                    }
                } else {
                    commandAndReq[2] = commandAndReq[2].substring(1, commandAndReq[2].length() - 1);
                    for (Message item : messages) {
                        if (item.getFlag() == 1 && item.getRev().contains(commandAndReq[2])) {
                            System.out.println(item.getContent());
                        }
                    }
                }
            }
        }
    }
}
