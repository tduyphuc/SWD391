package com.hotel.phuctdse61834.hotelbooking;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hotel.phuctdse61834.hotelbooking.request.Requester;
import com.hotel.phuctdse61834.hotelbooking.request.ResponseCode;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomDetailActivity extends AppCompatActivity {

    private Map<String, String> mapData;
    private LinearLayout services_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_room_detail);
        services_layout = (LinearLayout) findViewById(R.id.services_list_layout);

        String data = getIntent().getStringExtra("DATA");
        final Gson gson = new Gson();
        mapData = gson.fromJson(data, HashMap.class);
        mapData.put("available", "0");

        prepareUI();
        Button btn_booking = (Button) findViewById(R.id.btn_booking);
        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomDetailActivity.this, BookingActivity.class);
                intent.putExtra("DATA", gson.toJson(mapData));
                startActivity(intent);
            }
        });
        new LoadAvailableTask().execute();
        new LoadServicesTask().execute();

    }

    private void prepareUI(){
        String name = mapData.get("name");
        String capacity = mapData.get("capacity") + "\ncapacity";
        String singleBed = mapData.get("singleBedAmount");
        String doubleBed = mapData.get("doubleBedAmount");
        Integer beds = Integer.valueOf(singleBed) + Integer.valueOf(doubleBed) * 2;
        String windows = mapData.get("numberOfWindow") + "\nwindows";
        String price = "$" + mapData.get("pricePerNight") + "\nper night";
        String available = mapData.get("available");

        TextView txt_detail_room_name = (TextView) findViewById(R.id.txt_detail_room_name);
        txt_detail_room_name.setText(name);
        // people
        TextView txt_detail_people = (TextView) findViewById(R.id.txt_detail_people);
        txt_detail_people.setText(capacity);
        // bed
        TextView txt_detail_bed = (TextView) findViewById(R.id.txt_detail_bed);
        txt_detail_bed.setText(beds + "\nbeds");
        // windows
        TextView txt_detail_window = (TextView) findViewById(R.id.txt_detail_window);
        txt_detail_window.setText(windows);
        // price
        TextView txt_detail_price = (TextView) findViewById(R.id.txt_detail_price);
        txt_detail_price.setText(price);

        //
        TextView txt_detail_available = (TextView) findViewById(R.id.txt_detail_available);
        txt_detail_available.setText(available + "\navailable");

        ImageView imageView = (ImageView) findViewById(R.id.room_detail_image);
        Picasso.with(this)
                .load(mapData.get("imageLink"))
                .placeholder(R.drawable.loading)
                .fit()
                .into(imageView);

    }

    private View getItemView(){
        View view = getLayoutInflater().inflate(R.layout.service_item, null);
        return view;
    }

    private class LoadAvailableTask extends AsyncTask<Void, Void, String> {

        private Requester requester;
        private List<String> paths;
        private Gson gson;


        @Override
        protected void onPreExecute() {
            requester = new Requester();
            paths = new ArrayList<>();
            paths.add("room");
            paths.add("getAvailable");
            gson = new Gson();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Map<String, String> qparams = new HashMap<>();
                qparams.put("typeId", mapData.get("typeId"));
                Response response = requester.getReq(paths, qparams);
                if(response != null){
                    String body = response.body().string();
                    Map<String, String> message = gson.fromJson(body, HashMap.class);
                    if(message.get("code").equals(ResponseCode.RESPONSE_OK)){
                        String avaiNum = message.get("message");
                        return avaiNum;
                    }
                }
                else {
                    Log.d("LoadAvailableTask", "Response failed.");
                }
            } catch (IOException e) {
                Log.d("LoadAvailableTask", "IOException: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null){
                TextView txt_detail_available = (TextView) findViewById(R.id.txt_detail_available);
                mapData.put("available", s);
                txt_detail_available.setText(s + "\navailable");
            }
        }
    }

    private class LoadServicesTask extends AsyncTask<Void, Map<String, String>, Void> {

        private Requester requester;
        private List<String> paths;
        private Gson gson;

        @Override
        protected void onPreExecute() {
            requester = new Requester();
            paths = new ArrayList<>();
            paths.add("room");
            paths.add("getServices");
            gson = new Gson();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Map<String, String> qparams = new HashMap<>();
                qparams.put("typeId", mapData.get("typeId"));
                Response response = requester.getReq(paths, qparams);
                if(response != null){
                    String body = response.body().string();
                    JsonArray jsonArray = gson.fromJson(body, JsonArray.class);
                    for(JsonElement element : jsonArray){
                        Map<String, String> item = gson.fromJson(element, HashMap.class);
                        publishProgress(item);
                    }

                }
                else {
                    Log.d("LoadServicesTask", "Response failed.");
                }
            } catch (IOException e) {
                Log.d("LoadServicesTask", "IOException: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Map<String, String>... values) {
            Map<String, String> item = values[0];
            String service_name = item.get("name");
            View item_view = getItemView();
            TextView textView = (TextView) item_view.findViewById(R.id.service_item_name);
            textView.setText(service_name);
            services_layout.addView(item_view);
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }

    }

}
