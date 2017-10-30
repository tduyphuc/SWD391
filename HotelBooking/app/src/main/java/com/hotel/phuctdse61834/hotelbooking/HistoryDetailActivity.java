package com.hotel.phuctdse61834.hotelbooking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class HistoryDetailActivity extends AppCompatActivity {

    private Map<String, String> mapData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        String data = getIntent().getStringExtra("DATA");
        Gson gson = new Gson();
        mapData = gson.fromJson(data, HashMap.class);

        setTextForTextView(mapData.get("roomType"), (TextView) findViewById(R.id.txt_hisrory_roomtype));
        setTextForTextView(mapData.get("amount"), (TextView) findViewById(R.id.txt_history_amount));
        setTextForTextView(mapData.get("bookingDay"), (TextView) findViewById(R.id.txt_history_bday));
        setTextForTextView(mapData.get("arrivalDay"), (TextView) findViewById(R.id.txt_history_aday));
        setTextForTextView(mapData.get("checkOutDay"), (TextView) findViewById(R.id.txt_history_cday));
        setTextForTextView(mapData.get("adult"), (TextView) findViewById(R.id.txt_history_adult));
        setTextForTextView(mapData.get("child"), (TextView) findViewById(R.id.txt_history_child));
        setTextForTextView(mapData.get("comment"), (TextView) findViewById(R.id.txt_history_comment));

    }

    private void setTextForTextView(String mes, TextView textView){
        textView.setText(mes);
    }
}
