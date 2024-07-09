
import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyNetwork implements Network {
    private ArrayList<Person> people;
    private int graphNum;
    private int triNum;

    public MyNetwork() {
        this.people = new ArrayList<>();
        graphNum = 0;
        triNum = 0;
    }

    public boolean contains(int id) {
        for (Person it : people) {
            if (it.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public Person getPerson(int id) {
        if (contains(id)) {
            for (Person it : people) {
                if (it.getId() == id) {
                    return it;
                }
            }
        }
        return null;
    }

    public Person getFatherNode(MyPerson person) {  //不知道是否有问题
        if (person.getFather().equals(person)) {
            return person;
        }
        return getFatherNode((MyPerson) person.getFather());
    }

    public void addPerson(Person person) throws EqualPersonIdException {
        for (Person value : people) {
            if (person.equals(value)) {
                throw new MyEqualPersonIdException(person.getId());
            }
        }
        people.add(person);
        this.graphNum++;
    }

    public void updateTri(MyPerson person1, MyPerson person2) {
        //新增1 2的关系，新增的三人组必然包括1和2，只需找到有多少个第三人即可
        //第三个人一定认识1和2
        for (int i = 0; i < person1.getAcquaintance().size(); i++) {
            if (person1.getAcquaintance().get(i).queryValue(person2) != 0) {
                this.triNum++;
            }
        }
    }

    public void addRelation(int id1, int id2, int value) throws  //为两个人相互添加关系
            PersonIdNotFoundException, EqualRelationException {
        if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            MyPerson person1 = (MyPerson) getPerson(id1);
            MyPerson person2 = (MyPerson) getPerson(id2);
            person1.getAcquaintance().add(person2);
            person1.getValue().add(value);
            person2.getAcquaintance().add(person1);
            person2.getValue().add(value);
            if (!getFatherNode(person1).equals(getFatherNode(person2))) {
                graphNum--;
                ((MyPerson)getFatherNode(person1)).setFather(person2);
            }
            updateTri(person1, person2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        }
    }

    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2))) {
            return getPerson(id1).queryValue(getPerson(id2));
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2); //此时id1一定不等于id2
        }
        return -1; //无意义
    }

    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException { //从id1到id2找一条路径出来即可
        if (contains(id1) && contains(id2)) {
            MyPerson person1 = (MyPerson) getPerson(id1);
            MyPerson person2 = (MyPerson) getPerson(id2);
            return getFatherNode(person1).equals(getFatherNode(person2));
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return false; //无意义
    }

    public int queryBlockSum() {  //计算有多少个连通子图！！！
        return graphNum;
    }

    public int queryTripleSum() {
        return triNum;
    }

    public boolean okTestIsLinked(HashMap<Integer, Integer> map1, int id) {
        for (HashMap.Entry<Integer, Integer> entry : map1.entrySet()) {
            if (entry.getKey() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean okTestIsHashmapSame(HashMap<Integer, Integer> map1,
                                       HashMap<Integer, Integer> map2) {
        HashMap<Integer, Integer> clone1 = new HashMap<>(map1);
        HashMap<Integer, Integer> clone2 = new HashMap<>(map2);
        if (clone1.keySet().size() == clone2.keySet().size()) {
            Iterator<Map.Entry<Integer, Integer>> it1 = clone1.entrySet().iterator();
            while (it1.hasNext()) {
                Map.Entry<Integer, Integer> entry1 = it1.next();
                Iterator<Map.Entry<Integer, Integer>> it2 = clone2.entrySet().iterator();
                while (it2.hasNext()) {
                    Map.Entry<Integer, Integer> entry2 = it2.next();
                    if (entry1.getKey().equals(entry2.getKey()) &&
                            entry1.getValue().equals(entry2.getValue())) {
                        it1.remove();
                        it2.remove();
                        break;
                    }
                }
            }
            return clone1.size() == 0 && clone2.size() == 0;
        } else {
            return false;
        }
    }

    public boolean isDiff(int id1, int id2, int id3, int[][] ans, int len) {
        int[] re = new int[3];
        re[0] = id1;
        re[1] = id2;
        re[2] = id3;
        Arrays.sort(re);
        for (int i = 0; i < len; i++) {
            if (ans[i][0] == re[0] && ans[i][1] == re[1] && ans[i][2] == re[2]) {
                return false;
            }
        }
        ans[len][0] = re[0];
        ans[len][1] = re[1];
        ans[len][2] = re[2];
        return true;
    }

    public boolean queryTripleSumOKTest(HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                        HashMap<Integer, HashMap<Integer, Integer>> afterData,
                                        int result) {
        //1 要检查before和after“状态” 2 要检查结果是否正确“结果” hashmap是无序的 怎么令他三重遍历呢
        boolean isStatusMatch; //三人组
        int[][] ans = new int[1000][3];
        int cnt = 0;
        for (Integer id1 : beforeData.keySet()) {  //人 熟人们 和他们的value
            HashMap<Integer, Integer> map1 = beforeData.get(id1);  //id1人 map<熟人， value>
            for (Integer id2 : beforeData.keySet()) {
                HashMap<Integer, Integer> map2 = beforeData.get(id2);
                for (Integer id3 : beforeData.keySet()) {
                    HashMap<Integer, Integer> map3 = beforeData.get(id3);
                    if (!id1.equals(id2) && !id2.equals(id3) && !id3.equals(id1)) {
                        if (okTestIsLinked(map1, id2) && okTestIsLinked(map2, id3)
                                && okTestIsLinked(map3, id1)) {  //可能会计算重复的id 我测！
                            if (isDiff(id1, id2, id3, ans, cnt)) {
                                cnt++;
                            }
                        }
                    }
                }
            }
        }

        HashMap<Integer, HashMap<Integer, Integer>> clone1 = new HashMap<>(beforeData);
        HashMap<Integer, HashMap<Integer, Integer>> clone2 = new HashMap<>(afterData);
        if (clone1.size() == clone2.size()) {
            Iterator<Map.Entry<Integer, HashMap<Integer, Integer>>> it1 =
                    clone1.entrySet().iterator();
            while (it1.hasNext()) {
                Map.Entry<Integer, HashMap<Integer, Integer>> entry1 = it1.next();
                int id1 = entry1.getKey();
                HashMap<Integer, Integer> map1 = entry1.getValue();
                Iterator<Map.Entry<Integer, HashMap<Integer, Integer>>> it2 =
                        clone2.entrySet().iterator();
                while (it2.hasNext()) {
                    Map.Entry<Integer, HashMap<Integer, Integer>> entry2 = it2.next();
                    int id2 = entry2.getKey();
                    HashMap<Integer, Integer> map2 = entry2.getValue();
                    if (id1 == id2) {
                        if (okTestIsHashmapSame(map1, map2)) {
                            it1.remove();
                            it2.remove();
                            break;
                        } else {
                            return false;
                        }
                    }
                }
            }
            isStatusMatch = clone1.size() == 0 && clone2.size() == 0;
        } else {
            isStatusMatch = false;
        }
        return cnt == result && isStatusMatch;
    }
}

