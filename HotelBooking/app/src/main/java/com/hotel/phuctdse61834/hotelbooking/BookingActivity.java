package com.hotel.phuctdse61834.hotelbooking;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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
    private TextView txt_arrival;
    private TextView txt_checkOut;
    private TextView txt_total_price;
    private Spinner spinner_person;
    private Spinner spinner_amount;
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

        final int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        final int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        final int year = Calendar.getInstance().get(Calendar.YEAR);
        String day_txt = day + "-" + month + "-" + year;
        txt_arrival = (TextView) findViewById(R.id.txt_arrival_day);
        txt_arrival.setText(day_txt);
        arrival_listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                txt_arrival.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
            }
        };
        txt_arrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        };
        txt_checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(BookingActivity.this, checkOut_listener,
                        year, month, day);
                dialog.setTitle("CheckOut Day");
                dialog.show();
            }
        });
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i < 12; i++){
            list.add(i);
        }
        spinner_person = (Spinner) findViewById(R.id.spinner_person);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_person.setAdapter(adapter);

        spinner_amount = (Spinner) findViewById(R.id.spinner_amount);
        ArrayAdapter<Integer> amount_adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, list);
        amount_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_amount.setAdapter(amount_adapter);
        spinner_amount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer select_amount = Integer.valueOf(parent.getSelectedItem().toString());
                txt_total_price.setText("$" + (select_amount * pricePerNight));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
    }

    private void pay(){
        
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
            Log.d("VALIDATE", "Gap: " + gap);
            if((arrival.getTime() - now.getTime()) < oneDayGap){
                valid = false;
                errors.add("You must booking before 1 day");
            }
            if(gap < oneDayGap){
                valid = false;
                errors.add("CheckOut after arrival at least one day");
            }
        } catch (ParseException e) {
            Log.d("BookingActivity", "ParseException: " + e.getMessage());
        }

        Integer available = Integer.valueOf(mapData.get("available"));

        // validate capacity
        Integer numberOfPerson = Integer.valueOf(spinner_person.getSelectedItem().toString());
        Integer amountRoom = Integer.valueOf(spinner_amount.getSelectedItem().toString());
        Integer capacity = Integer.valueOf(mapData.get("capacity"));

        if(amountRoom > available){
            valid = false;
            errors.add("Amount room over available");
        }
        if(numberOfPerson > amountRoom * capacity){
            valid = false;
            errors.add("Not enough capacity");
        }
        return valid;
    }
}
