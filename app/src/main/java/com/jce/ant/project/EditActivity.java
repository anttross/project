package com.jce.ant.project;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ZoomControls;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class EditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
    private static final String TAG = "Touch";
    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;


    private static int RESULT_LOAD_IMG = 1;
    EditView view;
    public ImageView imgView;
    String uri;
    ImageButton addBtn;
    EditText addEditText;
    Button addTxt;
    Button orderBtn;
    //  float imgScale=1,tempScale=1,scaledImage ;
    //  int imgTop, imgLeft,oldLeft,oldTop;
    //   int originalWidth, originalHeight;
    //   ImageView originalImageView, scaledImageView;
    //   float fingerTopEnd,fingerLeftEnd,fingerTopStart,fingerLeftStart;

    private Spinner textSizeChange;
    private static final String[] paths = {"25", "30", "35", "45"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Log.e("TAG", "error");
        view = new EditView(this);
        view = (EditView) findViewById(R.id.editView);

       /* mask = new EditViewMask(this);
        mask = (EditViewMask) findViewById(R.id.editViewMask);*/

        imgView = (ImageView) findViewById(R.id.imageView);
        // imgView.setOnTouchListener((View.OnTouchListener) this);
        imgView.setOnTouchListener(this);

        addEditText = (EditText) findViewById(R.id.addTextView);
        addTxt = (Button) findViewById(R.id.addText);
        addTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addEditText.setVisibility(View.VISIBLE);
            }
        });

        orderBtn = (Button) findViewById(R.id.orderBtn);
        orderBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrderForm.class);
                Bundle b = new Bundle();
                //String FileName, int FileSize, int Top, int Left, float Size, float Angle, byte[] FileBody
                b.putString("ImgName", getImgName());
                b.putLong("ImgFileSize", getImgFileSize());
                b.putFloat("ImgTop", getImgTop());
                b.putFloat("ImgLeft", getImgLeft());
                b.putFloat("ImgSize", getImgSize());
                b.putFloat("ImgAngle", getImgAngle());
                // b.putByteArray("ImgBody", getImgBody());
                b.putString("ImgPath", uri);
                b.putInt("ScreenW", getScreenW());
                b.putInt("ScreenH",getScreenH());

                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
                // finish();


                //startActivity(new Intent(getApplicationContext(), OrderForm.class));

            }
        });

        addBtn = (ImageButton) findViewById(R.id.addPic);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMG);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            uri = picturePath;
            cursor.close();
            ImageView imageView = (ImageView) findViewById(R.id.imageView);

            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
           /* //-------
            originScale=BitmapFactory.decodeFile(picturePath).getWidth();
            scaledImage=originScale;
           // originScale=imgView.getLayoutParams().width;
            //-------*/
            //  oldLeft =imgView.getLeft();
            // oldTop=imgView.getTop();
            // imgLeft=oldLeft-32;imgTop=oldTop-32;
            // tempScale=imgView.getScaleX();
            // originalWidth = imageView.getWidth();
            // originalHeight=imageView.getHeight();
            //originalImageView = imageView;
            // scaledImageView=imageView;
        }

        textSizeChange = (Spinner) findViewById(R.id.textSizeSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditActivity.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textSizeChange.setAdapter(adapter);
        textSizeChange.setOnItemSelectedListener(this);


    }




    @Override
    public boolean onTouch(View v, MotionEvent event) {
        imgView = (ImageView) v;
        float scale=1.0F;
        // Dump touch event to log
        dumpEvent(event);

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                //   fingerLeftStart=event.getX();
                //  fingerTopStart=event.getY();
                Log.d(TAG, "mode=DRAG");
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;
            case MotionEvent.ACTION_UP:
                /*        fingerLeftEnd=event.getX();fingerTopEnd= event.getY();

                            imgTop = imgTop + (int) (fingerTopEnd - fingerTopStart);
                            imgLeft = imgLeft + (int) (fingerLeftEnd - fingerLeftStart);
*/
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    // ...
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x,
                            event.getY() - start.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
//                               imgScale=scale;
//                                scaledImage=scaledImage*scale;

                              /*  tempY = imgTop + (int) (fingerTopEnd - fingerTopStart);
                                tempX = imgLeft + (int) (fingerLeftEnd - fingerLeftStart);*/
                                /*imgLeft= (int)(mid.x-(scale*imgView.getWidth()/2));
                                imgTop= (int) (mid.y-(scale*imgView.getHeight()/2));*/
                               /* imgTop = imgTop + (int) (mid.y);
                                imgLeft = imgLeft + (int) (mid.x);*/
                    }
                }
                break;
        }
        imgView.setImageMatrix(matrix);
        //scaledImageView.setImageMatrix(matrix);
        imgView.invalidate();


        // imgScale=imgScale*tempScale;
        // float iii=imgView.getScaleX();
        // imgScale=iii;
/*                int[] location = new int[2];
                imgView.getLocationOnScreen(location);

                tempX= (int) imgView.getPivotX();
                tempY=imgView.getMeasuredHeightAndState();*/


        return true; // indicate event was handled
    }



    /** Show an event in the LogCat view, for debugging */
    private void dumpEvent(MotionEvent event) {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
        Log.d(TAG, sb.toString());
    }

    /** Determine the space between the first two fingers */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    /** Calculate the mid point of the first two fingers */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }


    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                addEditText.setTextSize(Float.parseFloat(paths[0]));
                break;
            case 1:
                addEditText.setTextSize(Float.parseFloat(paths[1]));
                break;
            case 2:
                addEditText.setTextSize(Float.parseFloat(paths[2]));
                break;
            case 3:
                addEditText.setTextSize(Float.parseFloat(paths[3]));
                break;
            default:
                addEditText.setTextSize(Float.parseFloat(paths[1]));
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public byte[] getImgBody(){
        Bitmap bitmap = BitmapFactory.decodeFile((uri));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        return imageInByte;
    }

    public long getImgFileSize(){
        Bitmap bitmap = BitmapFactory.decodeFile((uri));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        //BitmapFactory.decodeFile(uri).compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        long lengthbmp = imageInByte.length;//bitmap.getByteCount();//
        return lengthbmp;
    }
    public String getImgName(){
        return uri.substring(uri.lastIndexOf("/")+1);
    }
    public float getImgSize(){
        float[] s = new float[9];
        matrix.getValues(s);
        float f = s[Matrix.MSCALE_X];
        return imgView.getLayoutParams().width;
    }

    public float getImgTop(){
        RectF r = new RectF();
        matrix.mapRect(r);
        return r.top;
    }

    public float getImgLeft(){
        RectF r = new RectF();
        matrix.mapRect(r);
        return r.left;
    }

    public float getImgAngle(){
        float[] v = new float[9];
        matrix.getValues(v);
        return Math.round(Math.atan2(v[Matrix.MSKEW_X], v[Matrix.MSCALE_X]) * (180 / Math.PI));
    }

    public int getScreenW(){
        return view.getWidth();
    }
    public int getScreenH(){
        return view.getWidth();
    }
}
