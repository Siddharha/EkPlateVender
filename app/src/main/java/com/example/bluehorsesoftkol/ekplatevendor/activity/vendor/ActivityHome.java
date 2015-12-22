package com.example.bluehorsesoftkol.ekplatevendor.activity.vendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.CallServiceAction;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.NetworkConnectionCheck;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.activity.registration.ActivityFAQ;
import com.example.bluehorsesoftkol.ekplatevendor.activity.registration.ActivityLogin;
import com.example.bluehorsesoftkol.ekplatevendor.dbpackage.DbAdapter;
import com.example.bluehorsesoftkol.ekplatevendor.fragments.MapViewOfAreaCoveredFragment;
import com.example.bluehorsesoftkol.ekplatevendor.fragments.SupervisorNoteFragment;
import com.example.bluehorsesoftkol.ekplatevendor.fragments.UserProfileFragment;
import com.example.bluehorsesoftkol.ekplatevendor.fragments.VendorManagementFragment;
import com.example.bluehorsesoftkol.ekplatevendor.interfaces.BackgroundActionInterface;
import org.json.JSONObject;

public class ActivityHome extends AppCompatActivity implements BackgroundActionInterface{

    private Toolbar toolbar;
    private TextView toolbarHeaderText, tvWelCome, tvProfile, tvNote, tvAreaCovered, tvManagement, tvTarget, tvCaptured, tvPending;
    private LinearLayout llWelcome, llProfile, llNote, llAreaCovered, llManagement,llLogout, llFAQ;
    private FrameLayout barProfile, barNote, barAreaCovered, barManagement;
    private FloatingActionButton fabButton;

    private NetworkConnectionCheck _connectionCheck;
    private CallServiceAction _serviceAction;
    private ProgressDialog progressDialog;
    private DbAdapter db;
    private Pref _pref;

