package com.essindia.stlapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.essindia.stlapp.Bean.ReceiptItemLocBean;
import com.essindia.stlapp.R;

import java.util.ArrayList;

public class LocationListAdapter extends BaseAdapter {
    Context context;
    ArrayList<ReceiptItemLocBean> locList;
    LayoutInflater inflter;

    public LocationListAdapter(Context context, ArrayList<ReceiptItemLocBean> locList) {
        this.context = context;
        this.locList = locList;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return locList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.row_receipt_item_location, null);
        ReceiptItemLocBean bean = locList.get(position);
        TextView tvLocationDesc = (TextView) view.findViewById(R.id.tv_loc_desc);
        tvLocationDesc.setText(bean.getVcLocationDesc());
        return view;
    }
}
