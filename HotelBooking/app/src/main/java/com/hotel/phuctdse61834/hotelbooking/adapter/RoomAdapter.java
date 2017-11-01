package com.hotel.phuctdse61834.hotelbooking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hotel.phuctdse61834.hotelbooking.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class RoomAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Map<String, String>> maps;

    public RoomAdapter(Context mContext, ArrayList<Map<String, String>> maps) {
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
            view = layoutInflater.inflate(R.layout.room_item, parent, false);

        }
        else{
            view = (View) convertView;
        }
        Map<String, String> item = maps.get(position);
        String roomName = item.get("name");
        String price = "$" + item.get("pricePerNight");
        String imageLink = item.get("imageLink");

        TextView txtName = (TextView) view.findViewById(R.id.txt_room_name);
        txtName.setText(roomName);

        TextView txtPrice = (TextView) view.findViewById(R.id.txt_price);
        txtPrice.setText(price);

        ImageView imageView = (ImageView) view.findViewById(R.id.img_room);

        Picasso.with(mContext)
                .load(imageLink)
                .placeholder(R.drawable.loading)
                .fit()
                .transform(new RoundedCornersTransformation(15, 0))
                .into(imageView);

        return view;
    }
}
