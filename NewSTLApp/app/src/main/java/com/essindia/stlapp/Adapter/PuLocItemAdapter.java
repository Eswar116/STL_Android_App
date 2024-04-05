package com.essindia.stlapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.essindia.stlapp.Bean.PickUpSummaryData;
import com.essindia.stlapp.R;

import java.util.List;

/**
 * Created by Administrator on 06-12-2016.
 */

public class PuLocItemAdapter extends RecyclerView.Adapter<PuLocItemAdapter.MyViewHolder> {

    Context mContext;
    List<PickUpSummaryData> mBeanList;

    public PuLocItemAdapter(Context context, List<PickUpSummaryData> beanList) {
        mContext = context;
        mBeanList = beanList;
    }

    @Override
    public PuLocItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pu_loc_item, parent, false);
        return new PuLocItemAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PickUpSummaryData bean = mBeanList.get(position);
        holder.productCode.setText(bean.getPRODUCTCODE());
        holder.packSizeTv.setText(bean.getPACKSIZE());
        holder.tv_picking_boxes.setText(bean.getPEND_QTY());
        try {
            if (Integer.parseInt(bean.getPICKQTY()) < Integer.parseInt(bean.getPACKSIZE())) {
                holder.tv_no_of_boxes.setText(bean.getPEND_BOXES()+ " / NON-STD");
            } else {
                holder.tv_no_of_boxes.setText(bean.getPEND_BOXES() + " / STD");
            }
        } catch (Exception e) {
            holder.tv_no_of_boxes.setText(bean.getPEND_BOXES() + " / NON-STD");
        }
        holder.itemDescTv.setText(bean.getPRODUCTNAME());
    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView productCode, packSizeTv, tv_picking_boxes, tv_no_of_boxes, itemDescTv;

        public MyViewHolder(View view) {
            super(view);
            productCode = (TextView) view.findViewById(R.id.productCodeTv);
            packSizeTv = (TextView) view.findViewById(R.id.packSizeTv);
            tv_picking_boxes = (TextView) view.findViewById(R.id.tv_picking_qty);
            tv_no_of_boxes = (TextView) view.findViewById(R.id.tv_no_of_boxes);
            itemDescTv = (TextView) view.findViewById(R.id.itemDescTv);
        }
    }
}
