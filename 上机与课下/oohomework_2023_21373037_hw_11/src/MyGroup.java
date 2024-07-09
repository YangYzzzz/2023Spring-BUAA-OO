import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.ArrayList;

public class MyGroup implements Group {

    private final int id;
    private final ArrayList<Person> people;
    private int ageSum;
    private int agePow;

    public MyGroup(int id) {
        this.id = id;
        this.people = new ArrayList<>();
        this.ageSum = 0;
        this.agePow = 0;
    }

    public void addAgeSum(int age) {
        this.ageSum = this.ageSum + age;
        this.agePow = this.agePow + age * age;
    }

    public void subAgeSum(int age) {
        this.ageSum = this.ageSum - age;
        this.agePow = this.agePow - age * age;
    }

    public int getId() {
        return this.id;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Group) {
            return ((Group) obj).getId() == this.id;
        } else {
            return false;
        }
    }

    public void addPerson(Person person) {
        this.people.add(person);
    }

    public boolean hasPerson(Person person) {
        for (Person it : people) {
            if (it.equals(person)) {
                return true;
            }
        }
        return false;
    }

    public int getValueSum() { //也可以及时维护
        int sum = 0;
        for (int i = 0; i < people.size(); i++) {
            for (int j = i + 1; j < people.size(); j++) {
                if (people.get(i).isLinked(people.get(j))) {
                    sum = sum + people.get(i).queryValue(people.get(j)) * 2;
                }
            }
        }
        return sum;
    }

    public int getAgeMean() {  //一个小组里的年龄均值
        if (people.size() == 0) {
            return 0;
        }
        return ageSum / people.size();
    }

    public int getAgeVar() {   //一个小组里的年龄方差
        if (people.size() == 0) {
            return 0;
        }
        int ageMean = getAgeMean();
        return (agePow - 2 * ageMean * ageSum + people.size() * ageMean * ageMean) / people.size();
    }

    public void delPerson(Person person) {
        people.remove(person);
    }

    public int getSize() {
        return people.size();
    }

    public ArrayList<Person> getPeople() {
        return people;
    }
}
