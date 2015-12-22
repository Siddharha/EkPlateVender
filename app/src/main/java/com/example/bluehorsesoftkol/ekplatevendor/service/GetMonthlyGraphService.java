package com.example.bluehorsesoftkol.ekplatevendor.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.bluehorsesoftkol.ekplatevendor.Utils.CallServiceAction;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.dbpackage.DbAdapter;
import com.example.bluehorsesoftkol.ekplatevendor.interfaces.BackgroundActionInterface;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Avishek on 12/16/2015.
 */
public class GetMonthlyGraphService extends Service implements BackgroundActionInterface {

    private CallServiceAction _serviceAction;
    private DbAdapter db;
    private Pref _pref;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        _serviceAction = new CallServiceAction(GetMonthlyGraphService.this);
        db = new DbAdapter(GetMonthlyGraphService.this);
        _pref = new Pref(GetMonthlyGraphService.this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStart(Intent intent, int startid) {

        try {
            JSONObject childJsonObj = new JSONObject();
            JSONObject parentJsonObj = new JSONObject();
            childJsonObj.put("accessToken", _pref.getUserAccessToken());
            parentJsonObj.put("data", childJsonObj);
            _serviceAction.actionInterface = GetMonthlyGraphService.this;
            _serviceAction.requestVersionApi(parentJsonObj, "get-monthly-graph");

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onCompleted(JSONObject response) {

        Log.v("response", String.valueOf(response));

        try{
            JSONObject jsonObj = response.getJSONObject("errNode");
            if(jsonObj.getInt("errCode") == 0) {
                JSONObject dataObj = response.getJSONObject("data");
                _pref.saveUserTodaysTarget(dataObj.getString("todays_target"));
                _pref.saveUserTotalVendor(dataObj.getString("total_vendor"));
                _pref.saveUserTotalUploadedVendor(dataObj.getString("total_uploaded"));
                _pref.saveUserTotalApprovedVendor(dataObj.getString("total_approved"));
                _pref.saveMonth(dataObj.getString("month"));
                _pref.saveMonthTotalDay(dataObj.getString("total_day"));
                JSONArray detailsArray = dataObj.getJSONArray("details");
                if (detailsArray.length() > 0) {
                    db.open();
                    db.deleteMonthlyGraphRecord();
                    for (int i = 0; i < detailsArray.length(); i++) {
                        JSONObject detailsObj = detailsArray.getJSONObject(i);
                        db.insertMonthlyGraph(detailsObj.getString("date"), detailsObj.getString("count"));
                    }
                    db.close();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
