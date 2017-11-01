package com.hotel.phuctdse61834.hotelbooking.fragment;

import android.support.v4.app.Fragment;

import com.hotel.phuctdse61834.hotelbooking.IResetable;


public abstract class CustomFragment extends Fragment {
    protected IResetable resetable;
    public void setResetable(IResetable resetable){
        this.resetable = resetable;
    }
    public abstract void updateUI();
}
