package com.essindia.stlapp.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.essindia.stlapp.Bean.LoadingAdviceMRS;
import com.essindia.stlapp.R;

import java.util.ArrayList;

public class LoadingAdviceMRSListAdapter extends RecyclerView.Adapter<LoadingAdviceMRSListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<LoadingAdviceMRS> mBeanList;
    private iTranNoListClick mITranNoList;

    public interface iTranNoListClick {
        void onTranNoListClick(int position);
    }

    public LoadingAdviceMRSListAdapter(Context context, ArrayList<LoadingAdviceMRS> beanList, iTranNoListClick pITranNoList) {
        mContext = context;
        mBeanList = beanList;
        mITranNoList = pITranNoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_loading_advice_mrs, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final LoadingAdviceMRS bean = mBeanList.get(position);
        holder.tvTranNo.setText("Transaction No: " + bean.getTRAN_NO());
        holder.tvTranDate.setText("Transaction Date: " + bean.getTRAN_DATE());
        holder.tv_grad.setText("Item Code: " + bean.getGRAD());
        holder.tv_location_name.setText("Requirement Area : " + bean.getLOCATION_NAME());
        holder.cvTranDetailContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITranNoList.onTranNoListClick(position);
            }
        });
        holder.btn_view_dtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITranNoList.onTranNoListClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTranNo, tvTranDate;
        public TextView tv_grad, tv_location_name;
        public CardView cvTranDetailContainer;
        public Button btn_view_dtl;

        public MyViewHolder(View view) {
            super(view);
            tvTranNo = (TextView) view.findViewById(R.id.tv_tran_no);
            tvTranDate = (TextView) view.findViewById(R.id.tv_tran_date);
            tv_grad = (TextView) view.findViewById(R.id.tv_grad);
            tv_location_name = (TextView) view.findViewById(R.id.tv_location_name);
            cvTranDetailContainer = (CardView) view.findViewById(R.id.cv);
            btn_view_dtl = (Button) view.findViewById(R.id.btn_view_dtl);
        }
    }
}