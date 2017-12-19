package com.servicehub.Custom_Compo;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Nirav on 5/16/2016.
 */
public class Custom_Edittext extends EditText {

    public Custom_Edittext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Custom_Edittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Custom_Edittext(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Yantramanav_Regular.ttf");
        setTypeface(tf);
    }

}
