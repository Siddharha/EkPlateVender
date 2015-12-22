package com.example.bluehorsesoftkol.ekplatevendor.dbpackage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.bluehorsesoftkol.ekplatevendor.Utils.ConstantClass;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.bean.AboutVendorItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.MonthlyGraphItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorAddressInfoItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorBasicInfoItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorFoodSoldItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorGeneralInfoItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorImageItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorPurchaseItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorTimingItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorVideoItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rahul on 9/23/2015.
 */
public class DbAdapter {

    private DbHandler dbHandler;
    private Context context;
    private SQLiteDatabase dbSqLiteDatabase;
    private Pref _pref;

    public DbAdapter(Context context){
        this.context = context;
        dbHandler = new DbHandler(context);
        _pref = new Pref(context);
    }

    public DbAdapter open(){
        dbHandler = new DbHandler(context);
        dbSqLiteDatabase = dbHandler.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHandler.close();
    }

    private class DbHandler extends SQLiteOpenHelper{

        private DbHandler(Context context) {
            super(context, DbConstants.TAG_DB_NAME, null, DbConstants.TAG_DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DbConstants.TAG_CREATE_USER_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_CREATE_VENDOR_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_CREATE_TIMING_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_CREATE_FOOD_MENU_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_CREATE_GENERAL_INFO_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_CREATE_PURCHASE_ITEM_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_CREATE_ADDRESS_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_CREATE_ABOUT_VENDOR_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_CREATE_GALLERY_IMAGE_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_CREATE_GALLERY_VIDEO_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_CREATE_FOOD_ITEM_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_CREATE_MONTHLY_GRAPH_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
            sqLiteDatabase.execSQL(DbConstants.TAG_DROP_USER_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_DROP_VENDOR_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_DROP_TIMING_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_DROP_FOOD_MENU);
            sqLiteDatabase.execSQL(DbConstants.TAG_DROP_GENERAL_INFO);
            sqLiteDatabase.execSQL(DbConstants.TAG_DROP_ITEM_PURCHASE_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_DROP_ADDRESS_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_DROP_ABOUT_VENDOR);
            sqLiteDatabase.execSQL(DbConstants.TAG_DROP_GALLERY_IMAGE_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_DROP_GALLERY_VIDEO_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_CREATE_FOOD_ITEM_TABLE);
            sqLiteDatabase.execSQL(DbConstants.TAG_CREATE_MONTHLY_GRAPH_TABLE);
            onCreate(sqLiteDatabase);
        }
    }

    public void insertUserLogin(String email,String mobile_no,String accesstoken){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_EMAIL, email);
        contentValues.put(DbConstants.TAG_MOBILE, mobile_no);
        contentValues.put(DbConstants.TAG_ACCESSTOKEN, accesstoken);
        Long ll = dbSqLiteDatabase.insert(DbConstants.TAG_TB_USER, null, contentValues);
    }

    public Long insertVendorInfo(String shop_name, String year, String mobile_no, String contact_person, String added_by){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_VENDOR_SHOP_NAME, shop_name);
        contentValues.put(DbConstants.TAG_VENDOR_ESTABLISHMENT_YEAR, year);
        contentValues.put(DbConstants.TAG_VENDOR_SHOP_MOBILE, mobile_no);
        contentValues.put(DbConstants.TAG_VENDOR_CONTACT_PERSON, contact_person);
        contentValues.put(DbConstants.TAG_VENDOR_USER_ACCESSTOKEN, added_by);
        contentValues.put(DbConstants.TAG_VENDOR_COMPLETE_STEP, 1);

