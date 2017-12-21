package com.example.hua.huachuang.bean.music;

/**
 * Created by hua on 2017/2/19.
 */

public class MenuInfo {
    private int coverId;
    private String name;
    private String stringSum;

    public MenuInfo(int id, String name, String sum) {
        this.coverId = id;
        this.name = name;
        this.stringSum = sum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStringSum() {
        return stringSum;
    }

    public void setStringSum(String stringSum) {
        this.stringSum = stringSum;
    }

    public int getCoverId() {
        return coverId;
    }

    public void setCoverId(int coverId) {
        this.coverId = coverId;
    }
}
