package com.servicehub.Custom_Compo;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Nirav on 5/16/2016.
 */
public class CustomTextview_scroll extends TextView {

    private final static String CONDENSED_FONT = "fonts/Yantramanav_Regular.ttf";

    public CustomTextview_scroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        rotate();

    }

    public CustomTextview_scroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        rotate();
    }

    public CustomTextview_scroll(Context context) {
        super(context);
        rotate();
        init();
    }
    private void rotate() {
        // TODO Auto-generated method stub
        setSelected(true);
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), CONDENSED_FONT);
        setTypeface(tf);
        if (!isInEditMode()) {

        }
    }

}
