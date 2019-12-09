package com.creative.share.apps.ebranch.models;

import java.io.Serializable;
import java.util.List;

public class Catogries_Model implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class  Data implements Serializable
    {
        private int id;
                private String ar_title;
                private String en_title;
                private String image;
                private String level;
        private String parent_id;
        private String market_added;
        private String is_deleted;

        public int getId() {
            return id;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getImage() {
            return image;
        }

        public String getLevel() {
            return level;
        }

        public String getParent_id() {
            return parent_id;
        }

        public String getMarket_added() {
            return market_added;
        }

        public String getIs_deleted() {
            return is_deleted;
        }
    }
}
