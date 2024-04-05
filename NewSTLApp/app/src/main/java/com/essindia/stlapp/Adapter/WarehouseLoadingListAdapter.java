package com.essindia.stlapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.essindia.stlapp.Bean.InvoiceBean;
import com.essindia.stlapp.Bean.ReceiptItemBean;
import com.essindia.stlapp.R;

import java.util.ArrayList;

public class WarehouseLoadingListAdapter extends RecyclerView.Adapter<WarehouseLoadingListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<InvoiceBean> mBeanList;
    private iTranNoListClick mITranNoList;

    public interface iTranNoListClick {
        void onTranNoListClick(int position);
    }

    public WarehouseLoadingListAdapter(Context context, ArrayList<InvoiceBean> beanList, iTranNoListClick pITranNoList) {
        mContext = context;
        mBeanList = beanList;
        mITranNoList = pITranNoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_warehouse_loading, parent, false);
        return new WarehouseLoadingListAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final InvoiceBean bean = mBeanList.get(position);
        holder.tvInvoiceNo.setText("Invoice No: " + bean.getVC_INVOICE_NO());
        holder.tvInvoiceDate.setText("Invoice Date: " + bean.getDT_INVOICE_DATE());
        holder.tvCustomerName.setText("Customer Name: " + bean.getVC_CUSTOMER_NAME());
        holder.tv_ref_inv_no.setText("Reference Invoice No.: " + bean.getVC_REF_INVOICE_NO());
        holder.tv_ref_inv_date.setText("Reference Invoice Date: " + bean.getDT_INVOICE_DATE());
        holder.tv_total_cases.setText("Total Boxes: " + bean.getNU_TOTAL_CASES());
        holder.cvTranDetailContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITranNoList.onTranNoListClick(position);
            }
        });
        holder.btn_view_dtl.setOnClickListener(new View.OnClickListener() {
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
        public TextView tvInvoiceNo, tvInvoiceDate, tvCustomerName, tv_ref_inv_no, tv_ref_inv_date, tv_total_cases;
        public Button btn_view_dtl;
        public CardView cvTranDetailContainer;

        public MyViewHolder(View view) {
            super(view);
            tvInvoiceNo = (TextView) view.findViewById(R.id.tv_invoice_no);
            tvInvoiceDate = (TextView) view.findViewById(R.id.tv_invoice_date);
            tvCustomerName = (TextView) view.findViewById(R.id.tv_customer_name);
            tv_ref_inv_no = (TextView) view.findViewById(R.id.tv_ref_inv_no);
            tv_ref_inv_date = (TextView) view.findViewById(R.id.tv_ref_inv_date);
            tv_total_cases = (TextView) view.findViewById(R.id.tv_total_cases);
            btn_view_dtl = (Button) view.findViewById(R.id.btn_view_dtl);
            cvTranDetailContainer = (CardView) view.findViewById(R.id.cv);
        }
    }
}
