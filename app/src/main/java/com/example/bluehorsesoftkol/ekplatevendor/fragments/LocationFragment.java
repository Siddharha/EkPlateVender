package com.example.bluehorsesoftkol.ekplatevendor.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.NetworkConnectionCheck;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.activity.vendor.ActivityAddVendor;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorAddressInfoItem;
import com.example.bluehorsesoftkol.ekplatevendor.dbpackage.DbAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationFragment extends Fragment {

    private View rootView;
    private ProgressDialog progressDialog;
    private Spinner spinLocality;
    private EditText etVendorAddress, etLatitude, etLongitude;
    private TextView tvMarkerStatus, tvMarkerPosition, tvMarkerAddress,tvLockLocation;
    private Button btnBack, btnNext;
    private ScrollView svLocations;
    private MapView mapView;
    private GoogleMap map;
    private View customView;
    ImageView marker;
    private NetworkConnectionCheck _connectionCheck;
    private LatLng latlng;
    private double latitude = 0.0, longitude = 0.0, prevLatitude = 0.0, prevLongitude = 0.0;

    private Geocoder geocoder;
    private List<Address> addresses ;

    private ActivityAddVendor activityAddVendor;
    private DbAdapter db;
    private Pref _pref;
    private ArrayList<VendorAddressInfoItem> vendorAddressInfoItems;
    private  Boolean lockTrigger;
    Bundle savedInstanceState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.savedInstanceState = savedInstanceState;
        rootView = inflater.inflate(R.layout.fragment_location, container, false);
        initialize();

        db.open();
        if (db.getVendorCompleteStep()>=2){
            vendorAddressInfoItems = db.getVendorAddressInfo(Integer.parseInt(_pref.getVendorId()));
            db.close();
            setAddressField();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
            // Gets the MapView from the XML layout and creates it
            mapView = (MapView) rootView.findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);

            mapView.onResume();
            // Gets to GoogleMap from the MapView and does initialization stuff
            map = mapView.getMap();
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.setMyLocationEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setCompassEnabled(true);
            map.getUiSettings().setRotateGesturesEnabled(true);
            map.getUiSettings().setZoomGesturesEnabled(true);

            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
            MapsInitializer.initialize(getActivity());

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latitude, longitude)).zoom(12f).tilt(70).build();
            map.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            marker.animate().translationY(-marker.getHeight() / 2);
        tvLockLocation.animate().translationY(-(tvLockLocation.getHeight() / 2)-10);
        lockTrigger = false;
        tvLockLocation.setText("Lock Map");
            try {
                new GetLocationAsync().execute(latitude, longitude);

            } catch (Exception e) {

            }

            map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    if (!(map.getMyLocation() == null)) {
                        latitude = map.getMyLocation().getLatitude();
                        longitude = map.getMyLocation().getLongitude();
                        CameraPosition cameraPosition;
                        db.open();
                        if (db.getVendorCompleteStep()>=2) {
                            cameraPosition = new CameraPosition.Builder()
                                    .target(new LatLng(prevLatitude, prevLongitude)).zoom(15f).build();
                        } else {
                            cameraPosition = new CameraPosition.Builder()
                                    .target(new LatLng(latitude, longitude)).zoom(15f).build();
                        }
                        map.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                        marker.animate().translationY(-marker.getHeight() / 2);
                        tvLockLocation.animate().translationY(-(tvLockLocation.getHeight() / 2)-10);
                        try {
                            if (db.getVendorCompleteStep()>=2) {
                                new GetLocationAsync().execute(prevLatitude, prevLongitude);
                            } else {
                                new GetLocationAsync().execute(latitude, longitude);
                            }
                        } catch (Exception e) {

                        }
                        db.close();
                    }
                }
            });

        if(_connectionCheck.isNetworkAvailable()) {
            onClick();
        }
        else
        {
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAlert().show();

                }
            });

        }

    }


    private void setAddressField(){
        if(vendorAddressInfoItems.size()>0){
            etVendorAddress.setText(vendorAddressInfoItems.get(0).getAddress());
            etLatitude.setText(vendorAddressInfoItems.get(0).getLatitude());
            etLongitude.setText(vendorAddressInfoItems.get(0).getLongitude());
            tvMarkerPosition.setText(vendorAddressInfoItems.get(0).getLatitude()
                    + ", " + vendorAddressInfoItems.get(0).getLongitude());
            tvMarkerAddress.setText(vendorAddressInfoItems.get(0).getAddress());
            prevLatitude = Double.parseDouble(vendorAddressInfoItems.get(0).getLatitude());
            prevLongitude = Double.parseDouble(vendorAddressInfoItems.get(0).getLongitude());
        }

    }


    public AlertDialog getAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Network Status");
        builder.setMessage("Network connection not available. Please connect the network to Select Locations!.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                        db.updateVendorCompleteStep(2);
                        activityAddVendor.findViewById(R.id.llTiming).setEnabled(true);
                        activityAddVendor.loadTimingFragment();
                    }
                });
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    private void initialize() {

        activityAddVendor = (ActivityAddVendor) getActivity();
        db = new DbAdapter(getActivity());
        _pref = new Pref(getActivity());
        _connectionCheck = new NetworkConnectionCheck(getActivity());
        mapView = (MapView)rootView.findViewById(R.id.mapView);
        customView = (View)rootView.findViewById(R.id.customView);
        spinLocality = (Spinner) rootView.findViewById(R.id.spinLocality);
        etVendorAddress = (EditText) rootView.findViewById(R.id.etVendorAddress);
        etLatitude = (EditText) rootView.findViewById(R.id.etLatitude);
        etLongitude = (EditText) rootView.findViewById(R.id.etLongitude);
        tvMarkerStatus = (TextView) rootView.findViewById(R.id.tvMarkerStatus);
        tvMarkerPosition = (TextView) rootView.findViewById(R.id.tvMarkerPosition);
        tvMarkerAddress = (TextView) rootView.findViewById(R.id.tvMarkerAddress);
        btnBack = (Button)rootView.findViewById(R.id.btnBack);
        btnNext = (Button)rootView.findViewById(R.id.btnNext);
        svLocations = (ScrollView)rootView.findViewById(R.id.svLocations);
        marker = (ImageView)rootView.findViewById(R.id.imgMarker);
        tvLockLocation = (TextView)rootView.findViewById(R.id.tvLockLocation);
    }

    private void onClick() {

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                latlng = map.getCameraPosition().target;
                latitude = latlng.latitude;
                longitude = latlng.longitude;
                Log.e("LatLong:", latlng.toString());
                tvMarkerPosition.setText(latitude + "," + longitude);
                try {
                    new GetLocationAsync().execute(latitude, longitude);
                } catch (Exception e) {

                }
            }
        });

        customView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        svLocations.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        svLocations.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        svLocations.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }

            }
        });

        //region Lat Events
        etLatitude.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(etVendorAddress.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    if (!(etLatitude.getText().toString().isEmpty())) {
                        if (etLatitude.getText().toString().isEmpty()) {
                            etLatitude.setText("0");
                        } else if (etLongitude.getText().toString().isEmpty()) {
                            etLongitude.setText("0");
                        }
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(Double.parseDouble(etLatitude.getText().toString()),
                                        Double.parseDouble(etLongitude.getText().toString()))).zoom(12f).tilt(70).build();
                        map.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                        marker.animate().translationY(-marker.getHeight() / 2);
                        tvLockLocation.animate().translationY(-(tvLockLocation.getHeight() / 2)-10);
                        try {
                            new GetLocationAsync().execute(Double.parseDouble(etLatitude.getText().toString()),
                                    Double.parseDouble(etLongitude.getText().toString()));

                        } catch (Exception e) {

                        }
                    }
                    return true;
                }
                return false;
            }
        });

        etLongitude.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(etVendorAddress.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if (!(etLongitude.getText().toString().isEmpty())) {
                        if (etLatitude.getText().toString().isEmpty()) {
                            etLatitude.setText("0");
                        } else if (etLongitude.getText().toString().isEmpty()) {
                            etLongitude.setText("0");
                        }

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(Double.parseDouble(etLatitude.getText().toString()),
                                        Double.parseDouble(etLongitude.getText().toString()))).zoom(12f).tilt(70).build();
                        map.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                        marker.animate().translationY(-marker.getHeight() / 2);
                        tvLockLocation.animate().translationY(-(tvLockLocation.getHeight() / 2)-10);

                        try {
                            new GetLocationAsync().execute(Double.parseDouble(etLatitude.getText().toString()),
                                    Double.parseDouble(etLongitude.getText().toString()));

                        } catch (Exception e) {

                        }
                    }
                    return true;
                }
                return false;
            }
        });

        etVendorAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if(!(etVendorAddress.getText().toString().isEmpty()))
                    {
                        //region Get Latlong By Text
                        Geocoder g = new Geocoder(getActivity());
                        List<Address> addressList = null;
                        String searchRoad = etVendorAddress.getText().toString();
                        try {
                            addressList = g.getFromLocationName(searchRoad, 1);
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), "Location not found", Toast.LENGTH_SHORT)
                                    .show();
                            e.printStackTrace();
                        } finally {
                            Address address = addressList.get(0);
                            if (address.hasLatitude() && address.hasLongitude()) {
                                double selectedLat = address.getLatitude();
                                double selectedLng = address.getLongitude();
                                LatLng Road = new LatLng(selectedLat, selectedLng);
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(Road).zoom(15f).build();
                                map.animateCamera(CameraUpdateFactory
                                        .newCameraPosition(cameraPosition));
                                marker.animate().translationY(-marker.getHeight() / 2);
                                tvLockLocation.animate().translationY(-(tvLockLocation.getHeight() / 2)-10);
                            }
                        }
                    }

                    InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(etVendorAddress.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    return true;

                }
                return false;
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addresses != null) {
                    db.open();
                    if (db.getVendorCompleteStep()>=2) {
                        if (vendorAddressInfoItems.size() > 0) {
                            int success = db.updateVendorLocation("", tvMarkerAddress.getText().toString(),
                                    String.valueOf(latitude), String.valueOf(longitude));
                            Toast.makeText(getActivity(), "Location Info Updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Long success = db.insertVendorLocation("", tvMarkerAddress.getText().toString(),
                                    String.valueOf(latitude), String.valueOf(longitude));
                            Toast.makeText(getActivity(), "Location Info Updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Long success = db.insertVendorLocation("", tvMarkerAddress.getText().toString(),
                                String.valueOf(latitude), String.valueOf(longitude));
                        db.updateVendorCompleteStep(2);
                        Toast.makeText(getActivity(), "Location Info Inserted successfully", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                    activityAddVendor.findViewById(R.id.llTiming).setEnabled(true);
                    activityAddVendor.loadTimingFragment();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityAddVendor.loadBasicInformationFragment();
            }
        });

        tvLockLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!lockTrigger)
                {
                    mapView.onPause();
                    tvLockLocation.setText("Unlock Map");
                    lockTrigger = true;
                } else
                {
                    mapView.onResume();
                    tvLockLocation.setText("Lock Map");
                    lockTrigger = false;
                }

            }
        });

    }
    private void setUpProgressDialog(){

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

    }
    private class GetLocationAsync extends AsyncTask<Double, Void, String> {

        double x, y;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Double... params) {

                x = params[0];
                y = params[1];
    try {
        if(_connectionCheck.isNetworkAvailable()) {
            addresses = new ArrayList<>();
            geocoder = new Geocoder(getActivity(), Locale.ENGLISH);

                addresses = geocoder.getFromLocation(x, y, 1);
                Log.v("addresses", addresses.toString());

        }

    } catch (IOException e) {
        Log.e("tag", e.getMessage());
    }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {

            try {
                if(addresses !=null) {
                    tvMarkerAddress.setText(addresses.get(0).getAddressLine(0)
                            + ", " + addresses.get(0).getAddressLine(1));
                    etVendorAddress.setText(addresses.get(0).getAddressLine(0)
                            + ", " + addresses.get(0).getAddressLine(1));
                    etLatitude.setText(String.valueOf(latitude));
                    etLongitude.setText(String.valueOf(longitude));
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }



}