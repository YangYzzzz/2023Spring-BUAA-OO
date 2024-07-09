import com.oocourse.spec2.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException { //rnf-x, id1-y, id2-z
    private static int count = 0;
    private static HashMap<Integer, Integer> personCnt = new HashMap<>();
    private final int min;
    private final int max;

    public MyRelationNotFoundException(int id1, int id2) {
        if (id1 > id2) {
            this.min = id2;
            this.max = id1;
        } else {
            this.min = id1;
            this.max = id2;
        }
        count++;
        if (personCnt.containsKey(min)) {
            personCnt.put(min, personCnt.get(min) + 1);
        } else {
            personCnt.put(min, 1);
        }
        if (personCnt.containsKey(max)) {
            personCnt.put(max, personCnt.get(max) + 1);
        } else {
            personCnt.put(max, 1);
        }
    }

    @Override
    public void print() {
        System.out.println("rnf-" + count + ", " + min + "-" +
                personCnt.get(min) + ", " + max + "-" + personCnt.get(max));
    }
}