package com.example.bluehorsesoftkol.ekplatevendor.bean;

/**
 * Created by user on 17-10-2015.
 */
public class AboutVendorItem {
    private int id, vendorId;
    private String licenseStatus, licenseNumber, addharNumber, additionalInfo;

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

    public String getLicenseStatus() {
        return licenseStatus;
    }

    public void setLicenseStatus(String licenseStatus) {
        this.licenseStatus = licenseStatus;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getAddharNumber() {
        return addharNumber;
    }

    public void setAddharNumber(String addharNumber) {
        this.addharNumber = addharNumber;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
