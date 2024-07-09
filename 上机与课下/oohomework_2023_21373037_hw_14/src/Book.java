package src;

public class Book {
    private final char category;
    private final String name;
    private final String schoolName;
    private boolean smeared;
    private final boolean privateOwned; //是否私有

    public Book(char category, String name, String schoolName, String owned) {
        this.schoolName = schoolName;
        this.category = category;
        this.name = name;
        this.smeared = false;
        if (owned.charAt(0) == 'Y') {
            this.privateOwned = false;
        } else {
            this.privateOwned = true;
        }
    }

    public Book(char category, String name, String schoolName) {
        this.schoolName = schoolName;
        this.category = category;
        this.name = name;
        this.smeared = false;
        this.privateOwned = false;
    }

    public boolean isPrivateOwned() {
        return privateOwned;
    }

    public String getSchoolName() {
        return schoolName;
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

    @Override
    public int hashCode() {
        return String.valueOf(category).hashCode() + name.hashCode();
    }
}
