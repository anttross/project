package com.jce.ant.project;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class EditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    EditView view;
    EditViewMask mask;
    ImageView imgView;
    ImageButton addBtn;
    EditText addEditText;
    Button addTxt;
    ZoomControls zoom;
    private Spinner textSizeChange;
    private static final String[] paths = {"25", "30", "35", "45"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Log.e("TAG", "error");
        view = new EditView(this);
        view = (EditView) findViewById(R.id.editView);
        mask = new EditViewMask(this);
        mask = (EditViewMask) findViewById(R.id.editViewMask);

        imgView = (ImageView) findViewById(R.id.imageView);
        zoom = (ZoomControls) findViewById(R.id.zoomControls);

        addEditText = (EditText)findViewById(R.id.addTextView);
        addTxt = (Button)findViewById(R.id.addText);
        addTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addEditText.setVisibility(View.VISIBLE);
            }
        });

        addBtn = (ImageButton) findViewById(R.id.addPic);
        addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadImagefromGallery(view);

            }
        });

        textSizeChange = (Spinner)findViewById(R.id.textSizeSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textSizeChange.setAdapter(adapter);
        textSizeChange.setOnItemSelectedListener(this);


    /*  addEditText.setOnLongClickListener(new View.OnLongClickListener() {


          @Override
          public boolean onLongClick(View v) {

              System.out.println("### long click");
             // addEditText.getEditableText();
              return true;
          }
      });*/

/*
            //text move
            addEditText.setOnTouchListener(new View.OnTouchListener() {
                PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
                PointF StartPT = new PointF(); // Record Start Position of 'img'

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int eid = event.getAction();
                    switch (eid) {
                        case MotionEvent.ACTION_MOVE:
                            PointF mv = new PointF(event.getX() - DownPT.x, event.getY() - DownPT.y);
                            addEditText.setX((int) (StartPT.x + mv.x));
                            addEditText.setY((int) (StartPT.y + mv.y));
                            StartPT = new PointF(addEditText.getX(), addEditText.getY());
                            break;
                        case MotionEvent.ACTION_DOWN:
                            DownPT.x = event.getX();
                            DownPT.y = event.getY();
                            StartPT = new PointF(addEditText.getX(), addEditText.getY());
                            break;
                        case MotionEvent.ACTION_UP:
                            // Nothing have to do
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });*/


        imgView.setOnTouchListener(new View.OnTouchListener() {
            PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
            PointF StartPT = new PointF(); // Record Start Position of 'img'

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eid = event.getAction();
                switch (eid) {
                    case MotionEvent.ACTION_MOVE:
                        PointF mv = new PointF(event.getX() - DownPT.x, event.getY() - DownPT.y);
                        imgView.setX((int) (StartPT.x + mv.x));
                        imgView.setY((int) (StartPT.y + mv.y));
                        StartPT = new PointF(imgView.getX(), imgView.getY());
                        break;
                    case MotionEvent.ACTION_DOWN:
                        DownPT.x = event.getX();
                        DownPT.y = event.getY();
                        StartPT = new PointF(imgView.getX(), imgView.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        // Nothing have to do
                        break;
                    default:
                        break;
                }
                return true;
            }
        });


        zoom.setOnZoomInClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                float x = imgView.getScaleX();
                float y = imgView.getScaleY();

                imgView.setScaleX((float) (x + 0.1));
                imgView.setScaleY((float) (y + 0.1));
            }
        });

        zoom.setOnZoomOutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                float x = imgView.getScaleX();
                float y = imgView.getScaleY();

                imgView.setScaleX((float) (x - 0.1));
                imgView.setScaleY((float) (y - 0.1));
            }
        });

    }

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.imageView);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

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
}
