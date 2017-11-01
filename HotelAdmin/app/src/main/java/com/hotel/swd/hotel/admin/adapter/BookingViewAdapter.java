package com.hotel.swd.hotel.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hotel.swd.hotel.admin.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by johntran on 10/31/17.
 */

public class BookingViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Map<String, String>> maps;

    public BookingViewAdapter(Context mContext, ArrayList<Map<String, String>> maps) {
        this.mContext = mContext;
        this.maps = maps;
    }

    @Override
    public int getCount() {
        return maps.size();
    }

    @Override
    public Object getItem(int i) {
        return maps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = new View(mContext);
            view = layoutInflater.inflate(R.layout.booking_view_item, viewGroup, false);

        }
        else{
            view = (View) convertView;
        }
        Map<String, String> item = maps.get(i);

        setTextForTextView((TextView) view.findViewById(R.id.item_booking_view_cusId), item.get("cusId"));
        setTextForTextView((TextView) view.findViewById(R.id.item_booking_view_roomType), item.get("roomType"));
        setTextForTextView((TextView) view.findViewById(R.id.item_booking_view_bdate), "Book at: " + item.get("bookingDay"));
        setTextForTextView((TextView) view.findViewById(R.id.item_booking_view_adate), "Arrival day: " + item.get("arrivalDay"));

        return view;
    }

    private void setTextForTextView(TextView textView, String txt){
        textView.setText(txt);
    }
}
