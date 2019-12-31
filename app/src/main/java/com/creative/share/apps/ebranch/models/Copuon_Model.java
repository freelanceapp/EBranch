package com.creative.share.apps.ebranch.models;

import java.io.Serializable;

public class Copuon_Model implements Serializable {
    private int id;
            private String name;
            private String coupon_serial;
            private String value;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCoupon_serial() {
        return coupon_serial;
    }

    public String getValue() {
        return value;
    }
}
