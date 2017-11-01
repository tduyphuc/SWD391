package com.hotel.phuctdse61834.hotelbooking.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hotel.phuctdse61834.hotelbooking.R;


public class UserFragment extends CustomFragment {

    public static final String NAME_TAG = "Name";
    public static final String PHONE_TAG = "Phone";
    public static final String EMAIL_TAG = "Email";
    public static final String FIRST_NAME_TAG = "First Name";
    public static final String LAST_NAME_TAG = "Last Name";
    public static final String ADDRESS_TAG = "Address";
    public static final String COUNTRY_TAG = "Country";
    public static final String POSTAL_TAG = "Postal Code";

    private View item_phone;
    private View item_email;
    private View item_first_name;
    private View item_last_name;
    private View item_address;
    private View item_country;
    private View item_postal;
    private View item_logout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.user_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //test();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        item_phone = getItemView();
        item_email = getItemView();
        item_address = getItemView();
        item_country = getItemView();
        item_first_name = getItemView();
        item_last_name = getItemView();
        item_postal = getItemView();
        item_logout = getItemView();
        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.user_layout_info);
        linearLayout.addView(item_first_name);
        linearLayout.addView(item_last_name);
        linearLayout.addView(item_phone);
        linearLayout.addView(item_email);
        linearLayout.addView(item_address);
        linearLayout.addView(item_country);
        linearLayout.addView(item_postal);
        setItemInfo(item_logout, "Log out", "");
        item_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPref();
                resetable.resetThis();
                Toast.makeText(getContext(), "Log out !!!", Toast.LENGTH_SHORT).show();
            }
        });
        linearLayout.addView(item_logout);
    }


    private void clearPref(){
        SharedPreferences preferences = getActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().apply();
    }

    private View getItemView(){
        View view = getActivity().getLayoutInflater().inflate(R.layout.info_item, null);
        return view;
    }

    private void setItemInfo(View view, String title, String value){
        TextView txt_title = (TextView) view.findViewById(R.id.item_info_title);
        txt_title.setText(title);

        TextView txt_value = (TextView) view.findViewById(R.id.item_info_value);
        txt_value.setText(value);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    public void updateUI() {
        SharedPreferences preferences = getActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);

        TextView txt_name = (TextView) getView().findViewById(R.id.user_layout_txt_username);
        txt_name.setText(preferences.getString(NAME_TAG, ""));

        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.user_layout_info);
        setItemInfo(item_phone, PHONE_TAG, preferences.getString(PHONE_TAG, ""));
        setItemInfo(item_email, EMAIL_TAG, preferences.getString(EMAIL_TAG, ""));
        setItemInfo(item_first_name, FIRST_NAME_TAG, preferences.getString(FIRST_NAME_TAG, ""));
        setItemInfo(item_last_name, LAST_NAME_TAG, preferences.getString(LAST_NAME_TAG, ""));
        setItemInfo(item_address, ADDRESS_TAG, preferences.getString(ADDRESS_TAG, ""));
        setItemInfo(item_country, COUNTRY_TAG, preferences.getString(COUNTRY_TAG, ""));
        setItemInfo(item_postal, POSTAL_TAG, preferences.getString(POSTAL_TAG, ""));
        linearLayout.invalidate();
    }
}
