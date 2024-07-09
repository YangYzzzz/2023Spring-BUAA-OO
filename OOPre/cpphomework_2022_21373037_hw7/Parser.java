public class Parser {
    private Lexer lexer;
    private String name;
    private String emojiId;
    private int count;

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmojiId(String emojiId) {
        this.emojiId = emojiId;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Parser(Lexer lexer, String name, String emojiId, int count) {
        this.lexer = lexer;
        this.name = name;
        this.emojiId = emojiId;
        this.count = count;
    }

    public Parser() {
    }

    // parseObject专门解析并返回json对象
    public void parseObject(JsonMessage jsonMessage) {
        // 1、当前 Lexer 读到的是左大括号
        // 2、循环读取 Lexer 的下一个词法
        //        若是右大括号，则停止循环
        //         若不是，则调用parseAttribute
        // 3、返回一个json对象
        while (!lexer.getNow().equals("}")) {
            lexer.read(); //读一个
            if (lexer.getNow().equals("}")) {
                break;
            }
            parseAttribute(jsonMessage);
        }
    }

    //parseAttribute专门解析并返回一个属性对象
    // 1、当前 Lexer 读到的是属性的名称
    // 2、分类讨论 Lexer 之后读取的内容
    //        若是字符串，则创建字符串属性对象
    //        若是数字，则创建数字属性对象
    //        ...
    //        若是左大括号，则调用parseObject读取json对象作为属性
    //        若是左中括号，则调用parseArray读取数组对象作为属性
    // 3、返回一个属性对象

    public void parseAttribute(JsonMessage jsonMessage) {
        String now = lexer.getNow();
        lexer.read();
        //System.out.println(now + " " + lexer.getNow());
        switch (now) {
            case "download_datetime":
                //lexer.read();
                jsonMessage.setDownloadDatetime(lexer.getNow());
                break;
            case "created_at":
                //lexer.read();
                jsonMessage.setCreatedAt(lexer.getNow());
                break;
            case "id":
                //lexer.read();
                jsonMessage.setId(lexer.getNow());
                break;
            case "full_text":
                //lexer.read();
                if (lexer.getNow().equals("null") && lexer.getContentFlag() == 1) {
                    jsonMessage.setFullText("None");  //若空 输出none
                } else {
                    jsonMessage.setFullText(lexer.getNow());
                }
                break;
            case "user_id":
                //lexer.read();
                jsonMessage.setUserId(lexer.getNow());
                break;
            case "retweet_count":
                //lexer.read();
                jsonMessage.setRetweetCount(Integer.parseInt(lexer.getNow()));
                break;
            case "favorite_count":
                //lexer.read();
                jsonMessage.setFavoriteCount(Integer.parseInt(lexer.getNow()));
                break;
            case "reply_count":
                //lexer.read();
                jsonMessage.setReplyCount(Integer.parseInt(lexer.getNow()));
                break;
            case "possibly_sensitive_editable":
                //lexer.read();
                if (lexer.getNow().equals("true")) {
                    jsonMessage.setPossiblySensitiveEditable(true);
                }
                break;
            case "lang":
                //lexer.read();
                jsonMessage.setLang(lexer.getNow());
                break;
            case "emojis":
                //lexer.read();
                parseArray(jsonMessage);
                break;
            case "name":
                //lexer.read();
                name = lexer.getNow();
                break;
            case "emoji_id":
                //lexer.read();
                emojiId = lexer.getNow();
                break;
            case "count":
                //lexer.read();
                count = Integer.parseInt(lexer.getNow());
                break;
            case "{":
                parseObject(jsonMessage);
                break;
            default:
                break;
        }
    }

    // parseArray专门解析并返回数组对象
    public void parseArray(JsonMessage jsonMessage) {
        // 1、当前 Lexer 读到的是左方括号
        // 2、循环读取 Lexer 的下一个词法
        //        若是右方括号，则停止循环
        //        若不是，则一定是json对象/数字/字符串
        //        小tip：java中可以用Object表示任意类型
        // 3、返回一个数组对象
        while (lexer.getNow() != "]") {
            lexer.read();   //两种情况 读取到{ 或 】
            if (lexer.getNow() == "{") {
                parseObject(jsonMessage);
                Emojis emojis = new Emojis();
                emojis.setName(name);
                emojis.setCount(count);
                emojis.setEmojiId(emojiId);
                jsonMessage.getEmojisArrayList().add(emojis);
            } else {
                if (lexer.getNow() == "]") {
                    break;
                }
            }
        }
    }
}
