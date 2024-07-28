package com.example.lm;

import com.example.lm.Dao.BorrowRepository;
import com.example.lm.Dao.UserRepository;
import com.example.lm.Model.Borrow;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LmApplicationTests {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private BorrowRepository borrowRepository;
    @Test
    void contextLoads() {
//       Borrow a = borrowRepository.findByBorrowId(1);
//        System.out.println(a.getBookId());
    }

}
