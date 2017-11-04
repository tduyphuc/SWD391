package com.hotel.phuctdse61834.hotelbooking;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hotel.phuctdse61834.hotelbooking.request.Requester;
import com.hotel.phuctdse61834.hotelbooking.request.ResponseCode;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {

    private Map<String, String> mapData;
    private DatePickerDialog.OnDateSetListener arrival_listener;
    private DatePickerDialog.OnDateSetListener checkOut_listener;
    private TextView txt_roomType;
    private TextView txt_arrival;
    private TextView txt_checkOut;
    private TextView txt_total_price;
    private TextView txt_comment;
    private TextView txt_adult;
    private TextView txt_child;
    private TextView txt_amount;
    private SimpleDateFormat sdf;
    private List<String> errors;
    private Double pricePerNight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        String data = getIntent().getStringExtra("DATA");
        Gson gson = new Gson();
        mapData = gson.fromJson(data, HashMap.class);
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        errors = new LinkedList<>();
        pricePerNight = Double.valueOf(mapData.get("pricePerNight"));
        txt_total_price = (TextView) findViewById(R.id.txt_total_price);
        txt_comment = (TextView) findViewById(R.id.txt_comment);

        final int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        final int month = Calendar.getInstance().get(Calendar.MONTH);
        final int year = Calendar.getInstance().get(Calendar.YEAR);
        String day_txt = day + "-" + (month + 1) + "-" + year;
        txt_arrival = (TextView) findViewById(R.id.txt_arrival_day);
        txt_arrival.setText(day_txt);
        arrival_listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                txt_arrival.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                setPriceTag();
            }
        };
        txt_arrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                final int month = Calendar.getInstance().get(Calendar.MONTH);
                final int year = Calendar.getInstance().get(Calendar.YEAR);
                DatePickerDialog dialog = new DatePickerDialog(BookingActivity.this, arrival_listener,
                        year, month, day);
                dialog.setTitle("Arrival Day");
                dialog.show();
            }
        });

        txt_checkOut = (TextView) findViewById(R.id.txt_checkout_day);
        txt_checkOut.setText(day_txt);
        checkOut_listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                txt_checkOut.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                setPriceTag();
            }
        };
        txt_checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                final int month = Calendar.getInstance().get(Calendar.MONTH);
                final int year = Calendar.getInstance().get(Calendar.YEAR);
                DatePickerDialog dialog = new DatePickerDialog(BookingActivity.this, checkOut_listener,
                        year, month, day);
                dialog.setTitle("CheckOut Day");
                dialog.show();
            }
        });
        final String[] amount_items = new String[12];
        for (int i = 0; i < amount_items.length; i++){
            amount_items[i] = String.valueOf(i);
        }

        txt_adult = (TextView) findViewById(R.id.txt_adult);
        txt_adult.setText(amount_items[0]);
        txt_adult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(BookingActivity.this);
                b.setTitle("Adult amount");
                b.setItems(amount_items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        txt_adult.setText(String.valueOf(which));
                    }

                });

                b.show();
            }
        });

        txt_child = (TextView) findViewById(R.id.txt_child);
        txt_child.setText(amount_items[0]);
        txt_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(BookingActivity.this);
                b.setTitle("Child amount");
                b.setItems(amount_items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        txt_child.setText(String.valueOf(which));
                    }

                });

                b.show();
            }
        });

        txt_amount = (TextView) findViewById(R.id.txt_amount);
        txt_amount.setText(amount_items[0]);
        txt_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(BookingActivity.this);
                b.setTitle("Room amount");
                b.setItems(amount_items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        txt_amount.setText(String.valueOf(which));
                        setPriceTag();
                    }

                });

                b.show();
            }
        });

        Button btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = validateInput();
                if(result){
                    pay();
                }
                else {
                    Toast.makeText(BookingActivity.this, errors.get(0), Toast.LENGTH_SHORT).show();
                }
            }
        });
        txt_roomType = (TextView) findViewById(R.id.txt_roomType);
        txt_roomType.setText(mapData.get("name"));
        setPriceTag();
    }

    private void setPriceTag(){
        Integer select_amount = Integer.valueOf(txt_amount.getText().toString());
        try{
            Date arrival = sdf.parse(txt_arrival.getText().toString());
            Date checkOut = sdf.parse(txt_checkOut.getText().toString());
            long gap = checkOut.getTime() - arrival.getTime();
            long oneDayGap = 1000 * 3600 * 24;
            if(gap < 0){
                gap = 0;
            }
            txt_total_price.setText("$" + (select_amount * pricePerNight * (gap / oneDayGap)));
        }catch (ParseException e){
            Log.d("BookingActivity", "ParseException: " + e.getMessage());
        }
    }

    private void pay(){
        new BookingTask().execute(createBookingParams());
    }

    private Map<String, String> createBookingParams(){
        Map<String, String> params = new HashMap<>();
        SharedPreferences preferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String username = preferences.getString("Name", "");
        params.put("cusId", username);
        params.put("adult", txt_adult.getText().toString());
        params.put("child", txt_child.getText().toString());
        params.put("arrivalDay", txt_arrival.getText().toString());
        params.put("checkOutDay", txt_checkOut.getText().toString());
        params.put("comment", txt_comment.getText().toString());
        String details = "{\"" + mapData.get("typeId") + "\":\"" + txt_amount.getText().toString() + "\"}";
        params.put("details", details);
        params.put("paymentType", "1");
        return params;
    }

    private boolean validateInput(){
        boolean valid = true;
        errors.clear();
        // validate date
        try {
            final int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            final int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            final int year = Calendar.getInstance().get(Calendar.YEAR);
            String day_txt = day + "-" + month + "-" + year;
            Date now = sdf.parse(day_txt);
            Date arrival = sdf.parse(txt_arrival.getText().toString());
            Date checkOut = sdf.parse(txt_checkOut.getText().toString());
            long gap = checkOut.getTime() - arrival.getTime();
            long oneDayGap = 1000 * 3600 * 24;
            if((arrival.getTime() - now.getTime()) < oneDayGap){
                errors.add("You must booking before 1 day");
                return false;
            }
            if(gap < oneDayGap){
                errors.add("CheckOut after arrival at least one day");
                return false;
            }
        } catch (ParseException e) {
            Log.d("BookingActivity", "ParseException: " + e.getMessage());
        }

        Integer available = Integer.valueOf(mapData.get("available"));

        // validate capacity
        Integer numberOfAdult = Integer.valueOf(txt_adult.getText().toString());
        Integer numberOfChild = Integer.valueOf(txt_child.getText().toString());
        Integer amountRoom = Integer.valueOf(txt_amount.getText().toString());
        Integer capacity = Integer.valueOf(mapData.get("capacity"));
        Integer totalCapacity = amountRoom * capacity;

        if(amountRoom < 1){
            errors.add("Please book at least one room");
            return false;
        }
        if(amountRoom > available){
            errors.add("Amount room over available");
            return false;
        }
        if(numberOfAdult < 1){
            errors.add("At least 1 adult");
            return false;
        }
        if((numberOfAdult > totalCapacity)
                || numberOfChild > 2 * (totalCapacity - numberOfAdult) + numberOfAdult){
            errors.add("Not enough capacity");
            return false;
        }

        return true;
    }

    private class BookingTask extends AsyncTask<Map<String, String>, Void, Boolean> {

        private Requester requester;
        private List<String> paths;
        private Gson gson;

        @Override
        protected void onPreExecute() {
            requester = new Requester();
            paths = new ArrayList<>();
            paths.add("booking");
            paths.add("book");
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
                        return true;
                    }
                    else {
                        Log.d("BOOKING", "Booking failed");
                    }
                }
            }
            catch (IOException e){
                Log.d("BOOKING", "IOException: " + e.getMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(BookingActivity.this , "OK", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

}
