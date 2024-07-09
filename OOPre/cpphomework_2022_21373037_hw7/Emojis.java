public class Emojis implements Comparable<Emojis> {
    private String name;
    private String emojiId;
    private int count;

    public Emojis() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmojiId() {
        return emojiId;
    }

    public void setEmojiId(String emojiId) {
        this.emojiId = emojiId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Emojis(String name, String emojiId, int count) {
        this.name = name;
        this.emojiId = emojiId;
        this.count = count;
    }

    @Override
    public int compareTo(Emojis o) {
        if (this.count > o.count) {
            return -1;
        } else if (this.count < o.count) {
            return 1;
        } else {
            if (this.name.compareTo(o.name) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
