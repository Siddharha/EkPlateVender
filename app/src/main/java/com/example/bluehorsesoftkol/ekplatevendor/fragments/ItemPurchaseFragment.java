package com.example.bluehorsesoftkol.ekplatevendor.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.activity.vendor.ActivityAddVendor;
import com.example.bluehorsesoftkol.ekplatevendor.bean.BeanItemPurchaseLayout;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorPurchaseItem;
import com.example.bluehorsesoftkol.ekplatevendor.dbpackage.DbAdapter;
import java.util.ArrayList;
import java.util.List;

public class ItemPurchaseFragment extends Fragment {

    private View rootView;
    private int itemPosition, index,index2,infIndex,infIndex2;
    private EditText etFragItemPurchased, etFragItemDays, etFragItemQty, etFragItemUnit, etFragItemCost,
            etFragItemCostPerUnit, etFragItemPerUnit;
    private Button btnFragItemPlus, btnFragItemMinus, btnItemPlus, btnItemMinus, btnBack, btnNext;
    private RecyclerView rvAddMinusLayout;
    private Spinner sqUnt,sqQntPer ;
    private String[] qntArray = {"UNIT","Kg","Ltrs","Pcs"};
    String vendor_id = "";
    int rowNumber = 0, validationFlag = 0;

    private ArrayList<String> Purchased_Array, Days_Array, Qty_Array, Unit_Array, Cost_Array, Cost_Per_Unit_Array, Per_Unit_Array;
    private List<BeanItemPurchaseLayout> list;
    private ItemPlusMinusAdapter itemPlusMinusAdapter;
    private ActivityAddVendor activityAddVendor;
    private DbAdapter db;
    private Pref _pref;
    private ArrayList<VendorPurchaseItem> vendorPurchaseItems;
    private ArrayAdapter<String> qntAdp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        rootView = inflater.inflate(R.layout.fragment_item_purchase,container,false);
        initialize();

        db.open();
        if (db.getVendorCompleteStep()>=6) {
            vendorPurchaseItems = db.getFoodPurchaseItemList(Integer.parseInt(_pref.getVendorId()));
            setFieldsItem();
        }
        db.close();

        onClick();
        loadQntData();

