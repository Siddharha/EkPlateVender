package com.example.bluehorsesoftkol.ekplatevendor.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.bluehorsesoftkol.ekplatevendor.bean.AboutVendorItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorAddressInfoItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorBasicInfoItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorFoodSoldItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorGeneralInfoItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorImageItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorPurchaseItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorTimingItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorVideoItem;
import com.example.bluehorsesoftkol.ekplatevendor.dbpackage.DbAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by user on 18-10-2015.
 */
public class JsonOutputFormatter {
    int bytesRead, bytesAvailable, bufferSize;
    private ArrayList<VendorBasicInfoItem> selectedVendorInfoList;
    private ArrayList<ArrayList<VendorTimingItem>> timingInfoListAllVendor;
    private ArrayList<ArrayList<VendorFoodSoldItem>> foodSolsItemListAllVendor;
    private ArrayList<ArrayList<VendorGeneralInfoItem>> generalInfoVendor;
    private ArrayList<ArrayList<VendorPurchaseItem>> foodPuchaseItem;
    private ArrayList<ArrayList<AboutVendorItem>> aboutVendorInfo;
    private ArrayList<ArrayList<VendorAddressInfoItem>> vendorAddressInfo;
    private ArrayList<ArrayList<VendorImageItem>> vendorImage;
    private ArrayList<ArrayList<VendorVideoItem>> vendorVideo;
    private JSONArray vendorInfoJsonArray;
    private JSONObject vendorInfoChildObj, mainDataJsonObj, userJsonObj, outputJsonContainerObj;
    private DbAdapter dbAdapter;
    private Context context;
    private Pref _pref;

    private ImageCompressCrop objCompressCrop;

    public JsonOutputFormatter(Context context, ArrayList<VendorBasicInfoItem> selectedVendorInfoList){
        this.selectedVendorInfoList = selectedVendorInfoList;
        dbAdapter = new DbAdapter(context);
        this.context = context;
        _pref = new Pref(context);
        objCompressCrop = new ImageCompressCrop(context);
        initialize();
    }

    private void initialize(){
        mainDataJsonObj = new JSONObject();
        vendorInfoJsonArray = new JSONArray();
        outputJsonContainerObj = new JSONObject();
        userJsonObj = new JSONObject();
    }

