package com.example.bluehorsesoftkol.ekplatevendor.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.CallServiceAction;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.CircularImageView;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.ConstantClass;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.NetworkConnectionCheck;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.activity.registration.ActivityChangePassword;
import com.example.bluehorsesoftkol.ekplatevendor.activity.vendor.ActivityHome;
import com.example.bluehorsesoftkol.ekplatevendor.bean.MonthlyGraphItem;
import com.example.bluehorsesoftkol.ekplatevendor.dbpackage.DbAdapter;
import com.example.bluehorsesoftkol.ekplatevendor.interfaces.BackgroundActionInterface;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.LargeValueFormatter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class UserProfileFragment extends Fragment implements BackgroundActionInterface{

    private View rootView;

    private CircularImageView imgViewUser;
    private LinearLayout llEditProfile, llSaveProfile, llChangePassword;
    private EditText etName, etEmail, etMobile, etFacebook, etTwitter, etPininterest, etInstagram;
    private TextView tvTotalVendor, tvTotalUploadedVendor, tvTotalApprovedVendor;
    private LineChart mChart;

    private NetworkConnectionCheck _connectionCheck;
    private CallServiceAction _serviceAction;
    private ProgressDialog progressDialog;
    private DbAdapter db;
    private Pref _pref;

    private ImageLoader imageLoader;
    DisplayImageOptions options;

    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;

    private String picturePath = "", encodedImage = "";

    int total_day = 0;
    private ArrayList<MonthlyGraphItem> monthlyGraphItems = new ArrayList<MonthlyGraphItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);

        initialize();
        setValue();
        setData();
        onClick();

        return rootView;
    }

    private void initialize(){

        _connectionCheck = new NetworkConnectionCheck(getActivity());
        _serviceAction = new CallServiceAction(getActivity());
        db = new DbAdapter(getActivity());
        _pref = new Pref(getActivity());

        imgViewUser = (CircularImageView) rootView.findViewById(R.id.imgViewUser);
        llEditProfile = (LinearLayout) rootView.findViewById(R.id.llEditProfile);
        llSaveProfile = (LinearLayout) rootView.findViewById(R.id.llSaveProfile);
        llChangePassword = (LinearLayout) rootView.findViewById(R.id.llChangePassword);
        etName = (EditText) rootView.findViewById(R.id.etName);
        etEmail = (EditText) rootView.findViewById(R.id.etEmail);
        etMobile = (EditText) rootView.findViewById(R.id.etMobile);
        etFacebook = (EditText) rootView.findViewById(R.id.etFacebook);
        etTwitter = (EditText) rootView.findViewById(R.id.etTwitter);
        etPininterest = (EditText) rootView.findViewById(R.id.etPininterest);
        etInstagram = (EditText) rootView.findViewById(R.id.etInstagram);
        tvTotalVendor = (TextView) rootView.findViewById(R.id.tvTotalVendor);
        tvTotalUploadedVendor = (TextView) rootView.findViewById(R.id.tvTotalUploadedVendor);
        tvTotalApprovedVendor = (TextView) rootView.findViewById(R.id.tvTotalApprovedVendor);

        ////////////////////////////////////////////////////////////////////////////////////

        mChart = (LineChart) rootView.findViewById(R.id.chart1);
        mChart.setDrawGridBackground(false);
        mChart.setDescription("");
        mChart.setNoDataTextDescription("");

    }

    private void setValue(){

        ActivityHome activityHome = (ActivityHome)getActivity();
        Log.v("back_flag", String.valueOf(activityHome.back_flag));

        if(activityHome.back_flag == 5){

            if(!_pref.getUserImagePathFromSdCard().equals("")){

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(_pref.getUserImagePathFromSdCard(), options);
                imgViewUser.setImageBitmap(bitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] byteArrayImage = baos.toByteArray();
                encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
            }
            else{
                imageLoader = ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
                options = new DisplayImageOptions.Builder()
                        .cacheInMemory(true).cacheOnDisk(true)
                        .showImageOnLoading(R.drawable.image_box)
                        .showImageForEmptyUri(R.drawable.image_box)
                        .showImageOnFail(R.drawable.image_box)
                        .imageScaleType(ImageScaleType.EXACTLY).build();
                imageLoader.displayImage(_pref.getUserImagePath(), imgViewUser, options);
            }

            etName.setText(ConstantClass.USER_EDIT_NAME);
            etEmail.setText(_pref.getUserEmail());
            etMobile.setText(ConstantClass.USER_EDIT_MOBILE);
            etFacebook.setText(ConstantClass.USER_EDIT_FACEBOOK);
            etTwitter.setText(ConstantClass.USER_EDIT_TWITTER);
            etPininterest.setText(ConstantClass.USER_EDIT_PININTEREST);
            etInstagram.setText(ConstantClass.USER_EDIT_INSTAGRAM);

            llEditProfile.setVisibility(View.GONE);
            llSaveProfile.setVisibility(View.VISIBLE);

            etName.setFocusable(true);
            etName.setClickable(true);
            etName.setFocusableInTouchMode(true);
            //etEmail.setFocusable(true);
            //etEmail.setClickable(true);
            //etEmail.setFocusableInTouchMode(true);
            etMobile.setFocusable(true);
            etMobile.setClickable(true);
            etMobile.setFocusableInTouchMode(true);
            etFacebook.setFocusable(true);
            etFacebook.setClickable(true);
            etFacebook.setFocusableInTouchMode(true);
            etTwitter.setFocusable(true);
            etTwitter.setClickable(true);
            etTwitter.setFocusableInTouchMode(true);
            etPininterest.setFocusable(true);
            etPininterest.setClickable(true);
            etPininterest.setFocusableInTouchMode(true);
            etInstagram.setFocusable(true);
            etInstagram.setClickable(true);
            etInstagram.setFocusableInTouchMode(true);

        }
        else{
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true).cacheOnDisk(true)
                    .showImageOnLoading(R.drawable.image_box)
                    .showImageForEmptyUri(R.drawable.image_box)
                    .showImageOnFail(R.drawable.image_box)
                    .imageScaleType(ImageScaleType.EXACTLY).build();
            imageLoader.displayImage(_pref.getUserImagePath(), imgViewUser, options);

            etName.setText(_pref.getUserName());
            etEmail.setText(_pref.getUserEmail());
            etMobile.setText(_pref.getUserMobileNo());
            etFacebook.setText(_pref.getUserFacebook());
            etTwitter.setText(_pref.getUserTwitter());
            etPininterest.setText(_pref.getUserPininterest());
            etInstagram.setText(_pref.getUserInstagram());

            llSaveProfile.setVisibility(View.GONE);
            llEditProfile.setVisibility(View.VISIBLE);

            etName.setFocusable(false);
            etEmail.setFocusable(false);
            etMobile.setFocusable(false);
            etFacebook.setFocusable(false);
            etTwitter.setFocusable(false);
            etPininterest.setFocusable(false);
            etInstagram.setFocusable(false);
        }

        tvTotalVendor.setText(_pref.getUserTotalVendor() + " VENDOR");
        tvTotalUploadedVendor.setText(_pref.getUserTotalUploadedVendor() + " VENDOR");
        tvTotalApprovedVendor.setText(_pref.getUserTotalApprovedVendor() + " VENDOR");
    }

    private void onClick(){

        llEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llEditProfile.setVisibility(View.GONE);
                llSaveProfile.setVisibility(View.VISIBLE);

                etName.setFocusable(true);
                etName.setClickable(true);
                etName.setFocusableInTouchMode(true);
                etEmail.setFocusable(false);
                etMobile.setFocusable(true);
                etMobile.setClickable(true);
                etMobile.setFocusableInTouchMode(true);
                etFacebook.setFocusable(true);
                etFacebook.setClickable(true);
                etFacebook.setFocusableInTouchMode(true);
                etTwitter.setFocusable(true);
                etTwitter.setClickable(true);
                etTwitter.setFocusableInTouchMode(true);
                etPininterest.setFocusable(true);
                etPininterest.setClickable(true);
                etPininterest.setFocusableInTouchMode(true);
                etInstagram.setFocusable(true);
                etInstagram.setClickable(true);
                etInstagram.setFocusableInTouchMode(true);

            }
        });

        llSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llSaveProfile.setVisibility(View.GONE);
                llEditProfile.setVisibility(View.VISIBLE);

                etName.setFocusable(false);
                etEmail.setFocusable(false);
                etMobile.setFocusable(false);
                etFacebook.setFocusable(false);
                etTwitter.setFocusable(false);
                etPininterest.setFocusable(false);
                etInstagram.setFocusable(false);

                if(etName.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                }
                else if(etMobile.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please Enter Your Mobile number", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (_connectionCheck.isNetworkAvailable()) {
                        requestUpdateProfile();
                    }else {
                        _connectionCheck.getNetworkActiveAlert().show();
                    }
                }

            }
        });

        llChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), ActivityChangePassword.class));

            }
        });

        imgViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(llSaveProfile.getVisibility() == View.VISIBLE){
                    displayImageUploadDialog();
                }
            }
        });

    }

    private void requestUpdateProfile() {
        try {
            JSONObject childJsonObj = new JSONObject();
            JSONObject parentJsonObj = new JSONObject();
            childJsonObj.put("accessToken", _pref.getUserAccessToken());
            childJsonObj.put("img", encodedImage);
            childJsonObj.put("name", etName.getText().toString());
            childJsonObj.put("mobile", etMobile.getText().toString());
            childJsonObj.put("facebook", etFacebook.getText().toString());
            childJsonObj.put("twitter", etTwitter.getText().toString());
            childJsonObj.put("instagram", etInstagram.getText().toString());
            childJsonObj.put("pininterest", etPininterest.getText().toString());
            parentJsonObj.put("data",childJsonObj);
            Log.e("JSON:", parentJsonObj.toString());
            setUpProgressDialog();
            _serviceAction.actionInterface = UserProfileFragment.this;
            _serviceAction.requestVersionApi(parentJsonObj, "update-user-profile");

        } catch (Exception e){
            e.printStackTrace();
        }
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
        Log.e("response", String.valueOf(response));

        try{
            JSONObject jsonObj = response.getJSONObject("errNode");
            if(jsonObj.getInt("errCode") == 0) {
                JSONObject dataObj = response.getJSONObject("data");
                if(dataObj.getString("success").equals("true")){
                    _pref.saveUserName(dataObj.getString("name"));
                    _pref.saveUserMobileNo(dataObj.getString("mobile"));
                    _pref.saveUserFacebook(dataObj.getString("facebook"));
                    _pref.saveUserTwitter(dataObj.getString("twitter"));
                    _pref.saveUserPininterest(dataObj.getString("pininterest"));
                    _pref.saveUserInstagram(dataObj.getString("instagram"));
                    _pref.saveUserImagePath(dataObj.getString("image_url"));
                }
            } else {
                Toast.makeText(getActivity(), jsonObj.getString("errMsg"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setData() {

        db.open();
        monthlyGraphItems = db.getMonthlyGraphInfo();
        total_day = Integer.parseInt(_pref.getMonthTotalDay());
        db.close();

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        try{
            for (int i = 0; i < total_day; i++) {
                xVals.add((i) + "");
            }
            for(int i=0; i< monthlyGraphItems.size(); i++){
                yVals.add(new Entry(Integer.parseInt(monthlyGraphItems.get(i).getCount()), Integer.parseInt(monthlyGraphItems.get(i).getDate())));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "");

        // set the line to be drawn like this "- - - - - -"
        //set1.enableDashedLine(10f, 10f, 0f);
        set1.setColor(Color.parseColor("#91D4EE"));
        set1.setCircleColor(Color.parseColor("#91D4EE"));
        set1.setLineWidth(2f);
        set1.setCircleSize(5f);
        set1.setDrawCircleHole(true);
        set1.setValueTextSize(9f);

        set1.setFillColor(Color.parseColor("#F2E9E9"));
        set1.setFillAlpha(225);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        data.setValueFormatter(new LargeValueFormatter());
        XAxis bottomAxis = mChart.getXAxis();
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // set data
        mChart.setData(data);

        // enable value highlighting
        mChart.setHighlightEnabled(true);
        // enable touch gestures
        mChart.setTouchEnabled(true);
        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);
        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);
        // enable/disable highlight indicators (the lines that indicate the
        // highlighted Entry)'
        mChart.setHighlightEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setStartAtZero(true);
        //leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setValueFormatter(new LargeValueFormatter());

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);
        mChart.getAxisRight().setEnabled(false);

        mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        ArrayList<LineDataSet> sets = (ArrayList<LineDataSet>) mChart.getData().getDataSets();
        for (LineDataSet set : sets) {
            if (set.isDrawFilledEnabled())
                set.setDrawFilled(false);
            else
                set.setDrawFilled(true);
        }

        mChart.setPinchZoom(true);
        // dont forget to refresh the drawing
        mChart.invalidate();
    }

    private void displayImageUploadDialog() {
        // TODO Auto-generated method stub

        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                getActivity());
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        ConstantClass.USER_EDIT_NAME = etName.getText().toString();
                        ConstantClass.USER_EDIT_MOBILE = etMobile.getText().toString();
                        ConstantClass.USER_EDIT_FACEBOOK = etFacebook.getText().toString();
                        ConstantClass.USER_EDIT_TWITTER = etTwitter.getText().toString();
                        ConstantClass.USER_EDIT_PININTEREST = etPininterest.getText().toString();
                        ConstantClass.USER_EDIT_INSTAGRAM = etInstagram.getText().toString();

                        Intent gallerypickerIntent = new Intent(
                                Intent.ACTION_PICK);
                        gallerypickerIntent.setType("image/*");
                        startActivityForResult(gallerypickerIntent,
                                GALLERY_PICTURE);
                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        ConstantClass.USER_EDIT_NAME = etName.getText().toString();
                        ConstantClass.USER_EDIT_MOBILE = etMobile.getText().toString();
                        ConstantClass.USER_EDIT_FACEBOOK = etFacebook.getText().toString();
                        ConstantClass.USER_EDIT_TWITTER = etTwitter.getText().toString();
                        ConstantClass.USER_EDIT_PININTEREST = etPininterest.getText().toString();
                        ConstantClass.USER_EDIT_INSTAGRAM = etInstagram.getText().toString();

                        Intent pictureActionIntent = new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(pictureActionIntent,
                                CAMERA_REQUEST);

                    }
                });
        myAlertDialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("requestCode", String.valueOf(requestCode));
        Log.e("resultCode", String.valueOf(resultCode));
        Log.e("data", String.valueOf(data));

        if (requestCode == GALLERY_PICTURE && resultCode == getActivity().RESULT_OK
                && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);

        } else if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            String bitmap_path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                    bitmap, "Title", null);
            Uri contentUri = Uri.parse(bitmap_path);
            Cursor cursor = getActivity().getContentResolver().query(contentUri, null,
                    null, null, null);
            if (cursor == null) {
                picturePath = contentUri.getPath();
            } else {
                cursor.moveToFirst();
                int index = cursor
                        .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                picturePath = cursor.getString(index);

            }
        }
        Log.e("Picture Path", picturePath);
        _pref.saveUserImagePathFromSdCard(picturePath);

    }
}