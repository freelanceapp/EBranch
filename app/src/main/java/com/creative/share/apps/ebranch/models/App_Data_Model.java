package com.creative.share.apps.ebranch.models;

import java.io.Serializable;

public class App_Data_Model implements Serializable {
    private int id;
            private String key_word;
            private String link;
            private String type;
            private String en_title;
            private String ar_title;
    private String ar_content;
    private String en_content;
    private String image;
    private double drive_proportion;
    public int getId() {
        return id;
    }

    public double getDrive_proportion() {
        return drive_proportion;
    }

    public String getKey_word() {
        return key_word;
    }

    public String getLink() {
        return link;
    }

    public String getType() {
        return type;
    }

    public String getEn_title() {
        return en_title;
    }

    public String getAr_title() {
        return ar_title;
    }

    public String getAr_content() {
        return ar_content;
    }

    public String getEn_content() {
        return en_content;
    }

    public String getImage() {
        return image;
    }
}
