package com.essindia.stlapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.essindia.stlapp.Bean.GRN_Verification_Bean;
import com.essindia.stlapp.R;

import java.util.ArrayList;

public class PutAwayListAdapter extends RecyclerView.Adapter<PutAwayListAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<GRN_Verification_Bean> mBeanList;

    public PutAwayListAdapter(Context context, ArrayList<GRN_Verification_Bean> beanList) {
        mContext = context;
        mBeanList = beanList;
    }

    @Override
    public PutAwayListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_putaway_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PutAwayListAdapter.MyViewHolder holder, int position) {
        final GRN_Verification_Bean grnBean = mBeanList.get(position);
        holder.itemCode.setText(grnBean.getITEM_CODE());
        holder.packSize.setText(grnBean.getPACK_SIZE());
        holder.noOfBox.setText(grnBean.getNO_OF_BOXES());
        holder.putAwayId.setText(grnBean.getPUT_AWAY_ID());
        holder.itemDescTv.setText(grnBean.getITEM_DESC());
    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemCode, packSize, noOfBox, putAwayId, itemDescTv;

        public MyViewHolder(View view) {
            super(view);
            itemCode = (TextView) view.findViewById(R.id.itemCode);
            packSize = (TextView) view.findViewById(R.id.tv_pack_size);
            noOfBox = (TextView) view.findViewById(R.id.tv_no_of_box);
            putAwayId = (TextView) view.findViewById(R.id.tv_v_no);
            itemDescTv = (TextView) view.findViewById(R.id.tv_itemDescTv);
        }
    }
}
