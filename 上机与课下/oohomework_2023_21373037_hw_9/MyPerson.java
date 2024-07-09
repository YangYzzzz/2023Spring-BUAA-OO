
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private ArrayList<Person> acquaintance;
    private ArrayList<Integer> value;

    private Person father;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ArrayList<Person> getAcquaintance() {
        return acquaintance;
    }

    public void setAcquaintance(ArrayList<Person> acquaintance) {
        this.acquaintance = acquaintance;
    }

    public ArrayList<Integer> getValue() {
        return value;
    }

    public void setValue(ArrayList<Integer> value) {
        this.value = value;
    }

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;  //是否需要完成对数组的初始化
        this.acquaintance = new ArrayList<>();
        this.value = new ArrayList<>();
        this.father = this;
    }

    public Person getFather() {
        return father;
    }

    public void setFather(Person father) {
        this.father = father;
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

    public boolean equals(Object obj) {   //只要出现id相同的就一定是错
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
        for (Person item : this.acquaintance) {
            if (item.getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }

    public int queryValue(Person person) {
        for (int i = 0; i < this.acquaintance.size(); i++) {
            if (this.acquaintance.get(i).getId() == person.getId()) {
                return this.value.get(i);
            }
        }
        return 0;
    }

    @Override
    public int compareTo(Person p2) {
        return this.name.compareTo(p2.getName());
    }
}

