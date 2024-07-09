import com.oocourse.spec3.exceptions.PathNotFoundException;
import java.util.HashMap;

public class MyPathNotFoundException extends PathNotFoundException {
    private static int count = 0;
    private static HashMap<Integer, Integer> personCnt = new HashMap<>();
    private final int id;

    public MyPathNotFoundException(int id) {
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
        System.out.println("pnf-" + count + ", " + id + "-" + personCnt.get(id));
    }
}
