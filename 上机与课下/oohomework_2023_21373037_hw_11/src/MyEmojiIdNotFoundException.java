import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

import java.util.HashMap;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private static int count = 0;
    private static HashMap<Integer, Integer> emojiCnt = new HashMap<>();
    private final int id;

    public MyEmojiIdNotFoundException(int id) {
        this.id = id;
        count++;
        if (emojiCnt.containsKey(id)) {
            emojiCnt.put(id, emojiCnt.get(id) + 1);
        } else {
            emojiCnt.put(id, 1);
        }
    }

    @Override
    public void print() {
        System.out.println("einf-" + count + ", " + id + "-" + emojiCnt.get(id));
    }
}
