package com.example.bluehorsesoftkol.ekplatevendor.activity.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.CallServiceAction;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.ConstantClass;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.NetworkConnectionCheck;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.activity.vendor.ActivityHome;
import com.example.bluehorsesoftkol.ekplatevendor.dbpackage.DbAdapter;
import com.example.bluehorsesoftkol.ekplatevendor.interfaces.BackgroundActionInterface;
import com.example.bluehorsesoftkol.ekplatevendor.service.GetFoodItemService;
import com.example.bluehorsesoftkol.ekplatevendor.service.GetMonthlyGraphService;

import org.json.JSONObject;

public class ActivityLogin extends AppCompatActivity implements BackgroundActionInterface {

    private EditText etEmailId, etMobile, etPassword;
    private Button btnLogin;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private NetworkConnectionCheck _connectionCheck;
    private CallServiceAction _serviceAction;
    private ProgressDialog progressDialog;

    private DbAdapter dbAdapter;
    private Pref _pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_login);

        initialize();
        onClick();
    }

    private void initialize() {

        _connectionCheck = new NetworkConnectionCheck(this);
        _serviceAction = new CallServiceAction(this);
        dbAdapter = new DbAdapter(this);
        _pref = new Pref(this);

        etEmailId = (EditText)findViewById(R.id.etEmailId);
        etMobile = (EditText)findViewById(R.id.etMobile);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);

    }

    private void onClick() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_connectionCheck.isNetworkAvailable()) {

                    if (etEmailId.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Enter Email Id", Toast.LENGTH_SHORT).show();
                    } else if(!etEmailId.getText().toString().matches(emailPattern)){
                        Toast.makeText(getApplicationContext(), "Please Enter Valid Email Id", Toast.LENGTH_SHORT).show();
                    }else if (etPassword.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
                    } else {
                        doLogin();
                    }
                } else {
                    _connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });
    }

    private void doLogin(){

        try {

            JSONObject parentJsonObj = new JSONObject();
            JSONObject childJsonObj = new JSONObject();
            childJsonObj.put("email_id", etEmailId.getText().toString());
            //childJsonObj.put("mobile_no", etMobile.getText().toString());
            childJsonObj.put("password", etPassword.getText().toString());
            parentJsonObj.put("data", childJsonObj);
            setUpProgressDialog();
            _serviceAction.actionInterface = ActivityLogin.this;
            _serviceAction.requestVersionApi(parentJsonObj, "vendor-login");

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private void setUpProgressDialog(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

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

                if(jsonObjData.getBoolean("success")){

                    saveUserInfo(etEmailId.getText().toString(), etMobile.getText().toString(),
                            jsonObjData.getString("accessToken"));
                    _pref.saveUserAccessToken(jsonObjData.getString("accessToken"));
                    _pref.saveUserName(jsonObjData.getString("name"));
                    _pref.saveUserEmail(jsonObjData.getString("email"));
                    _pref.saveUserMobileNo(jsonObjData.getString("mobile"));
                    _pref.saveUserFacebook(jsonObjData.getString("facebook"));
                    _pref.saveUserTwitter(jsonObjData.getString("twitter"));
                    _pref.saveUserPininterest(jsonObjData.getString("pininterest"));
                    _pref.saveUserInstagram(jsonObjData.getString("instagram"));
                    _pref.saveUserImagePath(jsonObjData.getString("image_url"));

                    if (_connectionCheck.isNetworkAvailable()) {
                        startService(new Intent(ActivityLogin.this, GetMonthlyGraphService.class));
                        startService(new Intent(ActivityLogin.this, GetFoodItemService.class));
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            progressDialog.dismiss();
                            Toast.makeText(ActivityLogin.this, "Successfully login", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(ActivityLogin.this, ActivityHome.class));
                            finish();
                        }
                    }, 3000);

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(this, jsonObjData.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(this, jsonObj.getString("errMsg"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveUserInfo(String email, String mobileNo, String accesstoken){

        dbAdapter.open();
        dbAdapter.insertUserLogin(email, mobileNo, accesstoken);
        dbAdapter.close();

    }

}
