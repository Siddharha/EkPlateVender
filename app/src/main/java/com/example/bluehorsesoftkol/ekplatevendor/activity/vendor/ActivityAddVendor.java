package com.example.bluehorsesoftkol.ekplatevendor.activity.vendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.CallServiceAction;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.ConstantClass;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.NetworkConnectionCheck;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.activity.registration.ActivityFAQ;
import com.example.bluehorsesoftkol.ekplatevendor.activity.registration.ActivityLogin;
import com.example.bluehorsesoftkol.ekplatevendor.dbpackage.DbAdapter;
import com.example.bluehorsesoftkol.ekplatevendor.fragments.AboutVendorFragment;
import com.example.bluehorsesoftkol.ekplatevendor.fragments.BasicInformationFragment;
import com.example.bluehorsesoftkol.ekplatevendor.fragments.FoodSoldFragment;
import com.example.bluehorsesoftkol.ekplatevendor.fragments.GeneralInformationFragment;
import com.example.bluehorsesoftkol.ekplatevendor.fragments.ItemPurchaseFragment;
import com.example.bluehorsesoftkol.ekplatevendor.fragments.PicVideoFragment;
import com.example.bluehorsesoftkol.ekplatevendor.fragments.LocationFragment;
import com.example.bluehorsesoftkol.ekplatevendor.fragments.TimingFragment;
import com.example.bluehorsesoftkol.ekplatevendor.interfaces.BackgroundActionInterface;

import org.json.JSONObject;

public class ActivityAddVendor extends AppCompatActivity implements BackgroundActionInterface {

    public static boolean editVendor;
    public static int editVendorId;
    Toolbar toolbar;
    TextView toolbarHeaderText;
    LinearLayout llMain, llBasicInfo, llLocation, llTiming, llFoodSold, llGenInfo, llItemPurchase, llPicVideo, llAboutVendor, llLogout, llFAQ;
    TextView tvBasicInfo, tvLocation,  tvTiming, tvFoodSold, tvGenInfo, tvItemPurchase, tvPicVideo, tvAboutVendor, tvTarget, tvCaptured, tvPending;

    private NetworkConnectionCheck _connectionCheck;
    private CallServiceAction _serviceAction;
    private ProgressDialog progressDialog;
    private DbAdapter db;
    private Pref _pref;

