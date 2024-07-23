package com.example.lm.Controller;

import com.example.lm.Dao.BorrowRepository;
import com.example.lm.Model.Borrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BorrowApiController {

    @Autowired
    private BorrowRepository borrowRepository;

    @GetMapping("/api/borrows")
    public List<Borrow> getAllBorrows() {
        return borrowRepository.findAll();
    }

    @GetMapping("/api/borrows/{username}")
    public List<Borrow> getBorrowsByUsername(@PathVariable String username) {
        return borrowRepository.findByUsername(username);
    }
}
