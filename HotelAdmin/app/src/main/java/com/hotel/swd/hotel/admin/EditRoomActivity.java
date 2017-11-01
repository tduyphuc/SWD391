package com.hotel.swd.hotel.admin;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hotel.swd.hotel.admin.adapter.RoomEditAdapter;
import com.hotel.swd.hotel.admin.requester.Requester;
import com.hotel.swd.hotel.admin.requester.ResponseCode;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditRoomActivity extends AppCompatActivity {

    private ListView listView;
    private RoomEditAdapter adapter;
    private ArrayList<Map<String, String>> maps;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);

        gson = new Gson();
        listView = (ListView) findViewById(R.id.list_room);
        maps = new ArrayList<>();
        adapter = new RoomEditAdapter(this, maps);
        listView.setAdapter(adapter);

        Button btn_update = (Button) findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UpdateReqTask().execute(createParams());
            }
        });

        new LoadRoomTask().execute();
    }

    private Map<String, String> createParams(){
        Map<String, String> params = new HashMap<>();
        for (Map<String, String> map : maps){
            params.put(map.get("roomId"), map.get("available"));
        }
        return params;
    }

    private class LoadRoomTask extends AsyncTask<Void, Void, Void> {

        private Requester requester;
        private List<String> paths;
        private Gson gson;

        @Override
        protected void onPreExecute() {
            requester = new Requester();
            paths = new ArrayList<>();
            paths.add("room");
            paths.add("getAllRooms");
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
                        maps.add(item);
                    }
                }
                else {
                    Log.d("LoadRoomTask", "Response failed.");
                }
            } catch (IOException e) {
                Log.d("LoadRoomTask", "IOException: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }

    }

    private class UpdateReqTask extends AsyncTask<Map<String, String>, Void, Boolean>{
        private Requester requester;
        private List<String> paths;
        private Gson gson;

        @Override
        protected void onPreExecute() {
            requester = new Requester();
            paths = new ArrayList<>();
            paths.add("room");
            paths.add("updateRooms");
            gson = new Gson();
        }

        @Override
        protected Boolean doInBackground(Map<String, String>... params) {
            Map<String, String> map = params[0];

            try{
                Response response = requester.postReqRaw(paths, gson.toJson(map));
                if(response != null) {
                    String body = response.body().string();
                    if(body.equals("OK")){
                        return true;
                    }
                    else {
                        Log.d("Update", "Update failed");
                    }
                }
            }
            catch (IOException e){
                Log.d("Update", "IOException: " + e.getMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(EditRoomActivity.this, "Update Ok", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
