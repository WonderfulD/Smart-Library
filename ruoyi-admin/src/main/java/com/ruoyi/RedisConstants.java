package com.ruoyi;

public class RedisConstants {
    public static final String CACHE_BOOKRATING_KEY = "cache:bookrating:";
    public static final String LOCK_BOOKRATING_KEY = "lock:bookrating:";
    public static final Long LOCK_BOOKRATING_EX = 10L;
    public static final String CACHE_LIBRARY_BOOKRATING_KEY = "cache:librarybookrating:";
    public static final Long CACHE_BOOKRATING_EX = 30L;
    public static final String CACHE_NULL_PLACEHOLDER = "NULL";
    public static final Long CACHE_NULL_EX = 5L;
    public static final String BOOK_BORROW_LOCK = "lock:borrow:";
}
