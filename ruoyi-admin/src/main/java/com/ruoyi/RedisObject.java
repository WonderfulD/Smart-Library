package com.ruoyi;

import java.time.LocalDateTime;


public class RedisObject {
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    private LocalDateTime localDateTime;

    @Override
    public String toString() {
        return "RedisObject{" +
                "data=" + data +
                ", localDateTime=" + localDateTime +
                '}';
    }
}
