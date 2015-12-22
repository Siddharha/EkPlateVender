package com.example.bluehorsesoftkol.ekplatevendor.bean;

/**
 * Created by user on 19-10-2015.
 */
public class VendorVideoItem {
    private int id, vendorId;

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

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    private String video_path;
}
