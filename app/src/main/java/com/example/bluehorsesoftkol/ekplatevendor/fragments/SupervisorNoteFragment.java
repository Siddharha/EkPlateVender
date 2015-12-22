package com.example.bluehorsesoftkol.ekplatevendor.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.CallServiceAction;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.NetworkConnectionCheck;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.adapters.FaqAdapter;
import com.example.bluehorsesoftkol.ekplatevendor.bean.FaqItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.FaqSubItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.SupervisorInformationItem;
import com.example.bluehorsesoftkol.ekplatevendor.interfaces.BackgroundActionInterface;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SupervisorNoteFragment extends Fragment implements BackgroundActionInterface {

    private View rootView;
    private RecyclerView rvSupervisornote;
    private List<SupervisorInformationItem> list;
    private SupervisorListAdapter supervisorListAdapter;

    private NetworkConnectionCheck _connectionCheck;
    private CallServiceAction _serviceAction;
    private ProgressDialog progressDialog;
    private Pref _pref;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_supervisor_note, container, false);

        initialize();
        if (_connectionCheck.isNetworkAvailable()) {
            requestSupervisorDetails();
        }else {
            _connectionCheck.getNetworkActiveAlert().show();
        }

        return rootView;

    }

    private void attachingListAdapter() {

        rvSupervisornote.setAdapter(supervisorListAdapter);
    }

    private void requestSupervisorDetails() {
        try {
            JSONObject childJsonObj = new JSONObject();
            JSONObject parentJsonObj = new JSONObject();
            childJsonObj.put("accesstoken", _pref.getUserAccessToken());
            parentJsonObj.put("data",childJsonObj);
            Log.e("JSON:", parentJsonObj.toString());
            setUpProgressDialog();
            _serviceAction.actionInterface = SupervisorNoteFragment.this;
            _serviceAction.requestVersionApi(parentJsonObj, "supervisor-notes");

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initialize() {

        _connectionCheck = new NetworkConnectionCheck(getActivity());
        _serviceAction = new CallServiceAction(getActivity());
        _pref = new Pref(getActivity());


        rvSupervisornote = (RecyclerView)rootView.findViewById(R.id.rvSupervisornote);
        list = new ArrayList<>();
        supervisorListAdapter = new SupervisorListAdapter(list,R.layout.supervisor_note_layout);
        rvSupervisornote.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                JSONArray jsonObjArray = response.getJSONArray("data");
                for(int i = 0;i<jsonObjArray.length();i++)
                {
                    JSONObject jsonObjDetails = jsonObjArray.getJSONObject(i);

                    SupervisorInformationItem x = new SupervisorInformationItem();
                    x.setSenderImagePath(jsonObjDetails.getString("image_url"));
                    x.setSupervisorName(jsonObjDetails.getString("name"));
                    x.setTime(jsonObjDetails.getString("time"));
                    x.setSupervisorNotes(jsonObjDetails.getString("message"));
                    x.setSender(getFirstChar(jsonObjDetails.getString("sender")));

                    list.add(x);
                }
                attachingListAdapter();
            } else {
                Toast.makeText(getActivity(), jsonObj.getString("errMsg"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private String getFirstChar(String sender) {

        char x;
        x = sender.charAt(0);
        return String.valueOf(x);
    }

    public class SupervisorListAdapter extends RecyclerView.Adapter<SupervisorListAdapter.ViewHolder> {
        private List<SupervisorInformationItem> supervisorInformations;
        private int itemLayout;
        private View rowView;
        private ImageLoader imageLoader;
        DisplayImageOptions options;

        public SupervisorListAdapter(List<SupervisorInformationItem> supervisorInformations, int itemLayout) {
            this.supervisorInformations = supervisorInformations;
            this.itemLayout = itemLayout;

            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true).cacheOnDisk(true)
                    .showImageOnLoading(R.drawable.image_box)
                    .showImageForEmptyUri(R.drawable.image_box)
                    .showImageOnFail(R.drawable.image_box)
                    .imageScaleType(ImageScaleType.EXACTLY).build();
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            rowView = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout, viewGroup, false);
            return new ViewHolder(rowView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            SupervisorInformationItem item = supervisorInformations.get(position);
            viewHolder.tvSupervisorName.setText(item.getSupervisorName());
            viewHolder.tvTime.setText(item.getTime());
            viewHolder.tvSupervisorNotes.setText(item.getSupervisorNotes());
            viewHolder.tvSender.setText(item.getSender());
            viewHolder.itemView.setTag(item);

            imageLoader.displayImage(item.getSenderImagePath(), viewHolder.imgViewSender, options);
        }

        @Override
        public int getItemCount() {
            return supervisorInformations.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgViewSender;
            TextView tvSupervisorName, tvTime, tvSupervisorNotes, tvSender;

            public ViewHolder(View itemView) {
                super(itemView);
                this.imgViewSender = (ImageView) itemView.findViewById(R.id.imgViewSender);
                this.tvSupervisorName = (TextView) itemView.findViewById(R.id.tvSupervisorName);
                this.tvTime = (TextView) itemView.findViewById(R.id.tvTime);
                this.tvSupervisorNotes = (TextView) itemView.findViewById(R.id.tvSupervisorNotes);
                this.tvSender = (TextView) itemView.findViewById(R.id.tvSender);
            }
        }
    }
}