    public int back_flag = 0;
    private int total_target = 0, captured_work = 0, pending_work = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

    }

    @Override
    protected void onResume() {
        super.onResume();

        setContentView(R.layout.activity_home);
        initialize();
        onClick();

        //////////////////////////////////////////////////////////////////////

        total_target = Integer.parseInt(_pref.getUserTodaysTarget());
        captured_work = Integer.parseInt(_pref.getUserCapturedWork());
        if(total_target >= captured_work){
            pending_work = total_target - captured_work;
        }

        tvWelCome.setText("Welcome " + _pref.getUserName());
        tvTarget.setText(_pref.getUserTodaysTarget() + " VENDORS");
        tvCaptured.setText(_pref.getUserCapturedWork() + " VENDORS");
        tvPending.setText(String.valueOf(pending_work) + " VENDORS");

        //////////////////////////////////////////////////////////////////////

        if(back_flag == 1 || back_flag == 5){
            back_flag = 5;
            loadUserProfileFragment();
        }
        else if(back_flag == 2 ){
            loadSupervisorNoteFragment();
        }
        else if(back_flag == 3 ){
            loadMapViewOfAreaCoveredFragment();
        }
        else if(back_flag == 4 ){
            loadVendorManagementFragment();
        }
    }

    @Override
    public void onBackPressed() {
        if (back_flag == 0) {
            super.onBackPressed();
            finish();
        } else {
            back_flag = 0;
            onResume();
        }
    }

    private void initialize() {

        _connectionCheck = new NetworkConnectionCheck(this);
        _serviceAction = new CallServiceAction(this);
        db = new DbAdapter(ActivityHome.this);
        _pref = new Pref(ActivityHome.this);

        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbarHeaderText = (TextView)findViewById(R.id.toolbarHeaderText);
        toolbarHeaderText.setText("HOME");

        llWelcome = (LinearLayout)findViewById(R.id.llWelcome);
        llProfile = (LinearLayout)findViewById(R.id.llProfile);
        llNote = (LinearLayout)findViewById(R.id.llNote);
        llAreaCovered = (LinearLayout)findViewById(R.id.llAreaCovered);
        llManagement = (LinearLayout)findViewById(R.id.llManagement);
        llLogout = (LinearLayout) findViewById(R.id.llLogout);
        llFAQ = (LinearLayout) findViewById(R.id.llFAQ);

        tvWelCome = (TextView) findViewById(R.id.tvWelCome);
        tvProfile =( TextView)findViewById(R.id.tvProfile);
        tvNote = (TextView)findViewById(R.id.tvNote);
        tvAreaCovered = (TextView)findViewById(R.id.tvAreaCovered);
        tvManagement = (TextView)findViewById(R.id.tvManagement);
        tvTarget = (TextView)findViewById(R.id.tvTarget);
        tvCaptured = (TextView)findViewById(R.id.tvCaptured);
        tvPending = (TextView)findViewById(R.id.tvPending);

        barProfile = (FrameLayout)findViewById(R.id.barProfile);
        barNote = (FrameLayout)findViewById(R.id.barNote);
        barAreaCovered = (FrameLayout)findViewById(R.id.barAreaCovered);
        barManagement = (FrameLayout)findViewById(R.id.barManagement);

        fabButton = (FloatingActionButton)findViewById(R.id.fabButton);
        //fabButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(225,230,168,0)));

    }

    private void onClick() {

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.open();
                if(!_pref.getVendorId().equals("")) {
                    _pref.saveVendorId(db.getLastVendorId());
                    if(db.getVendorCompleteStep()==8){
                        _pref.saveVendorId("");
                    }
                }
                db.close();
                Intent i = new Intent(ActivityHome.this, ActivityAddVendor.class);
                startActivity(i);
                finish();
            }
        });

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                back_flag = 1;
                loadUserProfileFragment();
            }


        });

        llNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                back_flag = 2;
                loadSupervisorNoteFragment();
            }
        });

        llAreaCovered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                back_flag = 3;
                loadMapViewOfAreaCoveredFragment();
            }
        });

        llManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                back_flag = 4;
                loadVendorManagementFragment();
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

               startActivity(new Intent(ActivityHome.this, ActivityFAQ.class));

           }
       });


    }

    public void loadUserProfileFragment(){

        llWelcome.setVisibility(View.GONE);
        toolbarHeaderText.setText("USER PROFILE");
        fabButton.setVisibility(View.INVISIBLE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        UserProfileFragment fr = new UserProfileFragment();
        fragmentTransaction.replace(R.id.flProfileContainer, fr);
        fragmentTransaction.commit();
        unSelectTab();
        barProfile.setSelected(true);
        tvProfile.setTextColor(getResources().getColor(R.color.primary_theme_color));

    }

    public void loadSupervisorNoteFragment(){

        llWelcome.setVisibility(View.GONE);
        toolbarHeaderText.setText("SUPERVISOR NOTE");
        fabButton.setVisibility(View.INVISIBLE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SupervisorNoteFragment fr = new SupervisorNoteFragment();
        fragmentTransaction.replace(R.id.flProfileContainer, fr);
        fragmentTransaction.commit();
        unSelectTab();
        barNote.setSelected(true);
        tvNote.setTextColor(getResources().getColor(R.color.primary_theme_color));

    }

    public void loadMapViewOfAreaCoveredFragment(){

        llWelcome.setVisibility(View.GONE);
        toolbarHeaderText.setText("MAPVIEW OF AREA COVERED");
        fabButton.setVisibility(View.INVISIBLE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MapViewOfAreaCoveredFragment fr = new MapViewOfAreaCoveredFragment();
        fragmentTransaction.replace(R.id.flProfileContainer, fr);
        fragmentTransaction.commit();
        unSelectTab();
        barAreaCovered.setSelected(true);
        tvAreaCovered.setTextColor(getResources().getColor(R.color.primary_theme_color));

    }

    public void loadVendorManagementFragment(){

        llWelcome.setVisibility(View.GONE);
        toolbarHeaderText.setText("VENDOR MANAGEMENT");
        fabButton.setVisibility(View.INVISIBLE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        VendorManagementFragment fr = new VendorManagementFragment();
        fragmentTransaction.replace(R.id.flProfileContainer, fr);
        fragmentTransaction.commit();
        unSelectTab();
        barManagement.setSelected(true);
        tvManagement.setTextColor(getResources().getColor(R.color.primary_theme_color));

    }

    private void unSelectTab() {
        barProfile.setSelected(false);
        barNote.setSelected(false);
        barAreaCovered.setSelected(false);
        barManagement.setSelected(false);

        tvProfile.setTextColor(Color.BLACK);
        tvNote.setTextColor(Color.BLACK);
        tvAreaCovered.setTextColor(Color.BLACK);
        tvManagement.setTextColor(Color.BLACK);

    }

    private void doLogout(){
        try {
            JSONObject parentJsonObj = new JSONObject();
            JSONObject childJsonObj = new JSONObject();
            childJsonObj.put("accesstoken", _pref.getUserAccessToken());
            parentJsonObj.put("data", childJsonObj);
            setUpProgressDialog();
            _serviceAction.actionInterface = ActivityHome.this;
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
                    Intent intent = new Intent(ActivityHome.this, ActivityLogin.class);
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