package com.ruoyi.Utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import com.ruoyi.borrow.domain.BookBorrowing;

import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class BorrowUtil {
    /**
     * 计算逾期归还图书的罚款金额。
     * 如果逾期天数小于或等于0，则罚款为0。
     * 前5天，每天罚款￥1。
     * 第6到10天，每天罚款￥2。
     * 超过10天，每天罚款￥5。
     * @param overdueDays 逾期天数
     * @return 罚款总金额（单位：人民币元）
     */
    public static Long calculateFine(long overdueDays) {
        if (overdueDays <= 0) {
            return 0L;
        }
        long fine;
        if (overdueDays <= 5) {
            fine = overdueDays;
        } else if (overdueDays <= 10) {
            fine = 5L + (overdueDays - 5) * 2L;
        } else {
            fine = 5L + 5L * 2L + (overdueDays - 10) * 5L;
        }
        return fine;
    }

    /**
     * 生成借阅ID
     * @return
     */
    public static Long generateBorrowId() {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        // 生成一个唯一ID
        return snowflake.nextId();
    }

    /**
     * 获取借阅状态
     */
    public static int getBorrowingStatus(BookBorrowing borrowing) {
        if(borrowing.getReturnMethod() != null && borrowing.getReturnDate() == null) {
            return 6;
        }
        if (borrowing.getPendingStatus() == 0L) {
            return 5;       //借阅拒绝
        } else if (borrowing.getDueDate() == null) {
            return 4;     //待审核
        } else if (borrowing.getReturnDate() != null) {
            if (borrowing.getReturnDate().isBefore(borrowing.getDueDate())) {
                return 0; //如期归还
            } else {
                return 2; //逾期归还
            }
        } else {
            LocalDate today = LocalDate.now();
            if (today.isBefore(borrowing.getDueDate())) {
                return 1; //借阅正常
            } else {
                return 3; //逾期未还
            }
        }
    }
}
