package com.essindia.stlapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.essindia.stlapp.Bean.GRNBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 29-11-2016.
 */

public class DockAdapter extends BaseAdapter {

    private ArrayList<GRNBean> data;

    public DockAdapter(ArrayList<GRNBean> list) {
        data = list;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // LayoutInflater inflater = getLayoutInflater();
        View row = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_item, parent,
                false);

        TextView label = (TextView) row;
        label.setText(data.get(position).getBIN_DESC());
        return row;
    }
}