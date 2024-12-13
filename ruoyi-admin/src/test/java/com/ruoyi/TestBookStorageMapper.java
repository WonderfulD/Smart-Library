package com.ruoyi;

import com.ruoyi.storage.mapper.BookStorageMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestBookStorageMapper {

    @Autowired
    private BookStorageMapper bookStorageMapper;

    @Test
    void selectLibraryIdsByBookId() {
        Long bookId = 1L;
        List<Long> integers = bookStorageMapper.selectLibraryIdsByBookId(bookId);
        System.out.println(integers);
    }

    @Test
    void selectBookIdsByLibraryId() {
        List<Long> bookIdsByLibraryId = bookStorageMapper.selectBookIdsByLibraryId(null);
        System.out.println(bookIdsByLibraryId);
    }

    @Test
    void reduceStockByLibraryIdAndBookId() {
        Boolean result = bookStorageMapper.reduceStockByLibraryIdAndBookId(103L, 1L);
        System.out.println(result);
    }

    @Test
    void selectTotalStockByBookId() {
        Long totalStock = bookStorageMapper.selectTotalStockByBookId(1L);
        System.out.println(totalStock);
    }

}
