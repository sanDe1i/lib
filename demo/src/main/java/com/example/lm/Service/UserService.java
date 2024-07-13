package com.example.lm.Service;


import com.example.lm.Dao.FavourDao;
import com.example.lm.Dao.FileInfoDao;
import com.example.lm.Dao.UserInfoRepository;
import com.example.lm.Dao.UserRepository;
import com.example.lm.Model.Favour;
import com.example.lm.Model.FileInfo;
import com.example.lm.Model.User;
import com.example.lm.Model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserInfoRepository userRepository;

    @Autowired
    private FileInfoDao bookRepository;

    @Autowired
    private FavourDao userBookRepository;

    @Autowired
    private UserRepository userRepositoryForLogin;

    public User authenticate(String username) {
        User user = userRepositoryForLogin.findByUsername(username);
        return user;
    }

    public void addBookToUserCollection(String username, String bookisbn) {
        UserInfo user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }

        FileInfo book = bookRepository.findByisbn(bookisbn);

        if (book == null) {
            throw new IllegalArgumentException("Book not found!");
        }

    // Check if the user already has this book in their collection
        boolean alreadyFavorited = userBookRepository.existsByUserAndBook(user, book);

        if (alreadyFavorited) {
            throw new IllegalArgumentException("Book already in collection!");
        }

        Favour userBook = new Favour();
        userBook.setUser(user);
        userBook.setBook(book);
        userBookRepository.save(userBook);
    }

    public Set<FileInfo> listCollect(String username) {
        UserInfo user = userRepository.findByUsername(username);
        Set<Favour> favours = userBookRepository.findByUser(user);
        Set<FileInfo> books = new HashSet<>();
        for(Favour i :favours){
            books.add(i.getBook());
        }
        return books;
    }

    public void removeBookCollection(String username, String bookisbn) {
        UserInfo user = userRepository.findByUsername(username);
        if (user == null) {

            throw new IllegalArgumentException("User not found!");
        }
        FileInfo book = bookRepository.findByisbn(bookisbn);
        if (book == null) {
            throw new IllegalArgumentException("Book not found!");
        }

        boolean alreadyFavorited = userBookRepository.existsByUserAndBook(user, book);

        if (!alreadyFavorited) {
            throw new IllegalArgumentException("Book not in collection!");
        }

        Optional<Favour> favour = userBookRepository.findByUserAndBook(user, book);
        if (favour.isPresent()) {
            userBookRepository.delete(favour.get());
        }else {throw new IllegalArgumentException("Book not in collection!");}
    }

    public UserInfo userLogin(String username) {
        return userRepository.findByUsername(username);
    }
}
