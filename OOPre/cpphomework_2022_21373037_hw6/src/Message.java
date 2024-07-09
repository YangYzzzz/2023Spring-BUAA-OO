package src;

public class Message {
    private int year;
    private int month;
    private int day;
    private String sender;
    private String rev;
    private String others;
    private int flag;
    private String dialogue;

    public Message(int year, int month, int day, String sender, String rev,
                   String others, int flag, String dialogue) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.sender = sender;
        this.rev = rev;
        this.others = others;
        this.flag = flag;
        this.dialogue = dialogue;
    }

    public String getDialogue() {
        return dialogue;
    }

    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
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

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
