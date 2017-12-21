package com.example.hua.huachuang.bean.music;

/**
 * Created by hua on 2017/3/9.
 */

public class SettingInfo {
    //特殊处理的标记
    private String flag;
    private String text;
    private String subText;
    private String description;
    private boolean isSelect;
    private boolean isNoDivider;
    private boolean isCanClick;

    public interface flags {
        String NO_FLAG = "no_flag";
        String NET_FIRST = "net_first";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubText() {
        return subText;
    }

    public String getFlag() {
        return flag;
    }

    public boolean isCanClick() {
        return isCanClick;
    }

    public void setCanClick(boolean canClick) {
        isCanClick = canClick;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public boolean isNoDivider() {
        return isNoDivider;
    }

    public void setNoDivider(boolean noDivider) {
        isNoDivider = noDivider;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
