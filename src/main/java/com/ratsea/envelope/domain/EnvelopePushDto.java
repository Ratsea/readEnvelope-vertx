package com.ratsea.envelope.domain;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Ratsea on 2017/7/5.
 */
public class EnvelopePushDto {

    private String id;

    private BigDecimal money;

    private int num;

    private int useNum;

    private List<User> user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getUseNum() {
        return useNum;
    }

    public void setUseNum(int useNum) {
        this.useNum = useNum;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "EnvelopePushDto{" +
                "id='" + id + '\'' +
                ", money=" + money +
                ", num=" + num +
                ", useNum=" + useNum +
                ", user=" + user +
                '}';
    }
}
