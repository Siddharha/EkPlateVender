package com.example.bluehorsesoftkol.ekplatevendor.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alexbbb.uploadservice.MultipartUploadRequest;
import com.alexbbb.uploadservice.UploadNotificationConfig;
import com.alexbbb.uploadservice.UploadService;
import com.alexbbb.uploadservice.UploadServiceBroadcastReceiver;
import com.example.bluehorsesoftkol.ekplatevendor.BuildConfig;
import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.CallServiceAction;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.JsonOutputFormatter;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.NetworkConnectionCheck;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.RecyclerItemClickListner;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.UploadVideo;
import com.example.bluehorsesoftkol.ekplatevendor.activity.vendor.ActivityAddVendor;
import com.example.bluehorsesoftkol.ekplatevendor.activity.vendor.ActivityHome;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorBasicInfoItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorVideoItem;
import com.example.bluehorsesoftkol.ekplatevendor.dbpackage.DbAdapter;
import com.example.bluehorsesoftkol.ekplatevendor.interfaces.BackgroundActionInterface;
import com.example.bluehorsesoftkol.ekplatevendor.service.GetFoodItemService;
import com.example.bluehorsesoftkol.ekplatevendor.service.GetMonthlyGraphService;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.UUID;

public class VendorManagementFragment extends Fragment implements BackgroundActionInterface , RecyclerItemClickListner.OnItemClickListener{
//
    private View rootView,rowView;
    private DbAdapter dbAdapter;
    private ArrayList<VendorBasicInfoItem> vendorBasicInfoList, selectedVendorInfoList;
    private Pref _pref;
    private TextView tvNoVendor, tvSelectAll;
    private RecyclerView rvVendorList;
    private LinearLayout llSelectAll, llStartUpload;
    private VendorListAdapter adapter;
    private int selectAllFlag = 0,item_position;
    private JsonOutputFormatter outputFormatter;
    private JSONObject outputJsonObj;
    private NetworkConnectionCheck _connectionCheck;
    private CallServiceAction _serviceAction;
    private ProgressDialog progressDialog;
    private ArrayList<ArrayList<VendorVideoItem>> vendorVideo;
    UploadServiceBroadcastReceiver uploadReceiver;
    @Override
    public void onStart() {
        super.onStart();
        rvVendorList.addOnItemTouchListener(new RecyclerItemClickListner(getActivity(), this));
    }

