package com.ruoyi.Utils;

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
        //设置borrowId为日期+随机数
        long timestamp = new Date().getTime();
        long randomNumber = ThreadLocalRandom.current().nextLong(1, 1000);
        return (timestamp % 100000000L) * 1000 + randomNumber;
    }
}
