package com.example.bluehorsesoftkol.ekplatevendor.dbpackage;

public class DbConstants {

    public static final String TAG_DB_NAME = "db_ekplate_vendor";


    public static final int TAG_DB_VERSION = 1;
    public static final String TAG_TB_USER = "tb_user";
    public static final String TAG_TB_VENDOR = "tb_vendor";
    public static final String TAG_TB_TIMING = "tb_timing";
    public static final String TAG_TB_FOOD_MENU = "tb_food_menu";
    public static final String TAG_TB_GENERAL_INFO = "tb_general_info";
    public static final String TAG_TB_PURCHASE_ITEM = "tb_purchase_item";
    public static final String TAG_TB_ADDRESS = "tb_address";
    public static final String TAG_TB_ABOUT_VENDOR = "tb_about_vendor";
    public static final String TAG_TB_GALLERY_IMAGE = "tb_gallery_image";
    public static final String TAG_TB_GALLERY_VIDEO = "tb_gallery_video";
    public static final String TAG_TB_FOOD_ITEM = "tb_food_item";
    public static final String TAG_TB_MONTHLY_GRAPH = "tb_monthly_graph";

    /*******USER TABLE*******/
    public static final String TAG_USER_ID = "id";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_MOBILE = "mobile_no";
    public static final String TAG_ACCESSTOKEN = "accesstoken";

    /*******VENDOR TABLE*******/
    public static final String TAG_VENDOR_ID = "id";
    public static final String TAG_VENDOR_ESTABLISHMENT_YEAR = "establishment_year";
    public static final String TAG_VENDOR_SHOP_NAME = "shop_name";
    public static final String TAG_VENDOR_SHOP_MOBILE = "mobile_no";
    public static final String TAG_VENDOR_CONTACT_PERSON = "contact_person_name";
    public static final String TAG_VENDOR_USER_ACCESSTOKEN = "added_by";
    public static final String TAG_VENDOR_COMPLETE_STEP = "complete_step";

    /*******TIMING TABLE******/
    public static final String TAG_VENDOR_TIMING_ID = "id";
    public static final String TAG_VENDOR_TIMING_DAY = "day";
    public static final String TAG_VENDOR_TIMING_MOC_OPENING = "moc_opening";
    public static final String TAG_VENDOR_TIMING_MOC_CLOSING = "moc_closing";
    public static final String TAG_VENDOR_TIMING_EOC_OPENING = "eoc_opening";
    public static final String TAG_VENDOR_TIMING_EOC_CLOSING = "eoc_closing";
    public static final String TAG_VENDOR_TIMING_VENDOR_ID = "vendor_id";

    /*******MENU ITEM TABLE******/
    public static final String TAG_MENU_ITEM_ID = "id";
    public static final String TAG_MENU_VENDOR_ID = "vendor_id";
    public static final String TAG_MENU_ITEM_FOOD_NAME = "food_name";
    public static final String TAG_MENU_ITEM_PRICE = "price";

    /*******GENERAL INFO TABLE******/
    public static final String TAG_GENERAL_INFO_ID = "id";
    public static final String TAG_GENERAL_INFO_VENDOR_ID = "vendor_id";
    public static final String TAG_GENERAL_INFO_TASTE_RATE = "taste_rate";
    public static final String TAG_GENERAL_INFO_HYGIENE_RATE = "hygiene_rate";
    public static final String TAG_GENERAL_INFO_FOOD_TYPE = "food_type";
    public static final String TAG_GENERAL_INFO_ARRANGEMENT = "arrangement";
    public static final String TAG_GENERAL_INFO_CARTER_TYPE = "carter_type";
    public static final String TAG_GENERAL_INFO_DELIVERY_READY = "delivery_ready";
    public static final String TAG_GENERAL_INFO_AVG_SALES_PER_DAY = "avg_sales";
    public static final String TAG_GENERAL_INFO_AVG_CUSTOMERS_PER_DAY = "avg_customers";
    public static final String TAG_GENERAL_INFO_TAGS = "tags";

    /*******PURCHASE ITEM TABLE******/
    public static final String TAG_PURCHASE_ITEM_ID = "id";
    public static final String TAG_PURCHASE_ITEM_VENDOR_ID = "vendor_id";
    public static final String TAG_PURCHASE_ITEM_ITEM = "item";
    public static final String TAG_PURCHASE_ITEM_DAY_NUMBER = "days";
    public static final String TAG_PURCHASE_ITEM_QTY = "qty";
    public static final String TAG_PURCHASE_ITEM_UNIT = "unit";
    public static final String TAG_PURCHASE_ITEM_TOTAL_COST = "total_cost";
    public static final String TAG_PURCHASE_ITEM_PER_UNIT = "per_unite";
    public static final String TAG_PURCHASE_ITEM_COST_PER_UNIT = "cost_per_unit";

