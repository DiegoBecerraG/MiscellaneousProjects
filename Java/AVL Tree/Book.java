public class Book {
    Long id;
    String title;
    String author;

    public Book(Long i, String t, String a) {
        id = i;
        title = t;
        author = a;
    }

    public String toString() {
        return String.format("%s %s Authors: %s\n", id, title, author);
    }
}