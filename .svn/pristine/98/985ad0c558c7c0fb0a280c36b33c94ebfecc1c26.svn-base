package com.essindia.stlapp.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.essindia.stlapp.Bean.RouteCardProcessDataBean;
import com.essindia.stlapp.R;

import java.util.ArrayList;

public class RouteCardOtherProcessListAdapter extends RecyclerView.Adapter<RouteCardOtherProcessListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<RouteCardProcessDataBean> mBeanList;
    private iTranNoListClick mITranNoList;

    public interface iTranNoListClick {
        void onTranNoListClick(int position);
    }

    public RouteCardOtherProcessListAdapter(Context context, ArrayList<RouteCardProcessDataBean> beanList, iTranNoListClick pITranNoList) {
        mContext = context;
        mBeanList = beanList;
        mITranNoList = pITranNoList;
    }

    @Override
    public RouteCardOtherProcessListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_route_card_other_process_item_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RouteCardOtherProcessListAdapter.MyViewHolder holder, final int position) {
        final RouteCardProcessDataBean bean = mBeanList.get(position);
        holder.tv_actual_bin_no.setText(bean.getVcActualBinNo());
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

        TextView tv_actual_bin_no;
        public CardView cvTranDetailContainer;

        MyViewHolder(View view) {
            super(view);
            tv_actual_bin_no = (TextView) view.findViewById(R.id.tv_actual_bin_no);
            cvTranDetailContainer = (CardView) view.findViewById(R.id.cv);
        }
    }
}
