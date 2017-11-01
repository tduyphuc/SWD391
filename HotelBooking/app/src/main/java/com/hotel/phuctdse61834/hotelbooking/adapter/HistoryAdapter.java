package com.hotel.phuctdse61834.hotelbooking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hotel.phuctdse61834.hotelbooking.R;

import java.util.ArrayList;
import java.util.Map;


public class HistoryAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Map<String, String>> maps;

    public HistoryAdapter(Context mContext, ArrayList<Map<String, String>> maps) {
        this.mContext = mContext;
        this.maps = maps;
    }

    @Override
    public int getCount() {
        return maps.size();
    }

    @Override
    public Object getItem(int position) {
        return maps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = new View(mContext);
            view = layoutInflater.inflate(R.layout.history_item, parent, false);
        }
        else{
            view = (View) convertView;
        }
        Map<String, String> item = maps.get(position);

        TextView txt_roomType = (TextView) view.findViewById(R.id.item_history_title);
        txt_roomType.setText(item.get("roomType"));

        TextView txt_amount = (TextView) view.findViewById(R.id.item_history_amount);
        txt_amount.setText("Amount: " + item.get("amount"));

        TextView txt_date = (TextView) view.findViewById(R.id.item_history_date);
        txt_date.setText(item.get("bookingDay"));
        return view;
    }
}
