package com.example.antoine.deadornotdead.wiki;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Antoine on 24/12/2017.
 */

public class PersonneView extends LinearLayout {


    public PersonneView(Context context) {
        super(context);
        init();
    }

    public PersonneView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public PersonneView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        /*inflate(getContext(), R.layout.card, this);
        this.header = (TextView)findViewById(R.id.header);
        this.description = (TextView)findViewById(R.id.description);
        this.thumbnail = (ImageView)findViewById(R.id.thumbnail);
        this.icon = (ImageView)findViewById(R.id.icon);*/
    }
}