    /*******ADDRESS TABLE******/
    public static final String TAG_ADDRESS_ID = "id";
    public static final String TAG_ADDRESS_VENDOR_ID = "vendor_id";
    public static final String TAG_ADDRESS_AREA = "area";
    public static final String TAG_ADDRESS_ADDRESS = "address";
    public static final String TAG_ADDRESS_LATITUDE = "latitude";
    public static final String TAG_ADDRESS_LONGITUDE = "longitude";

    /*******ABOUT VENDOR TABLE******/
    public static final String TAG_ABOUT_ID = "id";
    public static final String TAG_ABOUT_VENDOR_VENDOR_ID = "vendor_id";
    public static final String TAG_ABOUT_VENDOR_LICENSE_STATUS = "license_status";
    public static final String TAG_ABOUT_VENDOR_LICENSE_NUMBER = "license_number";
    public static final String TAG_ABOUT_VENDOR_ADDHAR_NUMBER = "addhar_number";
    public static final String TAG_ABOUT_VENDOR_ADDITIONAL_INFO = "additional_info";

    /*******GALLERY IMAGE TABLE******/
    public static final String TAG_GALLERY_IMAGE_ID = "id";
    public static final String TAG_GALLERY_IMAGE_VENDOR_ID = "vendor_id";
    public static final String TAG_GALLERY_IMAGE_PATH = "image_path";

    /*******GALLERY VIDEO TABLE******/
    public static final String TAG_GALLERY_VIDEO_ID = "id";
    public static final String TAG_GALLERY_VIDEO_VENDOR_ID = "vendor_id";
    public static final String TAG_GALLERY_VIDEO_PATH = "video_path";

    /*******FOOD ITEM TABLE******/
    public static final String TAG_FOOD_ID = "id";
    public static final String TAG_FOOD_NAME = "food_name";
    public static final String TAG_FOOD_IMAGE = "food_image";

    /*******MONTHLY GRAPH TABLE******/
    public static final String TAG_GRAPH_ID = "id";
    public static final String TAG_GRAPH_DATE = "graph_date";
    public static final String TAG_GRAPH_COUNT = "graph_count";


    public static final String
            TAG_CREATE_USER_TABLE = "create table tb_user" +
            "(id integer primary key autoincrement," +
            " email text not null," +
            " mobile_no text not null," +
            " accesstoken text not null)";

    public static final String TAG_QUERY_USER_LAST_ID = "SELECT accesstoken FROM tb_user ORDER BY id DESC";

    public static final String TAG_CREATE_VENDOR_TABLE = "create table tb_vendor" +
            "(id integer primary key autoincrement," +
            " establishment_year text," +
            " shop_name text," +
            " mobile_no text," +
            " contact_person_name text," +
            " added_by integer," +
            " complete_step Integer)";

    //////////////////////////////////////////

    public static final String TAG_QUERY_VENDOR_LAST_ID = "SELECT id FROM tb_vendor ORDER BY id DESC";

    ///////////////////////////////////////

    public static final String TAG_QUERY_VENDOR_COMPLETE_STEP = "SELECT complete_step FROM tb_vendor";

    public static final String TAG_CREATE_TIMING_TABLE = "create table tb_timing" +
                "(id integer primary key autoincrement," +
                " day text," +
                " moc_opening text," +
                " moc_closing text," +
                " eoc_opening text," +
                " eoc_closing text," +
                " vendor_id integer)";

    //////////////////////////////////////////

    public static final String TAG_SELECT_FROM_TB_FOOD_MENU = "SELECT id FROM tb_food_menu_details ORDER BY id DESC";

    ///////////////////////////////////////

    public static final String TAG_CREATE_FOOD_MENU_TABLE =
            "create table tb_food_menu" +
                    "(id integer primary key autoincrement," +
                    " vendor_id integer," +
                    " food_name text," +
                    " price text)";

    public static final String TAG_CREATE_GENERAL_INFO_TABLE =
            "create table tb_general_info" +
                    "(id integer primary key autoincrement," +
                    " vendor_id integer," +
                    " taste_rate text," +
                    " hygiene_rate text," +
                    " food_type text," +
                    " arrangement text," +
                    " carter_type text," +
                    " delivery_ready text," +
                    " avg_sales text," +
                    " avg_customers text," +
                    " tags text)";

