package com.hotel.swd.hotel.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hotel.swd.hotel.admin.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by johntran on 10/31/17.
 */

public class RoomEditAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Map<String, String>> maps;

    public RoomEditAdapter(Context mContext, ArrayList<Map<String, String>> maps) {
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
            view = layoutInflater.inflate(R.layout.room_edit_item, viewGroup, false);

        }
        else{
            view = (View) convertView;
        }
        Map<String, String> item = maps.get(i);
        final int position = i;

        TextView txt_roomId = (TextView) view.findViewById(R.id.txt_room_edit_roomId);
        txt_roomId.setText(item.get("roomId"));

        TextView txt_roomType = (TextView) view.findViewById(R.id.txt_room_edit_roomType);
        txt_roomType.setText(item.get("roomType"));

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox_room_edit_available);
        checkBox.setChecked(Boolean.valueOf(item.get("available")));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Map<String, String> item = maps.get(position);
                item.put("available", String.valueOf(b));
            }
        });
        return view;
    }
}
