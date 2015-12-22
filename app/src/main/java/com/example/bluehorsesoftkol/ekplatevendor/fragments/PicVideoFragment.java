package com.example.bluehorsesoftkol.ekplatevendor.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.ConstantClass;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.activity.vendor.Action;
import com.example.bluehorsesoftkol.ekplatevendor.activity.vendor.ActivityAddVendor;
import com.example.bluehorsesoftkol.ekplatevendor.activity.vendor.CustomGallery;
import com.example.bluehorsesoftkol.ekplatevendor.adapters.GalleryAdapter;
import com.example.bluehorsesoftkol.ekplatevendor.adapters.GalleryAdapterVideo;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorImageItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorVideoItem;
import com.example.bluehorsesoftkol.ekplatevendor.dbpackage.DbAdapter;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import java.util.ArrayList;

public class PicVideoFragment extends Fragment {

    private View rootView;

    private GridView gridGallery, gridGalleryVideo;
    private Handler handler;
    private  ImageView imgViewCamera, imgViewVideo;
    private GalleryAdapter adapter;
    private GalleryAdapterVideo adapterVideo;
    private Button btnBack, btnNext;

    ArrayList<CustomGallery> dataT = new ArrayList<>();
    ArrayList<CustomGallery> dataTVideo = new ArrayList<>();
    int pos, posVideo;

    private ActivityAddVendor activityAddVendor;
    private DbAdapter db;
    private Pref _pref;

    ImageLoader imageLoader;
    private ArrayList<VendorImageItem> vendorImageItems;
    private ArrayList<VendorVideoItem> vendorVideoItems;

