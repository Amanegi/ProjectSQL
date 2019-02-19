package com.example.aman_negi.projectsql;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin, btnRegister;
    private EditText uname, psswd;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    MyDatabaseHelper myDatabaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.loginBtnRegister);
        uname = findViewById(R.id.loginUname);
        psswd = findViewById(R.id.loginPsswd);

        myDatabaseHelper = new MyDatabaseHelper(this);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                db = myDatabaseHelper.getReadableDatabase();
                String stringUsername = uname.getText().toString().trim().toLowerCase();
                String stringPassword = psswd.getText().toString().trim();
                //check for empty field
                if (!stringUsername.equals("") && !stringPassword.equals("")) {
                    String[] columns = {MyDatabaseHelper.USERNAME, MyDatabaseHelper.PASSWORD, MyDatabaseHelper.NAME};
                    String whereClause = MyDatabaseHelper.USERNAME + "=?";
                    String[] whereArgs = {stringUsername};
                    Cursor cursor = db.query(MyDatabaseHelper.TABLE_NAME, columns, whereClause, whereArgs, null, null, null);
                    //check for existence of username
                    if (cursor.moveToFirst()) {
                        String myUsername = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.USERNAME));
                        String myPassword = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.PASSWORD));
                        String myName = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.NAME));
                        if (stringUsername.equals(myUsername) && stringPassword.equals(myPassword)) {
                            editor.putString("username_key", myUsername);
                            editor.putString("name_key", myName);
                            editor.putBoolean("flagLoginCalled", true);
                            editor.apply();
                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                            finish();
                        } else if (!stringUsername.equals(myUsername)) {
                            FancyToast.makeText(LoginActivity.this, "Incorrect Username", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            uname.startAnimation(shake);
                            uname.setError("Incorrect Username");
                        } else if (!stringPassword.equals(myPassword)) {
                            FancyToast.makeText(LoginActivity.this, "Incorrect Password", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            psswd.startAnimation(shake);
                            psswd.setError("Incorrect Password");
                        }
                    } else {
                        FancyToast.makeText(LoginActivity.this, "User Not Registered", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                    cursor.close();
                } else {
                    if (stringUsername.equals("")) {
                        uname.startAnimation(shake);
                        uname.setError("Field Empty");
                    }
                    if (stringPassword.equals("")) {
                        psswd.startAnimation(shake);
                        psswd.setError("Field Empty");
                    }
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}
