package com.hotel.swd.hotel.admin;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hotel.swd.hotel.admin.adapter.BookingViewAdapter;
import com.hotel.swd.hotel.admin.adapter.RoomEditAdapter;
import com.hotel.swd.hotel.admin.requester.Requester;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewBookingActivity extends AppCompatActivity {

    private ListView listView;
    private BookingViewAdapter adapter;
    private ArrayList<Map<String, String>> maps;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking);

        gson = new Gson();
        listView = (ListView) findViewById(R.id.list_booking);
        maps = new ArrayList<>();
        adapter = new BookingViewAdapter(this, maps);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ViewBookingActivity.this, ViewBookingDetailActivity.class);
                intent.putExtra("DATA", gson.toJson(maps.get(i)));
                startActivity(intent);
            }
        });
        new LoadBookingTask().execute();
    }

    private class LoadBookingTask extends AsyncTask<Void, Void, Void> {

        private Requester requester;
        private List<String> paths;
        private Gson gson;

        @Override
        protected void onPreExecute() {
            requester = new Requester();
            paths = new ArrayList<>();
            paths.add("booking");
            paths.add("getAllBooking");
            gson = new Gson();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Response response = requester.getReq(paths, null);
                if(response != null){
                    maps.clear();
                    String body = response.body().string();
                    JsonArray jsonArray = gson.fromJson(body, JsonArray.class);
                    for(JsonElement element : jsonArray){
                        Map<String, String> item = gson.fromJson(element, HashMap.class);
                        getFirstRoomType(item);
                        maps.add(item);
                    }
                }
                else {
                    Log.d("LoadBookingTask", "Response failed.");
                }
            } catch (IOException e) {
                Log.d("LoadBookingTask", "IOException: " + e.getMessage());
            }
            return null;
        }

        private void getFirstRoomType(Map<String, String> item){
            String details_str = item.get("details");
            Map<String, String> details = gson.fromJson(details_str, HashMap.class);
            for(Map.Entry<String, String> entry : details.entrySet()){
                item.put("roomType", entry.getKey());
                item.put("amount", entry.getValue());
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }

    }
}
