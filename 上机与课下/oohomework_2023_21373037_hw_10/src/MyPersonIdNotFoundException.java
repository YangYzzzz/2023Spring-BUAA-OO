import com.oocourse.spec2.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException { //pinf-x, id-y
    private static int count = 0;
    private static HashMap<Integer, Integer> personCnt = new HashMap<>();
    private final int id;

    public MyPersonIdNotFoundException(int id) {
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
        System.out.println("pinf-" + count + ", " + id + "-" + personCnt.get(id));
    }
}