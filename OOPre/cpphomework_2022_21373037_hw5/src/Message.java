package src;

public class Message {
    private String year;
    private String month;
    private String day;
    private String sender;
    private String rev;
    private String content;
    private String dialogue;
    private int flag;

    public String getDialogue() {
        return dialogue;
    }

    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Message(String year, String month, String day, String sender, String rev, String content, String dialogue, int flag) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.sender = sender;
        this.rev = rev;
        this.content = content;
        this.dialogue = dialogue;
        this.flag = flag;
    }
}
