package com.ruoyi;

import com.ruoyi.bookorder.mapper.BookOrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class TestBookOrderMapper {

    @Autowired
    private BookOrderMapper bookOrderMapper;

    @Test
    void test() {
        Integer integer = bookOrderMapper.selectTotalAmountByLibraryIdAndDate(103L, LocalDate.now());
        System.out.println(integer);
    }
}
