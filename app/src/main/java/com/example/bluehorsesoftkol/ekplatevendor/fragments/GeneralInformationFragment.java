package com.example.bluehorsesoftkol.ekplatevendor.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.ConstantClass;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.activity.vendor.ActivityAddVendor;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorGeneralInfoItem;
import com.example.bluehorsesoftkol.ekplatevendor.dbpackage.DbAdapter;
import java.util.ArrayList;


public class GeneralInformationFragment extends Fragment {

    private View rootView;

    private RatingBar ratingBarHygine, ratingBarTaste;
    private Button btnFoodVeg, btnFoodNonVeg, btnFoodJain, btnEatingStanding, btnEatingSeating, btnEatingServeInCar, btnEatingOther,
            btnCarterNo, btnCarterCityOnly, btnCarterAcrossIndia, btnCarterInternational, btnReadyYes, btnReadyNo, btnBack, btnNext;
    private EditText etAvgSalesPerDay, etAvgCustPerDay, etAddTags;

    private String[] availableFoodTypeList = {"", "", ""}, eatingArrangementList = {"", "", "", ""};
    private int vegFoodTypeFlag = 0, nonVegFoodTypeFlag = 0, jainFoodTypeFlag = 0, standingFlag = 0,
            seatingFlag = 0, inCarFlag = 0, otherFlag = 0;
    String foodType = "", eatingType = "", carterType = "", isReady = "", tags = "",
            ratingTaste = "0.0", ratingHygine = "0.0", avgSalesPerDay = "", avgCustomersPerDay = "";

    private ActivityAddVendor activityAddVendor;
    private DbAdapter db;
    private Pref _pref;
    private ArrayList<VendorGeneralInfoItem> generalInfoItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        rootView = inflater.inflate(R.layout.fragment_general_information,container,false);
        initialize();

        db.open();
        if (db.getVendorCompleteStep()>=5) {
            generalInfoItems = db.getVendorGeneralInfo(Integer.parseInt(
                    _pref.getVendorId()));
            setGeneralInfoFields();
        }
        db.close();

