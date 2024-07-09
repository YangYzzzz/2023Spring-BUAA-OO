import com.oocourse.spec3.exceptions.EqualPersonIdException;

import java.util.HashMap;

public class MyEqualPersonIdException extends EqualPersonIdException { //epi-x, id-y
    private static int count = 0;
    private static HashMap<Integer, Integer> personCnt = new HashMap<>();
    private final int id;

    public MyEqualPersonIdException(int id) {
        this.id = id;
        count++;
        if (personCnt.containsKey(id)) {
            personCnt.put(id, personCnt.get(id) + 1);
        } else {
            personCnt.put(id, 1);
        }
    }

    @Override
    public void print() {
        System.out.println("epi-" + count + ", " + id + "-" + personCnt.get(id));
    }
}
