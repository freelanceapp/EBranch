package com.creative.share.apps.ebranch.models;

import java.io.Serializable;
import java.util.List;

public class Add_Order_Model implements Serializable {


    private int user_id;
    private int market_id;
    private String branch_id;
    private int order_type;
    private double longitude;
    private double latitude;
    private String address;
    private double total_cost;
    private List<Products> products;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMarket_id() {
        return market_id;
    }

    public void setMarket_id(int market_id) {
        this.market_id = market_id;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public static class Products implements Serializable {
      private int product_id;
              private int amount;
              private double total_price;
private String ar_title;
private String en_title;
private String ar_desc;
private String en_des;
private String image;
        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public void setAr_title(String ar_title) {
            this.ar_title = ar_title;
        }

        public void setEn_title(String en_title) {
            this.en_title = en_title;
        }

        public void setAr_desc(String ar_desc) {
            this.ar_desc = ar_desc;
        }

        public void setEn_des(String en_des) {
            this.en_des = en_des;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public double getTotal_price() {
            return total_price;
        }

        public void setTotal_price(double total_price) {
            this.total_price = total_price;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getAr_desc() {
            return ar_desc;
        }

        public String getEn_des() {
            return en_des;
        }

        public String getImage() {
            return image;
        }
    }



}
