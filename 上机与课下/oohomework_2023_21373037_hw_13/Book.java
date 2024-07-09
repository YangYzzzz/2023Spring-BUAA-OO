
public class Book {
    private char category;
    private String name;
    private boolean smeared;

    public Book(char category, String name) {
        this.category = category;
        this.name = name;
        this.smeared = false;
    }

    public boolean isSmeared() {
        return smeared;
    }

    public void setSmeared(boolean smeared) {
        this.smeared = smeared;
    }

    public char getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Book) {
            return this.category == ((Book) obj).getCategory() &&
                    this.name.equals(((Book) obj).getName());
        } else {
            return false;
        }
    }
}