    protected static final int CAMERA_PICTURE = 0;
    protected static final int GALLERY_PICTURE = 1;
    protected static final int CAMERA_VIDEO = 2;
    protected static final int GALLERY_VIDEO = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_pic_video, container, false);
        ConstantClass.CURRENT_TAB = 6;

        initialize();

        db.open();
        if(db.getVendorCompleteStep()>=7){
            vendorImageItems = db.getVendorGalleryImageInfo(Integer.parseInt(_pref.getVendorId()));
            vendorVideoItems = db.getVendorGalleryVideoInfo(Integer.parseInt(_pref.getVendorId()));
            setSelectedImage();
            setSelectedVideo();
        }
        db.close();

        onClick();

        return rootView;
    }

    private void initialize(){

        activityAddVendor = (ActivityAddVendor) getActivity();
        db = new DbAdapter(getActivity());
        _pref = new Pref(getActivity());

        handler = new Handler();
        gridGallery = (GridView) rootView.findViewById(R.id.gridGallery);
        gridGallery.setFastScrollEnabled(true);
        gridGalleryVideo = (GridView) rootView.findViewById(R.id.gridGalleryVideo);
        gridGalleryVideo.setFastScrollEnabled(true);
        imgViewCamera = (ImageView) rootView.findViewById(R.id.imgViewCamera);
        imgViewVideo = (ImageView) rootView.findViewById(R.id.imgViewVideo);
        btnBack = (Button) rootView.findViewById(R.id.btnBack);
        btnNext = (Button) rootView.findViewById(R.id.btnNext);

        adapter = new GalleryAdapter(getActivity(), imageLoader);
        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);

        adapterVideo = new GalleryAdapterVideo(getActivity(), imageLoader);
        adapterVideo.setMultiplePick(false);
        gridGalleryVideo.setAdapter(adapterVideo);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                getActivity()).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    private void setSelectedImage(){
        for(int i=0; i<vendorImageItems.size(); i++){
            CustomGallery _item = new CustomGallery();
            _item.isSeleted = true;
            _item.sdcardPath = vendorImageItems.get(i).getImage_path();
            dataT.add(_item);
        }

        setGridView();
    }

    private void setSelectedVideo(){
        for(int i=0; i<vendorVideoItems.size(); i++){
            CustomGallery _item = new CustomGallery();
            _item.isSeleted = true;
            _item.sdcardPath = vendorVideoItems.get(i).getVideo_path();
            dataTVideo.add(_item);
        }

        setGridViewVideo();
    }

    private void onClick(){

        imgViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataT.size() < 10) {

                    ConstantClass.CURRENT_TAB = 8;
                    Intent pictureActionIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(pictureActionIntent, CAMERA_PICTURE);
                } else {
                    Toast.makeText(getActivity(), "You cant add more than 10 photos",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        imgViewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataTVideo.size() < 10) {

                    ConstantClass.CURRENT_TAB = 8;
                    Intent pictureActionIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    startActivityForResult(pictureActionIntent, CAMERA_VIDEO);
                } else {
                    Toast.makeText(getActivity(), "You cant add more than 10 photos",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        gridGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                // TODO Auto-generated method stub

                pos = position;

                if (position == adapter.getCount() - 1) {

                    if (dataT.size() < 10) {

                        ConstantClass.CURRENT_TAB = 8;
                        displayImageUploadDialog();

                    } else {
                        Toast.makeText(getActivity(), "You cant add more than 10 photos",
                                Toast.LENGTH_LONG).show();
                    }

                } else {

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.setContentView(R.layout.dialog_image);
                    dialog.show();

                    ImageView imgViewPopup = (ImageView) dialog.findViewById(R.id.imgViewPopup);
                    LinearLayout llCrossPopup = (LinearLayout) dialog.findViewById(R.id.llCrossPopup);
                    LinearLayout llDeletePopup = (LinearLayout) dialog.findViewById(R.id.llDeletePopup);

                    imageLoader.displayImage(
                            "file://" + adapter.getItem(position).sdcardPath,
                            imgViewPopup);

                    llCrossPopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    llDeletePopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dataT.remove(position);
                            setGridView();
                            dialog.dismiss();

                        }
                    });
                }
            }
        });

        gridGalleryVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                // TODO Auto-generated method stub

                pos = position;

                if (position == adapterVideo.getCount() - 1) {

                    if (dataTVideo.size() < 10) {

                        ConstantClass.CURRENT_TAB = 8;
                        displayVideoUploadDialog();

                    } else {
                        Toast.makeText(getActivity(), "You cant add more than 10 videos",
                                Toast.LENGTH_LONG).show();
                    }

                }else {

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.setContentView(R.layout.dialog_video);
                    dialog.show();

                    VideoView videoViewPopup = (VideoView) dialog.findViewById(R.id.videoViewPopup);
                    LinearLayout llCrossPopup = (LinearLayout) dialog.findViewById(R.id.llCrossPopup);
                    LinearLayout llDeletePopup = (LinearLayout) dialog.findViewById(R.id.llDeletePopup);

                    Log.v("videoPath", adapterVideo.getItem(position).sdcardPath);
                    videoViewPopup.setVideoPath("file://" + adapterVideo.getItem(position).sdcardPath);

                    MediaController mediaController = new MediaController(getActivity());
                    mediaController.setAnchorView(videoViewPopup);;
                    videoViewPopup.requestFocus();
                    videoViewPopup.start();

                    llCrossPopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    llDeletePopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dataTVideo.remove(position);
                            setGridViewVideo();
                            dialog.dismiss();

                        }
                    });
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.open();
                if (db.getVendorCompleteStep()>=7) {
                    for (int i = 0; i < vendorImageItems.size(); i++) {
                        boolean result = db.deletePics(vendorImageItems.get(i).getId());
                        Log.e("result", String.valueOf(result));
                    }
                    for (int j = 0; j < vendorVideoItems.size(); j++) {
                        boolean result = db.deleteVideos(vendorVideoItems.get(j).getId());
                        Log.e("result", String.valueOf(result));
                    }
                }

                if (dataT.size() > 0) {
                    for (int i = 0; i < dataT.size(); i++) {
                        Log.v("imagepath" + i, dataT.get(i).sdcardPath);
                        Long success = db.insertVendorImage(dataT.get(i).sdcardPath);
                    }
                    if(dataTVideo.size()>0){
                        for (int j = 0; j < dataTVideo.size(); j++) {
                            Log.v("videopath" + j, dataTVideo.get(j).sdcardPath);
                            Long success = db.insertVendorVideo(dataTVideo.get(j).sdcardPath);
                        }
                    }

                    if (db.getVendorCompleteStep()>=7) {
                        Toast.makeText(getActivity(), "Vendor Images Updated successfully", Toast.LENGTH_LONG).show();
                    } else {
                        db.updateVendorCompleteStep(7);
                        Toast.makeText(getActivity(), "Vendor Images Inserted successfully", Toast.LENGTH_LONG).show();
                    }
                    db.close();
                    activityAddVendor.findViewById(R.id.llAboutVendor).setEnabled(true);
                    activityAddVendor.loadAboutVendorFragment();

                } else {
                    Toast.makeText(getActivity(), "Please Select Atleast One Image", Toast.LENGTH_LONG).show();
                }

            }

        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityAddVendor.loadItemPurchaseFragment();
            }
        });

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

                        Intent galleryActionIntent = new Intent(Action.ACTION_MULTIPLE_PICK);
                        startActivityForResult(galleryActionIntent, GALLERY_PICTURE);
                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent pictureActionIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(pictureActionIntent, CAMERA_PICTURE);
                    }
                });
        myAlertDialog.show();
    }

    private void displayVideoUploadDialog() {
        // TODO Auto-generated method stub

        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                getActivity());
        myAlertDialog.setTitle("Upload Videos Option");
        myAlertDialog.setMessage("How do you want to set your video?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent galleryActionIntent = new Intent(Action.ACTION_MULTIPLE_PICK_VIDEO);
                        startActivityForResult(galleryActionIntent, GALLERY_VIDEO);
                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent pictureActionIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        startActivityForResult(pictureActionIntent, CAMERA_VIDEO);
                    }
                });
        myAlertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PICTURE && resultCode == Activity.RESULT_OK
                && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            String bitmap_path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                    bitmap, "Title", null);
            Uri contentUri = Uri.parse(bitmap_path);
            Cursor cursor = getActivity().getContentResolver().query(contentUri, null,
                    null, null, null);
            String image_path;
            if (cursor == null) {
                image_path = contentUri.getPath();
            } else {
                cursor.moveToFirst();
                int index = cursor
                        .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                image_path = cursor.getString(index);
            }

            CustomGallery item = new CustomGallery();
            item.sdcardPath = image_path;
            dataT.add(item);
            setGridView();

        }

        if (requestCode == GALLERY_PICTURE && resultCode == Activity.RESULT_OK) {
            String[] all_path = data.getStringArrayExtra("all_path");

            for (String string : all_path) {
                CustomGallery item = new CustomGallery();
                item.sdcardPath = string;
                if(dataT.size()<10){
                    dataT.add(item);
                }
                else{
                    Toast.makeText(getActivity(), "You cant add more than 10 photos",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            setGridView();
        }

        if (requestCode == CAMERA_VIDEO && resultCode == Activity.RESULT_OK
                && data != null) {

            Uri selectedVideoUri = data.getData();
            String videoPath = getRealPathFromURI(selectedVideoUri);

            CustomGallery item = new CustomGallery();
            item.sdcardPath = videoPath;
            dataTVideo.add(item);
            setGridViewVideo();
        }

        if (requestCode == GALLERY_VIDEO && resultCode == Activity.RESULT_OK) {
            String[] all_path = data.getStringArrayExtra("all_path");

            for (String string : all_path) {
                CustomGallery item = new CustomGallery();
                item.sdcardPath = string;
                if(dataTVideo.size()<10){
                    dataTVideo.add(item);
                }
                else{
                    Toast.makeText(getActivity(), "You cant add more than 10 photos",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            setGridViewVideo();
        }
    }
    Cursor cursor;
    int column_index;
    public String getRealPathFromURI(Uri contentUri) {

        try{
            String[] proj = { MediaStore.Video.Media.DATA };
            cursor = getActivity().getContentResolver().query(
                    contentUri, proj, null, null, null);
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return cursor.getString(column_index);
    }

    private void setGridView(){
        adapter = new GalleryAdapter(getActivity(), imageLoader);
        adapter.addAll(dataT);
        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);
        pos = 0;
    }

    private void setGridViewVideo(){
        adapterVideo = new GalleryAdapterVideo(getActivity(), imageLoader);
        adapterVideo.addAll(dataTVideo);
        adapterVideo.setMultiplePick(false);
        gridGalleryVideo.setAdapter(adapterVideo);
        posVideo = 0;
    }

}