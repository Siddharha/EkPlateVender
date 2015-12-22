package com.example.bluehorsesoftkol.ekplatevendor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bluehorsesoftkol.ekplatevendor.R;

import java.util.ArrayList;

import com.example.bluehorsesoftkol.ekplatevendor.listitemclasses.VendorListItem;

/**
 * Created by Bluehorse Soft Kol on 9/17/2015.
 */
public class VenderListAdapter extends RecyclerView.Adapter<VenderListAdapter.VendorItemHolder> {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<VendorListItem> vendorListItems;


    public VenderListAdapter(Context context, ArrayList<VendorListItem> vendorListItems) {
        this.context = context;
        this.vendorListItems = vendorListItems;
    }

    @Override
    public VendorItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rowView = LayoutInflater.from(context)
                .inflate(R.layout.vendor_row_item_layouy, parent, false);
        VendorItemHolder itemHolder = new VendorItemHolder(rowView);

        return itemHolder;
    }

    @Override
    public void onBindViewHolder(VendorItemHolder holder, int position) {

        //holder.imvProfile.setImageResource(vendorListItems.get(position).getImageId());
    }

    @Override
    public int getItemCount() {
        return vendorListItems.size();
    }

    public class VendorItemHolder extends RecyclerView.ViewHolder{


        public VendorItemHolder(View itemView) {
            super(itemView);
        }
    }

}


