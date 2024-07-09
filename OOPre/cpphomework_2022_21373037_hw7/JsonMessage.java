import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonMessage {
    private String objectType;
    private String downloadDate;
    private String createdAtDate;
    private String id;
    private String fullText;
    private String userId;
    private int retweetCount;
    private int favoriteCount;
    private int replyCount;
    private boolean possiblySensitiveEditable;
    private String lang;
    private ArrayList<Emojis> emojisArrayList; //记得新开

    public void setCreatedAt(String createdAt) {
        //Sat Dec 04 17:16:55 2021
        String[] date = createdAt.split(" ");
        StringBuilder dateStr = new StringBuilder();
        String month = null;
        dateStr.append(date[4] + "-");
        switch (date[1]) {
            case "Dec":
                month = "12";
                break;
            case "Nov":
                month = "11";
                break;
            case "Oct":
                month = "10";
                break;
            case "Sep":
                month = "09";
                break;
            case "Aug":
                month = "08";
                break;
            case "Jul":
                month = "07";
                break;
            case "Jun":
                month = "06";
                break;
            case "May":
                month = "05";
                break;
            case "Apr":
                month = "04";
                break;
            case "Mar":
                month = "03";
                break;
            case "Feb":
                month = "02";
                break;
            case "Jan":
                month = "01";
                break;
            default:
                break;
        }
        dateStr.append(month + "-" + date[2]);
        this.setCreatedAtDate(dateStr.toString());
        //要拆成数
    }

    public String getCreatedAtDate() {
        return createdAtDate;
    }

    public void setCreatedAtDate(String createdAtDate) {
        this.createdAtDate = createdAtDate;
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = downloadDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public boolean isPossiblySensitiveEditable() {
        return possiblySensitiveEditable;
    }

    public void setPossiblySensitiveEditable(boolean possiblySensitiveEditable) {
        this.possiblySensitiveEditable = possiblySensitiveEditable;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public ArrayList<Emojis> getEmojisArrayList() {
        return emojisArrayList;
    }

    public void setEmojisArrayList(ArrayList<Emojis> emojisArrayList) {
        this.emojisArrayList = emojisArrayList;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public void setDownloadDatetime(String downloadDatetime) {
        String date = "(\\d{4}-\\d{1,2}-\\d{1,2}) \\d{1,2}:\\d{1,2}";
        Pattern pattern = Pattern.compile(date);
        Matcher matcher = pattern.matcher(downloadDatetime);
        matcher.find();
        this.setDownloadDate(matcher.group(1));
    }
}
