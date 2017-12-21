package com.example.hua.huachuang.bean.joke;

import java.util.List;

/**
 * Created by hua on 2017/3/13.
 */

public class TabType {

    private List<TabInfo> data;

    public List<TabInfo> getData() {
        return data;
    }

    public static class TabInfo {
        private String name;
        private String list_id;

        public String getName() {
            return name;
        }

        public String getList_id() {
            return list_id;
        }
    }

}
