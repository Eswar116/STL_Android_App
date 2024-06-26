package com.essindia.stlapp.Adapter;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.essindia.stlapp.Bean.DashboardBean;
import com.essindia.stlapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class DashboardAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<DashboardBean> items;

    public DashboardAdapter(Context c, ArrayList<DashboardBean> items) {
        mContext = c;
        Log.e(TAG, "DashboardAdapter: "+items.toString() );
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public DashboardBean getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int i, final View convertView, ViewGroup viewGroup) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            Collections.sort(items, new Comparator<DashboardBean>() {
                public int compare(DashboardBean dashboardBean1, DashboardBean dashboardBean2) {
                    // ## Ascending order
                    return dashboardBean1.getCode().compareToIgnoreCase(dashboardBean2.getCode()); // To compare string values
                }
            });
            DashboardBean bean = items.get(i);
            grid = inflater.inflate(R.layout.dashboard_list_data, null);
            TextView textView = (TextView) grid.findViewById(R.id.textView);
            ImageView imageView = (ImageView) grid.findViewById(R.id.imageView);
            if (bean.getCode().equalsIgnoreCase("1")) {
                imageView.setImageResource(R.drawable.ic_grn);
                textView.setText(bean.getName() + " (" + bean.getCount() + ")");
            } else if (bean.getCode().equalsIgnoreCase("2")) {
                imageView.setImageResource(R.drawable.ic_put_away);
                textView.setText(bean.getName() + " (" + bean.getCount() + ")");
            } else if (bean.getCode().equalsIgnoreCase("3")) {
                imageView.setImageResource(R.drawable.ic_pickup);
                textView.setText(bean.getName() + " (" + bean.getCount() + ")");
            } else if (bean.getCode().equalsIgnoreCase("4")) {
                imageView.setImageResource(R.drawable.ic_loading);
                textView.setText(bean.getName() + " (" + bean.getCount() + ")");
            } else if (bean.getCode().equalsIgnoreCase("5")) {
                imageView.setImageResource(R.drawable.mrs_loading);
                textView.setText(bean.getName() + " (" + bean.getCount() + ")");
            } else if (bean.getCode().equalsIgnoreCase("6")) {
                imageView.setImageResource(R.drawable.ic_issue);
                textView.setText(bean.getName() + " (" + bean.getCount() + ")");
            } else if (bean.getCode().equalsIgnoreCase("7")) {
                imageView.setImageResource(R.drawable.ic_issue);
                textView.setText(bean.getName() + " (" + bean.getCount() + ")");
            } else if (bean.getCode().equalsIgnoreCase("8")) {
                imageView.setImageResource(R.drawable.mrs_loading);
                textView.setText(bean.getName() + " (" + bean.getCount() + ")");
            } else if (bean.getCode().equalsIgnoreCase("9")) {
                imageView.setImageResource(R.drawable.mrs_loading);
                textView.setText(bean.getName() + " (" + bean.getCount() + ")");
            } else if (bean.getCode().equalsIgnoreCase("10")) {
                imageView.setImageResource(R.drawable.ic_receiving);
                textView.setText(bean.getName() + " (" + bean.getCount() + ")");
            } else if (bean.getCode().equalsIgnoreCase("11")) {
                imageView.setImageResource(R.drawable.ic_loading);
                textView.setText(bean.getName() + " (" + bean.getCount() + ")");
            } else if (bean.getCode().equalsIgnoreCase("12")) {
                imageView.setImageResource(R.drawable.mrs_loading);
                textView.setText(bean.getName() + " (" + bean.getCount() + ")");
            } else if (bean.getCode().equalsIgnoreCase("13")) {
                imageView.setImageResource(R.drawable.kanban_scanning);
                textView.setText(bean.getName() + " (" + bean.getCount() + ")");
            }
            else if (bean.getCode().equalsIgnoreCase("15")) {
                imageView.setImageResource(R.drawable.kanban_scanning);
                textView.setText(bean.getName() + " (" + bean.getCount() + ")");
            }

//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(mContext, "Clicked: " + i, Toast.LENGTH_SHORT).show();
//                    if (i == 0) {
//                        Intent grn = new Intent(mContext, GRNVerfication.class);
//                        mContext.startActivity(grn);
//                    }
//                }
//            });
        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}
