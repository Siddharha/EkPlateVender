package com.example.bluehorsesoftkol.ekplatevendor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.ConstantClass;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.activity.vendor.ActivityAddVendor;
import com.example.bluehorsesoftkol.ekplatevendor.activity.vendor.ActivityHome;
import com.example.bluehorsesoftkol.ekplatevendor.bean.AboutVendorItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorGeneralInfoItem;
import com.example.bluehorsesoftkol.ekplatevendor.dbpackage.DbAdapter;

import java.util.ArrayList;

public class AboutVendorFragment extends Fragment {

    private View rootView;
    private Button btnYes, btnNo, btnBack,btnNext;
    private EditText etAadharCardNo, etLicenseNo, etAdditionalInformation;

    private boolean licenseExistFlag = true;

    private ActivityAddVendor activityAddVendor;
    private DbAdapter db;
    private Pref _pref;
    private ArrayList<AboutVendorItem> aboutVendorItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        rootView = inflater.inflate(R.layout.fragment_about_vendor,container,false);

        initialize();

        db.open();
        if (db.getVendorCompleteStep()>=8) {
            aboutVendorItems = db.getAboutVendorInfo(Integer.parseInt(
                    _pref.getVendorId()));
            setaboutVendorFields();
        }
        db.close();

        onClick();

        return rootView;
    }
    private void initialize() {

        activityAddVendor = (ActivityAddVendor) getActivity();
        db = new DbAdapter(getActivity());
        _pref = new Pref(getActivity());

        btnYes = (Button) rootView.findViewById(R.id.btnYes);
        btnNo = (Button) rootView.findViewById(R.id.btnNo);
        btnBack = (Button) rootView.findViewById(R.id.btnBack);
        btnNext = (Button) rootView.findViewById(R.id.btnNext);
        etAadharCardNo  = (EditText) rootView.findViewById(R.id.etAadharCardNo);
        etLicenseNo  = (EditText) rootView.findViewById(R.id.etLicenseNo);
        etAdditionalInformation  = (EditText) rootView.findViewById(R.id.etAdditionalInformation);
        btnYes.setSelected(true);
    }

    private void setaboutVendorFields(){

        etLicenseNo.setText(aboutVendorItems.get(0).getLicenseNumber());
        etAadharCardNo.setText(aboutVendorItems.get(0).getAddharNumber());
        etAdditionalInformation.setText(aboutVendorItems.get(0).getAdditionalInfo());

        if(aboutVendorItems.get(0).getLicenseStatus().equals("yes")){
            unsetButtonBg();
            btnYes.setSelected(true);
            licenseExistFlag = true;
        }
        else{
            unsetButtonBg();
            btnNo.setSelected(true);
            licenseExistFlag = false;
        }

    }

    private void onClick(){

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unsetButtonBg();
                btnYes.setSelected(true);
                licenseExistFlag = true;
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unsetButtonBg();
                btnNo.setSelected(true);
                licenseExistFlag = false;
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(licenseExistFlag==true){
                    if (etLicenseNo.getText().toString().equals("")){
                        Toast.makeText(getActivity(), "Please insert the license no.", Toast.LENGTH_SHORT).show();
                    }else if(etAadharCardNo.getText().toString().length() <16)
                    {
                        Toast.makeText(getActivity(), "Please insert the 16 Digit Adhar Number no!", Toast.LENGTH_SHORT).show();
                    } else if(etAdditionalInformation.getText().toString().equals("")){
                        Toast.makeText(getActivity(), "Please insert some additional info.", Toast.LENGTH_SHORT).show();
                    } else {
                        String licenseStatus = "yes"; // change
                        saveAboutVendorData(licenseStatus, etLicenseNo.getText().toString(), // change
                                etAadharCardNo.getText().toString(), etAdditionalInformation.getText().toString());

                        ConstantClass.CURRENT_TAB = 0;
                        Intent intent = new Intent(getActivity(), ActivityHome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        activityAddVendor.finish();
                    }
                } else if(etAdditionalInformation.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please insert some additional info.", Toast.LENGTH_SHORT).show();
                } else {
                    String licenseStatus = "no"; // change
                    saveAboutVendorData(licenseStatus, etLicenseNo.getText().toString(), // change
                            etAadharCardNo.getText().toString(), etAdditionalInformation.getText().toString());
                    ConstantClass.CURRENT_TAB = 0;
                    Intent intent = new Intent(getActivity(), ActivityHome.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    activityAddVendor.finish();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityAddVendor.loadPicVideoFragment();
            }
        });
    }

    private void unsetButtonBg(){
        btnYes.setSelected(false);
        btnNo.setSelected(false);
    }

    private void saveAboutVendorData(String  licenseExistFlag, String licenseNo, String addharNo, String aboutVendorDesc){ // change

        db.open();
        if (db.getVendorCompleteStep()>=8) {
            int success = db.updateAboutVendor(licenseExistFlag, licenseNo, addharNo, aboutVendorDesc);
            Toast.makeText(getActivity(), "About Vendor Info Updated successfully", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Long success = db.insertAboutVendor(licenseExistFlag, licenseNo, addharNo, aboutVendorDesc);
            db.updateVendorCompleteStep(8);
            Toast.makeText(getActivity(), "About Vendor Info Inserted successfully", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}
