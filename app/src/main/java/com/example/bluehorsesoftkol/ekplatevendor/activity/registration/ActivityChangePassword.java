package com.example.bluehorsesoftkol.ekplatevendor.activity.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.CallServiceAction;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.NetworkConnectionCheck;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.interfaces.BackgroundActionInterface;

import org.json.JSONObject;


public class ActivityChangePassword extends Activity implements BackgroundActionInterface{

    private TextView toolbarHeaderText;
    private EditText etOldPass, etNewPass, etConfirmPass;
    private Button btnOk, btnCancel;

    String old_password = "", new_password = "", confirm_password = "";

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
        setContentView(R.layout.activity_change_password);

        initialize();
        onClick();

    }

    private void initialize() {

        _connectionCheck = new NetworkConnectionCheck(this);
        _serviceAction = new CallServiceAction(this);
        _pref = new Pref(this);

        toolbarHeaderText = (TextView) findViewById(R.id.toolbarHeaderText);
        toolbarHeaderText.setText("CHANGE PASSWORD");

        etOldPass = (EditText) findViewById(R.id.etOldPass);
        etNewPass = (EditText) findViewById(R.id.etNewPass);
        etConfirmPass = (EditText) findViewById(R.id.etConfirmPass);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnCancel = (Button) findViewById(R.id.btnCancel);

    }

    private void onClick(){

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_connectionCheck.isNetworkAvailable()) {

                    old_password = etOldPass.getText().toString();
                    new_password = etNewPass.getText().toString();
                    confirm_password = etConfirmPass.getText().toString();

                    if(old_password.equals("") || new_password.equals("") || confirm_password.equals("")){
                        Toast.makeText(ActivityChangePassword.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                    }
                    else if(!new_password.equals(confirm_password)){
                        Toast.makeText(ActivityChangePassword.this, "New Password and Confirm Password not matched", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //Toast.makeText(ActivityChangePassword.this, "Everything is OK", Toast.LENGTH_SHORT).show();
                        doChangePassword();
                    }

                }else {
                    _connectionCheck.getNetworkActiveAlert().show();
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }

    private void doChangePassword(){

        try {

            JSONObject parentJsonObj = new JSONObject();
            JSONObject childJsonObj = new JSONObject();
            childJsonObj.put("accesstoken", _pref.getUserAccessToken());
            childJsonObj.put("old_password", old_password);
            childJsonObj.put("new_password", new_password);
            parentJsonObj.put("data", childJsonObj);
            setUpProgressDialog();
            _serviceAction.actionInterface = ActivityChangePassword.this;
            _serviceAction.requestVersionApi(parentJsonObj, "change-password");

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

        Log.v("response", String.valueOf(response));

        progressDialog.dismiss();

        try{

            JSONObject jsonObj = response.getJSONObject("errNode");

            if(jsonObj.getInt("errCode") == 0) {

                JSONObject jsonObjData = response.getJSONObject("data");

                if(jsonObjData.getBoolean("success")){

                    Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    finish();

                } else {

                    Toast.makeText(this, jsonObjData.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } else {

                //Toast.makeText(this, jsonObj.getString("errMsg"), Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