        onClick();
        return rootView;
    }

    private void initialize() {

        activityAddVendor = (ActivityAddVendor) getActivity();
        db = new DbAdapter(getActivity());
        _pref = new Pref(getActivity());

        ratingBarHygine = (RatingBar) rootView.findViewById(R.id.ratingBarHygine);
        ratingBarTaste = (RatingBar) rootView.findViewById(R.id.ratingBarTaste);
        btnFoodVeg = (Button) rootView.findViewById(R.id.btnFoodVeg);
        btnFoodNonVeg = (Button) rootView.findViewById(R.id.btnFoodNonVeg);
        btnFoodJain = (Button) rootView.findViewById(R.id.btnFoodJain);
        btnEatingStanding = (Button) rootView.findViewById(R.id.btnEatingStanding);
        btnEatingSeating = (Button) rootView.findViewById(R.id.btnEatingSeating);
        btnEatingServeInCar = (Button) rootView.findViewById(R.id.btnEatingServeInCar);
        btnEatingOther = (Button) rootView.findViewById(R.id.btnEatingOther);
        btnCarterNo = (Button) rootView.findViewById(R.id.btnCarterNo);
        btnCarterCityOnly = (Button) rootView.findViewById(R.id.btnCarterCityOnly);
        btnCarterAcrossIndia = (Button) rootView.findViewById(R.id.btnCarterAcrossIndia);
        btnCarterInternational = (Button) rootView.findViewById(R.id.btnCarterInternational);
        btnReadyYes = (Button) rootView.findViewById(R.id.btnReadyYes);
        btnReadyNo = (Button) rootView.findViewById(R.id.btnReadyNo);
        btnBack = (Button) rootView.findViewById(R.id.btnBack);
        btnNext = (Button) rootView.findViewById(R.id.btnNext);
        etAvgSalesPerDay = (EditText) rootView.findViewById(R.id.etAvgSalesPerDay);
        etAvgCustPerDay = (EditText) rootView.findViewById(R.id.etAvgCustPerDay);
        etAddTags = (EditText) rootView.findViewById(R.id.etAddTags);

        LayerDrawable starsHygine = (LayerDrawable) ratingBarHygine.getProgressDrawable();
        LayerDrawable starsTaste = (LayerDrawable) ratingBarTaste.getProgressDrawable();
        starsHygine.getDrawable(2).setColorFilter(Color.parseColor("#FEBE10"), PorterDuff.Mode.SRC_ATOP);
        starsTaste.getDrawable(2).setColorFilter(Color.parseColor("#FEBE10"), PorterDuff.Mode.SRC_ATOP);

    }

    private void setGeneralInfoFields(){
        ratingBarHygine.setRating(Float.parseFloat(generalInfoItems.get(0).getHygieneRate()));
        ratingBarTaste.setRating(Float.parseFloat(generalInfoItems.get(0).getTasteRate()));
        String tempFoodTypeList[] = generalInfoItems.get(0).getFoodType().split(",");
        for (int i=0; i<tempFoodTypeList.length; i++){
            if(tempFoodTypeList[i].equals("Veg")){
                btnFoodVeg.setBackgroundResource(R.drawable.button_shape_red);
                vegFoodTypeFlag = 1;
                availableFoodTypeList[0] = "Veg";
            } else if(tempFoodTypeList[i].equals("Non-Veg")){
                btnFoodNonVeg.setBackgroundResource(R.drawable.button_shape_red);
                nonVegFoodTypeFlag = 1;
                availableFoodTypeList[1] = "Non-Veg";
            } else {
                btnFoodJain.setBackgroundResource(R.drawable.button_shape_red);
                jainFoodTypeFlag =1;
                availableFoodTypeList[2] = "Jain";

                btnFoodVeg.setBackgroundResource(R.drawable.button_shape_red);
                vegFoodTypeFlag = 1;
                availableFoodTypeList[0] = "Veg";
            }
        }
        String tempArrangementList[] = generalInfoItems.get(0).getArrangement().split(",");
        for (int i=0; i<tempArrangementList.length; i++){
            if(tempArrangementList[i].equals("FR")){
                btnEatingStanding.setBackgroundResource(R.drawable.button_shape_red);
                standingFlag = 1;
                eatingArrangementList[0] = "FR";
            } else if(tempArrangementList[i].equals("DE")){
                btnEatingSeating.setBackgroundResource(R.drawable.button_shape_red);
                seatingFlag = 1;
                eatingArrangementList[1] = "DE";
            } else if(tempArrangementList[i].equals("In Car")){
                btnEatingServeInCar.setBackgroundResource(R.drawable.button_shape_red);
                inCarFlag = 1;
                eatingArrangementList[2] = "In Car";
            } else {
                btnEatingOther.setBackgroundResource(R.drawable.button_shape_red);
                otherFlag = 1;
                eatingArrangementList[3] = "Other";
            }
        }

        if(generalInfoItems.get(0).getCarterType().equals("No")){
            unSelectAllCarter();
            btnCarterNo.setBackgroundResource(R.drawable.button_shape_red);
            carterType = "No";
        } else if(generalInfoItems.get(0).getCarterType().equals("City")){
            unSelectAllCarter();
            btnCarterCityOnly.setBackgroundResource(R.drawable.button_shape_red);
            carterType = "City";
        } else if(generalInfoItems.get(0).getCarterType().equals("India")){
            unSelectAllCarter();
            btnCarterAcrossIndia.setBackgroundResource(R.drawable.button_shape_red);
            carterType = "India";
        } else {
            unSelectAllCarter();
            btnCarterInternational.setBackgroundResource(R.drawable.button_shape_red);
            carterType = "International";
        }

        if(generalInfoItems.get(0).getDeliveryReady().equals("no")){
            unSelectAllReady();
            btnReadyNo.setBackgroundResource(R.drawable.button_shape_red);
            isReady = "no";
        } else {
            unSelectAllReady();
            btnReadyYes.setBackgroundResource(R.drawable.button_shape_red);
            isReady = "yes";
        }

        etAvgSalesPerDay.setText(generalInfoItems.get(0).getAverageSales());
        etAvgCustPerDay.setText(generalInfoItems.get(0).getAverageCustomers());
        etAddTags.setText(generalInfoItems.get(0).getTags());
    }

    private void onClick(){

        btnFoodVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //unSelectAllFood();
                if (vegFoodTypeFlag == 0) {
                    btnFoodVeg.setBackgroundResource(R.drawable.button_shape_red);
                    vegFoodTypeFlag = 1;
                    availableFoodTypeList[0] = "Veg";
                } else {
                    if(jainFoodTypeFlag != 1){
                        btnFoodVeg.setBackgroundResource(R.drawable.button_shape_gray);
                        vegFoodTypeFlag = 0;
                        availableFoodTypeList[0] = "";
                    }
                }
            }
        });

        btnFoodNonVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //unSelectAllFood();
                if (nonVegFoodTypeFlag == 0) {
                    btnFoodNonVeg.setBackgroundResource(R.drawable.button_shape_red);
                    nonVegFoodTypeFlag = 1;
                    availableFoodTypeList[1] = "Non-Veg";

                } else {
                    btnFoodNonVeg.setBackgroundResource(R.drawable.button_shape_gray);
                    nonVegFoodTypeFlag = 0;
                    availableFoodTypeList[1] = "";
                }
            }
        });

        btnFoodJain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //unSelectAllFood();
                if (jainFoodTypeFlag == 0){
                    btnFoodJain.setBackgroundResource(R.drawable.button_shape_red);
                    jainFoodTypeFlag =1;
                    availableFoodTypeList[2] = "Jain";

                    btnFoodVeg.setBackgroundResource(R.drawable.button_shape_red);
                    vegFoodTypeFlag = 1;
                    availableFoodTypeList[0] = "Veg";

                } else {
                    btnFoodJain.setBackgroundResource(R.drawable.button_shape_gray);
                    jainFoodTypeFlag = 0;
                    availableFoodTypeList[2] = "";

                    btnFoodVeg.setBackgroundResource(R.drawable.button_shape_gray);
                    vegFoodTypeFlag = 0;
                    availableFoodTypeList[0] = "";
                }
            }
        });

        btnEatingStanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //unSelectAllEating();
                if (standingFlag == 0){
                    btnEatingStanding.setBackgroundResource(R.drawable.button_shape_red);
                    standingFlag = 1;
                    eatingArrangementList[0] = "FR";
                } else {
                    btnEatingStanding.setBackgroundResource(R.drawable.button_shape_gray);
                    standingFlag = 0;
                    eatingArrangementList[0] = "";
                }
            }
        });

        btnEatingSeating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //unSelectAllEating();
                if (seatingFlag == 0){
                    btnEatingSeating.setBackgroundResource(R.drawable.button_shape_red);
                    seatingFlag = 1;
                    eatingArrangementList[1] = "DE";
                } else {
                    btnEatingSeating.setBackgroundResource(R.drawable.button_shape_gray);
                    seatingFlag = 0;
                    eatingArrangementList[1] = "";
                }
            }
        });

        btnEatingServeInCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //unSelectAllEating();
                if (inCarFlag == 0){
                    btnEatingServeInCar.setBackgroundResource(R.drawable.button_shape_red);
                    inCarFlag = 1;
                    eatingArrangementList[2] = "In Car";
                } else {
                    btnEatingServeInCar.setBackgroundResource(R.drawable.button_shape_gray);
                    inCarFlag = 0;
                    eatingArrangementList[2] = "";
                }
            }
        });

        btnEatingOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //unSelectAllEating();
                if (otherFlag == 0){
                    btnEatingOther.setBackgroundResource(R.drawable.button_shape_red);
                    otherFlag = 1;
                    eatingArrangementList[3] = "Other";
                } else {
                    btnEatingOther.setBackgroundResource(R.drawable.button_shape_gray);
                    otherFlag = 0;
                    eatingArrangementList[3] = "";
                }
            }
        });

        btnCarterNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unSelectAllCarter();
                btnCarterNo.setBackgroundResource(R.drawable.button_shape_red);
                carterType = "No";

            }
        });

        btnCarterCityOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unSelectAllCarter();
                btnCarterCityOnly.setBackgroundResource(R.drawable.button_shape_red);
                carterType = "City";

            }
        });

        btnCarterAcrossIndia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unSelectAllCarter();
                btnCarterAcrossIndia.setBackgroundResource(R.drawable.button_shape_red);
                carterType = "India";

            }
        });

        btnCarterInternational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unSelectAllCarter();
                btnCarterInternational.setBackgroundResource(R.drawable.button_shape_red);
                carterType = "International";

            }
        });

        btnReadyYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unSelectAllReady();
                btnReadyYes.setBackgroundResource(R.drawable.button_shape_red);
                isReady = "yes";

            }
        });

        btnReadyNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unSelectAllReady();
                btnReadyNo.setBackgroundResource(R.drawable.button_shape_red);
                isReady = "no";

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                foodType = "";
                eatingType = "";
                ratingTaste = String.valueOf(ratingBarTaste.getRating());
                ratingHygine = String.valueOf(ratingBarHygine.getRating());
                avgSalesPerDay = etAvgSalesPerDay.getText().toString();
                avgCustomersPerDay = etAvgCustPerDay.getText().toString();
                tags = etAddTags.getText().toString();

                for (int i = 0; i < availableFoodTypeList.length; i++) {
                    if (!availableFoodTypeList[i].equals("")) {
                        foodType = foodType + availableFoodTypeList[i] + ",";
                    }
                }
                for (int i = 0; i < eatingArrangementList.length; i++) {
                    if (!eatingArrangementList[i].equals("")) {
                        eatingType = eatingType + eatingArrangementList[i] + ",";
                    }
                }
                if (foodType.equals("")) {
                    Toast.makeText(getActivity(), "Please Select One Food Type", Toast.LENGTH_SHORT).show();
                } else if (carterType.equals("")) {
                    Toast.makeText(getActivity(), "Please Select One Carter Type", Toast.LENGTH_SHORT).show();
                } else if (isReady.equals("")) {
                    Toast.makeText(getActivity(), "Please Select Delivery Available or Not", Toast.LENGTH_SHORT).show();
                } else {
                    db.open();
                    if (db.getVendorCompleteStep()>=5){
                        updateInformation(generalInfoItems.get(0).getId());
                        Toast.makeText(getActivity(), "General Info Updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        saveInformation();
                        Toast.makeText(getActivity(), "General Info Inserted successfully", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                    activityAddVendor.findViewById(R.id.llItemPurchase).setEnabled(true);
                    activityAddVendor.loadItemPurchaseFragment();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityAddVendor.loadFoodSoldFragment();
            }
        });

    }

    private void unSelectAllCarter(){

        btnCarterNo.setBackgroundResource(R.drawable.button_shape_gray);
        btnCarterCityOnly.setBackgroundResource(R.drawable.button_shape_gray);
        btnCarterAcrossIndia.setBackgroundResource(R.drawable.button_shape_gray);
        btnCarterInternational.setBackgroundResource(R.drawable.button_shape_gray);
    }

    private void unSelectAllReady(){

        btnReadyYes.setBackgroundResource(R.drawable.button_shape_gray);
        btnReadyNo.setBackgroundResource(R.drawable.button_shape_gray);
    }

    private void saveInformation(){

        db.open();
        Long success = db.insertGeneralInfo(ratingTaste, ratingHygine, foodType, eatingType, carterType, isReady, avgSalesPerDay, avgCustomersPerDay, tags);
        db.updateVendorCompleteStep(5);
        db.close();
        foodType = ""; eatingType = ""; carterType = ""; isReady = ""; tags = ""; ratingTaste = "0.0"; ratingHygine = "0.0"; avgSalesPerDay = ""; avgCustomersPerDay = "";
    }

    private void updateInformation(int id){

        db.open();
        int success = db.updateGeneralInfo(ratingTaste, ratingHygine, foodType, eatingType, carterType, isReady, avgSalesPerDay, avgCustomersPerDay, tags);
        db.close();
        foodType = ""; eatingType = ""; carterType = ""; isReady = ""; tags = ""; ratingTaste = "0.0"; ratingHygine = "0.0"; avgSalesPerDay = ""; avgCustomersPerDay = "";

    }
}
