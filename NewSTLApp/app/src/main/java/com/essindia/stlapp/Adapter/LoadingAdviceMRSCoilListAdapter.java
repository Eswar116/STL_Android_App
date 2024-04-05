package com.essindia.stlapp.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.essindia.stlapp.Bean.LoadingAdviceMRSCoil;
import com.essindia.stlapp.R;

import java.util.ArrayList;

public class LoadingAdviceMRSCoilListAdapter extends RecyclerView.Adapter<LoadingAdviceMRSCoilListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<LoadingAdviceMRSCoil> mBeanList;
    private iTranNoListClick mITranNoList;

    public interface iTranNoListClick {
        void onTranNoListClick(int position);
    }

    public LoadingAdviceMRSCoilListAdapter(Context context, ArrayList<LoadingAdviceMRSCoil> beanList, iTranNoListClick pITranNoList) {
        mContext = context;
        mBeanList = beanList;
        mITranNoList = pITranNoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_loading_advice_mrs_coil, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final LoadingAdviceMRSCoil bean = mBeanList.get(position);
        holder.tv_grad.setText("Item Code: " + bean.getGRAD());
        holder.tv_vc_tag_no.setText("Tag No: " + bean.getVc_TAG_NO());
        holder.tv_vc_division.setText("Division: " + bean.getVC_DIVISION());
        holder.tv_vc_route_no.setText("Route No: " + bean.getVC_ROUTE_NO());
        holder.tv_mrir_date.setText("Mrir Date: " + bean.getMRIR_DATE());
        holder.tv_mrir_no.setText("Mrir No: " + bean.getMRIR_NO());
        holder.tv_coil_weight.setText("Coil weight: " + bean.getCOIL_WEIGHT());
        holder.tv_req_quantity.setText("Required Qty: " + bean.getREQ_QUANTITY());
        holder.tv_dt_route_date.setText("Route Date: " + bean.getDT_ROUTE_DATE());
        holder.tv_vc_s_heat_no.setText("Heat No: " + bean.getVC_S_HEAT_NO());
        holder.tv_location_name.setText("Requirement Area : " + bean.getLOCATION_NAME());
        if (bean.getLOAD_STATUS() != null && bean.getLOAD_STATUS().equalsIgnoreCase("Y")) {
            holder.chk_verify.setChecked(true);
        } else {
            holder.chk_verify.setChecked(false);
        }
        holder.cvTranDetailContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITranNoList.onTranNoListClick(position);
            }
        });
       /* holder.btn_view_dtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITranNoList.onTranNoListClick(position);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_grad, tv_location_name, tv_vc_tag_no, tv_vc_division, tv_vc_route_no;
        public TextView tv_mrir_date, tv_mrir_no, tv_coil_weight, tv_req_quantity, tv_dt_route_date;
        public TextView tv_vc_s_heat_no;
        public CardView cvTranDetailContainer;
        public CheckBox chk_verify;

        public MyViewHolder(View view) {
            super(view);
            tv_grad = (TextView) view.findViewById(R.id.tv_grad);
            tv_vc_tag_no = (TextView) view.findViewById(R.id.tv_vc_tag_no);
            tv_vc_division = (TextView) view.findViewById(R.id.tv_vc_division);
            tv_vc_route_no = (TextView) view.findViewById(R.id.tv_vc_route_no);
            tv_mrir_date = (TextView) view.findViewById(R.id.tv_mrir_date);
            tv_mrir_no = (TextView) view.findViewById(R.id.tv_mrir_no);
            tv_coil_weight = (TextView) view.findViewById(R.id.tv_coil_weight);
            tv_req_quantity = (TextView) view.findViewById(R.id.tv_req_quantity);
            tv_dt_route_date = (TextView) view.findViewById(R.id.tv_dt_route_date);
            tv_vc_s_heat_no = (TextView) view.findViewById(R.id.tv_vc_s_heat_no);
            tv_location_name = (TextView) view.findViewById(R.id.tv_location_name);
            cvTranDetailContainer = (CardView) view.findViewById(R.id.cv);
            chk_verify = (CheckBox) view.findViewById(R.id.chk_verify);
        }
    }
}
