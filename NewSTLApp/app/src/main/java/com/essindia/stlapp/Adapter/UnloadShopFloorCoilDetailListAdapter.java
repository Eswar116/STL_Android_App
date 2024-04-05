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

public class UnloadShopFloorCoilDetailListAdapter extends RecyclerView.Adapter<UnloadShopFloorCoilDetailListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<CoilIssueItemDetailBean> mBeanList;

    public UnloadShopFloorCoilDetailListAdapter(Context context, ArrayList<CoilIssueItemDetailBean> beanList) {
        mContext = context;
        mBeanList = beanList;
    }

    @Override
    public UnloadShopFloorCoilDetailListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_unload_shop_floor_item_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UnloadShopFloorCoilDetailListAdapter.MyViewHolder holder, int position) {
        final CoilIssueItemDetailBean bean = mBeanList.get(position);
        holder.tvMrirNo.setText(bean.getMrirNo());
        holder.tvMrirDate.setText(bean.getMrirDate());
        holder.tvCoilNo.setText(bean.getVcSCoilNo());
        holder.tvCoilWeight.setText(bean.getCoilWeight());
        holder.tvHeatNo.setText(bean.getVcSHeatNo());
        holder.tvTagNo.setText(bean.getTagNo());
        holder.tvGrade.setText(mContext.getString(R.string.set_1s_2s,"Item Code: ", bean.getGrad()));
        holder.tv_route_card_no.setText(mContext.getString(R.string.set_1s_2s,"Route Card No.: ", bean.getRouteCardNo()));
        holder.tv_route_card_date.setText(mContext.getString(R.string.set_1s_2s,"Route Card Date :", bean.getRouteCardDate()));
        holder.tv_division.setText(mContext.getString(R.string.set_1s_2s,"Division :", bean.getVcDivision()));
        if (bean.getLoadStatus().equalsIgnoreCase("Y")) {
            holder.cbScan.setChecked(true);
        } else holder.cbScan.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMrirNo, tvMrirDate, tvCoilNo, tvCoilWeight, tvHeatNo, tvTagNo, tvGrade, tv_route_card_no, tv_route_card_date, tv_division;

        private CheckBox cbScan;

        MyViewHolder(View view) {
            super(view);
            tvMrirNo = view.findViewById(R.id.tv_mrir_no);
            tvMrirDate = view.findViewById(R.id.tv_mrir_date);
            tvCoilNo = view.findViewById(R.id.tv_coil_no);
            tvCoilWeight = view.findViewById(R.id.tv_coil_weight);
            tvHeatNo = view.findViewById(R.id.tv_heat_no);
            tvTagNo = view.findViewById(R.id.tv_tag_no);
            tvGrade = view.findViewById(R.id.tv_grad);
            tv_route_card_no = view.findViewById(R.id.tv_route_card_no);
            tv_route_card_date = view.findViewById(R.id.tv_route_card_date);
            tv_division = view.findViewById(R.id.tv_division);
            cbScan = view.findViewById(R.id.cb_scan);
        }
    }
}
