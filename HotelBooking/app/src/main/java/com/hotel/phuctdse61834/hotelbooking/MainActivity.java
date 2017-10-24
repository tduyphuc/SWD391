package com.hotel.phuctdse61834.hotelbooking;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hotel.phuctdse61834.hotelbooking.request.Requester;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button btn_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Login = (Button) findViewById(R.id.btn_login);
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RoomViewActivity.class);
                startActivity(intent);
                  //new TestNet().execute();
//                EditText txt_username = (EditText) findViewById(R.id.txt_username);
//                EditText txt_password = (EditText) findViewById(R.id.txt_password);
//
//                String username = txt_username.getText().toString();
//                String password = txt_password.getText().toString();
//                Log.d("TEST", "Username: " + username + "_Pass: " + password);
//                testLogin(username, password);
            }
        });

        new TestNet().execute();

    }

    private class TestNet extends AsyncTask<Void, Void, Void>{

        private Requester requester;
        private List<String> paths;

        @Override
        protected void onPreExecute() {
            requester = new Requester();
            paths = new ArrayList<>();
            paths.add("user");
            paths.add("login");
        }

        @Override
        protected Void doInBackground(Void... params) {
//            Map<String, String> map = new HashMap<>();
//            map.put("id", "tdphuc");
//            map.put("password", "123456");
//            try {
//                Map<String, String> response = requester.postReq(paths, map);
//                Log.d("TEST", response.get("code") + "_"+ response.get("message"));
//            } catch (IOException e) {
//                Log.d("TEST", "IOException: " + e.getMessage());
//            }
            return null;
        }

    }

}
