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

public class LoadShopFloorListAdapter extends RecyclerView.Adapter<LoadShopFloorListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ReceiptItemBean> mBeanList;
    private iTranNoListClick mITranNoList;

    public interface iTranNoListClick {
        void onTranNoListClick(int position);
    }

    public LoadShopFloorListAdapter(Context context, ArrayList<ReceiptItemBean> beanList, iTranNoListClick pITranNoList) {
        mContext = context;
        mBeanList = beanList;
        mITranNoList = pITranNoList;
    }

    @Override
    public LoadShopFloorListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_loading_shop_floor, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LoadShopFloorListAdapter.MyViewHolder holder, final int position) {
        final ReceiptItemBean bean = mBeanList.get(position);
        holder.tvTranNo.setText("Transaction No: " + bean.getVcTranNo());
        holder.tvTranDate.setText("Transaction Date: " + bean.getTranDate());
        holder.tv_route_card_no.setText("Route Card No: " + bean.getRouteCardNo());
        holder.tv_route_card_date.setText("Route Card Date: " + bean.getRouteCardDate());
        holder.tv_machine_code.setText("Machine Code: " + bean.getMachineCode());
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTranNo, tvTranDate,tv_machine_code;
        public TextView tv_route_card_no, tv_route_card_date;
        public CardView cvTranDetailContainer;

        public MyViewHolder(View view) {
            super(view);
            tvTranNo = (TextView) view.findViewById(R.id.tv_tran_no);
            tvTranDate = (TextView) view.findViewById(R.id.tv_tran_date);
            tv_route_card_no = (TextView) view.findViewById(R.id.tv_route_card_no);
            tv_route_card_date = (TextView) view.findViewById(R.id.tv_route_card_date);
            tv_machine_code = (TextView) view.findViewById(R.id.tv_machine_code);
            cvTranDetailContainer = (CardView) view.findViewById(R.id.cv);
        }
    }
}
