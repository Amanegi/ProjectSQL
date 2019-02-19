package com.example.aman_negi.projectsql;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

public class MyProfileFragment extends Fragment {
    private Button btnUpdate, btnDelete;
    private TextView profileName, profileUsername, profilePhone, profileEmail;
    private ImageView profileImage;
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase db;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        profileImage = view.findViewById(R.id.profileImage);
        profileName = view.findViewById(R.id.profileName);
        profileUsername = view.findViewById(R.id.profileUsername);
        profilePhone = view.findViewById(R.id.profilePhone);
        profileEmail = view.findViewById(R.id.profileEmail);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnDelete = view.findViewById(R.id.btnDelete);

        myDatabaseHelper = new MyDatabaseHelper(getContext());
        db = myDatabaseHelper.getReadableDatabase();
        sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String prefUsername = sharedPreferences.getString("username_key", null);

        //reading database for required value
        String whereClause = MyDatabaseHelper.USERNAME + "=?";
        String[] whereArgs = {prefUsername};
        Cursor cursor = db.query(MyDatabaseHelper.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
        //move to first value of cursor
        cursor.moveToFirst();
        byte[] myImage = cursor.getBlob(cursor.getColumnIndex(MyDatabaseHelper.IMAGE));
        String myName = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.NAME));
        final String myUsername = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.USERNAME));
        String myPhone = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.PHONE));
        String myEmail = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.EMAIL));

        //setting the data to textviews
        profileImage.setImageBitmap(MyBitmapHandler.getImage(myImage));
        profileName.setText(myName);
        profileUsername.setText(myUsername);
        profilePhone.setText(myPhone);
        profileEmail.setText(myEmail);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Delete Account");
                alertDialog.setMessage("Are you sure want to delete your account?");
                alertDialog.setPositiveButtonIcon(getResources().getDrawable(R.drawable.round_done_white_18dp));
                alertDialog.setNegativeButtonIcon(getResources().getDrawable(R.drawable.round_close_white_18dp));
                //listners for dialog response
                alertDialog.setPositiveButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete from database
                        db = myDatabaseHelper.getWritableDatabase();
                        String whereClause = MyDatabaseHelper.USERNAME + "=?";
                        String[] whereArgs = {myUsername};
                        int d = db.delete(MyDatabaseHelper.TABLE_NAME, whereClause, whereArgs);
                        if (d == -1) {
                            FancyToast.makeText(getContext(), "Error While Deleting Account", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        } else {
                            FancyToast.makeText(getContext(), "Account Deleted Successfully", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            editor = sharedPreferences.edit();
                            editor.remove("username_key");
                            editor.apply();
                            startActivity(new Intent(getContext(), LoginActivity.class));
                            getActivity().finish();
                        }

                    }
                });
                alertDialog.setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.create().show();

            }
        });

        return view;
    }

}
