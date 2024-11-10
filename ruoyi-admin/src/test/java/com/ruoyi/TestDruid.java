package com.ruoyi;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;


@SpringBootTest
@DisplayName("单元测试类")
public class TestDruid {

    @Autowired
    private DataSource dataSource;

    @DisplayName("测试数据库连接池")
    @Test
    public void test1() {
        System.out.println(dataSource.getClass());
    }

}

