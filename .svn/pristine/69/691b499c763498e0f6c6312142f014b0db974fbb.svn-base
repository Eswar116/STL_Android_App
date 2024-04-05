package com.essindia.stlapp.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.essindia.stlapp.Bean.IssueItemDetailBean;
import com.essindia.stlapp.R;

import java.util.ArrayList;

public class IssueMrsNoListAdapter extends RecyclerView.Adapter<IssueMrsNoListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<IssueItemDetailBean> mBeanList;
    private iMrsNoListClick mIMrsNoList;

    public interface iMrsNoListClick {
        void onMrsNoListClick(int position);
    }

    public IssueMrsNoListAdapter(Context context, ArrayList<IssueItemDetailBean> beanList, iMrsNoListClick pIMrsNoList) {
        mContext = context;
        mBeanList = beanList;
        mIMrsNoList = pIMrsNoList;
    }

    @Override
    public IssueMrsNoListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_issue_mrs_no_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IssueMrsNoListAdapter.MyViewHolder holder, final int position) {
        final IssueItemDetailBean bean = mBeanList.get(position);
        holder.tvRouteNo.setText(bean.getVcRouteNo());
        holder.tvDespNo.setText( bean.getVcDespNo());
        holder.cvTranDetailContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIMrsNoList.onMrsNoListClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvRouteNo, tvDespNo;
        CardView cvTranDetailContainer;

        MyViewHolder(View view) {
            super(view);
            tvRouteNo = (TextView) view.findViewById(R.id.tv_route_no);
            tvDespNo = (TextView) view.findViewById(R.id.tv_desp_no);
            cvTranDetailContainer = (CardView) view.findViewById(R.id.cv);
        }
    }
}
