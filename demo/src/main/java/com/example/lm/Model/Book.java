@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String pinyinInitials; // 如："xiyouji"
    private String isbn;
    private String author;
    private String category;

    // Getters and setters
}