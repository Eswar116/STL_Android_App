package com.essindia.stlapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.essindia.stlapp.Bean.LoadingAdviceInvoiceBean;
import com.essindia.stlapp.Bean.LoadingAdviceItemBean;
import com.essindia.stlapp.R;

import java.util.HashMap;
import java.util.List;


public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    //    HashMap<List<LoadingAdviceInvoiceBean>,List<LoadingAdviceItemBean>> parentChildMappedData;
    HashMap<LoadingAdviceInvoiceBean, List<LoadingAdviceItemBean>> parentChildMappedData;
    List<LoadingAdviceInvoiceBean> parentList;

    public ExpandableListAdapter(Context context, List<LoadingAdviceInvoiceBean> parentList, HashMap<LoadingAdviceInvoiceBean, List<LoadingAdviceItemBean>> parentChildMappedData) {
        this._context = context;
        this.parentChildMappedData = parentChildMappedData;
        this.parentList = parentList;

    }

    @Override
    public LoadingAdviceItemBean getChild(int groupPosition, int childPosititon) {
        return parentChildMappedData.get(parentList.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LoadingAdviceItemBean childBean = (LoadingAdviceItemBean) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.iteam_list, null);
        }

        TextView item_invoiceQty = (TextView) convertView.findViewById(R.id.item_invoiceQty);
        TextView item_itemCode = (TextView) convertView.findViewById(R.id.item_itemCode);
        TextView item_pack_size = (TextView) convertView.findViewById(R.id.item_pack_size);
        item_invoiceQty.setText("INVOICE QTY.: " + childBean.getInvoiceQty());
        item_itemCode.setText("ITEM CODE: " + childBean.getItemCode());
        item_pack_size.setText("PACK SIZE: " + childBean.getPackSize());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //  return parentChildMappedData.get(parentList.get(groupPosition)).size();
        if (this.parentList == null) {
            Log.e("Debug", "parentList is null.");
            return 0;
        } else if (groupPosition < 0 || groupPosition >= this.parentList.size()) {
            Log.e("Debug", "position invalid: " + groupPosition);
            return 0;
        } else if (this.parentList.get(groupPosition) == null) {
            Log.e("Debug", "Value of parentList at position is null: " + groupPosition);
            return 0;
        } else if (this.parentChildMappedData == null) {
            Log.e("Debug", "parentChildMappedData is null.");
            return 0;
        } else if (!this.parentChildMappedData.containsKey(this.parentList.get(groupPosition))) {
            Log.e("Debug", "No key: " + this.parentList.get(groupPosition));
            return 0;
        } else if (this.parentChildMappedData.get(this.parentList.get(groupPosition)) == null) {
            Log.e("Debug", "Value at key is null: " + this.parentList.get(groupPosition));
            return 0;
        } else {
            return this.parentChildMappedData.get(this.parentList.get(groupPosition)).size();
        }
    }

    @Override
    public LoadingAdviceInvoiceBean getGroup(int groupPosition) {
        return this.parentList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.parentList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
//        String headerTitle = (String) getGroup(groupPosition);
        LoadingAdviceInvoiceBean invoiceBeanData = (LoadingAdviceInvoiceBean) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.invoice_number_list, null);
        }
        ImageView imgArrow = (ImageView) convertView.findViewById(R.id.imageViewArrow);
        TextView invoice_invoiceNo = (TextView) convertView.findViewById(R.id.invoice_invoiceNo);
        TextView invoice_CustomerName = (TextView) convertView.findViewById(R.id.invoice_customer_name);
        TextView invoice_invoiceDate = (TextView) convertView.findViewById(R.id.invoice_invoiceDate);
        invoice_invoiceNo.setText(invoiceBeanData.getInvoiceNo());
        invoice_CustomerName.setText(invoiceBeanData.getCustomerName());
        invoice_invoiceDate.setText(invoiceBeanData.getInvoiceDate());
        if (isExpanded) {
            imgArrow.setImageResource(R.drawable.ic_up_arrow);
        } else {
            imgArrow.setImageResource(R.drawable.ic_down_arrow);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}