package com.zaviron.teaapplication.model;

import java.util.Date;

public class TeaDetails {
    String user_id;
    String date;
    Double tea_packet_price;
    Double total_tea ;
    Double advanced_fee;
    Double dolomite_fee;
    Double fertilizer_fee;

    public TeaDetails() {
    }

    public TeaDetails(String user_id, String date, Double tea_packet_price, Double total_tea, Double advanced_fee, Double dolomite_fee, Double fertilizer_fee) {
        this.user_id = user_id;
        this.date = date;
        this.tea_packet_price = tea_packet_price;
        this.total_tea = total_tea;
        this.advanced_fee = advanced_fee;
        this.dolomite_fee = dolomite_fee;
        this.fertilizer_fee = fertilizer_fee;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTea_packet_price() {
        return tea_packet_price;
    }

    public void setTea_packet_price(Double tea_packet_price) {
        this.tea_packet_price = tea_packet_price;
    }

    public Double getTotal_tea() {
        return total_tea;
    }

    public void setTotal_tea(Double total_tea) {
        this.total_tea = total_tea;
    }

    public Double getAdvanced_fee() {
        return advanced_fee;
    }

    public void setAdvanced_fee(Double advanced_fee) {
        this.advanced_fee = advanced_fee;
    }

    public Double getDolomite_fee() {
        return dolomite_fee;
    }

    public void setDolomite_fee(Double dolomite_fee) {
        this.dolomite_fee = dolomite_fee;
    }

    public Double getFertilizer_fee() {
        return fertilizer_fee;
    }

    public void setFertilizer_fee(Double fertilizer_fee) {
        this.fertilizer_fee = fertilizer_fee;
    }
}
