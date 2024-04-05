package com.essindia.stlapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.essindia.stlapp.Bean.DataItem;
import com.essindia.stlapp.R;

import java.util.ArrayList;

public class WhareHouseInwardScanAdapter  extends RecyclerView.Adapter<WhareHouseInwardScanAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<DataItem> productBox;
    public WhareHouseInwardScanAdapter(Context context, ArrayList<DataItem> productBox) {
        this.context = context;
        this.productBox = productBox;
    }
    @Override
    public WhareHouseInwardScanAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_producct_box, parent,false);
        return new WhareHouseInwardScanAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(WhareHouseInwardScanAdapter.MyViewHolder holder, int position) {
        holder.product_code.setText(": "+productBox.get(position).getVC_PRODUCT_CODE());
        holder.total_plant_boxes.setText(": "+productBox.get(position).getVC_PLANT_SCAN_BOX());
        holder.warehouse_scanned_boxes.setText(": "+productBox.get(position).getTOT_INV_SCAN_BOX());
        holder.pending_boxes.setText(": "+(String.valueOf((Integer.parseInt(productBox.get(position).getVC_PLANT_SCAN_BOX())-Integer.parseInt(productBox.get(position).getTOT_INV_SCAN_BOX())))));


        holder.product_code_.setText(productBox.get(position).getVC_PRODUCT_CODE());
        holder.total_boxes.setText( productBox.get(position).getVC_PLANT_SCAN_BOX());
        holder.scanned_boxes.setText(productBox.get(position).getTOT_INV_SCAN_BOX());
//        holder.scanned_boxes.setText(productBox.get(position).getTOT_INV_SCAN_BOX()+"/"+productBox.get(position).getVC_PLANT_SCAN_BOX());


    }

    @Override
    public int getItemCount() {
        return productBox.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView total_plant_boxes, warehouse_scanned_boxes,pending_boxes,product_code;
        private TextView product_code_, scanned_boxes,total_boxes,remarks_product;
        public MyViewHolder(View itemView) {
            super(itemView);
            total_plant_boxes=itemView.findViewById(R.id.total_plant_boxes);
            warehouse_scanned_boxes=itemView.findViewById(R.id.warehouse_scanned_boxes);
            pending_boxes=itemView.findViewById(R.id.pending_boxes);
            product_code=itemView.findViewById(R.id.product_code);

            product_code_=itemView.findViewById(R.id.product_code_);
            scanned_boxes=itemView.findViewById(R.id.scanned_boxes);
            total_boxes=itemView.findViewById(R.id.total_boxes);
            remarks_product=itemView.findViewById(R.id.remarks_product);
        }
    }
}
