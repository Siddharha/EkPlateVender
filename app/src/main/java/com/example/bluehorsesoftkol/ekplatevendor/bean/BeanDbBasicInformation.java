package com.example.bluehorsesoftkol.ekplatevendor.bean;

/**
 * Created by Bluehorse Soft Kol on 10/1/2015.
 */
public class BeanDbBasicInformation {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEstablishment_year() {
        return establishment_year;
    }

    public void setEstablishment_year(String establishment_year) {
        this.establishment_year = establishment_year;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getAadhar_no() {
        return aadhar_no;
    }

    public void setAadhar_no(String aadhar_no) {
        this.aadhar_no = aadhar_no;
    }

    public String getVendor_notes() {
        return vendor_notes;
    }

    public void setVendor_notes(String vendor_notes) {
        this.vendor_notes = vendor_notes;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    String name,establishment_year,shop_name,mobile_no,aadhar_no,vendor_notes,added_by;
}
