import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BingChaJi implements Network {
    private final ArrayList<Person> people;
    private final ArrayList<Group> groups;
    private final ArrayList<Message> messages;
    private int graphNum;
    private int triNum;
    private HashMap<Integer, ArrayList<Integer>> adjMatrix;

    public BingChaJi() {
        this.people = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.graphNum = 0;
        this.triNum = 0;
        this.adjMatrix = new HashMap<>();
    }

    public boolean contains(int id) {
        for (Person it : people) {
            if (it.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public void addPersonMatrix(int id) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        adjMatrix.put(id, arrayList);
    }

    public void addRelationMatrix(int id1, int id2) {
        adjMatrix.get(id1).add(id2);
        adjMatrix.get(id2).add(id1);
    }

    public void removeRelationMatrix(int id1, int id2) {
        adjMatrix.get(id1).remove((Object) id2);
        adjMatrix.get(id2).remove((Object) id1);
    }

    public Person getFatherNode(MyPerson person) {  //不知道是否有问题
        if (person.getFather().equals(person)) {
            return person;
        }
        return getFatherNode((MyPerson) person.getFather());
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

    public void addPerson(Person person) throws EqualPersonIdException {
        for (Person it : people) {
            if (person.equals(it)) {
                throw new MyEqualPersonIdException(person.getId());
            }
        }
        people.add(person);
        addPersonMatrix(person.getId());
        this.graphNum++;
    }

    public void updateTri(int type, MyPerson person1, MyPerson person2) {
        for (Map.Entry<Person, Integer> entry : person1.getAcquaintance().entrySet()) {
            if (entry.getKey().queryValue(person2) != 0) {
                if (type == 0) {
                    this.triNum++;
                } else {
                    this.triNum--;
                }
            }
        }
    }

    public void updateBestFriend(MyPerson person1, MyPerson person2, int value) { //添加的时候用包括ar和mr的正值
        if (person1.getBestFriendValue() < value || (person1.
                getBestFriendValue() == value && person1.getBestFriendId() > person2.getId())) {
            person1.setBestFriendId(person2.getId());
            person1.setBestFriendValue(value);
        }
    }

    public void updateBestFriend(MyPerson person) { //删除的时候用
        int id = queryBestAcquaintanceId(person.getId());
        person.setBestFriendId(id);
        person.setBestFriendValue(person.getAcquaintance().get(getPerson(id)));
    }

    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            MyPerson person1 = (MyPerson) getPerson(id1);
            MyPerson person2 = (MyPerson) getPerson(id2);
            person1.addAcquaintance(person2, value);
            person2.addAcquaintance(person1, value);
            if (!getFatherNode(person1).equals(getFatherNode(person2))) { //很重要 博客重点写
                graphNum--;
                ((MyPerson) getFatherNode(person1)).setFather(person2);
            }
            updateTri(0, person1, person2);
            addRelationMatrix(person1.getId(), person2.getId());
            updateBestFriend(person1, person2, value);
            updateBestFriend(person2, person1, value);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        }
    }

    private void removeRelation(int id, MyPerson person) {
        for (Map.Entry<Person, Integer> entry : person.getAcquaintance().entrySet()) {
            if (entry.getKey().getId() == id) {
                person.getAcquaintance().entrySet().remove(entry);
                break;
            }
        }
        if (person.getAcquaintance().size() != 0) {
            updateBestFriend(person);
        } else {
            person.setBestFriendValue(0);
            person.setBestFriendId(0);
        }
    }

    public void modifyRelation(int id1, int id2, int value) throws PersonIdNotFoundException,
            EqualPersonIdException, RelationNotFoundException {
        if (contains(id1) && contains(id2) && id1 != id2 && getPerson(id1).isLinked(getPerson(id2))
                && getPerson(id1).queryValue(getPerson(id2)) + value > 0) {
            MyPerson person1 = (MyPerson) getPerson(id1);
            MyPerson person2 = (MyPerson) getPerson(id2);
            for (Map.Entry<Person, Integer> entry : person1.getAcquaintance().entrySet()) {
                if (entry.getKey().getId() == id2) {
                    entry.setValue(entry.getValue() + value);
                    updateBestFriend(person1);
                }
            }
            for (Map.Entry<Person, Integer> entry : person2.getAcquaintance().entrySet()) {
                if (entry.getKey().getId() == id1) {
                    entry.setValue(entry.getValue() + value);
                    updateBestFriend(person2);
                }
            }

        } else if (contains(id1) && contains(id2) && id1 != id2 && getPerson(id1).isLinked(
                getPerson(id2)) && getPerson(id1).queryValue(getPerson(id2)) + value <= 0) {
            MyPerson person1 = (MyPerson) getPerson(id1);
            MyPerson person2 = (MyPerson) getPerson(id2);
            removeRelation(id2, person1); //这两步维护最好伙伴关系
            removeRelation(id1, person2);
            updateTri(1, person1, person2); //更新三人组
            //并查集删除边 不一定会影响连通性 需要bfs？ 删除邻接矩阵
            removeRelationMatrix(person1.getId(), person2.getId());
            //判断两个结点与祖先节点的连通性 改变并查集
            if (!bfs(person1.getId(), getFatherNode(person1).getId())) {
                person1.setFather(person1);
                graphNum++;
            }
            if (!bfs(person2.getId(), getFatherNode(person2).getId())) {
                person2.setFather(person2);
                graphNum++;
            }
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (id1 == id2) {
            throw new MyEqualPersonIdException(id1);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
    }

    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2))) {
            return getPerson(id1).queryValue(getPerson(id2));
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2); //此时id1一定不等于id2
        }
        return -1; //无意义
    }

    public boolean bfs(int start, int end) {  //start end用PersonId即可
        HashMap<Integer, Boolean> flag = new HashMap<>();
        for (Person person : people) {
            flag.put(person.getId(), false);
        }
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        flag.put(start, true);
        while (!queue.isEmpty()) {
            //取出队首元素top
            int s = queue.remove();
            if (s == end) {
                return true;
            }
            for (Integer id : adjMatrix.get(s)) {
                if (!flag.get(id)) {
                    queue.add(id);
                    flag.put(id, true);
                }
            }
        }
        return false;
    }

    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException { //从id1到id2找一条路径出来即可
        if (contains(id1) && contains(id2)) {
            MyPerson person1 = (MyPerson) getPerson(id1);
            MyPerson person2 = (MyPerson) getPerson(id2);
            //System.out.println(getFatherNode(person1).getId());
            //System.out.println(getFatherNode(person2).getId());
            return bfs(person1.getId(), person2.getId()); //不得不使用bfs辣
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return false; //无意义
    }

    public int queryBlockSum() {
        return graphNum;
    }

    public int queryTripleSum() {
        return triNum;
    }

    public void addGroup(Group group) throws EqualGroupIdException {
        for (Group it : groups) {
            if (it.equals(group)) {
                throw new MyEqualGroupIdException(group.getId());
            }
        }
        groups.add(group);
    }

    public Group getGroup(int id) {
        for (Group group : groups) {
            if (group.getId() == id) {
                return group;
            }
        }
        return null;
    }

    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException { //id1是person，id2是group 优先group异常
        if (getGroup(id2) != null && getPerson(id1) != null &&
                !getGroup(id2).hasPerson(getPerson(id1))) {
            getGroup(id2).addPerson(getPerson(id1));
            ((MyGroup)getGroup(id2)).addAgeSum(getPerson(id1).getAge());
        } else if (getGroup(id2) == null) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (getPerson(id1) == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
    }

    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (getGroup(id) != null) {
            return getGroup(id).getValueSum();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (getGroup(id) != null) {
            return getGroup(id).getAgeVar();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (getGroup(id2) != null && getPerson(id1) != null &&
                getGroup(id2).hasPerson(getPerson(id1))) {
            getGroup(id2).delPerson(getPerson(id1));
            ((MyGroup)getGroup(id2)).subAgeSum(getPerson(id1).getAge());
        } else if (getGroup(id2) == null) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (getPerson(id1) == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
    }

    public boolean containsMessage(int id) {
        for (Message message : messages) {
            if (message.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public void addMessage(Message message) throws
            EqualMessageIdException, EqualPersonIdException {
        if (!containsMessage(message.getId()) && ((message.getType() == 0 &&
                message.getPerson1() != message.getPerson2()) || message.getType() == 1)) {
            messages.add(message);
        } else if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message.getType() == 0 && message.getPerson1() == message.getPerson2()) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
    }

    public Message getMessage(int id) {
        if (containsMessage(id)) {
            for (Message message : messages) {
                if (message.getId() == id) {
                    return message;
                }
            }
        } else {
            return null;
        }
        return null;
    }

    public void sendMessage(int id) throws //id为信息id
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        if (containsMessage(id) && getMessage(id).getType() == 0 &&
                getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()) &&
                getMessage(id).getPerson1() != getMessage(id).getPerson2()) {
            ((MyPerson) getMessage(id).getPerson1()).setSocialValue(getMessage(id).
                    getPerson1().getSocialValue() + getMessage(id).getSocialValue());
            ((MyPerson) getMessage(id).getPerson2()).setSocialValue(getMessage(id).
                    getPerson2().getSocialValue() + getMessage(id).getSocialValue());
            getMessage(id).getPerson2().getMessages().add(0, getMessage(id)); //会永久remove么？？
            messages.remove(getMessage(id));
        } else if (containsMessage(id) && getMessage(id).getType() == 1 && getMessage(id)
                .getGroup().hasPerson(getMessage(id).getPerson1())) { //消息的组里面包含着发送信息的人 才允许发送1
            for (Person person : people) {
                if (getMessage(id).getGroup().hasPerson(person)) {
                    ((MyPerson) person).setSocialValue(person.getSocialValue()
                            + getMessage(id).getSocialValue());
                }
            }
            messages.remove(getMessage(id));
        } else if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        } else if (getMessage(id).getType() == 0 && !(getMessage(id).getPerson1().
                isLinked(getMessage(id).getPerson2()))) {
            throw new MyRelationNotFoundException(getMessage(id).getPerson1().getId(),
                    getMessage(id).getPerson2().getId());
        } else if (getMessage(id).getType() == 1 && !(getMessage(id).getGroup().
                hasPerson(getMessage(id).getPerson1()))) {
            throw new MyPersonIdNotFoundException(getMessage(id).getPerson1().getId());
        }
    }

    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getSocialValue();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getReceivedMessages();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    public int queryBestAcquaintanceId(int id) {
        int personId = -1;
        int maxValue = 0;
        HashMap<Person, Integer> acquaintance = ((MyPerson) getPerson(id)).getAcquaintance();
        for (Map.Entry<Person, Integer> entry : acquaintance.entrySet()) {
            if (entry.getValue() == maxValue && entry.getKey().getId() < personId) {
                personId = entry.getKey().getId();
            } else if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
                personId = entry.getKey().getId();
            }
        }
        return personId;
    }
    public int queryBestAcquaintance(int id) throws
            PersonIdNotFoundException, AcquaintanceNotFoundException {
        HashMap<Person, Integer> acquaintance = ((MyPerson) getPerson(id)).getAcquaintance();
        if (contains(id) && acquaintance.size() != 0) { //找最大value里面id最小的 返回PersonId
            return ((MyPerson) getPerson(id)).getBestFriendId();
        } else if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            throw new MyAcquaintanceNotFoundException(id);
        }
    }

    public int queryCoupleSum() {
        int cnt = 0;
        for (Person person : people) {
            MyPerson person1 = (MyPerson) person;
            MyPerson person2 = (MyPerson) getPerson(person1.getBestFriendId());
            if (person1.getBestFriendValue() > 0 && person2.getBestFriendValue() > 0
                    && person1.getId() == person2.getBestFriendId()) {
                cnt++;
            }
        }
        return cnt / 2;
    }

    public boolean ok20and21(HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                             HashMap<Integer, HashMap<Integer, Integer>> afterData, int id) {
        HashMap<Integer, Integer> hashMap1 = beforeData.get(id);
        HashMap<Integer, Integer> hashMap2 = afterData.get(id); //after里面有的 -> before一定全有
        for (Map.Entry<Integer, Integer> entry : hashMap2.entrySet()) {
            if (!hashMap1.containsKey(entry.getKey()) ||
                    !hashMap1.get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    public int ok2and3(HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                       HashMap<Integer, HashMap<Integer, Integer>> afterData, int id1, int id2) {
        for (Integer integer : beforeData.keySet()) {
            if (!afterData.containsKey(integer)) {
                return 2;
            }
            if (integer != id1 && integer != id2 &&
                    !beforeData.get(integer).equals(afterData.get(integer))) {
                return 3;
            }
        }
        return 0;
    }

    public boolean ok11and12(HashMap<Integer, HashMap<Integer, Integer>> before,
                             HashMap<Integer, HashMap<Integer, Integer>> after, int id1, int id2) {
        for (Integer i : after.get(id1).keySet()) {
            if (i != id2 && !after.get(id1).get(i).equals(before.get(id1).get(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean ok13and14and18and19(HashMap<Integer, HashMap<Integer, Integer>> data, int id) {
        int keyNum = 0;
        int valueNum = 0;
        for (Map.Entry<Integer, Integer> entry : data.get(id).entrySet()) {
            if (entry.getKey() != null) {
                keyNum++;
            }
            if (entry.getValue() != null) {
                valueNum++;
            }
        }
        return keyNum == valueNum;
    }

    public int modifyRelationOKTest(int id1, int id2, int value, HashMap<Integer, HashMap<Integer,
            Integer>> beforeData, HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        //副作用 改动内层hashmap的value 可能
        int tmp;
        if ((!beforeData.containsKey(id1) || !beforeData.containsKey(id2) || id1 == id2
                || !beforeData.get(id1).containsKey(id2)) && !beforeData.equals(afterData)) {
            return -1;
        } else if ((!beforeData.containsKey(id1) || !beforeData.containsKey(id2) || id1 == id2
                || !beforeData.get(id1).containsKey(id2)) && beforeData.equals(afterData)) {
            return 0;
        } else if (beforeData.size() != afterData.size()) {
            return 1;
        } else if ((tmp = ok2and3(beforeData, afterData, id1, id2)) != 0) {
            return tmp; //包含2和3
        } else if (beforeData.get(id1).get(id2) + value > 0) {
            if (!afterData.get(id1).containsKey(id2) || !afterData.get(id2).containsKey(id1)) {
                return 4;
            } else if (!(afterData.get(id1).get(id2) == beforeData.get(id1).get(id2) + value)) {
                return 5;
            } else if (!(afterData.get(id2).get(id1) == beforeData.get(id2).get(id1) + value)) {
                return 6;
            } else if (!(afterData.get(id1).size() == beforeData.get(id1).size())) {
                return 7;
            } else if (!(afterData.get(id2).size() == beforeData.get(id2).size())) {
                return 8;
            } else if (!afterData.get(id1).keySet().equals(beforeData.get(id1).keySet())) {
                return 9; //包含9，10
            } else if (!afterData.get(id2).keySet().equals(beforeData.get(id2).keySet())) {
                return 10;
            } else if (!ok11and12(beforeData, afterData, id1, id2)) {
                return 11;
            } else if (!ok11and12(beforeData, afterData, id2, id1)) {
                return 12;
            } else if (!ok13and14and18and19(afterData, id1)) { //默认1314一定对
                return 13;
            } else if (!ok13and14and18and19(afterData, id2)) {
                return 14;
            } else {
                return 0;
            }
        } else {
            if (afterData.get(id1).containsKey(id2) || afterData.get(id2).containsKey(id1)) {
                return 15;
            } else if (beforeData.get(id1).size() != afterData.get(id1).size() + 1) {
                return 16;
            } else if (beforeData.get(id2).size() != afterData.get(id2).size() + 1) {
                return 17;
            } else if (!ok13and14and18and19(afterData, id1)) { //默认1314一定对
                return 18;
            } else if (!ok13and14and18and19(afterData, id2)) {
                return 19;
            } else if (!ok20and21(beforeData, afterData, id1)) {
                return 20;
            } else if (!ok20and21(beforeData, afterData, id2)) {
                return 21;
            } else {
                return 0;
            }
        }
    }
}

