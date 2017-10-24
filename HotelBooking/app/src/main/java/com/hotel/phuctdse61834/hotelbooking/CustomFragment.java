package com.hotel.phuctdse61834.hotelbooking;

import android.support.v4.app.Fragment;

/**
 * Created by johntran on 10/19/17.
 */

public abstract class CustomFragment extends Fragment {
    protected IResetable resetable;
    public void setResetable(IResetable resetable){
        this.resetable = resetable;
    }
    public abstract void updateUI();
}
