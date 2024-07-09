package src;

public class MyPair { //Book可能没有
    private Person person; //独有的
    private char category;
    private String name;
    private boolean vis;

    public MyPair(Person person, char category, String name) {
        this.person = person;
        this.category = category;
        this.name = name;
        this.vis = false;
    }

    public boolean isVis() {
        return vis;
    }

    public void setVis(boolean vis) {
        this.vis = vis;
    }

    public Person getPerson() {
        return person;
    }

    public char getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }
}
