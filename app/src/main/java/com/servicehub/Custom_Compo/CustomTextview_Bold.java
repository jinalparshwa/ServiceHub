package com.servicehub.Custom_Compo;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Nirav on 5/16/2016.
 */
public class CustomTextview_Bold extends TextView {

    private final static String CONDENSED_FONT = "fonts/Yantramanav_Bold.ttf";

    public CustomTextview_Bold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextview_Bold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextview_Bold(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), CONDENSED_FONT);
        setTypeface(tf);
    }


}
