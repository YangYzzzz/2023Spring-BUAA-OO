import com.oocourse.spec3.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyNetwork implements Network {
    private final ArrayList<Person> people;
    private final ArrayList<Group> groups;
    private final ArrayList<Message> messages;
    private final HashMap<Integer, Integer> emojiIdHeat;
    private int triNum;
    private int graphNum;
    private final Graph graph;

    public MyNetwork() {
        this.people = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.triNum = 0;
        this.graphNum = 0;
        this.emojiIdHeat = new HashMap<>();
        this.graph = new Graph(this);
    }

    public boolean contains(int id) {
        for (Person it : people) {
            if (it.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Person> getPeople() {
        return people;
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
        graphNum++;
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
            updateTri(0, person1, person2);
            if (!graph.bfs(id1, id2)) { //在连通前是不连通的
                graphNum--;
            }
            person1.addAcquaintance(person2, value);
            person2.addAcquaintance(person1, value);
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
            if (!graph.bfs(id1, id2)) {
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

    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException { //从id1到id2找一条路径出来即可
        if (contains(id1) && contains(id2)) {
            return graph.bfs(id1, id2); //不得不使用bfs辣
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
                !getGroup(id2).hasPerson(getPerson(id1)) && getGroup(id2).getSize() <= 1111) {
            getGroup(id2).addPerson(getPerson(id1));
            ((MyGroup) getGroup(id2)).addAgeSum(getPerson(id1).getAge());
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
            ((MyGroup) getGroup(id2)).subAgeSum(getPerson(id1).getAge());
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

    public boolean containsEmojiId(int emojiId) {
        return emojiIdHeat.containsKey(emojiId);
    }

    public void addMessage(Message message) throws
            EqualMessageIdException, EqualPersonIdException, EmojiIdNotFoundException {
        if (!containsMessage(message.getId()) && ((message.getType() == 0 &&
                !message.getPerson1().equals(message.getPerson2())) || message.getType() == 1) &&
                (!(message instanceof EmojiMessage) || containsEmojiId(((EmojiMessage)
                        message).getEmojiId()))) {
            messages.add(message);
        } else if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message instanceof EmojiMessage &&
                !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        } else if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
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
                !getMessage(id).getPerson1().equals(getMessage(id).getPerson2())) {
            MyPerson person1 = (MyPerson) getMessage(id).getPerson1();
            MyPerson person2 = (MyPerson) getMessage(id).getPerson2();
            Message message = getMessage(id);
            person1.setSocialValue(person1.getSocialValue() + message.getSocialValue());
            person2.setSocialValue(person2.getSocialValue() + message.getSocialValue());
            if (message instanceof RedEnvelopeMessage) {
                person1.setMoney(person1.getMoney() - ((RedEnvelopeMessage) message).getMoney());
                person2.setMoney(person2.getMoney() + ((RedEnvelopeMessage) message).getMoney());
            }
            if (message instanceof EmojiMessage) {
                int heat = emojiIdHeat.get(((EmojiMessage) message).getEmojiId());
                emojiIdHeat.put(((EmojiMessage) message).getEmojiId(), heat + 1); //热度加一
            }
            getMessage(id).getPerson2().getMessages().add(0, getMessage(id));
            messages.remove(getMessage(id));
        } else if (containsMessage(id) && getMessage(id).getType() == 1 && getMessage(id)
                .getGroup().hasPerson(getMessage(id).getPerson1())) { //消息的组里面包含着发送信息的人 才允许发送1
            Message message = getMessage(id);
            MyGroup group = (MyGroup) message.getGroup();
            for (Person person : group.getPeople()) {
                ((MyPerson) person).setSocialValue(person.getSocialValue()
                        + message.getSocialValue());
                if (message instanceof RedEnvelopeMessage) {
                    int money = ((RedEnvelopeMessage) message).getMoney() / group.getSize();
                    if (person.equals(message.getPerson1())) {
                        ((MyPerson) person).setMoney(person.getMoney() -
                                money * (group.getSize() - 1));
                    } else {
                        ((MyPerson) person).setMoney(person.getMoney() + money);
                    }
                }
            }
            if (message instanceof EmojiMessage) {
                int heat = emojiIdHeat.get(((EmojiMessage) message).getEmojiId());
                emojiIdHeat.put(((EmojiMessage) message).getEmojiId(), heat + 1); //热度加一
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
        if (contains(id) && ((MyPerson) getPerson(id)).
                getAcquaintance().size() != 0) { //找最大value里面id最小的 返回PersonId
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

    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (!containsEmojiId(id)) {
            emojiIdHeat.put(id, 0);
        } else {
            throw new MyEqualEmojiIdException(id);
        }
    }

    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getMoney();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (containsEmojiId(id)) {
            return emojiIdHeat.get(id);
        } else {
            throw new MyEmojiIdNotFoundException(id);
        }
    }

    public int deleteColdEmoji(int limit) { //删掉比limit小的表情 也要清除相应的信息
        Iterator<Map.Entry<Integer, Integer>> iterator1 = emojiIdHeat.entrySet().iterator();
        while (iterator1.hasNext()) {
            if (iterator1.next().getValue() < limit) {
                iterator1.remove();
            }
        }
        Iterator<Message> iterator2 = messages.iterator();
        while (iterator2.hasNext()) {
            Message message = iterator2.next();
            if (message instanceof EmojiMessage &&
                    !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
                iterator2.remove();
            }
        }
        return emojiIdHeat.size();
    }

    public void clearNotices(int personId) throws PersonIdNotFoundException { //删除所有提示消息
        if (contains(personId)) {
            Iterator<Message> iterator = getPerson(personId).getMessages().iterator();
            while (iterator.hasNext()) {
                if (iterator.next() instanceof NoticeMessage) {
                    iterator.remove();
                }
            }
        } else {
            throw new MyPersonIdNotFoundException(personId);
        }
    }

    public int queryLeastMoments(int id) throws PersonIdNotFoundException, PathNotFoundException {
        return graph.queryLeastMoments(id);
    }

    public int deleteColdEmojiOKTest(int limit, ArrayList<HashMap<Integer, Integer>>
            beforeData, ArrayList<HashMap<Integer, Integer>> afterData, int result) {
        return OkTest.deleteColdEmojiOKTest(limit, beforeData, afterData, result);
    }
}

