import com.oocourse.spec2.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static int count = 0;
    private static HashMap<Integer, Integer> groupCnt = new HashMap<>();
    private final int id;

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        count++;
        if (groupCnt.containsKey(id)) {
            groupCnt.put(id, groupCnt.get(id) + 1);
        } else {
            groupCnt.put(id, 1);
        }
    }

    @Override
    public void print() {
        System.out.println("ginf-" + count + ", " + id + "-" + groupCnt.get(id));
    }
}
