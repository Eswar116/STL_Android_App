package com.essindia.stlapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.essindia.stlapp.Bean.InvoiceBean;
import com.essindia.stlapp.R;

import java.util.ArrayList;

public class WarehouseLoadingProductListAdapter extends RecyclerView.Adapter<WarehouseLoadingProductListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<InvoiceBean> mBeanList;
    private iTranNoListClick mITranNoList;

    public interface iTranNoListClick {
        void onTranNoListClick(int position);
    }

    public WarehouseLoadingProductListAdapter(Context context, ArrayList<InvoiceBean> beanList, iTranNoListClick pITranNoList) {
        mContext = context;
        mBeanList = beanList;
        mITranNoList = pITranNoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_warehouse_loading_product, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final InvoiceBean bean = mBeanList.get(position);
        holder.tv_product_code.setText("Product Code: " + bean.getVC_PRODUCT_CODE());
        holder.tv_product_quantity.setText("Product Quantity: " + bean.getNU_PRODUCT_QUANTITY());
        holder.tv_pack_size.setText("Pack Size: " + bean.getNU_PACK_SIZE());
        holder.tv_no_of_box.setText("Number of boxes: " + bean.getBOXES());
        holder.tv_count.setText("Previous box scanned : " + bean.getTOT_PROD_SCAN_BOX());
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
        public TextView tv_product_code, tv_product_quantity, tv_pack_size, tv_no_of_box, tv_count;
        public CardView cvTranDetailContainer;

        public MyViewHolder(View view) {
            super(view);
            tv_product_code = (TextView) view.findViewById(R.id.tv_product_code);
            tv_product_quantity = (TextView) view.findViewById(R.id.tv_product_quantity);
            tv_pack_size = (TextView) view.findViewById(R.id.tv_pack_size);
            tv_no_of_box = (TextView) view.findViewById(R.id.tv_no_of_box);
            tv_count = (TextView) view.findViewById(R.id.tv_count);
            cvTranDetailContainer = (CardView) view.findViewById(R.id.cv);
        }
    }
}
