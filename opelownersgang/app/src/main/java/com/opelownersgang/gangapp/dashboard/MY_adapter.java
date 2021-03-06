package com.opelownersgang.gangapp.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.opelownersgang.gangapp.R;

import java.util.ArrayList;


@SuppressWarnings({"SuspiciousMethodCalls", "ControlFlowStatementWithoutBraces", "ConstantConditions"})
public class MY_adapter extends BaseAdapter {
    private Context c;
    private ArrayList<item_Dash_Board> list;


    MY_adapter(Context c, ArrayList<item_Dash_Board> list) {
        this.c = c;
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.indexOf(getItem(position));
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View view, ViewGroup group) {
        LayoutInflater inf = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inf.inflate(R.layout.customer_list_dash, null);
        TextView x = view.findViewById(R.id.time);
        TextView y = view.findViewById(R.id.description_customer);
        TextView f = view.findViewById(R.id.id_customer);
        x.setText(list.get(position).getTime());
        y.setText(list.get(position).getDesc());
        f.setText(list.get(position).getId());
        return view;
    }
}
