import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPerson implements Person { //在Network里面统一进行异常处理，在其他类中不涉及异常处理 优点？？

    private int id;
    private String name;
    private int age;
    private HashMap<Person, Integer> acquaintance;
    private int socialValue;
    private ArrayList<Message> messages;
    private Person father;
    private int bestFriendId;
    private int bestFriendValue;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;  //是否需要完成对数组的初始化
        this.acquaintance = new HashMap<>();
        this.messages = new ArrayList<>();
        this.socialValue = 0;
        this.father = this;
        this.bestFriendId = 0;
        this.bestFriendValue = 0;
    }

    public int getBestFriendId() {
        return bestFriendId;
    }

    public void setBestFriendId(int bestFriendId) {
        this.bestFriendId = bestFriendId;
    }

    public int getBestFriendValue() {
        return bestFriendValue;
    }

    public void setBestFriendValue(int bestFriendValue) {
        this.bestFriendValue = bestFriendValue;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getFather() {
        return father;
    }

    public void setFather(Person father) {
        this.father = father;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public HashMap<Person, Integer> getAcquaintance() {
        return acquaintance;
    }

    public void setSocialValue(int socialValue) {
        this.socialValue = socialValue;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public /*@ pure @*/ boolean equals(Object obj) {
        if (obj != null && obj instanceof Person) {
            Person person = (MyPerson) obj;
            return person.getId() == this.id;
        }
        return false;
    }

    public boolean isLinked(Person person) {
        if (person.getId() == this.id) {
            return true;
        }
        for (Map.Entry<Person, Integer> entry : this.acquaintance.entrySet()) {
            if (entry.getKey().getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }

    public int queryValue(Person person) {
        for (Map.Entry<Person, Integer> entry : this.acquaintance.entrySet()) {
            if (entry.getKey().getId() == person.getId()) {
                return entry.getValue();
            }
        }
        return 0;
    }

    public void addSocialValue(int num) {
        this.socialValue = this.socialValue + num;
    }

    public int getSocialValue() {
        return this.socialValue;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Message> getReceivedMessages() { //接受前五条信息
        ArrayList<Message> results = new ArrayList<>();
        for (int i = 0; i < messages.size() && i <= 4; i++) {
            results.add(messages.get(i));
        }
        return results;
    }

    public void addAcquaintance(Person person, int value) {
        acquaintance.put(person, value);
    }

    @Override
    public int compareTo(Person p2) {
        return this.name.compareTo(p2.getName());
    }
}