    @Override
    public void onResume() {
        super.onResume();
        uploadReceiver.register(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        uploadReceiver.unregister(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_vendor_management, container, false);
        initialize();
        getVendorBasicInfo();
        setUpVendorList();
        onClick();
        return rootView;
    }

    private void initialize(){
        rvVendorList = (RecyclerView) rootView.findViewById(R.id.rvVendorList);
        rvVendorList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        llSelectAll = (LinearLayout) rootView.findViewById(R.id.llSelectAll);
        llStartUpload = (LinearLayout) rootView.findViewById(R.id.llStartUpload);
        tvNoVendor = (TextView) rootView.findViewById(R.id.tvNoVendor);
        tvSelectAll = (TextView) rootView.findViewById(R.id.tvSelectAll);
        dbAdapter = new DbAdapter(getActivity());
        _pref = new Pref(getActivity());
        adapter = new VendorListAdapter(getActivity());
        selectedVendorInfoList = new ArrayList<VendorBasicInfoItem>();
        _connectionCheck = new NetworkConnectionCheck(getActivity());
        _serviceAction = new CallServiceAction(getActivity());
        rvVendorList.setVerticalScrollBarEnabled(false);
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        vendorVideo = new ArrayList<ArrayList<VendorVideoItem>>();
        uploadReceiver = new UploadServiceBroadcastReceiver();
    }

    private void onClick(){

        llSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectAllFlag == 0 || selectAllFlag == 3) {
                    selectAllFlag = 1;
                    selectAllVendor();
                    tvSelectAll.setText("Deselect All");
                    llSelectAll.setBackgroundResource(R.drawable.button_shape_red);
                } else {
                    selectAllFlag = 0;
                    deselectAllVendor();
                    tvSelectAll.setText("Select All");
                    llSelectAll.setBackgroundResource(R.drawable.button_shape_green);
                }
            }
        });

        llStartUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_connectionCheck.isNetworkAvailable()) {
                    if (selectAllFlag == 1) {
                        outputFormatter = new JsonOutputFormatter(getActivity(), vendorBasicInfoList);
                        Log.v("outputFormatter", outputFormatter.getFinalOutputJson().toString());
                       videosGet();

                      //  Log.v(">>",outputJsonObj.getJSONArray());
                      //  setUpProgressDialog();
                       /* _serviceAction.actionInterface = VendorManagementFragment.this;
                        _serviceAction.requestVersionApi(outputFormatter.getFinalOutputJson(), "add-vendor");*/
                    } else {
                        if (selectedVendorInfoList.size() > 0) {
                            outputFormatter = new JsonOutputFormatter(getActivity(), selectedVendorInfoList);
                            Log.v("outputFormatter", outputFormatter.getFinalOutputJson().toString());
                            videosGet();
                        //    setUpProgressDialog();
                            /*_serviceAction.actionInterface = VendorManagementFragment.this;
                            _serviceAction.requestVersionApi(outputFormatter.getFinalOutputJson(), "add-vendor");*/
                        } else {
                            Toast.makeText(getActivity(), "Please select vendors to upload", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    _connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });
    }

    private void videosGet() {

            dbAdapter.open();
            for (int i=0; i<selectedVendorInfoList.size(); i++){
                ArrayList<VendorVideoItem> _vendorGalleryVideoItem = new ArrayList<VendorVideoItem>();
                _vendorGalleryVideoItem = dbAdapter.getVendorGalleryVideoInfo(selectedVendorInfoList.get(i).getId());
                vendorVideo.add(_vendorGalleryVideoItem);

            }
            dbAdapter.close();

for(int i = 0;selectedVendorInfoList.size()>i;i++) {
    for (int j = 0; j < vendorVideo.get(i).size(); j++) {
        VendorVideoItem _vendorVideoItem = vendorVideo.get(i).get(j);
        String filePath = _vendorVideoItem.getVideo_path();
        Log.e("Videos:", filePath);
        uploadMultipart(getContext(), filePath, "http://uat.ekplate.com/api/v1/upload-video");
    }
}


       /* for(int i=0; i< vendorVideo.size(); i++){
            VendorVideoItem _vendorVideoItem = vendorVideo.get(position).get(i);
            //JSONObject childrenVendorVideoItem = new JSONObject();

            // Bitmap bitmap = objCompressCrop.compressImageForPath(_vendorImageItem.getImage_path());
            // ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String filePath = _vendorVideoItem.getVideo_path();
            InputStream inStream = new FileInputStream(filePath);
            bytesAvailable = inStream.available();
            bufferSize = Math.min(bytesAvailable, 3);
            byte[] video = new byte[bufferSize];
            String encodedVideo = Base64.encodeToString(video, Base64.DEFAULT);
            childrenVendorVideoItem.put("vid", encodedVideo);
            // parentVendorVideoJsonArray.put(i, childrenVendorVideoItem);
            Log.e(">>", video.toString());
        }*/
       // }
    }

    private void setUpVendorList(){
        ViewGroup.LayoutParams params = rvVendorList.getLayoutParams();
        params.height = 150 * vendorBasicInfoList.size();
        if(vendorBasicInfoList.size()==0){
            tvNoVendor.setVisibility(View.VISIBLE);
            rvVendorList.setVisibility(View.GONE);
        } else {
            tvNoVendor.setVisibility(View.GONE);
            rvVendorList.setVisibility(View.VISIBLE);
        }
        rvVendorList.setAdapter(adapter);
    }

    private void getVendorBasicInfo(){
        dbAdapter.open();
        String accesstoken =  dbAdapter.getCurrentUserDetails();
        vendorBasicInfoList = dbAdapter.getVendorBasicInfo(accesstoken);

        dbAdapter.close();
    }

    private void selectAllVendor(){
        for (int i=0; i<rvVendorList.getChildCount(); i++){

            Log.e("Child Count",String.valueOf(vendorBasicInfoList.size()));
            View rowView = (LinearLayout) rvVendorList.getChildAt(i);
            CheckBox cbSelectItem = (CheckBox) rowView.findViewById(R.id.cbSelectItem);
            cbSelectItem.setChecked(true);
        }
    }

    private void deselectAllVendor(){
        for (int i=0; i<rvVendorList.getChildCount(); i++){
            View rowView = (LinearLayout) rvVendorList.getChildAt(i);
            CheckBox cbSelectItem = (CheckBox) rowView.findViewById(R.id.cbSelectItem);
            cbSelectItem.setChecked(false);
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
                    if (_connectionCheck.isNetworkAvailable()) {
                        getActivity().startService(new Intent(getActivity(), GetMonthlyGraphService.class));
                    }
                    Toast.makeText(getActivity(), "Vendor saved successfully", Toast.LENGTH_SHORT).show();
                    dbAdapter.open();
                    if(selectAllFlag == 1){
                        for(int i = 0; i< vendorBasicInfoList.size(); i++){
                            dbAdapter.deleteBasicInfo(vendorBasicInfoList.get(i).getId());
                            dbAdapter.deleteAllRecord();
                        }
                    }
                    else{
                        for(int i = 0; i< selectedVendorInfoList.size(); i++){
                            dbAdapter.deleteBasicInfo(selectedVendorInfoList.get(i).getId());
                        }
                    }
                    dbAdapter.close();
                    _pref.saveVendorId("");
                    vendorBasicInfoList.clear();
                    selectedVendorInfoList.clear();
                    getVendorBasicInfo();
                    setUpVendorList();
                } else {
                    Toast.makeText(getActivity(), jsonObjData.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), jsonObj.getString("errMsg"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View childView, int position) {


        View rowView = (LinearLayout) rvVendorList.getChildAt(position);
        CheckBox cbSelectItem = (CheckBox) rowView.findViewById(R.id.cbSelectItem);

        if(cbSelectItem.isChecked())
        {
            cbSelectItem.setChecked(false);
        }
        else
        {
            cbSelectItem.setChecked(true);
        }
        int ID = vendorBasicInfoList.get(position).getId();
        //Toast.makeText(getActivity(),"Vendor Id:"+String.valueOf(ID),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongPress(View childView, final int position) {

        PopupMenu popup = new PopupMenu(getActivity(), childView, Gravity.CENTER);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("Edit Vendor")) {
                    int vendorId = vendorBasicInfoList.get(position).getId();
                    _pref.saveVendorId(String.valueOf(vendorId));
                    Intent i = new Intent(getActivity(), ActivityAddVendor.class);
                    startActivity(i);
                }
                return true;
            }


        });

        popup.show(); //showing popup menu
    }

    public class VendorListAdapter extends RecyclerView.Adapter<VendorListAdapter.ItemHolder>{
        private Context context;

        public VendorListAdapter(Context context){
            this.context = context;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
             rowView = LayoutInflater.from(context).inflate(R.layout.vendor_row_item_layouy, parent, false);
            ItemHolder holder = new ItemHolder(rowView);
            return holder;
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            holder.tvVendorShop.setText(vendorBasicInfoList.get(position).getShopName());
            holder.tvVendorMobileNo.setText(vendorBasicInfoList.get(position).getMobileNo());
            holder.tvVendorEstablishmentYear.setText("Established on " + vendorBasicInfoList.get(position).getEstablishmentYear());
            holder.cbSelectItem.setOnCheckedChangeListener(checkedChangeListener);
            holder.cbSelectItem.setTag(holder);
            item_position = position;
        }

        @Override
        public int getItemCount() {
            return vendorBasicInfoList.size();
        }

        public class ItemHolder extends RecyclerView.ViewHolder{
            private CheckBox cbSelectItem;
            private TextView tvVendorShop, tvVendorMobileNo, tvVendorEstablishmentYear;
            public ItemHolder(View itemView) {
                super(itemView);
                cbSelectItem = (CheckBox) itemView.findViewById(R.id.cbSelectItem);
                tvVendorShop = (TextView) itemView.findViewById(R.id.tvVendorShop);
                tvVendorMobileNo = (TextView) itemView.findViewById(R.id.tvVendorMobileNo);
                tvVendorEstablishmentYear = (TextView) itemView.findViewById(R.id.tvVendorEstablishmentYear);

            }
        }

        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(selectAllFlag == 1 || selectAllFlag == 2){
                    selectAllFlag = 2;
                }
                else{
                    selectAllFlag = 3;
                }
                ItemHolder holder = (ItemHolder) buttonView.getTag();
                int position = holder.getAdapterPosition();
                if(isChecked){
                    VendorBasicInfoItem _item = new VendorBasicInfoItem();
                    _item.setId(vendorBasicInfoList.get(position).getId());
                    _item.setShopName(vendorBasicInfoList.get(position).getShopName());
                    _item.setMobileNo(vendorBasicInfoList.get(position).getMobileNo());
                    _item.setEstablishmentYear(vendorBasicInfoList.get(position).getEstablishmentYear());
                    _item.setContactPersonName(vendorBasicInfoList.get(position).getContactPersonName());
                    _item.setAddedBy(vendorBasicInfoList.get(position).getAddedBy());
                    selectedVendorInfoList.add(_item);
                } else {
                    for (int j=0; j<selectedVendorInfoList.size(); j++){
                        if(vendorBasicInfoList.get(position).getId() == selectedVendorInfoList.get(j).getId()){
                            selectedVendorInfoList.remove(j);
                        }
                    }
                }
                Log.v("size", String.valueOf(selectedVendorInfoList.size()));
            }
        };
    }

    public void uploadMultipart(final Context context,String filePath ,String serverUrlString) {

        final String uploadID = UUID.randomUUID().toString();
        //serverUrlString = "http://www.yoursite.com/yourscript";

        try {
            new MultipartUploadRequest(context, uploadID, serverUrlString)
                    .addFileToUpload(filePath, "your-param-name")
                    .addHeader("your-custom-header-name", "your-custom-value")
                    .addParameter("your-param-name", "your-param-value")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();
        } catch (Exception exc) {
            Log.e("AndroidUploadService", exc.getMessage(), exc);
        }

          final String TAG = "AndroidUploadService";

         final UploadServiceBroadcastReceiver uploadReceiver =
                new UploadServiceBroadcastReceiver() {

                    // you can override this progress method if you want to get
                    // the completion progress in percent (0 to 100)
                    // or if you need to know exactly how many bytes have been transferred
                    // override the method below this one
                    @Override
                    public void onProgress(String uploadId, int progress) {
                        Log.i(TAG, "The progress of the upload with ID "
                                + uploadId + " is: " + progress);
                    }

                    @Override
                    public void onProgress(final String uploadId,
                                           final long uploadedBytes,
                                           final long totalBytes) {
                        Log.i(TAG, "Upload with ID " + uploadId +
                                " uploaded bytes: " + uploadedBytes
                                + ", total: " + totalBytes);
                    }

                    @Override
                    public void onError(String uploadId, Exception exception) {
                        Log.e(TAG, "Error in upload with ID: " + uploadId + ". "
                                + exception.getLocalizedMessage(), exception);
                    }

                    @Override
                    public void onCompleted(String uploadId,
                                            int serverResponseCode,
                                            String serverResponseMessage) {
                        Log.i(TAG, "Upload with ID " + uploadId
                                + " has been completed with HTTP " + serverResponseCode
                                + ". Response from server: " + serverResponseMessage);

                        //If your server responds with a JSON, you can parse it
                        //from serverResponseMessage string using a library
                        //such as org.json (embedded in Android) or Google's gson
                    }
                };

    }
}
