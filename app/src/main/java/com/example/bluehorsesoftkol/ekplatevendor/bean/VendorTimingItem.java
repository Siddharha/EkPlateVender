package com.example.bluehorsesoftkol.ekplatevendor.bean;

/**
 * Created by user on 16-10-2015.
 */
public class VendorTimingItem {
    private String mocOpening, mocClosing, eocOpening, eocClosing, day;
    private int id, vendorId;

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getMocOpening() {
        return mocOpening;
    }

    public void setMocOpening(String mocOpening) {
        this.mocOpening = mocOpening;
    }

    public String getMocClosing() {
        return mocClosing;
    }

    public void setMocClosing(String mocClosing) {
        this.mocClosing = mocClosing;
    }

    public String getEocOpening() {
        return eocOpening;
    }

    public void setEocOpening(String eocOpening) {
        this.eocOpening = eocOpening;
    }

    public String getEocClosing() {
        return eocClosing;
    }

    public void setEocClosing(String eocClosing) {
        this.eocClosing = eocClosing;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
