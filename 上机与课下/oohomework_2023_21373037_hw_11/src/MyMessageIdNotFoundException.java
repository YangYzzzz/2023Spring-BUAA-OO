import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

import java.util.HashMap;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private static int count = 0;
    private static HashMap<Integer, Integer> messageCnt = new HashMap<>();
    private final int id;

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        count++;
        if (messageCnt.containsKey(id)) {
            messageCnt.put(id, messageCnt.get(id) + 1);
        } else {
            messageCnt.put(id, 1);
        }
    }

    @Override
    public void print() {
        System.out.println("minf-" + count + ", " + id + "-" + messageCnt.get(id));
    }
}
