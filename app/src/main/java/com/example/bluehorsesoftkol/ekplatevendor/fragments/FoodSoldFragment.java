package com.example.bluehorsesoftkol.ekplatevendor.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.activity.vendor.ActivityAddVendor;
import com.example.bluehorsesoftkol.ekplatevendor.bean.BeanVendorMenuInformationLayout;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorFoodSoldItem;
import com.example.bluehorsesoftkol.ekplatevendor.dbpackage.DbAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

public class FoodSoldFragment extends Fragment {

    private View rootView;
    private EditText  etFoodPrice, etPriceAddMinus, Price_txt;
    private TextView etFoodName, etFoodAddMinus, Food_txt;
    private Button btnMenuMinus, btnMenuPlus, btnAddMenuLayout, btnMinusMenuLayout, btnBack, btnNext;
    private RecyclerView rvAddMinusLayout;
    private String vendor_id = "";
    private int rowNumber = 0, validationFlag = 0, itemPosition;
    private ArrayList<String> Food_array, Price_array;
    private List<BeanVendorMenuInformationLayout> list;
    private AddMinusLayoutAdapter addMinusLayoutAdapter;
    private ActivityAddVendor activityAddVendor;
    private DbAdapter db;
    private Pref _pref;
    private ArrayList<VendorFoodSoldItem> vendorFoodSoldItems;
    private ScrollView slFoodSold;
    public Bundle savedInstanceState;
    private ArrayList<String> foodNameList = new ArrayList<>();
    private ArrayList<String> foodImageList = new ArrayList<>();
    ArrayList<String> foodNameArray = new ArrayList<>();
    ArrayList<String> foodImageArray = new ArrayList<>();
    private SpinnerAdapter foodAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.savedInstanceState = savedInstanceState;
        rootView = inflater.inflate(R.layout.fragment_food_sold, container, false);

        initialize();
        loadFoodItem();

        db.open();
        if (db.getVendorCompleteStep()>=4){
            vendorFoodSoldItems = db.getFoodSoldItemList(Integer.parseInt(_pref.getVendorId()));
            Log.v("vendorFoodSoldItems", String.valueOf(vendorFoodSoldItems.size()));
            setFieldItem();
        }
        db.close();

