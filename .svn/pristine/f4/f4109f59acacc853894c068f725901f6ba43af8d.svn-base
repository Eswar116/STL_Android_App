package com.essindia.stlapp.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.essindia.stlapp.Bean.RouteCardProcessDataBean;
import com.essindia.stlapp.R;

import java.util.ArrayList;

public class RouteCardProcessListAdapter extends RecyclerView.Adapter<RouteCardProcessListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<RouteCardProcessDataBean> mBeanList;
    private iTranNoListClick mITranNoList;

    public interface iTranNoListClick {
        void onTranNoListClick(int position);
    }

    public RouteCardProcessListAdapter(Context context, ArrayList<RouteCardProcessDataBean> beanList,iTranNoListClick pITranNoList) {
        mContext = context;
        mBeanList = beanList;
        mITranNoList = pITranNoList;
    }

    @Override
    public RouteCardProcessListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_route_card_process_item_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RouteCardProcessListAdapter.MyViewHolder holder,final int position) {
        final RouteCardProcessDataBean bean = mBeanList.get(position);

        holder.tv_route_no.setText(bean.getRouteNo());
        holder.tv_route_date.setText(bean.getRouteDate());
        holder.tv_vc_product_code.setText(bean.getProductCode());
        holder.tv_vc_process_desc.setText(bean.getVcProcessDesc());
        holder.tv_nu_ok_qty.setText("Nu Ok Quantity : " + bean.getNuOkQuantity());
        holder.tv_virtual_bin_no.setText("VC Virtual bin no.:" + bean.getVcVirtualBinNo());
        holder.tv_actual_bin_no.setText("VC Actual bin no.:" + bean.getVcActualBinNo());
        holder.cvTranDetailContainer.setOnClickListener(new View.OnClickListener() {
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

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_route_no, tv_route_date, tv_vc_product_code, tv_vc_process_desc, tv_nu_ok_qty, tv_actual_bin_no,tv_virtual_bin_no;
        public CardView cvTranDetailContainer;

        MyViewHolder(View view) {
            super(view);
            tv_route_no = (TextView) view.findViewById(R.id.tv_route_no);
            tv_route_date = (TextView) view.findViewById(R.id.tv_route_date);
            tv_vc_product_code = (TextView) view.findViewById(R.id.tv_vc_product_code);
            tv_vc_process_desc = (TextView) view.findViewById(R.id.tv_vc_process_desc);
            tv_nu_ok_qty = (TextView) view.findViewById(R.id.tv_nu_ok_qty);
            tv_virtual_bin_no = (TextView) view.findViewById(R.id.tv_virtual_bin_no);
            tv_actual_bin_no = (TextView) view.findViewById(R.id.tv_actual_bin_no);
            cvTranDetailContainer = (CardView) view.findViewById(R.id.cv);
        }
    }
}
