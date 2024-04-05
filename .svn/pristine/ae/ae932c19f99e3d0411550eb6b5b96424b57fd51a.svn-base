package com.essindia.stlapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.essindia.stlapp.Bean.CoilIssueItemDetailBean;
import com.essindia.stlapp.R;

import java.util.ArrayList;

public class CoilReceiveDetailListAdapter extends RecyclerView.Adapter<CoilReceiveDetailListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<CoilIssueItemDetailBean> mBeanList;

    public CoilReceiveDetailListAdapter(Context context, ArrayList<CoilIssueItemDetailBean> beanList) {
        mContext = context;
        mBeanList = beanList;
    }

    @Override
    public CoilReceiveDetailListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_coil_receive_item_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CoilReceiveDetailListAdapter.MyViewHolder holder, int position) {
        final CoilIssueItemDetailBean bean = mBeanList.get(position);
        holder.tvMrirNo.setText(bean.getMrirNo());
        holder.tvMrirDate.setText(bean.getMrirDate());
        holder.tvMrsNo.setText(bean.getMrsNo());
        holder.tvMrsDate.setText(bean.getMrsDate());
        holder.tvCoilNo.setText(bean.getVcSCoilNo());
        holder.tvCoilWeight.setText(bean.getCoilWeight());
        holder.tvHeatNo.setText(bean.getVcSHeatNo());
        holder.tvTagNo.setText(bean.getTagNo());
        holder.tvGrade.setText("Item Code: " + bean.getGrad());
        if (bean.getLoadStatus().equalsIgnoreCase("Y")) {
            holder.cbScan.setChecked(true);
        } else holder.cbScan.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMrirNo, tvMrirDate,tvMrsNo, tvMrsDate, tvCoilNo, tvCoilWeight, tvHeatNo, tvTagNo, tvGrade;
        private CheckBox cbScan;

        MyViewHolder(View view) {
            super(view);
            tvMrirNo = (TextView) view.findViewById(R.id.tv_mrir_no);
            tvMrirDate = (TextView) view.findViewById(R.id.tv_mrir_date);
            tvMrsNo = (TextView) view.findViewById(R.id.tv_mrs_no);
            tvMrsDate = (TextView) view.findViewById(R.id.tv_mrs_date);
            tvCoilNo = (TextView) view.findViewById(R.id.tv_coil_no);
            tvCoilWeight = (TextView) view.findViewById(R.id.tv_coil_weight);
            tvHeatNo = (TextView) view.findViewById(R.id.tv_heat_no);
            tvTagNo = (TextView) view.findViewById(R.id.tv_tag_no);
            tvGrade = (TextView) view.findViewById(R.id.tv_grad);
            cbScan = (CheckBox) view.findViewById(R.id.cb_scan);
        }
    }
}
