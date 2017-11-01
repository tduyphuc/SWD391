package com.hotel.swd.hotel.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_room_status = (Button) findViewById(R.id.btn_room_status);
        Button btn_view_booking = (Button) findViewById(R.id.btn_booking_list);

        btn_room_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditRoomActivity.class);
                startActivity(intent);
            }
        });

        btn_view_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, ViewBookingActivity.class);
//                startActivity(intent);
                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                b.setTitle("Example");
                String[] types = {"0", "1"};
                b.setItems(types, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Choose: " + which, Toast.LENGTH_SHORT).show();
                    }

                });

                b.show();
            }
        });
    }
}
