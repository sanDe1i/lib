package com.example.lm.Service;

import com.example.lm.Dao.BorrowRepository;
import com.example.lm.Model.Borrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class BorrowService {
    @Autowired
    private BorrowRepository borrowRepository;

    public Borrow saveBorrow(Borrow borrow, int borrowDays) {
        // 获取当前时间
        Date now = new Date();

        // 格式化当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedStartDate = sdf.format(now);

        // 设置借书的开始时间
        borrow.setLoanStartTime(formattedStartDate);

        // 计算结束时间（例如借书期限为 borrowDays 天）
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_YEAR, borrowDays);
        String formattedEndDate = sdf.format(calendar.getTime());

        // 设置借书的结束时间
        borrow.setLoanEndTime(formattedEndDate);

        // 保存借书实体
        return borrowRepository.save(borrow);
    }

    public Optional<Borrow> getBorrowByBookId(Integer bookId) {
        return borrowRepository.findByBookId(bookId);
    }

    // Other service methods can be added here
}

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.pinyinInitials) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "b.isbn = :keyword")
    List<Book> searchBooks(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT b.title FROM Book b WHERE b.title LIKE CONCAT(:prefix, '%')")
    List<String> autoCompleteTitle(@Param("prefix") String prefix);

    @Query(value = "SELECT b.title FROM books b GROUP BY b.title ORDER BY COUNT(*) DESC LIMIT 10", nativeQuery = true)
    List<String> getPopularTitles();
}