        Long success = dbSqLiteDatabase.insert(DbConstants.TAG_TB_VENDOR, null, contentValues);
        return success;
    }

    public String getLastVendorId(){
        Cursor cursor = dbSqLiteDatabase.rawQuery(DbConstants.TAG_QUERY_VENDOR_LAST_ID, null);
        cursor.moveToNext();
        String lastVendorId = cursor.getString(cursor.getColumnIndex(DbConstants.TAG_VENDOR_ID));
        cursor.close();

        return lastVendorId;
    }

    public int getVendorCompleteStep(){
        String vendorCompleteStepFetchQuery = DbConstants.TAG_QUERY_VENDOR_COMPLETE_STEP + " WHERE " +
                DbConstants.TAG_VENDOR_ID + "='" +_pref.getVendorId()+"'";
        Cursor cursor = dbSqLiteDatabase.rawQuery(vendorCompleteStepFetchQuery, null);
        cursor.moveToNext();
        int VendorCompleteStep = cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_VENDOR_COMPLETE_STEP));
        cursor.close();

        return VendorCompleteStep;
    }

    public int updateVendorCompleteStep(int complete_step){

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_VENDOR_COMPLETE_STEP, complete_step);

        int success = dbSqLiteDatabase.update(DbConstants.TAG_TB_VENDOR, contentValues,
                "id = " + _pref.getVendorId(), null);

        return success;
    }

    public Long insertVendorLocation(String area, String address, String latitude, String longitude) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_ADDRESS_VENDOR_ID, _pref.getVendorId());
        contentValues.put(DbConstants.TAG_ADDRESS_AREA, area);
        contentValues.put(DbConstants.TAG_ADDRESS_ADDRESS, address);
        contentValues.put(DbConstants.TAG_ADDRESS_LATITUDE, latitude);
        contentValues.put(DbConstants.TAG_ADDRESS_LONGITUDE, longitude);

        Long success = dbSqLiteDatabase.insert(DbConstants.TAG_TB_ADDRESS, null, contentValues);
        return success;
    }

    public Long insertTiming(String day, String mocOpening, String mocClosing, String eocOpening, String eocClosing){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_VENDOR_TIMING_VENDOR_ID, _pref.getVendorId());
        contentValues.put(DbConstants.TAG_VENDOR_TIMING_DAY, day);
        contentValues.put(DbConstants.TAG_VENDOR_TIMING_MOC_OPENING, mocOpening);
        contentValues.put(DbConstants.TAG_VENDOR_TIMING_MOC_CLOSING, mocClosing);
        contentValues.put(DbConstants.TAG_VENDOR_TIMING_EOC_OPENING, eocOpening);
        contentValues.put(DbConstants.TAG_VENDOR_TIMING_EOC_CLOSING, eocClosing);

        Long success = dbSqLiteDatabase.insert(DbConstants.TAG_TB_TIMING, null, contentValues);
        return success;
    }

    public Long insertFoodMenu(String foodName, String price){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_MENU_VENDOR_ID, _pref.getVendorId());
        contentValues.put(DbConstants.TAG_MENU_ITEM_FOOD_NAME, foodName);
        contentValues.put(DbConstants.TAG_MENU_ITEM_PRICE, price);
        Long success = dbSqLiteDatabase.insert(DbConstants.TAG_TB_FOOD_MENU, null, contentValues);
        return success;
    }

    public Long insertGeneralInfo(String ratingTaste, String ratingBarHygine, String foodType, String eatingType, String carterType, String isReady, String avgSales, String avgCustomers, String tags){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_GENERAL_INFO_VENDOR_ID, _pref.getVendorId());
        contentValues.put(DbConstants.TAG_GENERAL_INFO_TASTE_RATE, ratingTaste);
        contentValues.put(DbConstants.TAG_GENERAL_INFO_HYGIENE_RATE, ratingBarHygine);
        contentValues.put(DbConstants.TAG_GENERAL_INFO_FOOD_TYPE, foodType);
        contentValues.put(DbConstants.TAG_GENERAL_INFO_ARRANGEMENT, eatingType);
        contentValues.put(DbConstants.TAG_GENERAL_INFO_CARTER_TYPE, carterType);
        contentValues.put(DbConstants.TAG_GENERAL_INFO_DELIVERY_READY, isReady);
        contentValues.put(DbConstants.TAG_GENERAL_INFO_AVG_SALES_PER_DAY, avgSales);
        contentValues.put(DbConstants.TAG_GENERAL_INFO_AVG_CUSTOMERS_PER_DAY, avgCustomers);
        contentValues.put(DbConstants.TAG_GENERAL_INFO_TAGS, tags);

        Long success = dbSqLiteDatabase.insert(DbConstants.TAG_TB_GENERAL_INFO, null, contentValues);
        return success;
    }

    public Long insertPurchaseItems(String purchaseItem, String numberOfDays, String quantity, String unit, String totalCost,
                                    String perUnit, String costPerUnit){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_PURCHASE_ITEM_VENDOR_ID, _pref.getVendorId());
        contentValues.put(DbConstants.TAG_PURCHASE_ITEM_ITEM, purchaseItem);
        contentValues.put(DbConstants.TAG_PURCHASE_ITEM_DAY_NUMBER, numberOfDays);
        contentValues.put(DbConstants.TAG_PURCHASE_ITEM_QTY, quantity);
        contentValues.put(DbConstants.TAG_PURCHASE_ITEM_UNIT, unit);
        contentValues.put(DbConstants.TAG_PURCHASE_ITEM_TOTAL_COST, totalCost);
        contentValues.put(DbConstants.TAG_PURCHASE_ITEM_PER_UNIT, perUnit);
        contentValues.put(DbConstants.TAG_PURCHASE_ITEM_COST_PER_UNIT, costPerUnit);

        Long success = dbSqLiteDatabase.insert(DbConstants.TAG_TB_PURCHASE_ITEM, null, contentValues);
        return success;
    }

    public Long insertVendorImage(String imagePath) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_GALLERY_IMAGE_VENDOR_ID, _pref.getVendorId());
        contentValues.put(DbConstants.TAG_GALLERY_IMAGE_PATH, imagePath);

        Long success = dbSqLiteDatabase.insert(DbConstants.TAG_TB_GALLERY_IMAGE, null, contentValues);
        return success;
    }

    public Long insertVendorVideo(String videoPath) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_GALLERY_VIDEO_VENDOR_ID, _pref.getVendorId());
        contentValues.put(DbConstants.TAG_GALLERY_VIDEO_PATH, videoPath);

        Long success = dbSqLiteDatabase.insert(DbConstants.TAG_TB_GALLERY_VIDEO, null, contentValues);
        return success;
    }

    public Long insertAboutVendor(String licenseExistFlag, String licenseNo, String addharNo, String aboutVendorDesc){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_ABOUT_VENDOR_VENDOR_ID, _pref.getVendorId());
        contentValues.put(DbConstants.TAG_ABOUT_VENDOR_LICENSE_STATUS, licenseExistFlag);
        contentValues.put(DbConstants.TAG_ABOUT_VENDOR_LICENSE_NUMBER, licenseNo);
        contentValues.put(DbConstants.TAG_ABOUT_VENDOR_ADDHAR_NUMBER, addharNo);
        contentValues.put(DbConstants.TAG_ABOUT_VENDOR_ADDITIONAL_INFO, aboutVendorDesc);

        Long success = dbSqLiteDatabase.insert(DbConstants.TAG_TB_ABOUT_VENDOR, null, contentValues);
        return success;
    }

    public Long insertFoodItem(String foodName, String foodImage) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_FOOD_NAME, foodName);
        contentValues.put(DbConstants.TAG_FOOD_IMAGE, foodImage);

        return dbSqLiteDatabase.insert(DbConstants.TAG_TB_FOOD_ITEM, null, contentValues);
    }

    public Long insertMonthlyGraph(String date, String count) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_GRAPH_DATE, date);
        contentValues.put(DbConstants.TAG_GRAPH_COUNT, count);

        return dbSqLiteDatabase.insert(DbConstants.TAG_TB_MONTHLY_GRAPH, null, contentValues);
    }

    public String getCurrentUserDetails(){
        String userDetailsFetchQuery = DbConstants.TAG_FETCH_USER_DETAILS + " WHERE " +
                DbConstants.TAG_ACCESSTOKEN + "='"+_pref.getUserAccessToken()+"'";
        Cursor cursor = dbSqLiteDatabase.rawQuery(userDetailsFetchQuery, null);
        cursor.moveToNext();
        String accessToken = cursor.getString(cursor.getColumnIndex(DbConstants.TAG_ACCESSTOKEN));
        cursor.close();

        return accessToken;
    }

    public ArrayList<VendorBasicInfoItem> getVendorBasicInfo(String accesstoken){
        ArrayList<VendorBasicInfoItem> basicInfoItems = new ArrayList<VendorBasicInfoItem>();
        String fetchingQuery = DbConstants.TAG_FETCH_VENDOR_BASIC_INFO + " WHERE " + DbConstants.TAG_VENDOR_USER_ACCESSTOKEN +
                 " = '" + accesstoken + "'";
        Cursor cursor = dbSqLiteDatabase.rawQuery(fetchingQuery, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            VendorBasicInfoItem _item = new VendorBasicInfoItem();
            _item.setId(cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_VENDOR_ID)));
            _item.setShopName(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_VENDOR_SHOP_NAME)));
            _item.setEstablishmentYear(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_VENDOR_ESTABLISHMENT_YEAR)));
            _item.setMobileNo(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_VENDOR_SHOP_MOBILE)));
            _item.setContactPersonName(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_VENDOR_CONTACT_PERSON)));
            _item.setAddedBy(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_VENDOR_USER_ACCESSTOKEN)));
            basicInfoItems.add(_item);
            cursor.moveToNext();
        }
        return basicInfoItems;
    }

    public HashMap<String, String> getVendorBasicInfo(){
        HashMap<String, String> vendorBasicInfo = new HashMap<String, String>();
        String fetchingQuery = DbConstants.TAG_FETCH_VENDOR_BASIC_INFO + " WHERE " + DbConstants.TAG_VENDOR_ID +
                " = '" + _pref.getVendorId() + "'";
        Cursor cursor = dbSqLiteDatabase.rawQuery(fetchingQuery, null);
        cursor.moveToFirst();
        vendorBasicInfo.put("VendorShopName", cursor.getString(cursor.getColumnIndex(DbConstants.TAG_VENDOR_SHOP_NAME)));
        vendorBasicInfo.put("EstablishmentYear", cursor.getString(cursor.getColumnIndex(DbConstants.TAG_VENDOR_ESTABLISHMENT_YEAR)));
        vendorBasicInfo.put("VendorMobileNo", cursor.getString(cursor.getColumnIndex(DbConstants.TAG_VENDOR_SHOP_MOBILE)));
        vendorBasicInfo.put("ContactPerson", cursor.getString(cursor.getColumnIndex(DbConstants.TAG_VENDOR_CONTACT_PERSON)));

        return vendorBasicInfo;
    }

    public ArrayList<VendorAddressInfoItem> getVendorAddressInfo(int vendorId) {
        ArrayList<VendorAddressInfoItem> vendorAddressInfoItems = new ArrayList<VendorAddressInfoItem>();
        String fetchingVendorAddressInfo = DbConstants.TAG_FETCH_VENDOR_ADDRESS_INFO + " WHERE " +
                DbConstants.TAG_ADDRESS_VENDOR_ID + " = '" + vendorId + "'";
        Cursor cursor = dbSqLiteDatabase.rawQuery(fetchingVendorAddressInfo, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            VendorAddressInfoItem _item = new VendorAddressInfoItem();
            _item.setId(cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_ADDRESS_ID)));
            _item.setVendorId(cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_ADDRESS_VENDOR_ID)));
            _item.setArea(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_ADDRESS_AREA)));
            _item.setAddress(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_ADDRESS_ADDRESS)));
            _item.setLatitude(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_ADDRESS_LATITUDE)));
            _item.setLongitude(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_ADDRESS_LONGITUDE)));
            vendorAddressInfoItems.add(_item);
            cursor.moveToNext();
        }
        return vendorAddressInfoItems;
    }

    public ArrayList<VendorTimingItem> getVendorTimingInfo(int vendorId){
        ArrayList<VendorTimingItem> timingInfo = new ArrayList<VendorTimingItem>();
        String fetchingQueryTimingInfo = DbConstants.TAG_FETCH_VENDOR_TIMING_INFO + " WHERE " + DbConstants.TAG_VENDOR_TIMING_VENDOR_ID+
                " = '" + vendorId + "'";
        Cursor cursor = dbSqLiteDatabase.rawQuery(fetchingQueryTimingInfo, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            VendorTimingItem _item = new VendorTimingItem();
            _item.setId(cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_VENDOR_TIMING_ID)));
            _item.setDay(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_VENDOR_TIMING_DAY)));
            _item.setMocOpening(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_VENDOR_TIMING_MOC_OPENING)));
            _item.setMocClosing(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_VENDOR_TIMING_MOC_CLOSING)));
            _item.setEocOpening(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_VENDOR_TIMING_EOC_OPENING)));
            _item.setEocClosing(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_VENDOR_TIMING_EOC_CLOSING)));
            _item.setVendorId(cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_VENDOR_TIMING_VENDOR_ID)));
            timingInfo.add(_item);
            cursor.moveToNext();
        }
        return timingInfo;
    }

    public ArrayList<VendorFoodSoldItem> getFoodSoldItemList(int vendorId){
        ArrayList<VendorFoodSoldItem> foodSoldItems = new ArrayList<VendorFoodSoldItem>();
        String fetchingQueryFoodSoldInfo = DbConstants.TAG_FETCH_FOOD_SOLD_INFO + " WHERE " + DbConstants.TAG_MENU_VENDOR_ID +
                " = '" + vendorId + "'";
        Cursor cursor = dbSqLiteDatabase.rawQuery(fetchingQueryFoodSoldInfo, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            VendorFoodSoldItem _item = new VendorFoodSoldItem();
            _item.setId(cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_MENU_ITEM_ID)));
            _item.setVendorId(cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_MENU_VENDOR_ID)));
            _item.setFoodItem(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_MENU_ITEM_FOOD_NAME)));
            _item.setPrice(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_MENU_ITEM_PRICE)));
            foodSoldItems.add(_item);
            cursor.moveToNext();
        }
        Log.e("food item size", String.valueOf(foodSoldItems.size()));
        return foodSoldItems;
    }

    public ArrayList<VendorGeneralInfoItem> getVendorGeneralInfo(int vendorId){
        ArrayList<VendorGeneralInfoItem> generalInfoItems = new ArrayList<VendorGeneralInfoItem>();
        String fetchingVendorGeneralInfo = DbConstants.TAG_FETCH_GENERAL_INFO + " WHERE " + DbConstants.TAG_GENERAL_INFO_VENDOR_ID +
                " = '" + vendorId + "'";
        Cursor cursor = dbSqLiteDatabase.rawQuery(fetchingVendorGeneralInfo, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            VendorGeneralInfoItem _item = new VendorGeneralInfoItem();
            _item.setId(cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_GENERAL_INFO_ID)));
            _item.setVendorId(cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_GENERAL_INFO_VENDOR_ID)));
            _item.setTasteRate(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_GENERAL_INFO_TASTE_RATE)));
            _item.setHygieneRate(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_GENERAL_INFO_HYGIENE_RATE)));
            _item.setFoodType(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_GENERAL_INFO_FOOD_TYPE)));
            _item.setArrangement(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_GENERAL_INFO_ARRANGEMENT)));
            _item.setCarterType(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_GENERAL_INFO_CARTER_TYPE)));
            _item.setDeliveryReady(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_GENERAL_INFO_DELIVERY_READY)));
            _item.setAverageSales(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_GENERAL_INFO_AVG_SALES_PER_DAY)));
            _item.setAverageCustomers(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_GENERAL_INFO_AVG_CUSTOMERS_PER_DAY)));
            _item.setTags(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_GENERAL_INFO_TAGS)));
            generalInfoItems.add(_item);
            cursor.moveToNext();
        }
        return generalInfoItems;
    }

    public ArrayList<VendorPurchaseItem> getFoodPurchaseItemList(int vendorId) {
        ArrayList<VendorPurchaseItem> foodPurchaseItems = new ArrayList<VendorPurchaseItem>();
        String fetchingFoodPurchaseInfo = DbConstants.TAG_FETCH_FOOD_PURCHASE_INFO + " WHERE " + DbConstants.TAG_PURCHASE_ITEM_VENDOR_ID +
                " = '" + vendorId + "'";
        Cursor cursor = dbSqLiteDatabase.rawQuery(fetchingFoodPurchaseInfo, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            VendorPurchaseItem _item = new VendorPurchaseItem();
            _item.setId(cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_PURCHASE_ITEM_ID)));
            _item.setVendorId(cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_PURCHASE_ITEM_VENDOR_ID)));
            _item.setItemName(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_PURCHASE_ITEM_ITEM)));
            _item.setDaysNumber(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_PURCHASE_ITEM_DAY_NUMBER)));
            _item.setQuantity(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_PURCHASE_ITEM_QTY)));
            _item.setUnit(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_PURCHASE_ITEM_UNIT)));
            _item.setTotalCost(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_PURCHASE_ITEM_TOTAL_COST)));
            _item.setPerUnit(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_PURCHASE_ITEM_PER_UNIT)));
            _item.setCostPerUnit(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_PURCHASE_ITEM_COST_PER_UNIT)));
            foodPurchaseItems.add(_item);
            cursor.moveToNext();
        }
        return foodPurchaseItems;
    }

    public ArrayList<VendorImageItem> getVendorGalleryImageInfo(int vendorId) {
        ArrayList<VendorImageItem> vendorImageItems = new ArrayList<VendorImageItem>();
        String fetchingVendorGalleryImageInfo = DbConstants.TAG_FETCH_IMAGE_PATH_VENDOR_INFO + " WHERE " +
                DbConstants.TAG_GALLERY_IMAGE_VENDOR_ID + " = '" + vendorId + "'";
        Cursor cursor = dbSqLiteDatabase.rawQuery(fetchingVendorGalleryImageInfo, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            VendorImageItem _item = new VendorImageItem();
            _item.setId(cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_GALLERY_IMAGE_ID)));
            _item.setVendorId(cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_GALLERY_IMAGE_VENDOR_ID)));
            _item.setImage_path(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_GALLERY_IMAGE_PATH)));
            vendorImageItems.add(_item);
            cursor.moveToNext();
        }
        return vendorImageItems;
    }

    public ArrayList<VendorVideoItem> getVendorGalleryVideoInfo(int vendorId) {
        ArrayList<VendorVideoItem> vendorVideoItems = new ArrayList<VendorVideoItem>();
        String fetchingVendorGalleryVideoInfo = DbConstants.TAG_FETCH_VIDEO_PATH_VENDOR_INFO + " WHERE " +
                DbConstants.TAG_GALLERY_VIDEO_VENDOR_ID + " = '" + vendorId + "'";
        Cursor cursor = dbSqLiteDatabase.rawQuery(fetchingVendorGalleryVideoInfo, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            VendorVideoItem _item = new VendorVideoItem();
            _item.setId(cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_GALLERY_VIDEO_ID)));
            _item.setVendorId(cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_GALLERY_VIDEO_VENDOR_ID)));
            _item.setVideo_path(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_GALLERY_VIDEO_PATH)));
            vendorVideoItems.add(_item);
            cursor.moveToNext();
        }
        return vendorVideoItems;
    }

    public ArrayList<AboutVendorItem> getAboutVendorInfo(int vendorId){
        ArrayList<AboutVendorItem> aboutVendorInfoItems = new ArrayList<AboutVendorItem>();
        String fetchingAboutVendorInfo = DbConstants.TAG_FETCH_ABOUT_VENDOR_INFO + " WHERE " + DbConstants.TAG_ABOUT_VENDOR_VENDOR_ID +
                " = '" + vendorId + "'";
        Cursor cursor = dbSqLiteDatabase.rawQuery(fetchingAboutVendorInfo, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            AboutVendorItem _item = new AboutVendorItem();
            _item.setId(cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_ABOUT_ID)));
            _item.setVendorId(cursor.getInt(cursor.getColumnIndex(DbConstants.TAG_ABOUT_VENDOR_VENDOR_ID)));
            _item.setLicenseStatus(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_ABOUT_VENDOR_LICENSE_STATUS)));
            _item.setLicenseNumber(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_ABOUT_VENDOR_LICENSE_NUMBER)));
            _item.setAddharNumber(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_ABOUT_VENDOR_ADDHAR_NUMBER)));
            _item.setAdditionalInfo(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_ABOUT_VENDOR_ADDITIONAL_INFO)));
            aboutVendorInfoItems.add(_item);
            cursor.moveToNext();
        }
        return aboutVendorInfoItems;
    }

    public ArrayList<MonthlyGraphItem> getMonthlyGraphInfo(){
        ArrayList<MonthlyGraphItem> monthlyGraphItems = new ArrayList<MonthlyGraphItem>();
        String fetchingMonthlyGraphInfo = DbConstants.TAG_FETCH_MONTHLY_GRAPH;
        Cursor cursor = dbSqLiteDatabase.rawQuery(fetchingMonthlyGraphInfo, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            MonthlyGraphItem _item = new MonthlyGraphItem();
            _item.setDate(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_GRAPH_DATE)));
            _item.setCount(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_GRAPH_COUNT)));
            monthlyGraphItems.add(_item);
            cursor.moveToNext();
        }
        return monthlyGraphItems;
    }

    public ArrayList<String> getFoodNames() {
        ArrayList<String> foodNames = new ArrayList<>();
        String fetchingFoodNames = DbConstants.TAG_FETCH_FOOD_ITEMS;
        Cursor cursor = dbSqLiteDatabase.rawQuery(fetchingFoodNames, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            foodNames.add(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_FOOD_NAME)));
            cursor.moveToNext();
        }
        return foodNames;
    }

    public ArrayList<String> getFoodImages() {
        ArrayList<String> foodImages = new ArrayList<>();
        String fetchingFoodImages = DbConstants.TAG_FETCH_FOOD_ITEMS;
        Cursor cursor = dbSqLiteDatabase.rawQuery(fetchingFoodImages, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            foodImages.add(cursor.getString(cursor.getColumnIndex(DbConstants.TAG_FOOD_IMAGE)));
            cursor.moveToNext();
        }
        return foodImages;
    }

    public int updateBasicInfo(String shop_name, String year, String mobile_no, String contact_person){

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_VENDOR_ESTABLISHMENT_YEAR, year); //These Fields should be your String values of actual column names
        contentValues.put(DbConstants.TAG_VENDOR_SHOP_NAME, shop_name);
        contentValues.put(DbConstants.TAG_VENDOR_SHOP_MOBILE, mobile_no);
        contentValues.put(DbConstants.TAG_VENDOR_CONTACT_PERSON, contact_person);

        int success = dbSqLiteDatabase.update(DbConstants.TAG_TB_VENDOR, contentValues,
                "id = " + _pref.getVendorId(), null);

        return success;
    }

    public int updateVendorLocation(String area, String address, String latitude, String longitude) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_ADDRESS_AREA, area);
        contentValues.put(DbConstants.TAG_ADDRESS_ADDRESS, address);
        contentValues.put(DbConstants.TAG_ADDRESS_LATITUDE, latitude);
        contentValues.put(DbConstants.TAG_ADDRESS_LONGITUDE, longitude);

        int success = dbSqLiteDatabase.update(DbConstants.TAG_TB_ADDRESS, contentValues,
                "id = " + _pref.getVendorId(), null);
        return success;
    }


    public int updateTiming(int id, String mocOpening, String mocClosing, String eocOpening, String eocClosing){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_VENDOR_TIMING_MOC_OPENING, mocOpening);
        contentValues.put(DbConstants.TAG_VENDOR_TIMING_MOC_CLOSING, mocClosing);
        contentValues.put(DbConstants.TAG_VENDOR_TIMING_EOC_OPENING, eocOpening);
        contentValues.put(DbConstants.TAG_VENDOR_TIMING_EOC_CLOSING, eocClosing);

        int success = dbSqLiteDatabase.update(DbConstants.TAG_TB_TIMING, contentValues,
                "id = " + id, null);

        return success;
    }

    public int updateFoodSoldItem(String foodName, String price) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_MENU_ITEM_FOOD_NAME, foodName);
        contentValues.put(DbConstants.TAG_MENU_ITEM_PRICE, price);

        int success = dbSqLiteDatabase.update(DbConstants.TAG_TB_FOOD_MENU, contentValues,
                "id = " + _pref.getVendorId(), null);
        return success;
    }

    public int updateGeneralInfo(String ratingTaste, String ratingBarHygine, String foodType,
                                 String eatingType, String carterType, String isReady, String avgSales, String avgCustomers, String tags){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_GENERAL_INFO_TASTE_RATE, ratingTaste);
        contentValues.put(DbConstants.TAG_GENERAL_INFO_HYGIENE_RATE, ratingBarHygine);
        contentValues.put(DbConstants.TAG_GENERAL_INFO_FOOD_TYPE, foodType);
        contentValues.put(DbConstants.TAG_GENERAL_INFO_ARRANGEMENT, eatingType);
        contentValues.put(DbConstants.TAG_GENERAL_INFO_CARTER_TYPE, carterType);
        contentValues.put(DbConstants.TAG_GENERAL_INFO_DELIVERY_READY, isReady);
        contentValues.put(DbConstants.TAG_GENERAL_INFO_AVG_SALES_PER_DAY, avgSales);
        contentValues.put(DbConstants.TAG_GENERAL_INFO_AVG_CUSTOMERS_PER_DAY, avgCustomers);
        contentValues.put(DbConstants.TAG_GENERAL_INFO_TAGS, tags);

        int success = dbSqLiteDatabase.update(DbConstants.TAG_TB_GENERAL_INFO, contentValues,
                "id = " + _pref.getVendorId(), null);

        return success;
    }

    public int updatePurchaseItem(String purchaseItem, String numberOfDays, String quantity, String unit, String totalCost,
                                  String perUnit, String costPerUnit) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_PURCHASE_ITEM_ITEM, purchaseItem);
        contentValues.put(DbConstants.TAG_PURCHASE_ITEM_DAY_NUMBER, numberOfDays);
        contentValues.put(DbConstants.TAG_PURCHASE_ITEM_QTY, quantity);
        contentValues.put(DbConstants.TAG_PURCHASE_ITEM_UNIT, unit);
        contentValues.put(DbConstants.TAG_PURCHASE_ITEM_TOTAL_COST, totalCost);
        contentValues.put(DbConstants.TAG_PURCHASE_ITEM_PER_UNIT, perUnit);
        contentValues.put(DbConstants.TAG_PURCHASE_ITEM_COST_PER_UNIT, costPerUnit);

        int success = dbSqLiteDatabase.update(DbConstants.TAG_TB_PURCHASE_ITEM, contentValues,
                "id = " + _pref.getVendorId(), null);
        return success;
    }

    public int updateAboutVendor(String licenseExistFlag, String licenseNo, String addharNo, String aboutVendorDesc){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.TAG_ABOUT_VENDOR_LICENSE_STATUS, licenseExistFlag);
        contentValues.put(DbConstants.TAG_ABOUT_VENDOR_LICENSE_NUMBER, licenseNo);
        contentValues.put(DbConstants.TAG_ABOUT_VENDOR_ADDHAR_NUMBER, addharNo);
        contentValues.put(DbConstants.TAG_ABOUT_VENDOR_ADDITIONAL_INFO, aboutVendorDesc);

        int success = dbSqLiteDatabase.update(DbConstants.TAG_TB_ABOUT_VENDOR, contentValues,
                "id = " + _pref.getVendorId(), null);
        return success;
    }

    public boolean deleteBasicInfo(int id){
        return dbSqLiteDatabase.delete(DbConstants.TAG_TB_VENDOR, "id = " + id, null) > 0;
    }

    public boolean deleteFoodSoldItem(int id){
        return dbSqLiteDatabase.delete(DbConstants.TAG_TB_FOOD_MENU, "id = " + id, null) > 0;
    }

    public boolean deletePurchaseItem(int id){
        return dbSqLiteDatabase.delete(DbConstants.TAG_TB_PURCHASE_ITEM, "id = " + id, null) > 0;
    }

    public boolean deletePics(int id){
        return dbSqLiteDatabase.delete(DbConstants.TAG_TB_GALLERY_IMAGE, "id = " + id, null) > 0;
    }

    public boolean deleteVideos(int id){
        return dbSqLiteDatabase.delete(DbConstants.TAG_TB_GALLERY_VIDEO, "id = " + id, null) > 0;
    }

    public void deleteAllRecord(){
        dbSqLiteDatabase.delete(DbConstants.TAG_TB_VENDOR, null, null);
        dbSqLiteDatabase.delete(DbConstants.TAG_TB_TIMING, null, null);
        dbSqLiteDatabase.delete(DbConstants.TAG_TB_FOOD_MENU, null, null);
        dbSqLiteDatabase.delete(DbConstants.TAG_TB_GENERAL_INFO, null, null);
        dbSqLiteDatabase.delete(DbConstants.TAG_TB_PURCHASE_ITEM, null, null);
        dbSqLiteDatabase.delete(DbConstants.TAG_TB_ADDRESS, null, null);
        dbSqLiteDatabase.delete(DbConstants.TAG_TB_ABOUT_VENDOR, null, null);
        dbSqLiteDatabase.delete(DbConstants.TAG_TB_GALLERY_IMAGE, null, null);
        dbSqLiteDatabase.delete(DbConstants.TAG_TB_GALLERY_VIDEO, null, null);
    }

    public void deleteFoodItemRecord(){
        dbSqLiteDatabase.delete(DbConstants.TAG_TB_FOOD_ITEM, null, null);
    }

    public void deleteMonthlyGraphRecord(){
        dbSqLiteDatabase.delete(DbConstants.TAG_TB_MONTHLY_GRAPH, null, null);
    }
}
