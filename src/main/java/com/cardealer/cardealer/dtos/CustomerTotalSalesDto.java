package com.cardealer.cardealer.dtos;

import com.google.gson.annotations.Expose;

public class CustomerTotalSalesDto {

    @Expose
    private String fullName;

    @Expose
    private int boughtCars;

    @Expose
    private double spentMoney;

    public CustomerTotalSalesDto() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getBoughtCars() {
        return boughtCars;
    }

    public void setBoughtCars(int boughtCars) {
        this.boughtCars = boughtCars;
    }

    public double getSpentMoney() {
        return spentMoney;
    }

    public void setSpentMoney(double spentMoney) {
        this.spentMoney = spentMoney;
    }
}
