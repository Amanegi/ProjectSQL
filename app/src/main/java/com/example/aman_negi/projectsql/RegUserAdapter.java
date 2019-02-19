package com.example.aman_negi.projectsql;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RegUserAdapter extends RecyclerView.Adapter<RegUserAdapter.MyViewHolder> {
    Context context;
    private ArrayList<RowLayout> rowLayoutArrayList;

    public RegUserAdapter(Context context, ArrayList<RowLayout> rowLayoutArrayList) {
        this.context = context;
        this.rowLayoutArrayList = rowLayoutArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RowLayout r = rowLayoutArrayList.get(position);
        //change image from byte array to bitmap
        Bitmap bitmapImage = MyBitmapHandler.getImage(r.getRowImage());
        holder.imageView.setImageBitmap(bitmapImage);
        holder.txtName.setText(r.getRowName());
        holder.txtEmail.setText(r.getRowEmail());
        holder.txtPhone.setText(r.getRowPhone());
    }

    @Override
    public int getItemCount() {
        return rowLayoutArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtName, txtEmail, txtPhone;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rowImage);
            txtName = itemView.findViewById(R.id.rowName);
            txtEmail = itemView.findViewById(R.id.rowEmail);
            txtPhone = itemView.findViewById(R.id.rowPhone);
        }
    }
}
