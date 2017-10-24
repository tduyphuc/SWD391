package com.hotel.phuctdse61834.hotelbooking;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hotel.phuctdse61834.hotelbooking.request.Requester;
import com.hotel.phuctdse61834.hotelbooking.request.ResponseCode;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by johntran on 10/19/17.
 */

public class LoginFragment extends CustomFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button btn_login = (Button) getView().findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt_username = (TextView) LoginFragment.this.getView().findViewById(R.id.txt_username);
                String username = txt_username.getText().toString();
                TextView txt_password = (TextView) LoginFragment.this.getView().findViewById(R.id.txt_password);
                String password = txt_password.getText().toString();
                Map<String, String> requestBody = new HashMap<String, String>();
                requestBody.put("id", username);
                requestBody.put("password", password);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                new LoginTask().execute(requestBody);
            }
        });
    }

    @Override
    public void updateUI() {
        TextView txt_username = (TextView) LoginFragment.this.getView().findViewById(R.id.txt_username);
        TextView txt_password = (TextView) LoginFragment.this.getView().findViewById(R.id.txt_password);
        txt_username.setText("");
        txt_password.setText("");
    }

    private class LoginTask extends AsyncTask<Map<String, String>, Void, Boolean> {

        private Requester requester;
        private List<String> paths;
        private Gson gson;

        @Override
        protected void onPreExecute() {
            requester = new Requester();
            paths = new ArrayList<>();
            paths.add("user");
            paths.add("login");
            gson = new Gson();
        }

        @Override
        protected Boolean doInBackground(Map<String, String>... params) {
            Map<String, String> map = params[0];

            try{
                Response response = requester.postReq(paths, map);
                if(response != null) {
                    String body = response.body().string();
                    Map<String, String> message = gson.fromJson(body, HashMap.class);
                    String response_code = message.get("code");
                    if(response_code.equals(ResponseCode.RESPONSE_OK)){
                        Map<String, String> info = gson.fromJson(message.get("message"), HashMap.class);
                        savePref(info);
                        return true;
                    }
                    else {
                        Log.d("LOGIN", "Login failed");
                    }
                }
            }
            catch (IOException e){

            }
            return false;
        }

        private void savePref(Map<String, String> info){
            SharedPreferences preferences = LoginFragment.this.getActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(UserFragment.NAME_TAG, info.get("id"));
            editor.putString(UserFragment.PHONE_TAG, info.get("phone"));
            editor.putString(UserFragment.EMAIL_TAG, info.get("email"));
            editor.putString(UserFragment.ADDRESS_TAG, info.get("address"));
            editor.putString(UserFragment.FIRST_NAME_TAG, info.get("firstName"));
            editor.putString(UserFragment.LAST_NAME_TAG, info.get("lastName"));
            editor.putString(UserFragment.COUNTRY_TAG, info.get("country"));
            editor.putString(UserFragment.POSTAL_TAG, info.get("postal"));
            editor.commit();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                resetable.resetThis();
            }else {
                Toast.makeText(getContext(), "Login failed", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