    public JSONObject getFinalOutputJson(){
        getTimingInfo();
        getFoodSoldItem();
        getGeneralVendorInfo();
        getAboutVendorInfo();
        getPurchaseFoodItem();
        getVendorAddressInfo();
        getVendorGalleryImage();
      //  getVendorGalleryVideo();
        try {

            for (int i=0; i<selectedVendorInfoList.size(); i++){
                VendorBasicInfoItem _vendorBasicInfoItem = selectedVendorInfoList.get(i);
                vendorInfoChildObj = new JSONObject();
                JSONObject foodDetailsObj = new JSONObject();
                JSONObject galleryImageobj = new JSONObject();
                JSONObject galleryVideoobj = new JSONObject();
                JSONArray parentTimingJsonArray = makeJsonTimingFormat(i);
                JSONArray parentFoodSoldJsonArray = makeJsonFoodSoldFormat(i);
                foodDetailsObj.put("food_items", parentFoodSoldJsonArray);
                JSONObject generalInfoJsonObj = makeJsonGeneralInfoFormat(i);
                JSONArray parentPurchaseFoodJsonArray = makeJsonPurchaseFoodFormat(i);
                JSONObject aboutVendorInfoJsonObj = makeJsonAboutVendorFormat(i);
                JSONObject vendorAddressInfoJsonObj = makeJsonVendorAddressFormat(i);
                JSONArray parentVendorImageJsonArray = makeJsonVendorImageFormat(i);
                JSONArray parentVendorVideoJsonArray = makeJsonVendorVideoFormat(i);
                galleryImageobj.put("gallery_img", parentVendorImageJsonArray);
                galleryVideoobj.put("gallery_vid", parentVendorVideoJsonArray);
                vendorInfoChildObj.put("shop_name", _vendorBasicInfoItem.getShopName());
                vendorInfoChildObj.put("establishment_year", _vendorBasicInfoItem.getEstablishmentYear());
                vendorInfoChildObj.put("mobile_no", _vendorBasicInfoItem.getMobileNo());
                vendorInfoChildObj.put("contact_person", _vendorBasicInfoItem.getContactPersonName());
                vendorInfoChildObj.put("timing", parentTimingJsonArray);
                vendorInfoChildObj.put("food_details", foodDetailsObj);
                vendorInfoChildObj.put("general_info", generalInfoJsonObj);
                vendorInfoChildObj.put("item_purchase", parentPurchaseFoodJsonArray);
                vendorInfoChildObj.put("about_vendor", aboutVendorInfoJsonObj);
                vendorInfoChildObj.put("location_info", vendorAddressInfoJsonObj);
                vendorInfoChildObj.put("form_img_info", galleryImageobj);
                vendorInfoChildObj.put("form_vid_info", galleryVideoobj);
                vendorInfoJsonArray.put(i, vendorInfoChildObj);
            }
            mainDataJsonObj.put("vendor_info", vendorInfoJsonArray);
            userJsonObj.put("accesstoken", _pref.getUserAccessToken());
            mainDataJsonObj.put("user", userJsonObj);
            outputJsonContainerObj.put("data", mainDataJsonObj);
            Log.e("vendorInfoJsonArray", outputJsonContainerObj.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
        return outputJsonContainerObj;
    }

    private JSONArray makeJsonTimingFormat(int position){
        JSONArray parentTimingJsonArray = new JSONArray();
        try {
            for(int i=0; i<timingInfoListAllVendor.get(position).size(); i++){
                VendorTimingItem _item = timingInfoListAllVendor.get(position).get(i);
                JSONObject childrenTimingJsonObj = new JSONObject();
                childrenTimingJsonObj.put("moc_opening", _item.getMocOpening());
                childrenTimingJsonObj.put("moc_closing", _item.getMocClosing());
                childrenTimingJsonObj.put("eoc_opening", _item.getEocOpening());
                childrenTimingJsonObj.put("eoc_closing", _item.getEocClosing());

                parentTimingJsonArray.put(i, childrenTimingJsonObj);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return parentTimingJsonArray;
    }

    private JSONObject makeJsonVendorAddressFormat(int position) {
        JSONObject vendorAddressJsonObj = new JSONObject();
        try {
            for (int i = 0; i < vendorAddressInfo.get(position).size(); i++) {
                VendorAddressInfoItem _item = vendorAddressInfo.get(position).get(i);
                vendorAddressJsonObj.put("area", _item.getArea());
                vendorAddressJsonObj.put("address", _item.getAddress());
                vendorAddressJsonObj.put("latitude", _item.getLatitude());
                vendorAddressJsonObj.put("longitude", _item.getLongitude());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vendorAddressJsonObj;
    }

    private JSONArray makeJsonFoodSoldFormat(int position){
        JSONArray parentFoodSoldJsonArray = new JSONArray();
        try {
            for(int i=0; i<foodSolsItemListAllVendor.get(position).size(); i++){
                VendorFoodSoldItem _foodSoldItem = foodSolsItemListAllVendor.get(position).get(i);
                JSONObject childrenFoodSoldItem = new JSONObject();
                childrenFoodSoldItem.put("item_name", _foodSoldItem.getFoodItem());
                childrenFoodSoldItem.put("price", _foodSoldItem.getPrice());

                parentFoodSoldJsonArray.put(i, childrenFoodSoldItem);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return parentFoodSoldJsonArray;
    }

    private JSONObject makeJsonGeneralInfoFormat(int position){
        JSONObject parentGeneralInfoJsonObj = new JSONObject();
        try {
            for(int i=0; i<foodSolsItemListAllVendor.get(position).size(); i++){
                VendorGeneralInfoItem _generalInfoItem = generalInfoVendor.get(position).get(i);
                parentGeneralInfoJsonObj.put("taste_rating", _generalInfoItem.getTasteRate());
                parentGeneralInfoJsonObj.put("hygiene_rating", _generalInfoItem.getHygieneRate());
                parentGeneralInfoJsonObj.put("food_type", _generalInfoItem.getFoodType());
                parentGeneralInfoJsonObj.put("eat_arrangement", _generalInfoItem.getArrangement());
                parentGeneralInfoJsonObj.put("ready_to_cater", _generalInfoItem.getCarterType());
                parentGeneralInfoJsonObj.put("delivery_status", _generalInfoItem.getDeliveryReady());
                parentGeneralInfoJsonObj.put("avg_sales_per_day", _generalInfoItem.getAverageSales());
                parentGeneralInfoJsonObj.put("avg_cust_per_day", _generalInfoItem.getAverageCustomers());
                parentGeneralInfoJsonObj.put("vendor_tag", _generalInfoItem.getTags());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return parentGeneralInfoJsonObj;
    }

    private JSONArray makeJsonPurchaseFoodFormat(int position){
        JSONArray parentPurchaseFoodJsonArray = new JSONArray();
        try {
            for(int i=0; i<foodSolsItemListAllVendor.get(position).size(); i++){
                VendorPurchaseItem _purchaseFoodItem = foodPuchaseItem.get(position).get(i);
                JSONObject childrenPurchaseFoodItem = new JSONObject();
                childrenPurchaseFoodItem.put("item_name", _purchaseFoodItem.getItemName());
                childrenPurchaseFoodItem.put("no_of_days", _purchaseFoodItem.getDaysNumber());
                childrenPurchaseFoodItem.put("quantity", _purchaseFoodItem.getQuantity());
                childrenPurchaseFoodItem.put("unite", _purchaseFoodItem.getUnit());
                childrenPurchaseFoodItem.put("total_cost", _purchaseFoodItem.getTotalCost());
                childrenPurchaseFoodItem.put("cost_per", _purchaseFoodItem.getCostPerUnit());
                childrenPurchaseFoodItem.put("cost_unite", _purchaseFoodItem.getUnit());

                parentPurchaseFoodJsonArray.put(i, childrenPurchaseFoodItem);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return parentPurchaseFoodJsonArray;
    }

    private JSONArray makeJsonVendorImageFormat(int position){
        JSONArray parentVendorImageJsonArray = new JSONArray();
        try {
            for(int i=0; i<vendorImage.get(position).size(); i++){
                VendorImageItem _vendorImageItem = vendorImage.get(position).get(i);
                JSONObject childrenVendorImageItem = new JSONObject();

                //Bitmap bitmap = BitmapFactory.decodeFile(_vendorImageItem.getImage_path());
                Bitmap bitmap = objCompressCrop.compressImageForPath(_vendorImageItem.getImage_path());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
                byte[] byteArrayImage = baos.toByteArray();
                String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                childrenVendorImageItem.put("img", encodedImage);
                parentVendorImageJsonArray.put(i, childrenVendorImageItem);
              //  Log.e(">>", byteArrayImage.toString());
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return parentVendorImageJsonArray;
    }

    private JSONArray makeJsonVendorVideoFormat(int position){
        JSONArray parentVendorVideoJsonArray = new JSONArray();
        try {
            for(int i=0; i<vendorVideo.get(position).size(); i++){
                VendorVideoItem _vendorVideoItem = vendorVideo.get(position).get(i);
                JSONObject childrenVendorVideoItem = new JSONObject();

               // Bitmap bitmap = objCompressCrop.compressImageForPath(_vendorImageItem.getImage_path());
               // ByteArrayOutputStream baos = new ByteArrayOutputStream();
                String filePath = _vendorVideoItem.getVideo_path();
                InputStream inStream = new FileInputStream(filePath);
                bytesAvailable = inStream.available();
                bufferSize = Math.min(bytesAvailable, 3);
                byte[] video = new byte[bufferSize];
                String encodedVideo = Base64.encodeToString(video, Base64.DEFAULT);
                childrenVendorVideoItem.put("vid", encodedVideo);
                parentVendorVideoJsonArray.put(i, childrenVendorVideoItem);
                Log.e(">>", video.toString());
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return parentVendorVideoJsonArray;
    }

    private JSONObject makeJsonAboutVendorFormat(int position) {
        JSONObject parentAboutVendorJsonObj = new JSONObject();
        try {
            for(int i=0; i<aboutVendorInfo.get(position).size(); i++) {
                AboutVendorItem _aboutVendorItem = aboutVendorInfo.get(position).get(i);
                parentAboutVendorJsonObj.put("license_status", _aboutVendorItem.getLicenseStatus());
                parentAboutVendorJsonObj.put("license_number", _aboutVendorItem.getLicenseNumber());
                parentAboutVendorJsonObj.put("addhar_number", _aboutVendorItem.getAddharNumber());
                parentAboutVendorJsonObj.put("additional_info", _aboutVendorItem.getAdditionalInfo());
            }
        } catch (Exception e){

        }
        return parentAboutVendorJsonObj;
    }

    private void getTimingInfo(){
        timingInfoListAllVendor = new ArrayList<ArrayList<VendorTimingItem>>();
        dbAdapter.open();
        for (int i=0; i<selectedVendorInfoList.size(); i++){
            ArrayList<VendorTimingItem> _timingInfoList;
            _timingInfoList = dbAdapter.getVendorTimingInfo(selectedVendorInfoList.get(i).getId());
            timingInfoListAllVendor.add(_timingInfoList);
        }
        Log.e("all vendor", String.valueOf(timingInfoListAllVendor.size()));
        dbAdapter.close();
    }

    private void getFoodSoldItem(){
        foodSolsItemListAllVendor = new ArrayList<ArrayList<VendorFoodSoldItem>>();
        dbAdapter.open();
        for (int i=0; i<selectedVendorInfoList.size(); i++){
            ArrayList<VendorFoodSoldItem> _foodSoldItem = new ArrayList<VendorFoodSoldItem>();
            _foodSoldItem = dbAdapter.getFoodSoldItemList(selectedVendorInfoList.get(i).getId());
            foodSolsItemListAllVendor.add(_foodSoldItem);
        }
        Log.e("all vendor food item", String.valueOf(timingInfoListAllVendor.size()));
        dbAdapter.close();
    }

    private void getPurchaseFoodItem(){
        foodPuchaseItem = new ArrayList<ArrayList<VendorPurchaseItem>>();
        dbAdapter.open();
        for (int i=0; i<selectedVendorInfoList.size(); i++){
            ArrayList<VendorPurchaseItem> _purchaseFoodItem = new  ArrayList<VendorPurchaseItem>();
            _purchaseFoodItem = dbAdapter.getFoodPurchaseItemList(selectedVendorInfoList.get(i).getId());
            foodPuchaseItem.add(_purchaseFoodItem);
        }
        dbAdapter.close();
    }

    private void getGeneralVendorInfo(){
        generalInfoVendor = new ArrayList<ArrayList<VendorGeneralInfoItem>>();
        dbAdapter.open();
        for (int i=0; i<selectedVendorInfoList.size(); i++){
            ArrayList<VendorGeneralInfoItem> _vendorGeneralInfoItem = new ArrayList<VendorGeneralInfoItem>();
            _vendorGeneralInfoItem = dbAdapter.getVendorGeneralInfo(selectedVendorInfoList.get(i).getId());
            generalInfoVendor.add(_vendorGeneralInfoItem);
        }
        dbAdapter.close();
    }

    private void getAboutVendorInfo(){
        aboutVendorInfo = new ArrayList<ArrayList<AboutVendorItem>>();
        dbAdapter.open();
        for (int i=0; i<selectedVendorInfoList.size(); i++){
            ArrayList<AboutVendorItem> _aboutVendorInfoItem = new ArrayList<AboutVendorItem>();
            _aboutVendorInfoItem = dbAdapter.getAboutVendorInfo(selectedVendorInfoList.get(i).getId());
            aboutVendorInfo.add(_aboutVendorInfoItem);
        }
        dbAdapter.close();
    }

    private void getVendorAddressInfo(){
        vendorAddressInfo = new ArrayList<ArrayList<VendorAddressInfoItem>>();
        dbAdapter.open();
        for (int i=0; i<selectedVendorInfoList.size(); i++){
            ArrayList<VendorAddressInfoItem> _vendorAddressInfoItem = new ArrayList<VendorAddressInfoItem>();
            _vendorAddressInfoItem = dbAdapter.getVendorAddressInfo(selectedVendorInfoList.get(i).getId());
            vendorAddressInfo.add(_vendorAddressInfoItem);
        }
        dbAdapter.close();
    }

    private void getVendorGalleryImage(){
        vendorImage = new ArrayList<ArrayList<VendorImageItem>>();
        dbAdapter.open();
        for (int i=0; i<selectedVendorInfoList.size(); i++){
            ArrayList<VendorImageItem> _vendorGalleryImageItem = new ArrayList<VendorImageItem>();
            _vendorGalleryImageItem = dbAdapter.getVendorGalleryImageInfo(selectedVendorInfoList.get(i).getId());
            vendorImage.add(_vendorGalleryImageItem);
        }
        dbAdapter.close();
    }

    private void getVendorGalleryVideo(){
        vendorVideo = new ArrayList<ArrayList<VendorVideoItem>>();
        dbAdapter.open();
        for (int i=0; i<selectedVendorInfoList.size(); i++){
            ArrayList<VendorVideoItem> _vendorGalleryVideoItem = new ArrayList<VendorVideoItem>();
            _vendorGalleryVideoItem = dbAdapter.getVendorGalleryVideoInfo(selectedVendorInfoList.get(i).getId());
            vendorVideo.add(_vendorGalleryVideoItem);
        }
        dbAdapter.close();
    }
}
