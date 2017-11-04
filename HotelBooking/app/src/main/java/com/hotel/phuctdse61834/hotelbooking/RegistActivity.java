package com.hotel.phuctdse61834.hotelbooking;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hotel.phuctdse61834.hotelbooking.request.Requester;
import com.hotel.phuctdse61834.hotelbooking.request.ResponseCode;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistActivity extends AppCompatActivity {

    private List<String> errors;

    private TextView txt_username;
    private TextView txt_password;
    private TextView txt_re_password;
    private TextView txt_firstName;
    private TextView txt_lastName;
    private TextView txt_phone;
    private TextView txt_email;
    private TextView txt_address;
    private TextView txt_postal;
    private TextView txt_country;
    private Button btn_regist;
    private String[] countries;

    private static final String USER_NAME_PATTERN = "[a-zA-Z0-9]{1,20}";
    private static final String NAME_PATTERN = "[a-zA-Z]{1,20}";
    private static final String POSTAL_PATTERN = "[0-9]{5,6}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        errors = new LinkedList<>();
        txt_username = (TextView) findViewById(R.id.txt_regist_username);
        txt_password = (TextView) findViewById(R.id.txt_regist_password);
        txt_re_password = (TextView) findViewById(R.id.txt_regist_re_password);
        txt_firstName = (TextView) findViewById(R.id.txt_regist_firstName);
        txt_lastName = (TextView) findViewById(R.id.txt_regist_lastName);
        txt_phone = (TextView) findViewById(R.id.txt_regist_phone);
        txt_email = (TextView) findViewById(R.id.txt_regist_email);
        txt_address = (TextView) findViewById(R.id.txt_regist_address);
        txt_postal = (TextView) findViewById(R.id.txt_regist_postal);
        txt_country = (TextView) findViewById(R.id.txt_country);
        btn_regist = (Button) findViewById(R.id.btn_regist);

        countries = getResources().getStringArray(R.array.countries);
        txt_country.setText(countries[0]);
        txt_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(RegistActivity.this);
                b.setTitle("Room amount");
                b.setItems(countries, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        txt_country.setText(countries[which]);
                    }

                });

                b.show();
            }
        });

        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validInfo()){
                    new RegistTask().execute(createParams());
                }
            }
        });
    }

    private Map<String, String> createParams(){
        Map<String, String> params = new HashMap<>();
        String username = txt_username.getText().toString();
        String password = txt_password.getText().toString();
        String phone = txt_phone.getText().toString();
        String firstName = txt_firstName.getText().toString();
        String lastName = txt_lastName.getText().toString();
        String email = txt_email.getText().toString();
        String postal = txt_postal.getText().toString();
        String address = txt_address.getText().toString();
        String country = txt_country.getText().toString();
        params.put("id", username);
        params.put("password", password);
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        params.put("phone", phone);
        params.put("email", email);
        params.put("postal", postal);
        params.put("country", country);
        params.put("address", address);
        params.put("imageLink", "");
        return params;
    }

    private void clearAllField(){
        txt_username.setText("");
        txt_password.setText("");
        txt_re_password.setText("");
        txt_firstName.setText("");
        txt_lastName.setText("");
        txt_phone.setText("");
        txt_email.setText("");
        txt_address.setText("");
        txt_postal.setText("");
        txt_country.setText(countries[0]);
    }

    private boolean validInfo(){
        boolean notEmptyField = checkNotEmptyField();
        boolean isMatchPattern = checkPattern();
        boolean isMatchPassword = checkPasswordMatch();
        boolean valid = notEmptyField && isMatchPattern && isMatchPassword;
        return valid;
    }

    private boolean checkNotEmptyField(){
        boolean valid = true;
        if(txt_username.getText().toString().equals("")){
            txt_username.setError("Required field");
            valid = false;
        }
        if(txt_password.getText().toString().equals("")){
            txt_firstName.setError("Required field");
            valid = false;
        }
        if(txt_firstName.getText().toString().equals("")){
            txt_firstName.setError("Required field");
            valid = false;
        }
        if(txt_lastName.getText().toString().equals("")){
            txt_lastName.setError("Required field");
            valid = false;
        }
        if(txt_email.getText().toString().equals("")){
            txt_email.setError("Required field");
            valid = false;
        }
        if(txt_address.getText().toString().equals("")){
            txt_address.setError("Required field");
            valid = false;
        }
        if(txt_phone.getText().toString().equals("")){
            txt_phone.setError("Required field");
            valid = false;
        }
        if(txt_postal.getText().toString().equals("")){
            txt_postal.setError("Required field");
            valid = false;
        }
        return valid;
    }

    private boolean checkPasswordMatch(){
        String pass = txt_password.getText().toString();
        String re_pass = txt_re_password.getText().toString();
        if(!pass.equals(re_pass)){
            txt_re_password.setError("Not match");
            return false;
        }
        return true;
    }

    private boolean checkPattern(){
        boolean valid = true;

        String username = txt_username.getText().toString().trim();
        String password = txt_password.getText().toString().trim();
        String phone = txt_phone.getText().toString().trim();
        String firstName = txt_firstName.getText().toString().trim();
        String lastName = txt_lastName.getText().toString().trim();
        String email = txt_email.getText().toString().trim();
        String postal = txt_postal.getText().toString().trim();

        if(!username.matches(USER_NAME_PATTERN)){
            valid = false;
            txt_username.setError("Contain special characters or spaces");
        }
        if(password.length() < 6){
            valid = false;
            txt_password.setError("Need 6 or more characters");
        }
        if(!firstName.matches(NAME_PATTERN)){
            valid = false;
            txt_firstName.setError("Alphabet only");
        }
        if(!lastName.matches(NAME_PATTERN)){
            valid = false;
            txt_lastName.setError("Alphabet only");
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            valid = false;
            txt_email.setError("Please input valid email");
        }
        if(!Patterns.PHONE.matcher(phone).matches()){
            valid = false;
            txt_phone.setError("Please input valid phone");
        }
        if(!postal.matches(POSTAL_PATTERN)){
            valid = false;
            txt_postal.setError("Please input valid postal");
        }
        return valid;
    }

    private class RegistTask extends AsyncTask<Map<String, String>, Void, Boolean> {

        private Requester requester;
        private List<String> paths;
        private Gson gson;
        private String err_message;

        @Override
        protected void onPreExecute() {
            requester = new Requester();
            paths = new ArrayList<>();
            paths.add("user");
            paths.add("regist");
            gson = new Gson();
            err_message = "Unexpected error";
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
                        return true;
                    }
                    else {
                        Log.d("REGIST", "Regist failed");
                        err_message = message.get("message");
                    }
                }
            }
            catch (IOException e){

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(RegistActivity.this, "Regist successful !!!", Toast.LENGTH_SHORT).show();
                clearAllField();
            }
            else{
                Toast.makeText(RegistActivity.this, err_message, Toast.LENGTH_SHORT).show();
            }
        }

    }

}
