package com.hotel.phuctdse61834.hotelbooking;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hotel.phuctdse61834.hotelbooking.fragment.CustomFragment;


public class RoomViewActivity extends AppCompatActivity implements IResetable{

    private int mPrimeColor;
    private int mAssistColor;
    private ImageView[] imageViews;
    private CustomFragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_view);
        mPrimeColor = Color.parseColor("#E91E63");
        mAssistColor = Color.parseColor("#B0BEC5");
        imageViews = new ImageView[3];
        imageViews[0] = (ImageView) findViewById(R.id.img_tab_1);
        imageViews[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTabColor(0);
                changeFragment(0);
            }
        });
        imageViews[1] = (ImageView) findViewById(R.id.img_tab_2);
        imageViews[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTabColor(1);
                if(checkHadLogin()){

                    changeFragment(1);
                }
                else {
                    changeFragment(3);
                }

            }
        });
        imageViews[2] = (ImageView) findViewById(R.id.img_tab_3);
        imageViews[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTabColor(2);
                if(checkHadLogin()){
                    changeFragment(2);
                }
                else {
                    changeFragment(3);
                }
            }
        });
        fragments = new CustomFragment[4];
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragments[0] = (CustomFragment) fragmentManager.findFragmentById(R.id.room_fragment);
        fragments[1] = (CustomFragment) fragmentManager.findFragmentById(R.id.user_fragment);
        fragments[2] = (CustomFragment) fragmentManager.findFragmentById(R.id.history_fragment);
        fragments[3] = (CustomFragment) fragmentManager.findFragmentById(R.id.login_fragment);
        for (int i = 0; i < fragments.length; i++){
            fragments[i].setResetable(this);
        }
        changeTabColor(0);
        changeFragment(0);
    }

    private void changeTabColor(int index){
        for(int i = 0; i < imageViews.length; i++){
            if(i == index){
                imageViews[i].setColorFilter(mPrimeColor);
            }
            else {
                imageViews[i].setColorFilter(mAssistColor);
            }
        }
    }

    private void changeFragment(int index){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for(int i = 0; i < fragments.length; i++){
            if(i == index){
                fragmentTransaction.show(fragments[i]);
            }
            else {
                fragmentTransaction.hide(fragments[i]);
            }
        }
        fragmentTransaction.commit();
    }

    private boolean checkHadLogin(){
        SharedPreferences preferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String username = preferences.getString("Name", "");
        return !username.equals("");
    }

    @Override
    public void resetThis() {
        Log.d("ROOM_VIEW", "RESET");

        for(int i = 0; i < fragments.length; i++){
            fragments[i].updateUI();
        }
        changeTabColor(0);
        changeFragment(0);
    }
}
