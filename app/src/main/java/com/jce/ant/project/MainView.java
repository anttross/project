package com.jce.ant.project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Ant on 07-Jan-16.
 */
public class MainView extends View {

    Paint paint = new Paint();
    private Path path;


    private void init (AttributeSet attrs, int defStyle){
        paint = new Paint();
       // xr=yr=x=y = 0;

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        path = new Path();
    }//init

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(5);
        paint.setColor(Color.parseColor("#333333"));
        int w = getWidth();
        int h = getHeight()/3;
        int side= (w<h?w:h)*2/3;

        canvas.drawRect((w-side)/2, 0, (w-side)/2+side, side, paint);
       /* canvas.drawRect((w-side*3/2)/2, h, (w-side*3/2)/2+(side*3/2), h+side, paint);
        canvas.drawRect((w-side)/2, h*2,(w-side)/2+side ,h*2+side*3/2, paint);*/
    }

    public MainView(Context context) {
        super(context);
    }

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
