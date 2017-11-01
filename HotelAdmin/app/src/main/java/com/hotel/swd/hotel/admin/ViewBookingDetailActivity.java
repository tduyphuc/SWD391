package com.hotel.swd.hotel.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ViewBookingDetailActivity extends AppCompatActivity {

    private Map<String, String> mapData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking_detail);

        String data = getIntent().getStringExtra("DATA");
        Gson gson = new Gson();
        mapData = gson.fromJson(data, HashMap.class);
    }
}
