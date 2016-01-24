package com.jce.ant.project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Spinner;

/**
 * Created by Ant on 08-Jan-16.
 */
public class EditView extends View {
    Paint paint = new Paint();//(Paint.ANTI_ALIAS_FLAG);
    private Path path;
    int border=40;


    private void init (AttributeSet attrs, int defStyle){
       // paint = new Paint();
        // xr=yr=x=y = 0;

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        path = new Path();
    }//init

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(5);
        paint.setColor(Color.parseColor("#CC333333"));
        int w = getWidth()-border;
        int h = getHeight()-border;
        int side= (w<h?w:h);

        //canvas.drawRect((w-side)/2, 0, (w-side)/2+side, side*3/2, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(border);
        canvas.drawRect((w - side) / 2 + (border / 2), border / 2, (w - side) / 2 + side + (border / 2), side + (border / 2), paint);



    }



    public EditView(Context context) {
        super(context);
    }

    public EditView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
