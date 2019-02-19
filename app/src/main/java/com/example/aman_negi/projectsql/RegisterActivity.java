package com.example.aman_negi.projectsql;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtname, edtuname, edtemail, edtphone, edtpassword, edtconfirmPassword;
    private ImageView imageView;
    private MyDatabaseHelper myDatabaseHelper;
    TextWatcher textWatcher;
    SQLiteDatabase db;
    Bitmap bitmap;
    boolean imageAdded = false;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");

        imageView = findViewById(R.id.regImage);
        edtname = findViewById(R.id.regName);
        edtuname = findViewById(R.id.regUname);
        edtemail = findViewById(R.id.regEmail);
        edtphone = findViewById(R.id.regPhone);
        edtpassword = findViewById(R.id.regPsswd);
        edtconfirmPassword = findViewById(R.id.regCfPsswd);

        myDatabaseHelper = new MyDatabaseHelper(this);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                db = myDatabaseHelper.getReadableDatabase();
                String[] columns = {MyDatabaseHelper.UID};
                String whereClause = MyDatabaseHelper.USERNAME + "=?";
                String[] whereArgs = {edtuname.getText().toString().trim()};
                Cursor cursor = db.query(MyDatabaseHelper.TABLE_NAME, columns, whereClause, whereArgs, null, null, null);
                if (cursor.getCount() > 0) {
                    edtuname.setError("Username not available");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        edtuname.addTextChangedListener(textWatcher);

    }

    public void registerButton(View view) {
        db = myDatabaseHelper.getWritableDatabase();
        Bitmap image;
        //check for if register is clicked without an image
        if (imageAdded) {
            image = bitmap;
        } else {
            image = BitmapFactory.decodeResource(getResources(), R.drawable.add_image);
        }
        byte[] rimage = MyBitmapHandler.getBytes(image);
        String rname = edtname.getText().toString().trim();
        String runame = edtuname.getText().toString().trim();
        String remail = edtemail.getText().toString().trim();
        String rphone = edtphone.getText().toString().trim();
        String rpassword = edtpassword.getText().toString().trim();
        String rconfirmPassword = edtconfirmPassword.getText().toString().trim();

        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        //check for empty fields
        if (!rname.equals("") && !runame.equals("") && !remail.equals("") && !rphone.equals("") && !rpassword.equals("") && !rconfirmPassword.equals("")) {
            //check availability of username
            String[] columns = {MyDatabaseHelper.UID, MyDatabaseHelper.NAME};
            String whereClause = MyDatabaseHelper.USERNAME + "=?";
            String[] whereArgs = {runame};
            Cursor cursor = db.query(MyDatabaseHelper.TABLE_NAME, columns, whereClause, whereArgs, null, null, null);
            if (cursor.getCount() <= 0) {
                //check for confirm password
                if (rpassword.equals(rconfirmPassword)) {
                    //insert data in database
                    ContentValues cv = new ContentValues();
                    cv.put(MyDatabaseHelper.NAME, rname);
                    cv.put(MyDatabaseHelper.IMAGE, rimage);
                    cv.put(MyDatabaseHelper.USERNAME, runame);
                    cv.put(MyDatabaseHelper.EMAIL, remail);
                    cv.put(MyDatabaseHelper.PHONE, rphone);
                    cv.put(MyDatabaseHelper.PASSWORD, rpassword);
                    long in = db.insert(MyDatabaseHelper.TABLE_NAME, null, cv);
                    //success toast on id != -1
                    if (in == -1) {
                        FancyToast.makeText(this, "Resgistration Unsuccessful", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    } else {
                        FancyToast.makeText(this, "Resgistration Successful", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                    }
                    finish();
                } else {
                    //for shake animation on password mismatch
                    edtpassword.startAnimation(shake);
                    edtconfirmPassword.startAnimation(shake);
                    edtconfirmPassword.setError("Password do not match");
                }
            } else {
                //username not available toast
                edtuname.startAnimation(shake);
                edtuname.setText("");
                FancyToast.makeText(this, "Username not available", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
            cursor.close();
        } else {
            //warning toast
            FancyToast.makeText(this, "Some fields are empty", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            //edittexts shake
            if (rname.equals("")) {
                edtname.startAnimation(shake);
                edtname.setError("Field cannot be empty");
            }
            if (runame.equals("")) {
                edtuname.startAnimation(shake);
                edtuname.setError("Field cannot be empty");
            }
            if (remail.equals("")) {
                edtemail.startAnimation(shake);
                edtemail.setError("Field cannot be empty");
            }
            if (rphone.equals("")) {
                edtphone.startAnimation(shake);
                edtphone.setError("Field cannot be empty");
            }
            if (rpassword.equals("")) {
                edtpassword.startAnimation(shake);
                edtpassword.setError("Field cannot be empty");
            }
            if (rconfirmPassword.equals("")) {
                edtconfirmPassword.startAnimation(shake);
                edtconfirmPassword.setError("Field cannot be empty");
            }
        }

    }

    public void setImage(View view) {
        FancyToast.makeText(this, "Select a profile pictue", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
                imageAdded = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
