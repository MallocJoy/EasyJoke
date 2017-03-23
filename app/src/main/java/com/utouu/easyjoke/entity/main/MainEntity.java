package com.utouu.easyjoke.entity.main;

/**
 * Create by 黄思程 on 2017/3/20   17:26
 * Function：
 * Desc：主页面的实体类
 */
public class MainEntity {

    public boolean double_col_mode;
    public String umeng_event;
    public boolean is_default_tab;
    public String url;
    public int list_id;
    public int refresh_interval;
    public int type;
    public String name;

    @Override
    public String toString() {
        return "MainEntity{" +
                "double_col_mode=" + double_col_mode +
                ", umeng_event='" + umeng_event + '\'' +
                ", is_default_tab=" + is_default_tab +
                ", url='" + url + '\'' +
                ", list_id=" + list_id +
                ", refresh_interval=" + refresh_interval +
                ", type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}
