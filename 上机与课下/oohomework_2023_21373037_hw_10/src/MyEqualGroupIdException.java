import com.oocourse.spec2.exceptions.EqualGroupIdException;

import java.util.HashMap;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static int count = 0;
    private static HashMap<Integer, Integer> groupCnt = new HashMap<>();
    private final int id;

    public MyEqualGroupIdException(int id) {
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
        System.out.println("egi-" + count + ", " + id + "-" + groupCnt.get(id));
    }
}
