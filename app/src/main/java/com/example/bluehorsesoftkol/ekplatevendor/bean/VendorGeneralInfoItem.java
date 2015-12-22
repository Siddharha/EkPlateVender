package com.example.bluehorsesoftkol.ekplatevendor.bean;

/**
 * Created by user on 17-10-2015.
 */
public class VendorGeneralInfoItem {
    private int id, vendorId;
    private String tasteRate, hygieneRate, foodType, arrangement, carterType, deliveryReady, avgSales, avgCustomers, tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getTasteRate() {
        return tasteRate;
    }

    public void setTasteRate(String tasteRate) {
        this.tasteRate = tasteRate;
    }

    public String getHygieneRate() {
        return hygieneRate;
    }

    public void setHygieneRate(String hygieneRate) {
        this.hygieneRate = hygieneRate;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getArrangement() {
        return arrangement;
    }

    public void setArrangement(String arrangement) {
        this.arrangement = arrangement;
    }

    public String getCarterType() {
        return carterType;
    }

    public void setCarterType(String carterType) {
        this.carterType = carterType;
    }

    public String getDeliveryReady() {
        return deliveryReady;
    }

    public void setDeliveryReady(String deliveryReady) {
        this.deliveryReady = deliveryReady;
    }

    public String getAverageSales() {
        return avgSales;
    }

    public void setAverageSales(String avgSales) {
        this.avgSales = avgSales;
    }

    public String getAverageCustomers() {
        return avgCustomers;
    }

    public void setAverageCustomers(String avgCustomers) {
        this.avgCustomers = avgCustomers;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
