package com.hotel.phuctdse61834.hotelbooking;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hotel.phuctdse61834.hotelbooking.request.Requester;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by johntran on 10/17/17.
 */

public class RoomViewFragment extends CustomFragment {

    private ListView listView;
    private RoomAdapter adapter;
    private ArrayList<Map<String, String>> maps;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.room_view_fragment, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        listView = (ListView) getView().findViewById(R.id.list_room);
        maps = new ArrayList<>();
        adapter = new RoomAdapter(getContext(), maps);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gson gson = new Gson();
                Map<String, String> map = maps.get(position);
                String data = gson.toJson(map);
                Intent intent = new Intent(getContext(), RoomDetailActivity.class);
                intent.putExtra("DATA", data);
                startActivity(intent);
            }
        });
        new LoadRoomTask().execute();
    }

    @Override
    public void updateUI() {

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
            paths.add("getAll");
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
                        Log.d("LoadRoomTask", item.toString());
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
}
