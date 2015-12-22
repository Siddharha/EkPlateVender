package com.example.bluehorsesoftkol.ekplatevendor.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.CallServiceAction;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.NetworkConnectionCheck;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.activity.vendor.ActivityAddVendor;
import com.example.bluehorsesoftkol.ekplatevendor.activity.vendor.ActivityHome;
import com.example.bluehorsesoftkol.ekplatevendor.dbpackage.DbAdapter;
import com.example.bluehorsesoftkol.ekplatevendor.interfaces.BackgroundActionInterface;
import org.json.JSONObject;
import java.util.HashMap;

public class BasicInformationFragment extends Fragment implements BackgroundActionInterface {

    private View rootView;

    private EditText etVendorMobile, etVendorShopName, etEstablishmentYear, etContactPerson;
    private Button btnBack, btnNext;
    String vendorMobileNo = "", vendorShopName = "", establishmentYear = "", contactPerson = "";

    private ActivityAddVendor activityAddVendor;
    private NetworkConnectionCheck _connectionCheck;
    private CallServiceAction _serviceAction;
    private ProgressDialog progressDialog;
    private DbAdapter db;
    private Pref _pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        rootView = inflater.inflate(R.layout.fragment_basic_information, container, false);

        initialize();
        onClick();

        db.open();
        if(!_pref.getVendorId().equals("")) {
            if (db.getVendorCompleteStep()>=1) {
                HashMap<String, String> vendorBasicInfo = db.getVendorBasicInfo();
                etVendorMobile.setText(vendorBasicInfo.get("VendorMobileNo"));
                etVendorShopName.setText(vendorBasicInfo.get("VendorShopName"));
                etEstablishmentYear.setText(vendorBasicInfo.get("EstablishmentYear"));
                etContactPerson.setText(vendorBasicInfo.get("ContactPerson"));
            }
        }
        db.close();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        etVendorMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable vendorMobileNumber) {
                if (vendorMobileNumber.length() == 10) {
                    if (_connectionCheck.isNetworkAvailable()) {
                        try {
                            JSONObject parentJsonObj = new JSONObject();
                            JSONObject childJsonObj = new JSONObject();
                            childJsonObj.put("mobile", vendorMobileNumber);
                            parentJsonObj.put("data", childJsonObj);
                            setUpProgressDialog();
                            _serviceAction.actionInterface = BasicInformationFragment.this;
                            _serviceAction.requestVersionApi(parentJsonObj, "check-vendor-mobile");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        _connectionCheck.getNetworkActiveAlert().show();
                    }
                }
            }
        });
    }

    private void initialize() {

        activityAddVendor = (ActivityAddVendor) getActivity();
        _connectionCheck = new NetworkConnectionCheck(getActivity());
        _serviceAction = new CallServiceAction(getActivity());
        db = new DbAdapter(getActivity());
        _pref = new Pref(getActivity());

        etVendorMobile = (EditText)rootView.findViewById(R.id.etVendorMobile);
        etVendorShopName = (EditText)rootView.findViewById(R.id.etVendorShopName);
        etEstablishmentYear = (EditText)rootView.findViewById(R.id.etEstablishmentYear);
        etContactPerson = (EditText)rootView.findViewById(R.id.etContactPerson);
        btnBack = (Button)rootView.findViewById(R.id.btnBack);
        btnNext = (Button)rootView.findViewById(R.id.btnNext);


    }

    private void onClick() {

        etVendorMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vendorMobileNo = etVendorMobile.getText().toString();
                vendorShopName = etVendorShopName.getText().toString();
                establishmentYear = etEstablishmentYear.getText().toString();
                contactPerson = etContactPerson.getText().toString();

                if (vendorMobileNo.equals("")){
                    Toast.makeText(getActivity(),"Please Enter Mobile Number",Toast.LENGTH_SHORT).show();
                }else if (vendorMobileNo.length()<10){
                    Toast.makeText(getActivity(),"Mobile Number should be 10 digit",Toast.LENGTH_SHORT).show();
                }
                else if (vendorShopName.equals("")){
                    Toast.makeText(getActivity(),"Please Enter Vendor or Shop Name",Toast.LENGTH_SHORT).show();
                }
                else if (establishmentYear.equals("")){
                    Toast.makeText(getActivity(),"Please Enter Establishment Year",Toast.LENGTH_SHORT).show();
                }
                else if (establishmentYear.length()<4 ){
                    Toast.makeText(getActivity(),"Establishment Year sholud be 4 digit",Toast.LENGTH_SHORT).show();
                }
                else {
                    db.open();
                    if(!_pref.getVendorId().equals("")) {
                        if (db.getVendorCompleteStep()>=1) {
                            int success = db.updateBasicInfo(vendorShopName, establishmentYear, vendorMobileNo, contactPerson);
                            Toast.makeText(getActivity(), "Basic Info Updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Long success = db.insertVendorInfo(vendorShopName, establishmentYear, vendorMobileNo,
                                contactPerson, _pref.getUserAccessToken());
                        _pref.saveVendorId(db.getLastVendorId());
                        Toast.makeText(getActivity(), "Basic Info Inserted successfully", Toast.LENGTH_SHORT).show();

                        int captured_work = Integer.valueOf(_pref.getUserCapturedWork());
                        ++captured_work;
                        _pref.saveUserCapturedWork(String.valueOf(captured_work));
                    }
                    db.close();
                    activityAddVendor.findViewById(R.id.llLocation).setEnabled(true);
                    activityAddVendor.loadLocationFragment();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ActivityHome.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                activityAddVendor.finish();
            }
        });
    }

    private void setUpProgressDialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onCompleted(JSONObject response) {
        progressDialog.dismiss();
        try{
            JSONObject jsonObjError = response.getJSONObject("errNode");
            if(jsonObjError.getInt("errCode") == 0) {
                JSONObject jsonObjData = response.getJSONObject("data");
                if(jsonObjData.getString("exist").equals("no")){
                    Toast.makeText(getActivity(), "You can proceed", Toast.LENGTH_SHORT).show();
                } else {
                    etVendorMobile.setText("");
                    Toast.makeText(getActivity(), "Vendor with this number already exist! Please Try again with different number", Toast.LENGTH_SHORT).show();
                }
            } else {
                etVendorMobile.setText("");
                Toast.makeText(getActivity(), "Some problem occurs! Please Try again with different number", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}