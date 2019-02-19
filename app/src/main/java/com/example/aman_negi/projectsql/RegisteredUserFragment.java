package com.example.aman_negi.projectsql;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class RegisteredUserFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyDatabaseHelper myDatabaseHelper;
    SQLiteDatabase db;
    RowLayout rowLayout;
    ArrayList arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registered_user, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList();

        myDatabaseHelper = new MyDatabaseHelper(getContext());
        db = myDatabaseHelper.getReadableDatabase();

        String[] columns = {MyDatabaseHelper.IMAGE, MyDatabaseHelper.NAME, MyDatabaseHelper.PHONE, MyDatabaseHelper.EMAIL};
        Cursor cursor = db.query(MyDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            //get image and change it to bitmap
            byte[] bytesImage = cursor.getBlob(cursor.getColumnIndex(MyDatabaseHelper.IMAGE));
            String name = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.NAME));
            String phone = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.PHONE));
            String email = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.EMAIL));

            rowLayout = new RowLayout(bytesImage, name, email, phone);
            arrayList.add(rowLayout);
        }

        RegUserAdapter adapter = new RegUserAdapter(getContext(), arrayList);
        recyclerView.setAdapter(adapter);

        return view;
    }

}
