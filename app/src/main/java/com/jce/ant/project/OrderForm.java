package com.jce.ant.project;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
//import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class OrderForm extends AppCompatActivity {
    EditText name, mail, phone, address, city, zip, pob;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    // public static final String shPref = "sharedPrefs";
    Button placeOrder;
    /*String fileName;
    int fileSize, top, left;
    float size, angle;
    byte[] fileBody;

    public OrderForm(String FileName, int FileSize, int Top, int Left, float Size, float Angle, byte[] FileBody){
        this.fileName=FileName;
        this.

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);



        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        city = (EditText) findViewById(R.id.city);
        zip = (EditText) findViewById(R.id.zip);
        pob = (EditText) findViewById(R.id.pob);
        mail = (EditText) findViewById(R.id.mail);
        phone = (EditText) findViewById(R.id.phone);

        prefs = getSharedPreferences("shPref", Context.MODE_PRIVATE);
        editor = prefs.edit();

        name.setText(prefs.getString("name", name.getText().toString()));
        address.setText(prefs.getString("address", address.getText().toString()));
        city.setText(prefs.getString("city", city.getText().toString()));
        zip.setText(prefs.getString("zip", zip.getText().toString()));
        pob.setText(prefs.getString("pob", pob.getText().toString()));
        mail.setText(prefs.getString("mail", mail.getText().toString()));
        phone.setText(prefs.getString("phone", phone.getText().toString()));




        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8
        // Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                //String possibleEmail = account.name;
                mail.setText(account.name);

            }
        }

        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        // phone.setText();
       /* TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();*/




        placeOrder = (Button)findViewById(R.id.send);
        placeOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                editor.putString("name", name.getText().toString());
                editor.putString("address", address.getText().toString());
                editor.putString("city", city.getText().toString());
                editor.putString("zip", zip.getText().toString());
                editor.putString("pob", pob.getText().toString());
                editor.putString("mail", mail.getText().toString());
                editor.putString("phone", phone.getText().toString());
                editor.apply();
                //String call="DESKTOP-BUV55UA", db="Canvas_DB", un="", passwords="";
                // String url = "jdbc:sqlserver://localhost:1433;databasename=Canvas_DB;integratedSecurity=true";

                String call="62.219.78.230", db="antont_co_il_canvas", un="antont_co_il_user", passwords="31A5959nt";
                Connection connect = CONN(un, passwords, db, call);
                //rs;

                try {
                    //PreparedStatement statement = connect.prepareStatement(BuildProcedureWithParameters());
                    //final ArrayList list = new ArrayList();
                    final String statement = BuildProcedureWithParameters();
                    //java.sql.DatabaseMetaData dm = connect.getMetaData();
                    Statement select = connect.createStatement();
                    ResultSet rs = null;
                    rs = select.executeQuery(statement);
                    String res = "no result";
                    if(rs!=null && rs.next())
                        res = rs.getString(1);

                    // String res = rs.getString(1);
                    //while (rs.next()) {
                    //    list.add(rs.getString("ORDER_ID"));
                    // }
                    Toast.makeText(OrderForm.this, res,
                            Toast.LENGTH_LONG).show();

                } catch (SQLException e) {
                    Toast.makeText(OrderForm.this, e.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    private String BuildProcedureWithParameters() {
        Bundle b = getIntent().getExtras();
        if(b==null)
            return "error";

        StringBuilder query = new StringBuilder();
        query.append(
                "DECLARE @Pct_Tbl [antont_co_il_user].[PICTURES_TBL]" +
                        "\nINSERT INTO @Pct_Tbl ("+
                        "[FileName],\n" +
                        "\t[FileBody],\n" +
                        "\t[FileSize],\n" +
                        "\t[Size],\n" +
                        "\t[Top],\n" +
                        "\t[Left],\n" +
                        "\t[Angle] )\n" +
                        " VALUES('"+ String.valueOf(b.getString("ImgName")) + "',\n" +
                        "\t'" + String.valueOf(getImgBody(b.getString("ImgPath"))) + "',\n" +
                        "\t" + String.valueOf(b.getLong("ImgFileSize")) + ",\n" +
                        "\t'" + String.valueOf(b.getFloat("ImgSize")) + "',\n" +
                        "\t" + String.valueOf(b.getFloat("ImgTop")) + ",\n" +
                        "\t" + String.valueOf(b.getFloat("ImgLeft")) + ",\n" +
                        "\t'" + String.valueOf(b.getFloat("ImgAngle")) + "'\n" +
                        "\t)\n");
        query.append(
                "DECLARE @Txt_Tbl [antont_co_il_user].[TEXTS_TBL]" +
                        "\nINSERT INTO @Txt_Tbl ("+
                        "[Font],\n" +
                        "\t[Size],\n" +
                        "\t[Color],\n" +
                        "\t[Top],\n" +
                        "\t[Left],\n" +
                        "\t[Angle],\n" +
                        "\t[Body] )\n" +
                        " VALUES('Ariel'" + ",\n" +
                        "\t" + "20" + ",\n" +
                        "\t" + "'Red'" + ",\n" +
                        "\t" + "10" + ",\n" +
                        "\t" + "50" + ",\n" +
                        "\t" + "0" + ",\n" +
                        "\t" + "'Hi'" + "\n" +
                        "\t)\n");
//string eMail, string phone, string address, string fullName, string city, int zip, int POB, int amount, int pattern, int delivery,
        query.append("EXEC [dbo].[Canvas_DB_Insert_ALL] '"+mail.getText()+"', '"+phone.getText()+"', '"
                +address.getText()+"', '"+name.getText() +"', '"+city.getText() +"', "
                +zip.getText()+", "+pob.getText()+", "+1+", "
                +1+", "+1+", "+String.valueOf(b.getInt("ScreenW"))+", "+String.valueOf(b.getInt("ScreenH"))+", @Pct_Tbl, @Txt_Tbl");

/*
    query.append("EXEC Canvas_DB_Insert_ALL 'abc@bca.com', '050505050', 'me and app', 'me', 'jerus', 99999, 1230, 1, 1, 1, @Pct_Tbl, @Txt_Tbl");
*/

        String resTemp = String.valueOf(query);
        return resTemp;
    }

    private Connection CONN(String _user, String _pass, String _DB,
                            String _server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

            /*ConnURL = "jdbc:jtds:sqlserver://" + _server + ";"
                    + "databaseName=" + _DB + ";user=" + _user + ";password="
                    + _pass + ";";*/
/*            ConnURL = "jdbc:jtds:sqlserver://" + _server + "//" + _DB + ";instance=//MSSQLSERVER2014;user=" + _user + ";password="
                    + _pass + ";";*/
            ConnURL = "jdbc:jtds:sqlserver://" + _server + "//" + _DB+ "//MSSQLSERVER2014";
            //ConnURL = "jdbc:jtds:sqlserver://127.0.0.1:1433;databasename=Canvas_DB;integratedSecurity=true";
            DriverManager.setLoginTimeout(0);
            conn = DriverManager.getConnection(ConnURL,_user,_pass);
            //  conn = DriverManager.getConnection(ConnURL);


        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }

    public static byte[] getBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,0, stream);
        return stream.toByteArray();
    }
    public static Bitmap getImage(byte[] image)
    {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public String getImgBody(String uri){
        //  return imgView.getImageMatrix();
        // convertedPath = ;
        // BitmapDrawable drawable = (BitmapDrawable) imgView.getDrawable();
        // Bitmap bitmap = drawable.getBitmap();

        Uri path = Uri.parse(uri);

        Bitmap bitmap = BitmapFactory.decodeFile((uri));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        //String encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);
        String encodedImage=Base64.encodeBytes(imageInByte);
        return encodedImage;
    }
}
