package com.essindia.stlapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.essindia.stlapp.Bean.IssueItemDetailBean;
import com.essindia.stlapp.R;

import java.util.ArrayList;

public class IssueItemListAdapter extends RecyclerView.Adapter<IssueItemListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<IssueItemDetailBean> mBeanList;

    public IssueItemListAdapter(Context context, ArrayList<IssueItemDetailBean> beanList) {
        mContext = context;
        mBeanList = beanList;
    }

    @Override
    public IssueItemListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_issue_item_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IssueItemListAdapter.MyViewHolder holder, int position) {
        final IssueItemDetailBean bean = mBeanList.get(position);
        holder.tvItemCode.setText(bean.getVcItemCode());
        holder.tvLocationName.setText(bean.getVcLocationName());
        holder.tvLocationId.setText(bean.getVcLocationCode());
        holder.tvMrirNo.setText(bean.getVcMrirNo());
        holder.tvMrirDate.setText(bean.getDtMrirDate());
        holder.tvRouteNo.setText(bean.getVcRouteNo());
        holder.tvRouteDate.setText(bean.getDtRouteDate());
        holder.tvMachineName.setText(bean.getVcMachineName());
        holder.tvMachineId.setText(bean.getVcMachineNo());
        holder.tvCoilNo.setText(bean.getVcCoilNo());
        holder.tvCoilWeight.setText(bean.getNuQtyIssued());
        holder.tvHeatNo.setText(bean.getVcHeatNo());
        holder.tvGrade.setText(bean.getVcGradeNo());
        holder.tvWireDia.setText(bean.getVcWireDia());
        holder.tvDiv.setText(bean.getVcDiv());
        if (bean.isItemPlaced()) holder.cbScan.setChecked(true);
        else holder.cbScan.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemCode;
        private TextView tvLocationName;
        private TextView tvLocationId;
        private TextView tvMrirNo;
        private TextView tvMrirDate;
        private TextView tvRouteNo;
        private TextView tvRouteDate;
        private TextView tvMachineName;
        private TextView tvMachineId;
        private TextView tvCoilNo;
        private TextView tvCoilWeight;
        private TextView tvHeatNo;
        private TextView tvGrade;
        private TextView tvWireDia;
        private TextView tvDiv;
        private CheckBox cbScan;

        MyViewHolder(View view) {
            super(view);
            tvItemCode = (TextView) view.findViewById(R.id.tv_item_code);
            tvLocationName = (TextView) view.findViewById(R.id.tv_location_name);
            tvLocationId = (TextView) view.findViewById(R.id.tv_location_id);
            tvMrirNo = (TextView) view.findViewById(R.id.tv_mrir_no);
            tvMrirDate = (TextView) view.findViewById(R.id.tv_mrir_date);
            tvRouteNo = (TextView) view.findViewById(R.id.tv_route_no);
            tvRouteDate = (TextView) view.findViewById(R.id.tv_route_date);
            tvMachineName = (TextView) view.findViewById(R.id.tv_machine_name);
            tvMachineId = (TextView) view.findViewById(R.id.tv_machine_id);
            tvCoilNo = (TextView) view.findViewById(R.id.tv_coil_no);
            tvCoilWeight = (TextView) view.findViewById(R.id.tv_coil_weight);
            tvHeatNo = (TextView) view.findViewById(R.id.tv_heat_no);
            tvGrade = (TextView) view.findViewById(R.id.tv_grade);
            tvWireDia = (TextView) view.findViewById(R.id.tv_wire_dia);
            tvDiv = (TextView) view.findViewById(R.id.tv_div);
            cbScan = (CheckBox) view.findViewById(R.id.cb_scan);
        }
    }
}
