package com.example.bluehorsesoftkol.ekplatevendor.bean;

/**
 * Created by user on 19-10-2015.
 */
public class VendorImageItem {
    private int id, vendorId;
    private String image_path;

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

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
