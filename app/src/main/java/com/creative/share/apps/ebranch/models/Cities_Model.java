package com.creative.share.apps.ebranch.models;

import java.io.Serializable;
import java.util.List;

public class Cities_Model implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class Data implements Serializable
    {
         private int id_city;
                 private String ar_city_title;
                 private String en_city_title;
                 private String country_id;

        public Data(String ar_city_title, String en_city_title) {
            this.ar_city_title = ar_city_title;
            this.en_city_title = en_city_title;
        }

        public int getId_city() {
            return id_city;
        }

        public String getAr_city_title() {
            return ar_city_title;
        }

        public String getEn_city_title() {
            return en_city_title;
        }

        public String getCountry_id() {
            return country_id;
        }
    }
}