        onClick();
        return rootView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rvAddMinusLayout.setScrollContainer(false);
    }



    private void loadFoodItem() {

        db.open();
        foodNameList = db.getFoodNames();
        foodImageList = db.getFoodImages();
        Log.v("fooditemSize", String.valueOf(foodNameList.size()));
        db.close();

        for(int i=0; i<foodNameList.size(); i++){

            foodNameArray.add(foodNameList.get(i).toString());
            foodImageArray.add(foodImageList.get(i).toString());
        }

        foodAdapter = new SpinnerAdapter(getActivity(), R.layout.spinner_layout, foodNameArray, foodImageArray);

        etFoodName.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Select Food Menu")
                        .setAdapter(foodAdapter, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int position) {
                                etFoodName.setText(foodNameArray.get(position).toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
    }

    private void initialize() {

        activityAddVendor = (ActivityAddVendor) getActivity();
        db = new DbAdapter(getActivity());
        _pref = new Pref(getActivity());
        etFoodName = (TextView) rootView.findViewById(R.id.etFoodName);
        etFoodPrice = (EditText) rootView.findViewById(R.id.etFoodPrice);
        btnMenuPlus = (Button) rootView.findViewById(R.id.btnMenuPlus);
        btnMenuMinus = (Button) rootView.findViewById(R.id.btnMenuMinus);
        btnBack = (Button) rootView.findViewById(R.id.btnBack);
        btnNext = (Button) rootView.findViewById(R.id.btnNext);
        rvAddMinusLayout = (RecyclerView) rootView.findViewById(R.id.rvAddMinusLayout);
        rvAddMinusLayout.setLayoutManager(new LinearLayoutManager(getActivity()));
        Food_array = new ArrayList<>();
        Price_array = new ArrayList<>();
        list = new ArrayList<>();
        addMinusLayoutAdapter = new AddMinusLayoutAdapter(list, R.layout.menu_information_add_minus);
        rvAddMinusLayout.setAdapter(addMinusLayoutAdapter);
        slFoodSold = (ScrollView)rootView.findViewById(R.id.slFoodSold);
    }


    private void onClick() {
        btnMenuPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rowNumber++;
                BeanVendorMenuInformationLayout items = new BeanVendorMenuInformationLayout();
                list.add(items);

                //addMinusLayoutAdapter.notifyDataSetChanged();
                addMinusLayoutAdapter.notifyItemChanged(rowNumber);
                ViewGroup.LayoutParams params = rvAddMinusLayout.getLayoutParams();
                params.height = params.height + 150;
                Log.e("Value of i", String.valueOf(itemPosition));

            }
        });

        btnMenuMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            rowNumber--;
            if (rowNumber >= 0) {
                addMinusLayoutAdapter.notifyDataSetChanged();
                list.remove(rowNumber);

               // addMinusLayoutAdapter.notifyDataSetChanged();
                addMinusLayoutAdapter.notifyItemChanged(rowNumber);
                ViewGroup.LayoutParams params = rvAddMinusLayout.getLayoutParams();
                params.height = params.height - 150;
                Log.e("Value of i", String.valueOf(itemPosition));
            } else if (rowNumber < 0) {
                rowNumber = 0;
            }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecyclerChildValues();
                if (validationFlag == 1) {
                    db.open();
                    if(db.getVendorCompleteStep()>=4){
                        for (int i=0; i<vendorFoodSoldItems.size(); i++){
                            boolean deleteSuccess = db.deleteFoodSoldItem(vendorFoodSoldItems.get(i).getId());
                            Log.e("update success", String.valueOf(deleteSuccess));
                        }
                        for (int i = 0; i < Food_array.size(); i++) {
                            Long success = db.insertFoodMenu(Food_array.get(i), Price_array.get(i));
                            Log.e("success", String.valueOf(success));
                        }
                    } else {
                        for (int i = 0; i < Food_array.size(); i++) {
                            Long success = db.insertFoodMenu(Food_array.get(i), Price_array.get(i));
                            Log.e("success", String.valueOf(success));
                        }
                    }
                    if(db.getVendorCompleteStep()>=4){
                        Toast.makeText(getActivity(), "Food Sold Info Updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        db.updateVendorCompleteStep(4);
                        Toast.makeText(getActivity(), "Food Sold Info Inserted successfully", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                    activityAddVendor.findViewById(R.id.llGenInfo).setEnabled(true);
                    activityAddVendor.loadGeneralInformationFragment();
                } else {
                    Toast.makeText(getActivity(), "Please Enter All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityAddVendor.loadTimingFragment();
            }
        });
    }

    private void setFieldItem(){
        VendorFoodSoldItem _item = vendorFoodSoldItems.get(0);
        etFoodName.setText(_item.getFoodItem());
        etFoodPrice.setText(_item.getPrice());
        if (vendorFoodSoldItems.size()>1){
            for (int i=1; i<vendorFoodSoldItems.size(); i++){
                BeanVendorMenuInformationLayout _beanVendorMenuInformationLayout = new BeanVendorMenuInformationLayout();
                _beanVendorMenuInformationLayout.setId(vendorFoodSoldItems.get(i).getId());
                _beanVendorMenuInformationLayout.setFood(vendorFoodSoldItems.get(i).getFoodItem());
                _beanVendorMenuInformationLayout.setPrice(vendorFoodSoldItems.get(i).getPrice());
                list.add(_beanVendorMenuInformationLayout);
                rvAddMinusLayout.setAdapter(addMinusLayoutAdapter);
                addMinusLayoutAdapter.notifyDataSetChanged();
                ViewGroup.LayoutParams params = rvAddMinusLayout.getLayoutParams();
                params.height = params.height + 150;
                rowNumber++;
            }

        }
    }

    private void getRecyclerChildValues(){
        Food_array.clear();
        Price_array.clear();

        if (!etFoodName.getText().toString().equals("") && !etFoodPrice.getText().toString().equals("")) {
            Food_array.add(etFoodName.getText().toString());
            Price_array.add(etFoodPrice.getText().toString());
            if (list.size()!=0) {
                for (int i = 0; i < list.size(); i++) {
                    Food_txt = (TextView) rvAddMinusLayout.getChildAt(i).findViewById(R.id.etFoodAddMinus);
                    Price_txt = (EditText) rvAddMinusLayout.getChildAt(i).findViewById(R.id.etPriceAddMinus);
                    if (Food_txt.getText().toString().equals("") || Price_txt.getText().toString().equals("")) {
                        validationFlag = 0;
                        break;
                    } else {
                        validationFlag = 1;
                    }
                    Food_array.add(Food_txt.getText().toString());
                    Price_array.add(Price_txt.getText().toString());
                }
            } else {
                validationFlag = 1;
            }
        } else {
            validationFlag = 0;
        }

        Log.v("Food_array", String.valueOf(Food_array));
    }

public class AddMinusLayoutAdapter extends RecyclerView.Adapter<AddMinusLayoutAdapter.ViewHolder>{

    private List<BeanVendorMenuInformationLayout> beanVendorMenuInformationItem;
    private int itemLayout;
    private View rowView;

    public AddMinusLayoutAdapter(List<BeanVendorMenuInformationLayout> beanVendorMenuInformationItem,int itemLayout){
        this.beanVendorMenuInformationItem = beanVendorMenuInformationItem;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        rowView = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout, viewGroup, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final BeanVendorMenuInformationLayout _item = beanVendorMenuInformationItem.get(i);
        viewHolder.etFoodAddMinus.setTag(String.valueOf(i));
        viewHolder.itemView.setTag(_item);
        if(beanVendorMenuInformationItem.size()>1 && i>beanVendorMenuInformationItem.size()) {
            viewHolder.etFoodAddMinus.setText(Food_array.get(i+1));
            viewHolder.etPriceAddMinus.setText(Price_array.get(i+1));
        }
        db.open();
        if(db.getVendorCompleteStep()>=4){
            viewHolder.etFoodAddMinus.setText(_item.getFood());
            viewHolder.etPriceAddMinus.setText(_item.getPrice());
        }
        db.close();

        btnAddMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemPosition = i;
                btnMenuPlus.performClick();
            }
        });
        btnMinusMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemPosition = i;
                btnMenuMinus.performClick();

            }
        });
    }

    @Override
    public int getItemCount() {
        return beanVendorMenuInformationItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText  etPriceAddMinus;
        TextView etFoodAddMinus;
        public ViewHolder(View itemView) {
            super(itemView);
            btnAddMenuLayout = (Button)itemView.findViewById(R.id.btnAddMenuLayout);
            btnMinusMenuLayout =(Button)itemView.findViewById(R.id.btnMinusMenuLayout);
            this.etFoodAddMinus =(TextView)itemView.findViewById(R.id.etFoodAddMinus);
            this.etPriceAddMinus =(EditText)itemView.findViewById(R.id.etPriceAddMinus);

            etFoodAddMinus.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Select Food Menu")
                            .setAdapter(foodAdapter, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int position) {
                                    etFoodAddMinus.setText(foodNameArray.get(position).toString());
                                    dialog.dismiss();
                                }
                            }).create().show();
                }
            });

            }
        }
    }

    public class SpinnerAdapter extends ArrayAdapter<String> {

        private Context ctx;
        private ArrayList<String> foodNameArray;
        private ArrayList<String> foodImageArray;

        private ImageLoader imageLoader;
        DisplayImageOptions options;

        public SpinnerAdapter(Context context, int resource, ArrayList<String> foodNameArray,
                              ArrayList<String> foodImageArray) {
            super(context,  R.layout.spinner_layout, R.id.spinnerTextView, foodNameArray);
            this.ctx = context;
            this.foodNameArray = foodNameArray;
            this.foodImageArray = foodImageArray;

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
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.spinner_layout, parent, false);

            TextView textView = (TextView) row.findViewById(R.id.spinnerTextView);
            textView.setText(foodNameArray.get(position).toString());

            ImageView imageView = (ImageView)row.findViewById(R.id.spinnerImages);
            imageLoader.displayImage(foodImageArray.get(position).toString(),
                    imageView, options);

            Log.v("food", foodImageArray.get(position).toString());

            return row;
        }
    }
}
