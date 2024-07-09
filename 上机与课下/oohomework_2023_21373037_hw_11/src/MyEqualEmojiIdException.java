import com.oocourse.spec3.exceptions.EqualEmojiIdException;

import java.util.HashMap;

public class MyEqualEmojiIdException extends EqualEmojiIdException {
    private static int count = 0;
    private static HashMap<Integer, Integer> emojiCnt = new HashMap<>();
    private final int id;

    public MyEqualEmojiIdException(int id) {
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
        System.out.println("eei-" + count + ", " + id + "-" + emojiCnt.get(id));
    }
}
