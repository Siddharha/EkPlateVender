package com.example.bluehorsesoftkol.ekplatevendor.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.bluehorsesoftkol.ekplatevendor.Utils.CallServiceAction;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.ConstantClass;
import com.example.bluehorsesoftkol.ekplatevendor.adapters.FaqAdapter;
import com.example.bluehorsesoftkol.ekplatevendor.bean.FaqItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.FaqSubItem;
import com.example.bluehorsesoftkol.ekplatevendor.dbpackage.DbAdapter;
import com.example.bluehorsesoftkol.ekplatevendor.interfaces.BackgroundActionInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GetFoodItemService extends Service implements BackgroundActionInterface {

    private CallServiceAction _serviceAction;
    private DbAdapter db;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        _serviceAction = new CallServiceAction(GetFoodItemService.this);
        db = new DbAdapter(GetFoodItemService.this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStart(Intent intent, int startid) {

        try {
            _serviceAction.actionInterface = GetFoodItemService.this;
            _serviceAction.requestVersionApi1("get-foods");

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
                JSONObject jsonObjData = response.getJSONObject("data");
                JSONArray foodArray = jsonObjData.getJSONArray("food");
                db.open();
                db.deleteFoodItemRecord();
                for (int i = 0; i < foodArray.length(); i++) {
                    JSONObject foodObj = foodArray.getJSONObject(i);
                    String foodName = foodObj.getString("name");
                    String foodImage = foodObj.getString("image").replaceAll(" ", "%20");
                    Log.v("foodImage", foodImage);
                    db.insertFoodItem(foodName, foodImage);
                }
                db.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}

