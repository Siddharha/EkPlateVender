package com.example.bluehorsesoftkol.ekplatevendor.activity.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.CallServiceAction;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.NetworkConnectionCheck;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.adapters.FaqAdapter;
import com.example.bluehorsesoftkol.ekplatevendor.bean.FaqItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.FaqSubItem;
import com.example.bluehorsesoftkol.ekplatevendor.interfaces.BackgroundActionInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ActivityFAQ extends Activity implements BackgroundActionInterface {

    private TextView toolbarHeaderText;

    private ExpandableListView expandableListView;
    private ArrayList<FaqItem> faqItems;
    private FaqAdapter faqAdapter;

    private NetworkConnectionCheck _connectionCheck;
    private CallServiceAction _serviceAction;
    private ProgressDialog progressDialog;
    private Pref _pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_faq);

        initialize();

        if (_connectionCheck.isNetworkAvailable()) {
            requestFaqDetails();
        }else {
            _connectionCheck.getNetworkActiveAlert().show();
        }

    }

    private void initialize() {

        _connectionCheck = new NetworkConnectionCheck(this);
        _serviceAction = new CallServiceAction(this);
        _pref = new Pref(this);

        toolbarHeaderText = (TextView) findViewById(R.id.toolbarHeaderText);
        toolbarHeaderText.setText("FAQ");

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        faqItems = new ArrayList<FaqItem>();
        //expandableListView.setGroupIndicator(null);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expandableListView.setIndicatorBounds(width - GetPixelFromDips(60),
                    width - GetPixelFromDips(20));
        } else {
            expandableListView.setIndicatorBoundsRelative(width
                    - GetPixelFromDips(60), width - GetPixelFromDips(20));
        }

    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private void requestFaqDetails() {
        try {
            JSONObject childJsonObj = new JSONObject();
            JSONObject parentJsonObj = new JSONObject();
            childJsonObj.put("accesstoken", _pref.getUserAccessToken());
            parentJsonObj.put("data", childJsonObj);
            Log.e("JSON:", parentJsonObj.toString());
            setUpProgressDialog();
            _serviceAction.actionInterface = ActivityFAQ.this;
            _serviceAction.requestVersionApi(parentJsonObj, "vendor-faq");

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setUpProgressDialog(){

        progressDialog = new ProgressDialog(ActivityFAQ.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onCompleted(JSONObject response) {

        Log.v("response", String.valueOf(response));
        progressDialog.dismiss();

        try{
            JSONObject jsonObj = response.getJSONObject("errNode");
            if(jsonObj.getInt("errCode") == 0) {
                JSONObject jsonObjData = response.getJSONObject("data");
                if(jsonObjData.getBoolean("success")){
                    JSONArray faqArray = jsonObjData.getJSONArray("faq");
                    for (int j = 0; j < faqArray.length(); j++) {
                        JSONObject faqObj = faqArray.getJSONObject(j);
                        FaqItem faqitem = new FaqItem();
                        faqitem.setFaqQuestion(faqObj.getString("question"));
                        ArrayList<FaqSubItem> subArrayList = new ArrayList<FaqSubItem>();
                        for (int i = 0; i < 1; i++) {
                            FaqSubItem subitem = new FaqSubItem();
                            subitem.setFaqAnswer(faqObj.getString("answer"));
                            subArrayList.add(subitem);
                        }
                        faqitem.setArrayList(subArrayList);
                        faqItems.add(faqitem);
                    }
                    faqAdapter = new FaqAdapter(ActivityFAQ.this, faqItems);
                    expandableListView.setAdapter(faqAdapter);
                } else {
                    Toast.makeText(this, jsonObjData.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Something going wrong", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
