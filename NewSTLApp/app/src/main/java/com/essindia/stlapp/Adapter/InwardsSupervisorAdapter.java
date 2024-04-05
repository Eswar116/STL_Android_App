package com.essindia.stlapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.essindia.stlapp.Bean.ProductItem;
import com.essindia.stlapp.Bean.SupervisorBean;
import com.essindia.stlapp.R;

import java.util.ArrayList;
import java.util.List;

public class InwardsSupervisorAdapter extends RecyclerView.Adapter<InwardsSupervisorAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ProductItem> productBox;
    private OnItemClickListener onItemClickListener;

    public InwardsSupervisorAdapter(Context context, ArrayList<ProductItem> productBox, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.productBox = productBox;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(ProductItem data, int position);
    }


    @Override
    public InwardsSupervisorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_pending_details, parent, false);
        return new InwardsSupervisorAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductItem productItem = productBox.get(position);
        holder.bindData(productItem);
        holder.check_box.setOnClickListener(view -> {
            if (holder.check_box.isChecked()) {
                productItem.setStatus(true);
               holder.cv.setBackgroundColor(context.getResources().getColor(R.color.green_ad));
                onItemClickListener.onItemClick(productItem, position);
            } else {
                productItem.setStatus(false);
                onItemClickListener.onItemClick(productItem, position);
                holder.cv.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        });
    }

    @Override
    public int getItemCount() {
        return productBox.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView product_code, invoice_number, product_string, slNo;
        CheckBox check_box;
        LinearLayout cv;

        public ViewHolder(View itemView) {
            super(itemView);
            product_code = itemView.findViewById(R.id.product_code);
            invoice_number = itemView.findViewById(R.id.invoice_number);
            product_string = itemView.findViewById(R.id.product_string);
            check_box = itemView.findViewById(R.id.check_box);
            cv = itemView.findViewById(R.id.cv);

        }

        public void bindData(ProductItem productItem) {
            product_code.setText(productItem.getProductCode());  //position+"."+
            invoice_number.setText(productItem.getInvoiceNumber());
            product_string.setText(productItem.getProductString());

            if (productItem.isStatus()){
                check_box.setChecked(true);
                cv.setBackgroundColor(context.getResources().getColor(R.color.green_ad));
            }else {
                check_box.setChecked(false);

                cv.setBackgroundColor(context.getResources().getColor(R.color.white));
            }

        }
    }
}
