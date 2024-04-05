package com.essindia.stlapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.essindia.stlapp.Bean.ReceiptTranItemDetailBean;
import com.essindia.stlapp.R;

import java.util.ArrayList;

public class ReceiptTranDetailListAdapter extends RecyclerView.Adapter<ReceiptTranDetailListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ReceiptTranItemDetailBean> mBeanList;

    public ReceiptTranDetailListAdapter(Context context, ArrayList<ReceiptTranItemDetailBean> beanList) {
        mContext = context;
        mBeanList = beanList;
    }

    @Override
    public ReceiptTranDetailListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_receipt_tran_detail, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReceiptTranDetailListAdapter.MyViewHolder holder, int position) {
        final ReceiptTranItemDetailBean bean = mBeanList.get(position);
        holder.tvGradeNo.setText(bean.getVcSGradeNo());
        holder.tvMrirNo.setText(bean.getVcSMrirNo());
        holder.tvCoilNo.setText(bean.getVcSCoilNo());
        holder.tvCoilWeight.setText(bean.getNuSDivQty());
        if (bean.isItemScanned()) holder.cbScan.setChecked(true);
        else holder.cbScan.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvGradeNo, tvMrirNo, tvCoilNo, tvCoilWeight;
        private CheckBox cbScan;

        MyViewHolder(View view) {
            super(view);
            tvGradeNo = (TextView) view.findViewById(R.id.tv_grade_no);
            tvMrirNo = (TextView) view.findViewById(R.id.tv_mrir_no);
            tvCoilNo = (TextView) view.findViewById(R.id.tv_coil_no);
            tvCoilWeight = (TextView) view.findViewById(R.id.tv_coil_weight);
            cbScan = (CheckBox) view.findViewById(R.id.cb_scan);
        }
    }
}