        return rootView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rvAddMinusLayout.setScrollContainer(false);
    }

    private void loadQntData() {

        sqUnt.setAdapter(qntAdp);
        sqQntPer.setAdapter(qntAdp);
        sqUnt.setSelection(index, true);
        sqQntPer.setSelection(index2, true);
    }

    private void initialize() {

        activityAddVendor = (ActivityAddVendor) getActivity();
        db = new DbAdapter(getActivity());
        _pref = new Pref(getActivity());
        qntAdp = new ArrayAdapter<>(getActivity(),R.layout.spinner_item,qntArray);
        etFragItemPurchased = (EditText) rootView.findViewById(R.id.etFragItemPurchased);
        etFragItemDays = (EditText) rootView.findViewById(R.id.etFragItemDays);
        etFragItemQty = (EditText) rootView.findViewById(R.id.etFragItemQty);
        etFragItemUnit = (EditText) rootView.findViewById(R.id.etFragItemUnit);
        etFragItemCost = (EditText) rootView.findViewById(R.id.etFragItemCost);
        etFragItemCostPerUnit = (EditText) rootView.findViewById(R.id.etFragItemCostPerUnit);
        etFragItemPerUnit = (EditText) rootView.findViewById(R.id.etFragItemPerUnit);
        btnFragItemPlus=(Button) rootView.findViewById(R.id.btnFragItemPlus);
        btnFragItemMinus=(Button) rootView.findViewById(R.id.btnFragItemMinus);
        btnBack = (Button) rootView.findViewById(R.id.btnBack);
        btnNext = (Button) rootView.findViewById(R.id.btnNext);
        rvAddMinusLayout=(RecyclerView) rootView.findViewById(R.id.rvAddMinusLayout);
        rvAddMinusLayout.setLayoutManager(new LinearLayoutManager(getActivity()));
        sqUnt = (Spinner)rootView.findViewById(R.id.sqQnt);
        sqQntPer = (Spinner)rootView.findViewById(R.id.sqQntPer);
        Purchased_Array = new ArrayList<>();
        Days_Array = new ArrayList<>();
        Qty_Array = new ArrayList<>();
        Unit_Array = new ArrayList<>();
        Cost_Array = new ArrayList<>();
        Cost_Per_Unit_Array = new ArrayList<>();
        Per_Unit_Array = new ArrayList<>();
        list = new ArrayList<>();
        itemPlusMinusAdapter = new ItemPlusMinusAdapter(list,R.layout.item_purchase_add_minus);
        rvAddMinusLayout.setAdapter(itemPlusMinusAdapter);
    }

    private void setFieldsItem(){
        if(vendorPurchaseItems.size()>0){
            VendorPurchaseItem _item =  vendorPurchaseItems.get(0);
            etFragItemPurchased.setText(_item.getItemName());
            etFragItemDays.setText(_item.getDaysNumber());
            etFragItemQty.setText(_item.getQuantity());

            index = -1;
            for (int i= 0; i < qntArray.length; i++) {
                if (qntArray[i].equals(_item.getUnit())) {
                    index = i;
                    break;
                }
            }
            index2 = -1;
            for (int i= 0; i < qntArray.length; i++) {
                if (qntArray[i].equals(_item.getPerUnit())) {
                    index2 = i;
                    break;
                }
            }
            etFragItemCost.setText(_item.getTotalCost());
            etFragItemCostPerUnit.setText(_item.getCostPerUnit());
        }

        if(vendorPurchaseItems.size()>1){
            for (int i=1; i<vendorPurchaseItems.size(); i++){
                BeanItemPurchaseLayout items = new BeanItemPurchaseLayout();
                items.setId(vendorPurchaseItems.get(i).getId());
                items.setVendorId(vendorPurchaseItems.get(i).getVendorId());
                items.setItemName(vendorPurchaseItems.get(i).getItemName());
                items.setDaysNumber(vendorPurchaseItems.get(i).getDaysNumber());
                items.setUnit(vendorPurchaseItems.get(i).getUnit());
                items.setQuantity(vendorPurchaseItems.get(i).getQuantity());
                items.setPerUnit(vendorPurchaseItems.get(i).getPerUnit());
                items.setCostPerUnit(vendorPurchaseItems.get(i).getCostPerUnit());
                items.setTotalCost(vendorPurchaseItems.get(i).getTotalCost());
                list.add(items);
                ViewGroup.LayoutParams params = rvAddMinusLayout.getLayoutParams();
                params.height = params.height + 200;
                rowNumber++;
            }
        }
    }

    private void onClick() {

        sqUnt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BeanItemPurchaseLayout item = new BeanItemPurchaseLayout();
                item.setUnit(sqUnt.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sqQntPer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BeanItemPurchaseLayout item = new BeanItemPurchaseLayout();
                item.setPerUnit(sqQntPer.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnFragItemPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowNumber++;
                BeanItemPurchaseLayout items = new BeanItemPurchaseLayout();
                list.add(items);
                itemPlusMinusAdapter.notifyItemChanged(rowNumber);
                ViewGroup.LayoutParams params = rvAddMinusLayout.getLayoutParams();
                params.height = params.height + rootView.getHeight();
            }
        });

        btnFragItemMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowNumber--;
                if (rowNumber >= 0) {
                    BeanItemPurchaseLayout items = new BeanItemPurchaseLayout();
                    list.remove(rowNumber);
                    itemPlusMinusAdapter.notifyItemChanged(rowNumber);
                    ViewGroup.LayoutParams params = rvAddMinusLayout.getLayoutParams();
                    params.height = params.height - rootView.getHeight();
                } else if (rowNumber < 0) {
                    rowNumber = 0;
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendor_id = _pref.getVendorId();
                getRecyclerChildValues();
                db.open();
                if (validationFlag == 1) {
                    if (db.getVendorCompleteStep()>=6) {
                        for (int i = 0; i < vendorPurchaseItems.size(); i++) {
                            boolean deleteSuccess = db.deletePurchaseItem(vendorPurchaseItems.get(i).getId());
                        }
                        for (int i = 0; i < Purchased_Array.size(); i++) {
                            Long success = db.insertPurchaseItems(Purchased_Array.get(i), Days_Array.get(i), Qty_Array.get(i),
                                    Unit_Array.get(i), Cost_Array.get(i), Per_Unit_Array.get(i), Cost_Per_Unit_Array.get(i));
                        }
                    } else {
                        for (int i = 0; i < Purchased_Array.size(); i++) {
                            Long success = db.insertPurchaseItems(Purchased_Array.get(i), Days_Array.get(i), Qty_Array.get(i),
                                    Unit_Array.get(i), Cost_Array.get(i), Per_Unit_Array.get(i), Cost_Per_Unit_Array.get(i));
                        }
                    }
                    if (db.getVendorCompleteStep()>=6) {
                        Toast.makeText(getActivity(), "Item Purchase Info Updated successfully", Toast.LENGTH_LONG).show();
                    } else {
                        db.updateVendorCompleteStep(6);
                        Toast.makeText(getActivity(), "Item Purchase Info Inserted successfully", Toast.LENGTH_LONG).show();
                    }
                    activityAddVendor.findViewById(R.id.llPicVideo).setEnabled(true);
                    activityAddVendor.loadPicVideoFragment();
                } else {
                    db.updateVendorCompleteStep(6);
                    activityAddVendor.findViewById(R.id.llPicVideo).setEnabled(true);
                    activityAddVendor.loadPicVideoFragment();
                }
                db.close();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityAddVendor.loadGeneralInformationFragment();
            }
        });
    }

    private void getRecyclerChildValues(){
        Purchased_Array.clear();
        Days_Array.clear();
        Qty_Array.clear();
        Unit_Array.clear();
        Cost_Array.clear();
        Cost_Per_Unit_Array.clear();
        Per_Unit_Array.clear();
        if (!etFragItemPurchased.getText().toString().equals("") && !etFragItemDays.getText().toString().equals("")
                && !etFragItemQty.getText().toString().equals("") && !sqUnt.getSelectedItem().toString().equals("UNIT")
                && !etFragItemCost.getText().toString().equals("") && !etFragItemCostPerUnit.getText().toString().equals("")
                && !sqQntPer.getSelectedItem().toString().equals("UNIT")) {
            Purchased_Array.add(etFragItemPurchased.getText().toString());
            Days_Array.add(etFragItemDays.getText().toString());
            Qty_Array.add(etFragItemQty.getText().toString());
            Unit_Array.add(sqUnt.getSelectedItem().toString());
            Cost_Array.add(etFragItemCost.getText().toString());
            Cost_Per_Unit_Array.add(etFragItemCostPerUnit.getText().toString());
            Per_Unit_Array.add(sqQntPer.getSelectedItem().toString());
            if (list.size()!=0) {
                for (int i = 0; i < list.size(); i++) {
                    EditText Purchased_txt = (EditText) rvAddMinusLayout.getChildAt(i).findViewById(R.id.etItemPurchased);
                    EditText Days_txt = (EditText) rvAddMinusLayout.getChildAt(i).findViewById(R.id.etItemDays);
                    EditText Qty_txt = (EditText) rvAddMinusLayout.getChildAt(i).findViewById(R.id.etItemQty);
                    Spinner Unit_txt = (Spinner) rvAddMinusLayout.getChildAt(i).findViewById(R.id.sqUntInfl);
                    EditText Cost_txt = (EditText) rvAddMinusLayout.getChildAt(i).findViewById(R.id.etItemCost);
                    EditText Cost_Per_Unit_txt = (EditText) rvAddMinusLayout.getChildAt(i).findViewById(R.id.etItemCostPerUnit);
                    Spinner Per_Unit_txt = (Spinner) rvAddMinusLayout.getChildAt(i).findViewById(R.id.sqUntInflPer);
                    if (Purchased_txt.getText().toString().equals("") || Days_txt.getText().toString().equals("")
                            || Qty_txt.getText().toString().equals("") || Unit_txt.getSelectedItem().toString().equals("UNIT")
                            || Cost_txt.getText().toString().equals("") || Cost_Per_Unit_txt.getText().toString().equals("")
                            || Per_Unit_txt.getSelectedItem().toString().equals("UNIT")) {
                        validationFlag = 0;
                        break;
                    } else {
                        validationFlag = 1;
                    }
                    Purchased_Array.add(Purchased_txt.getText().toString());
                    Days_Array.add(Days_txt.getText().toString());
                    Qty_Array.add(Qty_txt.getText().toString());
                    Unit_Array.add(Unit_txt.getSelectedItem().toString());
                    Cost_Array.add(Cost_txt.getText().toString());
                    Cost_Per_Unit_Array.add(Cost_Per_Unit_txt.getText().toString());
                    Per_Unit_Array.add(Per_Unit_txt.getSelectedItem().toString());
                    Log.e("Values",Unit_Array.toString());
                }
            } else {
                validationFlag = 1;
            }
        } else {
            validationFlag = 0;
        }
    }

    public class ItemPlusMinusAdapter extends RecyclerView.Adapter<ItemPlusMinusAdapter.ViewHolder>{

        private List<BeanItemPurchaseLayout> items;
        private int itemLayout;
        private View v;

    public ItemPlusMinusAdapter(List<BeanItemPurchaseLayout> items,int itemLayout){

        this.items = items;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        final BeanItemPurchaseLayout _item =items.get(i);
        viewHolder.itemView.setTag(_item);

        db.open();
        if(db.getVendorCompleteStep()>=6){
            viewHolder.etItemPurchased.setText(_item.getItemName());
            viewHolder.etItemDays.setText(_item.getDaysNumber());
            viewHolder.etItemQty.setText(_item.getQuantity());

            infIndex = -1;
            for (int z= 0; z < qntArray.length; z++) {
                if (qntArray[z].equals(_item.getUnit())) {
                    infIndex = z;
                    break;
                }
            }

            infIndex2 = -1;
            for (int z= 0; z < qntArray.length; z++) {
                if (qntArray[z].equals(_item.getPerUnit())) {
                    infIndex2 = z;
                    break;
                }
            }

            viewHolder.sqUntInfl.setSelection(infIndex);
            viewHolder.etItemCost.setText(_item.getTotalCost());
            viewHolder.etItemCostPerUnit.setText(_item.getCostPerUnit());
            viewHolder.sqUntInflPer.setSelection(infIndex2);
        }
        db.close();

        btnItemPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemPosition = i;
                btnFragItemPlus.performClick();
            }
        });

        btnItemMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemPosition = i;
                btnFragItemMinus.performClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText etItemPurchased;
        EditText etItemDays;
        EditText etItemQty;
        EditText etItemCost;
        EditText etItemCostPerUnit;
        Spinner sqUntInfl;
        Spinner sqUntInflPer;

        public ViewHolder(View itemView) {
            super(itemView);
            etItemPurchased = (EditText)itemView.findViewById(R.id.etItemPurchased);
            etItemDays = (EditText)itemView.findViewById(R.id.etItemDays);
            etItemQty = (EditText)itemView.findViewById(R.id.etItemQty);
            etItemCost = (EditText)itemView.findViewById(R.id.etItemCost);
            etItemCostPerUnit = (EditText)itemView.findViewById(R.id.etItemCostPerUnit);
            btnItemPlus=(Button)itemView.findViewById(R.id.btnItemPlus);
            btnItemMinus=(Button)itemView.findViewById(R.id.btnItemMinus);

            sqUntInfl = (Spinner)itemView.findViewById(R.id.sqUntInfl);
            sqUntInflPer = (Spinner)itemView.findViewById(R.id.sqUntInflPer);

            sqUntInfl.setAdapter(qntAdp);
            sqUntInflPer.setAdapter(qntAdp);

            sqUntInfl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    BeanItemPurchaseLayout _item = new BeanItemPurchaseLayout();
                    _item.setUnit(sqUntInfl.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            sqUntInflPer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    BeanItemPurchaseLayout _item = new BeanItemPurchaseLayout();
                    _item.setPerUnit(sqUntInflPer.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            }
        }
    }
}