    public static final String TAG_CREATE_PURCHASE_ITEM_TABLE =
            "create table tb_purchase_item" +
                    "(id integer primary key autoincrement," +
                    " vendor_id integer," +
                    " item text," +
                    " days text," +
                    " qty text," +
                    " unit text," +
                    " total_cost text," +
                    " per_unite text," +
                    " cost_per_unit text)";

    public static final String TAG_CREATE_ADDRESS_TABLE =
            "create table tb_address" +
                    "(id integer primary key autoincrement," +
                    " vendor_id integer," +
                    " area text," +
                    " address text," +
                    " latitude text," +
                    " longitude text)";

    public static final String TAG_CREATE_ABOUT_VENDOR_TABLE =
            "create table tb_about_vendor" +
                    "(id integer primary key autoincrement," +
                    " vendor_id integer," +
                    " license_status text," +
                    " license_number text," +
                    " addhar_number text," +
                    " additional_info text)";

    public static final String TAG_CREATE_GALLERY_IMAGE_TABLE =
            "create table tb_gallery_image" +
                    "(id integer primary key autoincrement," +
                    " vendor_id integer," +
                    " image_path text)";

    public static final String TAG_CREATE_GALLERY_VIDEO_TABLE =
            "create table tb_gallery_video" +
                    "(id integer primary key autoincrement," +
                    " vendor_id integer," +
                    " video_path text)";

    public static final String
            TAG_CREATE_FOOD_ITEM_TABLE =
            "create table tb_food_item" +
            "(id integer primary key autoincrement," +
            " food_name text not null," +
            " food_image text not null)";

    public static final String
            TAG_CREATE_MONTHLY_GRAPH_TABLE =
            "create table tb_monthly_graph" +
                    "(id integer primary key autoincrement," +
                    " graph_date text not null," +
                    " graph_count text not null)";


    public static final String TAG_DROP_USER_TABLE =
            "DROP TABLE IF EXISTS tb_user";

    public static final String TAG_DELETE_USER_TABLE =
            "DELETE TABLE IF EXISTS tb_user";

    public static final String TAG_DROP_VENDOR_TABLE =
            "DROP TABLE IF EXISTS tb_vendor";

    public static final String TAG_DROP_TIMING_TABLE =
            "DROP TABLE IF EXISTS tb_timing";

    public static final String TAG_DROP_FOOD_MENU =
            "DROP TABLE IF EXISTS tb_food_menu";

    public static final String TAG_DROP_GENERAL_INFO =
            "DROP TABLE IF EXISTS tb_general_info";

    public static final String TAG_DROP_ITEM_PURCHASE_TABLE =
            "DROP TABLE IF EXISTS tb_purchase_item";

    public static final String TAG_DROP_ADDRESS_TABLE =
            "DROP TABLE IF EXISTS tb_address";

    public static final String TAG_DROP_ABOUT_VENDOR =
            "DROP TABLE IF EXISTS tb_about_vendor";

    public static final String TAG_DROP_GALLERY_IMAGE_TABLE =
            "DROP TABLE IF EXISTS tb_gallery_image";

    public static final String TAG_DROP_GALLERY_VIDEO_TABLE =
            "DROP TABLE IF EXISTS tb_gallery_video";

    public static final String TAG_FETCH_USER_DETAILS =
            "SELECT * FROM tb_user";

    public static final String TAG_FETCH_VENDOR_ADDRESS_INFO =
            "SELECT * FROM tb_address";

    public static final String TAG_FETCH_VENDOR_BASIC_INFO =
            "SELECT * FROM tb_vendor";

    public static final String TAG_FETCH_VENDOR_TIMING_INFO =
            "SELECT * FROM tb_timing";

    public static final String TAG_FETCH_FOOD_PURCHASE_INFO =
            "SELECT * FROM tb_purchase_item";

    public static final String TAG_FETCH_FOOD_SOLD_INFO =
            "SELECT * FROM tb_food_menu";

    public static final String TAG_FETCH_GENERAL_INFO =
            "SELECT * FROM tb_general_info";

    public static final String TAG_FETCH_ABOUT_VENDOR_INFO =
            "SELECT * FROM tb_about_vendor";

    public static final String TAG_FETCH_IMAGE_PATH_VENDOR_INFO =
            "SELECT * FROM tb_gallery_image";

    public static final String TAG_FETCH_VIDEO_PATH_VENDOR_INFO =
            "SELECT * FROM tb_gallery_video";

    public static final String TAG_FETCH_FOOD_ITEMS =
            "SELECT * FROM tb_food_item";

    public static final String TAG_FETCH_MONTHLY_GRAPH =
            "SELECT * FROM tb_monthly_graph";

}
