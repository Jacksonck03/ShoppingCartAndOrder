package com.example.new_rice_shop;

import android.view.View;

public interface IOnImageViewAdapterClickListener {
    void onCalculatePriceListener (View view, int position, boolean isDecrease, boolean isDelete);

}
