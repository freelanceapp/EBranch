package com.creative.share.apps.ebranch.models;

import java.io.Serializable;

public class App_Data_Model implements Serializable {
    private String conditions;
private double commission;
private String advantages;
    public String getConditions() {
        return conditions;
    }

    public double getCommission() {
        return commission;
    }

    public String getAdvantages() {
        return advantages;
    }
}
