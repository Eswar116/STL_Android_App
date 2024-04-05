package com.essindia.stlapp.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.essindia.stlapp.Bean.GRNBean;
import com.essindia.stlapp.Bean.GRN_Verification_Bean;
import com.essindia.stlapp.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 25-10-2016.
 */

public class GRNVerificationAdapter extends RecyclerView.Adapter<GRNVerificationAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<GRN_Verification_Bean> mBeanList;

    public GRNVerificationAdapter(Context context, ArrayList<GRN_Verification_Bean> beanList) {
        mContext = context;
        mBeanList = beanList;
    }

    @Override
    public GRNVerificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grnverification_cardview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GRNVerificationAdapter.MyViewHolder holder, int position) {
        final GRN_Verification_Bean grnBean = mBeanList.get(position);
        holder.grnNo.setText(grnBean.getROUTE_NO());
        holder.irNo.setText(grnBean.getIR_NO());
        holder.itemCode.setText(grnBean.getITEM_CODE());
        holder.grnDateTv.setText(grnBean.getROUTE_DATE());
        holder.itemDescTv.setText(grnBean.getITEM_DESC());
        holder.machineNo.setText(grnBean.getVC_MACHINE_NO());
        holder.tvNoOfBox.setText(grnBean.getNO_OF_BOXES());
    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView grnNo, irNo, itemCode, grnDateTv, itemDescTv,machineNo, tvNoOfBox;

        public MyViewHolder(View view) {
            super(view);
            grnNo = (TextView) view.findViewById(R.id.grnNo);
            irNo = (TextView) view.findViewById(R.id.irNo);
            itemCode = (TextView) view.findViewById(R.id.itemCode);
            grnDateTv = (TextView) view.findViewById(R.id.grnDateTv);
            itemDescTv = (TextView) view.findViewById(R.id.tv_itemDescTv);
            machineNo= (TextView) view.findViewById(R.id.machineNo);
            tvNoOfBox= (TextView) view.findViewById(R.id.tv_no_of_box);
        }
    }
}
