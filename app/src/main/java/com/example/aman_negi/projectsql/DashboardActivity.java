package com.example.aman_negi.projectsql;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    ImageView navUserImage;
    SharedPreferences.Editor editor;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ListView navListView;
    TextView navTxtName;
    ActionBarDrawerToggle actionBarDrawerToggle;
    SQLiteDatabase db;
    MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setTitle("Dashboard");
        navUserImage = findViewById(R.id.navImageView);

        //getting username from shared prefrences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String prefUsername = sharedPreferences.getString("username_key", null);
        String prefName = sharedPreferences.getString("name_key", null);
        Boolean flagLoginCalled = sharedPreferences.getBoolean("flagLoginCalled", false);

        //setting image to navigation drawer
        myDatabaseHelper = new MyDatabaseHelper(this);
        db = myDatabaseHelper.getReadableDatabase();
        String[] columns = {MyDatabaseHelper.IMAGE};
        String whereClause = MyDatabaseHelper.USERNAME + "=?";
        String[] whereArgs = {prefUsername};
        Cursor cursor = db.query(MyDatabaseHelper.TABLE_NAME, columns, whereClause, whereArgs, null, null, null);
        //move to first value of cursor
        cursor.moveToFirst();
        byte[] byteImage = cursor.getBlob(cursor.getColumnIndex(MyDatabaseHelper.IMAGE));
        Bitmap bitmapImage = MyBitmapHandler.getImage(byteImage);
        navUserImage.setImageBitmap(bitmapImage);
        cursor.close();

        //show snackbar only if login activity was called
        if (flagLoginCalled) {
            String snackbarMsg = "Login Credentials Saved";
            Snackbar.make(findViewById(R.id.frameLayout), Html.fromHtml("<font color=\"#ff4081\"><b>" + snackbarMsg + "</b></font>"), Snackbar.LENGTH_SHORT).show();
        }

        drawerLayout = findViewById(R.id.drawerlayout);
        toolbar = findViewById(R.id.toolbar);
        navListView = findViewById(R.id.navListView);
        navTxtName = findViewById(R.id.navTextName);

        //set toolbar as action bar
        setSupportActionBar(toolbar);
        //shows the back button on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setting name to navigation bar textview
        navTxtName.setText(prefName);

        //populating navigation listview
        final String[] navItems = {"My Profile", "Registered Users", "Logout"};
        ArrayAdapter navListViewAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, navItems);
        navListView.setAdapter(navListViewAdapter);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawerOpened, R.string.drawerClosed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        navListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = navItems[position];
                selectItem(position, item);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new MyProfileFragment();
        fragmentTransaction.add(R.id.frameLayout, fragment);
        fragmentTransaction.commit();

    }

    private void selectItem(int position, String itemName) {
        final AlertDialog.Builder alertDialog;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new MyProfileFragment();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
                setTitle(itemName);
                break;
            case 1:
                fragment = new RegisteredUserFragment();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
                setTitle(itemName);
                break;
            case 2:
                alertDialog = new AlertDialog.Builder(DashboardActivity.this);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Logout");
                alertDialog.setMessage("Are you sure want to logout?");
                alertDialog.setPositiveButtonIcon(getResources().getDrawable(R.drawable.round_done_white_18dp));
                alertDialog.setNegativeButtonIcon(getResources().getDrawable(R.drawable.round_close_white_18dp));
                //listners for dialog response
                alertDialog.setPositiveButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor = sharedPreferences.edit();
                        editor.remove("username_key");
                        editor.apply();
                        startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                        finish();
                    }
                });
                alertDialog.setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.create().show();
                break;
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void NavLinearClick(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder backAlertDialog = new AlertDialog.Builder(DashboardActivity.this);
        backAlertDialog.setCancelable(false);
        backAlertDialog.setTitle("Exit");
        backAlertDialog.setMessage("Do you want to exit?");
        backAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DashboardActivity.super.onBackPressed();
            }
        });
        backAlertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        backAlertDialog.create().show();
    }
}
