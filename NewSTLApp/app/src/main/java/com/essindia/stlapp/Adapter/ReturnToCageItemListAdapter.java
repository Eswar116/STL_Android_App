package com.essindia.stlapp.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.essindia.stlapp.Bean.ReceiptItemBean;
import com.essindia.stlapp.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 25-10-2016.
 */

public class ReturnToCageItemListAdapter extends RecyclerView.Adapter<ReturnToCageItemListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ReceiptItemBean> mBeanList;
    private iReturnToCageListClick mITranNoList;

    public interface iReturnToCageListClick {
        void onReturnToCageListItemClick(int position);
    }

    public ReturnToCageItemListAdapter(Context context, ArrayList<ReceiptItemBean> beanList, iReturnToCageListClick pITranNoList) {
        mContext = context;
        mBeanList = beanList;
        mITranNoList = pITranNoList;
    }

    @Override
    public ReturnToCageItemListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_receipt_tran_no, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReturnToCageItemListAdapter.MyViewHolder holder, final int position) {
        final ReceiptItemBean bean = mBeanList.get(position);
        holder.tvTranNo.setText("Transaction No: " + bean.getVcTranNo());
        holder.tvTranDate.setText("Transaction Date: " + bean.getTranDate());
        holder.cvTranDetailContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITranNoList.onReturnToCageListItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTranNo, tvTranDate;
        public CardView cvTranDetailContainer;

        public MyViewHolder(View view) {
            super(view);
            tvTranNo = (TextView) view.findViewById(R.id.tv_tran_no);
            tvTranDate = (TextView) view.findViewById(R.id.tv_tran_date);
            cvTranDetailContainer = (CardView) view.findViewById(R.id.cv);
        }
    }
}
