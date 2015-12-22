package com.example.bluehorsesoftkol.ekplatevendor.activity.registration;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.NetworkConnectionCheck;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.activity.vendor.ActivityHome;
import com.example.bluehorsesoftkol.ekplatevendor.dbpackage.DbAdapter;
import com.example.bluehorsesoftkol.ekplatevendor.service.GetFoodItemService;
import com.example.bluehorsesoftkol.ekplatevendor.service.GetMonthlyGraphService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivitySplashScreen extends AppCompatActivity {

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    String currentDate = "", savedDate = "";

    private NetworkConnectionCheck _connectionCheck;
    private Pref _pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initialize();

        currentDate = dateFormat.format(calendar.getTime());
        Log.v("currentDate", currentDate);
        savedDate = _pref.getDate();
        Log.v("savedDate", savedDate);

        if(_pref.getUserAccessToken().equals("")){
            if(!currentDate.equals(savedDate)){
                _pref.saveDate(currentDate);
                _pref.saveUserCapturedWork("0");
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(ActivitySplashScreen.this, ActivityLogin.class));
                    finish();
                }
            }, 3000);
        }
        else{
            if(!currentDate.equals(savedDate)){
                if (_connectionCheck.isNetworkAvailable() ) {
                    startService(new Intent(ActivitySplashScreen.this, GetMonthlyGraphService.class));
                    startService(new Intent(ActivitySplashScreen.this, GetFoodItemService.class));
                    _pref.saveDate(currentDate);
                    _pref.saveUserCapturedWork("0");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(ActivitySplashScreen.this, ActivityHome.class));
                            finish();
                        }
                    }, 3000);
                }
                else{
                    _connectionCheck.getNetworkActiveAlert().show();
                }
            }
            else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            startActivity(new Intent(ActivitySplashScreen.this, ActivityHome.class));
                            finish();
                    }
                }, 3000);
            }
        }
    }

    private void initialize(){

        _connectionCheck = new NetworkConnectionCheck(ActivitySplashScreen.this);
        _pref = new Pref(ActivitySplashScreen.this);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }
}
