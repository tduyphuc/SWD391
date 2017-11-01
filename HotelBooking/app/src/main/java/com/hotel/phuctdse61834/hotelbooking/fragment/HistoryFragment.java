package com.hotel.phuctdse61834.hotelbooking.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hotel.phuctdse61834.hotelbooking.adapter.HistoryAdapter;
import com.hotel.phuctdse61834.hotelbooking.HistoryDetailActivity;
import com.hotel.phuctdse61834.hotelbooking.R;
import com.hotel.phuctdse61834.hotelbooking.request.Requester;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HistoryFragment extends CustomFragment {

    private ListView listView;
    private HistoryAdapter adapter;
    private ArrayList<Map<String, String>> maps;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.history_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        listView = (ListView) getView().findViewById(R.id.history_list);
        maps = new ArrayList<>();
        adapter = new HistoryAdapter(getContext(), maps);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gson gson = new Gson();
                Map<String, String> map = maps.get(position);
                String data = gson.toJson(map);
                Intent intent = new Intent(getContext(), HistoryDetailActivity.class);
                intent.putExtra("DATA", data);
                startActivity(intent);
            }
        });
        updateUI();
    }

    @Override
    public void updateUI() {
        SharedPreferences preferences = getActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String username = preferences.getString(UserFragment.NAME_TAG, "");
        if(!username.equals("")){
            Map<String, String> param = new HashMap<>();
            param.put("id", username);
            new LoadHistoryTask().execute(param);
        }

    }

    private class LoadHistoryTask extends AsyncTask<Map<String, String>, Void, Void> {

        private Requester requester;
        private List<String> paths;
        private Gson gson;

        @Override
        protected void onPreExecute() {
            requester = new Requester();
            paths = new ArrayList<>();
            paths.add("user");
            paths.add("history");
            gson = new Gson();
        }

        @Override
        protected Void doInBackground(Map<String, String>... params) {
            try {
                Response response = requester.postReq(paths, params[0]);
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
                    Log.d("LoadHistoryTask", "Response failed.");
                }
            } catch (IOException e) {
                Log.d("LoadHistoryTask", "IOException: " + e.getMessage());
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
