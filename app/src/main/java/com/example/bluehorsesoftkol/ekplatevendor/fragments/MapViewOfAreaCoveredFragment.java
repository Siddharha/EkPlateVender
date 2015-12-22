package com.example.bluehorsesoftkol.ekplatevendor.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.CallServiceAction;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.NetworkConnectionCheck;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.adapters.FaqAdapter;
import com.example.bluehorsesoftkol.ekplatevendor.bean.FaqItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.FaqSubItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorMapLocationItem;
import com.example.bluehorsesoftkol.ekplatevendor.interfaces.BackgroundActionInterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapViewOfAreaCoveredFragment extends Fragment implements BackgroundActionInterface{

    private View rootView;
    Bundle savedInstanceState;
    private MapView mapView;
    private GoogleMap map;

    private ArrayList<VendorMapLocationItem> uploadedVendorMapLocations = new ArrayList<>();
    private ArrayList<VendorMapLocationItem> approvedVendorMapLocations = new ArrayList<>();
    private ArrayList<VendorMapLocationItem> uploadedByOtherVendorMapLocations = new ArrayList<>();

    private NetworkConnectionCheck _connectionCheck;
    private CallServiceAction _serviceAction;
    private ProgressDialog progressDialog;
    private Pref _pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_map_area_covered, container, false);
        this.savedInstanceState = savedInstanceState;
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        _connectionCheck = new NetworkConnectionCheck(getActivity());
        _serviceAction = new CallServiceAction(getActivity());
        _pref = new Pref(getActivity());

        mapView = (MapView)rootView.findViewById(R.id.mapView);
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

        /*CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(20.593684, 78.962880)).zoom(5f).build();
        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));*/

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getActivity());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getActivity());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.LEFT);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getActivity());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        if (_connectionCheck.isNetworkAvailable()) {
            requestVendorMapLocation();
        }else {
            _connectionCheck.getNetworkActiveAlert().show();
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void requestVendorMapLocation(){

        try {
            JSONObject childJsonObj = new JSONObject();
            JSONObject parentJsonObj = new JSONObject();
            childJsonObj.put("accessToken", _pref.getUserAccessToken());
            parentJsonObj.put("data", childJsonObj);
            Log.e("JSON:", parentJsonObj.toString());
            setUpProgressDialog();
            _serviceAction.actionInterface = MapViewOfAreaCoveredFragment.this;
            _serviceAction.requestVersionApi(parentJsonObj, "get-vendor-map-locations");

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

        Log.v("response", String.valueOf(response));
        progressDialog.dismiss();

        try{
            JSONObject jsonObj = response.getJSONObject("errNode");
            if(jsonObj.getInt("errCode") == 0) {
                JSONObject jsonObjData = response.getJSONObject("data");
                if(jsonObjData.getBoolean("success")){
                    if(jsonObjData.has("uploaded")){
                        JSONArray uploadedArray = jsonObjData.getJSONArray("uploaded");
                        for (int i = 0; i < uploadedArray.length(); i++) {
                            JSONObject uploadedObj = uploadedArray.getJSONObject(i);
                            VendorMapLocationItem item = new VendorMapLocationItem();
                            item.setName(uploadedObj.getString("name"));
                            item.setContact(uploadedObj.getString("contact"));
                            item.setEstablishmentYear(uploadedObj.getString("establishmentYear"));
                            item.setLatitude(uploadedObj.getString("latitude"));
                            item.setLongitude(uploadedObj.getString("longitude"));
                            uploadedVendorMapLocations.add(item);
                        }
                    }

                    if(jsonObjData.has("approved")){
                        JSONArray approvedArray = jsonObjData.getJSONArray("approved");
                        for (int i = 0; i < approvedArray.length(); i++) {
                            JSONObject approvedObj = approvedArray.getJSONObject(i);
                            VendorMapLocationItem item = new VendorMapLocationItem();
                            item.setName(approvedObj.getString("name"));
                            item.setContact(approvedObj.getString("contact"));
                            item.setEstablishmentYear(approvedObj.getString("establishmentYear"));
                            item.setLatitude(approvedObj.getString("latitude"));
                            item.setLongitude(approvedObj.getString("longitude"));
                            approvedVendorMapLocations.add(item);
                        }
                    }

                    if(jsonObjData.has("uploaded_by_other")){
                        JSONArray uploadedByOtherArray = jsonObjData.getJSONArray("uploaded_by_other");

                        for (int i = 0; i < uploadedByOtherArray.length(); i++) {
                            JSONObject uploadedByOtherObj = uploadedByOtherArray.getJSONObject(i);
                            VendorMapLocationItem item = new VendorMapLocationItem();
                            item.setName(uploadedByOtherObj.getString("name"));
                            item.setContact(uploadedByOtherObj.getString("contact"));
                            item.setEstablishmentYear(uploadedByOtherObj.getString("establishmentYear"));
                            item.setLatitude(uploadedByOtherObj.getString("latitude"));
                            item.setLongitude(uploadedByOtherObj.getString("longitude"));
                            uploadedByOtherVendorMapLocations.add(item);
                        }
                        setVendorMapLocation();
                    }
                } else {
                    Toast.makeText(getActivity(), jsonObjData.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Something going wrong", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setVendorMapLocation(){

        if(uploadedVendorMapLocations.size()>0){
            for(int i=0; i< uploadedVendorMapLocations.size(); i++){
                if (!uploadedVendorMapLocations.get(i).getLatitude().equals("0.0") || !uploadedVendorMapLocations.get(i).getLongitude().equals("0.0")) {
                    map.addMarker(new MarkerOptions()
                            .title(uploadedVendorMapLocations.get(i).getName())
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .snippet("Contact: " + uploadedVendorMapLocations.get(i).getContact() + "\n" + "Establishment Year: " + uploadedVendorMapLocations.get(i).getEstablishmentYear())
                            .position(new LatLng(Double.parseDouble(uploadedVendorMapLocations.get(i).getLatitude()), Double.parseDouble(uploadedVendorMapLocations.get(i).getLongitude()))));
                }
            }

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(uploadedVendorMapLocations.get(0).getLatitude()), Double.parseDouble(uploadedVendorMapLocations.get(0).getLongitude()))).zoom(7f).build();
            map.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }

        if(approvedVendorMapLocations.size()>0){
            for(int i=0; i< approvedVendorMapLocations.size(); i++){
                if (!approvedVendorMapLocations.get(i).getLatitude().equals("0.0") || !approvedVendorMapLocations.get(i).getLongitude().equals("0.0")) {
                    map.addMarker(new MarkerOptions()
                            .title(approvedVendorMapLocations.get(i).getName())
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .snippet("Contact: " + approvedVendorMapLocations.get(i).getContact() + "\n" + "Establishment Year: " + approvedVendorMapLocations.get(i).getEstablishmentYear())
                            .position(new LatLng(Double.parseDouble(approvedVendorMapLocations.get(i).getLatitude()), Double.parseDouble(approvedVendorMapLocations.get(i).getLongitude()))));
                }
            }
        }

        if(uploadedByOtherVendorMapLocations.size()>0){
            for(int i=0; i< uploadedByOtherVendorMapLocations.size(); i++) {
                if (!uploadedByOtherVendorMapLocations.get(i).getLatitude().equals("0.0") || !uploadedByOtherVendorMapLocations.get(i).getLongitude().equals("0.0")) {
                    map.addMarker(new MarkerOptions()
                            .title(uploadedByOtherVendorMapLocations.get(i).getName())
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .snippet("Contact: " + uploadedByOtherVendorMapLocations.get(i).getContact() + "\n" + "Establishment Year: " + uploadedByOtherVendorMapLocations.get(i).getEstablishmentYear())
                            .position(new LatLng(Double.parseDouble(uploadedByOtherVendorMapLocations.get(i).getLatitude()), Double.parseDouble(uploadedByOtherVendorMapLocations.get(i).getLongitude()))));
                }
            }
        }

    }

}