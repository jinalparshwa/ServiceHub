package com.servicehub.Custom_Compo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Nirav on 5/16/2016.
 */
public class CustomTextview extends TextView {

    private final static String CONDENSED_FONT = "fonts/Yantramanav_Regular.ttf";

    public CustomTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextview(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), CONDENSED_FONT);
        setTypeface(tf);
    }


}
