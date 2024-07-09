import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MainClass {
    private static Scanner sc = new Scanner(System.in);
    private static ArrayList<JsonMessage> jsonMessages = new ArrayList<>();

    public static void main(String[] args) {
        String data;
        String question;
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        while (!(data = sc.nextLine()).equals("END_OF_MESSAGE")) {
            JsonMessage jsonMessage = new JsonMessage();
            ArrayList<Emojis> emojis = new ArrayList<>();
            jsonMessage.setEmojisArrayList(emojis);
            lexer.setData(data);
            lexer.setNow(null);
            lexer.setPosition(0);
            parser.setLexer(lexer);
            parser.setName(null);
            parser.setCount(0);
            parser.setEmojiId(null);
            lexer.read(); //先把第一个{读入
            parser.parseObject(jsonMessage);  //对他进行第一步操作
            jsonMessages.add(jsonMessage);
            //分解
        }
        while (sc.hasNext()) {
            question = sc.nextLine();
            String[] str = question.split(" ");
            switch (str[0]) {
                case "Qdate":
                    analysis1(str);
                    break;
                case "Qemoji":
                    analysis2(str);
                    break;
                case "Qcount":
                    analysis3(str);
                    break;
                case "Qtext":
                    analysis4(str);
                    break;
                case "Qsensitive":
                    analysis5(str);
                    break;
                case "Qlang":
                    analysis6(str);
                    break;
                default:
                    break;
            }
        }
    }

    private static void analysis6(String[] str) {
        String id = str[1];
        for (JsonMessage item : jsonMessages) {
            if (id.equals(item.getId())) {
                System.out.println(item.getLang());
            }
        }
    }

    private static void analysis5(String[] str) {
        String userId = str[1];
        int cnt = 0;
        for (JsonMessage item : jsonMessages) {
            if (userId.equals(item.getUserId()) && item.isPossiblySensitiveEditable()) {
                cnt++;
            }
        }
        System.out.println(cnt);
    }

    private static void analysis4(String[] str) {
        String id = str[1];
        for (JsonMessage item : jsonMessages) {
            if (item.getId().equals(id)) {
                System.out.println(item.getFullText());   //可能会出现”null“情况
            }
        }
    }

    private static void analysis3(String[] str) {
        String[] startEnd = str[1].split("~");
        String startDate = startEnd[0];
        String endDate = startEnd[1];
        int cnt = 0;
        for (JsonMessage item : jsonMessages) {
            if (item.getDownloadDate().compareTo(startDate) >= 0
                    && item.getDownloadDate().compareTo(endDate) <= 0) {
                cnt++;
            }
        }
        System.out.println(cnt);
    }

    private static void analysis2(String[] str) {
        String id = str[1];
        StringBuilder emojiName = new StringBuilder();
        for (JsonMessage item : jsonMessages) {
            if (item.getId().equals(id)) {
                if (item.getEmojisArrayList().size() != 0) {
                    Collections.sort(item.getEmojisArrayList());
                    for (Emojis items : item.getEmojisArrayList()) {
                        emojiName.append(items.getName()).append(" ");
                    }
                } else {
                    System.out.println("None");
                    return;
                }
            }
        }
        System.out.println(emojiName);
    }

    public static void analysis1(String[] str) {
        String userId = str[1];
        String[] startEnd = str[2].split("~");
        String startDate = startEnd[0];
        String endDate = startEnd[1];
        int cnt = 0;
        int retweet = 0;
        int comment = 0;
        int good = 0;
        for (JsonMessage item : jsonMessages) {
            if (item.getUserId().equals(userId)) {
                if (item.getCreatedAtDate().compareTo(startDate) >= 0
                        && item.getCreatedAtDate().compareTo(endDate) <= 0) {
                    cnt++;
                    retweet += item.getRetweetCount();
                    comment += item.getReplyCount();
                    good += item.getFavoriteCount();
                }
            }
        }
        System.out.println(cnt + " " + retweet + " " + good + " " + comment);
    }
}
