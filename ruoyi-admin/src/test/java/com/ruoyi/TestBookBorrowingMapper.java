package com.ruoyi;

import com.ruoyi.borrow.mapper.BookBorrowingMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestBookBorrowingMapper {
    @Autowired
    private BookBorrowingMapper bookBorrowingMapper;

    @Test
    void selectNotReturnedBooksCountByReaderIdAndBookId() {
        Long count = bookBorrowingMapper.selectNotReturnedBooksCountByReaderIdAndBookId(103L, 1L);
        System.out.println(count);
    }
}
