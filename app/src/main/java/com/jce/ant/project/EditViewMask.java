package com.jce.ant.project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Ant on 08-Jan-16.
 */
public class EditViewMask extends View {
    Paint paint = new Paint();//(Paint.ANTI_ALIAS_FLAG);
    private Path path;
    int border=40;

    private void init (AttributeSet attrs, int defStyle){
       // paint = new Paint();
        // xr=yr=x=y = 0;

        paint.setStyle(Paint.Style.FILL);
        path = new Path();
    }//init

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*int w = getWidth()-border;
        int h = getHeight()-border;
        int side= (w<h?w:h);*//*
        EditActivity pic = new EditActivity();
*/
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);
        paint.setColor(Color.parseColor("#ffffffff"));

        //canvas.drawRect(0,0, getWidth(), 110, paint);
        canvas.drawRect(0,getWidth(), getWidth(), this.getHeight(), paint);


    }



    public EditViewMask(Context context) {
        super(context);
    }

    public EditViewMask(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditViewMask(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