    int vendorCompleteFlag;
    private int total_target = 0, captured_work = 0, pending_work = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_add_vendor);

        initialize();
        onClick();

        total_target = Integer.parseInt(_pref.getUserTodaysTarget());
        captured_work = Integer.parseInt(_pref.getUserCapturedWork());
        if(total_target >= captured_work){
            pending_work = total_target - captured_work;
        }

        tvTarget.setText(_pref.getUserTodaysTarget() + " VENDORS");
        tvCaptured.setText(_pref.getUserCapturedWork() + " VENDORS");
        tvPending.setText(String.valueOf(pending_work) + " VENDORS");
    }

    @Override
    protected void onPause() {
        super.onPause();
       // android:windowSoftInputMode="stateHidden|adjustPan"

    }



    @Override
    protected void onResume() {
        super.onResume();

            if (ConstantClass.CURRENT_TAB == 0) {
                loadBasicInformationFragment();
            } else if (ConstantClass.CURRENT_TAB == 1) {
                loadLocationFragment();
            } else if (ConstantClass.CURRENT_TAB == 2) {
                loadTimingFragment();
            } else if (ConstantClass.CURRENT_TAB == 3) {
                loadFoodSoldFragment();
            } else if (ConstantClass.CURRENT_TAB == 4) {
                loadGeneralInformationFragment();
            } else if (ConstantClass.CURRENT_TAB == 5) {
                loadItemPurchaseFragment();
            } else if (ConstantClass.CURRENT_TAB == 6) {
                loadPicVideoFragment();
            } else if (ConstantClass.CURRENT_TAB == 7) {
                loadAboutVendorFragment();
            }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        //android:windowSoftInputMode="stateHidden|adjustPan"

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        ConstantClass.CURRENT_TAB = 0;
        Intent intent = new Intent(ActivityAddVendor.this, ActivityHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }



    private void initialize() {

        _connectionCheck = new NetworkConnectionCheck(this);
        _serviceAction = new CallServiceAction(this);
        db = new DbAdapter(ActivityAddVendor.this);
        _pref = new Pref(this);


        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("");
        toolbarHeaderText = (TextView) findViewById(R.id.toolbarHeaderText);
        toolbarHeaderText.setText("ADD VENDORS");

        llMain = (LinearLayout) findViewById(R.id.llMain);
        llBasicInfo = (LinearLayout)findViewById(R.id.llBasicInfo);
        llLocation = (LinearLayout)findViewById(R.id.llLocation);
        llTiming = (LinearLayout)findViewById(R.id.llTiming);
        llFoodSold = (LinearLayout)findViewById(R.id.llFoodSold);
        llGenInfo =(LinearLayout)findViewById(R.id.llGenInfo);
        llItemPurchase = (LinearLayout)findViewById(R.id.llItemPurchase);
        llPicVideo = (LinearLayout)findViewById(R.id.llPicVideo);
        llAboutVendor = (LinearLayout)findViewById(R.id.llAboutVendor);
        llLogout = (LinearLayout) findViewById(R.id.llLogout);
        llFAQ = (LinearLayout) findViewById(R.id.llFAQ);

        tvBasicInfo = (TextView)findViewById(R.id.tvBasicInfo);
        tvLocation = (TextView)findViewById(R.id.tvLocation);
        tvTiming = (TextView)findViewById(R.id.tvTiming);
        tvFoodSold = (TextView)findViewById(R.id.tvFoodSold);
        tvGenInfo = (TextView)findViewById(R.id.tvGenInfo);
        tvItemPurchase = (TextView)findViewById(R.id.tvItemPurchase);
        tvPicVideo = (TextView)findViewById(R.id.tvPicVideo);
        tvAboutVendor = (TextView)findViewById(R.id.tvAboutVendor);
        tvTarget = (TextView)findViewById(R.id.tvTarget);
        tvCaptured = (TextView)findViewById(R.id.tvCaptured);
        tvPending = (TextView)findViewById(R.id.tvPending);

        llBasicInfo.setSelected(true);
        tvBasicInfo.setTextColor(Color.WHITE);

    }

    private void onClick() {

        llBasicInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadBasicInformationFragment();
            }
        });

        llLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadLocationFragment();
            }
        });

        llTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadTimingFragment();
            }
        });

        llFoodSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFoodSoldFragment();
            }
        });

        llGenInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadGeneralInformationFragment();
            }
        });

        llItemPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadItemPurchaseFragment();
            }
        });

        llPicVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadPicVideoFragment();
            }
        });

        llAboutVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadAboutVendorFragment();
            }
        });

        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_connectionCheck.isNetworkAvailable()) {
                    doLogout();
                }
                else{
                    _connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ActivityAddVendor.this, ActivityFAQ.class));

            }
        });
    }

    public void loadBasicInformationFragment(){

        ConstantClass.CURRENT_TAB = 0;
        Fragment fragment = new BasicInformationFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flAddVendorContainor, fragment);
        fragmentTransaction.commit();
        unSelectTab();
        llBasicInfo.setBackgroundColor(Color.parseColor("#FFC9C6"));
        tvBasicInfo.setTextColor(Color.WHITE);

    }

    public void loadLocationFragment(){

        ConstantClass.CURRENT_TAB = 1;
        Fragment fragment = new LocationFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flAddVendorContainor, fragment);
        fragmentTransaction.commit();
        unSelectTab();
        llLocation.setBackgroundColor(Color.parseColor("#FFC9C6"));
        tvLocation.setTextColor(Color.WHITE);

    }

    public void loadTimingFragment(){

        ConstantClass.CURRENT_TAB = 2;
        Fragment fragment = new TimingFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flAddVendorContainor, fragment);
        fragmentTransaction.commit();
        unSelectTab();
        llTiming.setBackgroundColor(Color.parseColor("#FFC9C6"));
        tvTiming.setTextColor(Color.WHITE);

    }

    public void loadFoodSoldFragment(){

        ConstantClass.CURRENT_TAB = 3;
        Fragment fragment = new FoodSoldFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flAddVendorContainor, fragment);
        fragmentTransaction.commit();
        unSelectTab();
        llFoodSold.setBackgroundColor(Color.parseColor("#FFC9C6"));
        tvFoodSold.setTextColor(Color.WHITE);

    }

    public void loadGeneralInformationFragment(){

        ConstantClass.CURRENT_TAB = 4;
        Fragment fragment = new GeneralInformationFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flAddVendorContainor, fragment);
        fragmentTransaction.commit();
        unSelectTab();
        llGenInfo.setBackgroundColor(Color.parseColor("#FFC9C6"));
        tvGenInfo.setTextColor(Color.WHITE);

    }

    public void loadItemPurchaseFragment(){

        ConstantClass.CURRENT_TAB = 5;
        Fragment fragment = new ItemPurchaseFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flAddVendorContainor, fragment);
        fragmentTransaction.commit();
        unSelectTab();
        llItemPurchase.setBackgroundColor(Color.parseColor("#FFC9C6"));
        tvItemPurchase.setTextColor(Color.WHITE);

    }

    public void loadPicVideoFragment(){

        ConstantClass.CURRENT_TAB = 6;
        Fragment fragment = new PicVideoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flAddVendorContainor, fragment);
        fragmentTransaction.commit();
        unSelectTab();
        llPicVideo.setBackgroundColor(Color.parseColor("#FFC9C6"));
        tvPicVideo.setTextColor(Color.WHITE);
    }

    public void loadAboutVendorFragment(){

        ConstantClass.CURRENT_TAB = 7;
        Fragment fragment = new AboutVendorFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flAddVendorContainor, fragment);
        fragmentTransaction.commit();
        unSelectTab();
        llAboutVendor.setBackgroundColor(Color.parseColor("#FFC9C6"));
        tvAboutVendor.setTextColor(Color.WHITE);
    }

    private void unSelectTab(){

        db.open();
        Log.v("vendorId", _pref.getVendorId());
        if(!_pref.getVendorId().equals("")){
            vendorCompleteFlag = db.getVendorCompleteStep();
        }
        db.close();

        llLocation.setEnabled(false);
        llTiming.setEnabled(false);
        llFoodSold.setEnabled(false);
        llGenInfo.setEnabled(false);
        llItemPurchase.setEnabled(false);
        llPicVideo.setEnabled(false);
        llAboutVendor.setEnabled(false);

        llBasicInfo.setBackgroundColor(Color.parseColor("#FFFFFF"));
        llLocation.setBackgroundColor(Color.parseColor("#FFFFFF"));
        llTiming.setBackgroundColor(Color.parseColor("#FFFFFF"));
        llFoodSold.setBackgroundColor(Color.parseColor("#FFFFFF"));
        llGenInfo.setBackgroundColor(Color.parseColor("#FFFFFF"));
        llItemPurchase.setBackgroundColor(Color.parseColor("#FFFFFF"));
        llPicVideo.setBackgroundColor(Color.parseColor("#FFFFFF"));
        llAboutVendor.setBackgroundColor(Color.parseColor("#FFFFFF"));

        tvBasicInfo.setTextColor(Color.BLACK);
        tvLocation.setTextColor(Color.BLACK);
        tvTiming.setTextColor(Color.BLACK);
        tvFoodSold.setTextColor(Color.BLACK);
        tvGenInfo.setTextColor(Color.BLACK);
        tvItemPurchase.setTextColor(Color.BLACK);
        tvPicVideo.setTextColor(Color.BLACK);
        tvAboutVendor.setTextColor(Color.BLACK);

        if(vendorCompleteFlag == 1){
            llLocation.setEnabled(true);
            llBasicInfo.setBackgroundColor(Color.parseColor("#A7D0FA"));
            tvBasicInfo.setTextColor(Color.WHITE);
        }
        if(vendorCompleteFlag == 2) {
            llLocation.setEnabled(true);
            llTiming.setEnabled(true);
            llBasicInfo.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llLocation.setBackgroundColor(Color.parseColor("#A7D0FA"));
            tvBasicInfo.setTextColor(Color.WHITE);
            tvLocation.setTextColor(Color.WHITE);
        }
        if(vendorCompleteFlag == 3) {
            llLocation.setEnabled(true);
            llTiming.setEnabled(true);
            llFoodSold.setEnabled(true);
            llBasicInfo.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llLocation.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llTiming.setBackgroundColor(Color.parseColor("#A7D0FA"));
            tvBasicInfo.setTextColor(Color.WHITE);
            tvLocation.setTextColor(Color.WHITE);
            tvTiming.setTextColor(Color.WHITE);
        }
        if(vendorCompleteFlag == 4){
            llLocation.setEnabled(true);
            llTiming.setEnabled(true);
            llFoodSold.setEnabled(true);
            llGenInfo.setEnabled(true);
            llBasicInfo.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llLocation.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llTiming.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llFoodSold.setBackgroundColor(Color.parseColor("#A7D0FA"));
            tvBasicInfo.setTextColor(Color.WHITE);
            tvLocation.setTextColor(Color.WHITE);
            tvTiming.setTextColor(Color.WHITE);
            tvFoodSold.setTextColor(Color.WHITE);
        }
        if (vendorCompleteFlag == 5){
            llLocation.setEnabled(true);
            llTiming.setEnabled(true);
            llFoodSold.setEnabled(true);
            llGenInfo.setEnabled(true);
            llItemPurchase.setEnabled(true);
            llBasicInfo.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llLocation.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llTiming.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llFoodSold.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llGenInfo.setBackgroundColor(Color.parseColor("#A7D0FA"));
            tvBasicInfo.setTextColor(Color.WHITE);
            tvLocation.setTextColor(Color.WHITE);
            tvTiming.setTextColor(Color.WHITE);
            tvFoodSold.setTextColor(Color.WHITE);
            tvGenInfo.setTextColor(Color.WHITE);
        }
        if (vendorCompleteFlag == 6){
            llLocation.setEnabled(true);
            llTiming.setEnabled(true);
            llFoodSold.setEnabled(true);
            llGenInfo.setEnabled(true);
            llItemPurchase.setEnabled(true);
            llPicVideo.setEnabled(true);
            llBasicInfo.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llLocation.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llTiming.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llFoodSold.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llGenInfo.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llItemPurchase.setBackgroundColor(Color.parseColor("#A7D0FA"));
            tvBasicInfo.setTextColor(Color.WHITE);
            tvLocation.setTextColor(Color.WHITE);
            tvTiming.setTextColor(Color.WHITE);
            tvFoodSold.setTextColor(Color.WHITE);
            tvGenInfo.setTextColor(Color.WHITE);
            tvItemPurchase.setTextColor(Color.WHITE);
        }
        if (vendorCompleteFlag == 7){
            llLocation.setEnabled(true);
            llTiming.setEnabled(true);
            llFoodSold.setEnabled(true);
            llGenInfo.setEnabled(true);
            llItemPurchase.setEnabled(true);
            llPicVideo.setEnabled(true);
            llAboutVendor.setEnabled(true);
            llBasicInfo.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llLocation.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llTiming.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llFoodSold.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llGenInfo.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llItemPurchase.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llPicVideo.setBackgroundColor(Color.parseColor("#A7D0FA"));
            tvBasicInfo.setTextColor(Color.WHITE);
            tvLocation.setTextColor(Color.WHITE);
            tvTiming.setTextColor(Color.WHITE);
            tvFoodSold.setTextColor(Color.WHITE);
            tvGenInfo.setTextColor(Color.WHITE);
            tvItemPurchase.setTextColor(Color.WHITE);
            tvPicVideo.setTextColor(Color.WHITE);
        }
        if (vendorCompleteFlag == 8){
            llLocation.setEnabled(true);
            llTiming.setEnabled(true);
            llFoodSold.setEnabled(true);
            llGenInfo.setEnabled(true);
            llItemPurchase.setEnabled(true);
            llPicVideo.setEnabled(true);
            llAboutVendor.setEnabled(true);
            llBasicInfo.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llLocation.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llTiming.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llFoodSold.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llGenInfo.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llItemPurchase.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llPicVideo.setBackgroundColor(Color.parseColor("#A7D0FA"));
            llAboutVendor.setBackgroundColor(Color.parseColor("#A7D0FA"));
            tvBasicInfo.setTextColor(Color.WHITE);
            tvLocation.setTextColor(Color.WHITE);
            tvTiming.setTextColor(Color.WHITE);
            tvFoodSold.setTextColor(Color.WHITE);
            tvGenInfo.setTextColor(Color.WHITE);
            tvItemPurchase.setTextColor(Color.WHITE);
            tvPicVideo.setTextColor(Color.WHITE);
            tvAboutVendor.setTextColor(Color.WHITE);
        }
    }

    private void doLogout(){
        try {

            JSONObject parentJsonObj = new JSONObject();
            JSONObject childJsonObj = new JSONObject();
            childJsonObj.put("accesstoken", _pref.getUserAccessToken());
            parentJsonObj.put("data", childJsonObj);
            setUpProgressDialog();
            _serviceAction.actionInterface = ActivityAddVendor.this;
            _serviceAction.requestVersionApi(parentJsonObj, "vendor-logout");

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setUpProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
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
                if(jsonObjData.getBoolean("success")){
                    Toast.makeText(this, "Successfully logout", Toast.LENGTH_SHORT).show();
                    _pref.saveUserAccessToken("");
                    Intent intent = new Intent(ActivityAddVendor.this, ActivityLogin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(this, jsonObjData.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, jsonObjError.getString("errMsg"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}