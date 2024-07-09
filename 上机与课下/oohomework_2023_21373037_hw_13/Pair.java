public class Pair {
    private Person person; //独有的
    private Book book;
    private boolean vis;

    public Pair(Person person, Book book) {
        this.person = person;
        this.book = book;
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

    public Book getBook() {
        return book;
    }
}
