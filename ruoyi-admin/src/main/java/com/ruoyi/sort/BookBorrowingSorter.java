package com.ruoyi.sort;

import java.util.List;
import java.util.Collections;
import com.ruoyi.borrow.domain.BookBorrowing;

public class BookBorrowingSorter {

    public static List<BookBorrowing> sortBookBorrowingsByBorrowDateDesc(List<BookBorrowing> bookBorrowings) {
        // 使用Collections的sort方法和Comparator来排序，按borrowDate降序
        Collections.sort(bookBorrowings, (b1, b2) -> b2.getBorrowDate().compareTo(b1.getBorrowDate()));
        return bookBorrowings;
    }
}
