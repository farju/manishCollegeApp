package com.example.hp_pc.collegeapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by HP-PC on 02-08-2017.
 */

public class AssignmentAdapter extends ArrayAdapter {
    private Context context;
    private int layRes;
    private ArrayList<AssignmentPojo> arrayList;
    private LayoutInflater inflater;
    public AssignmentAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<AssignmentPojo> objects) {
        super(context, resource, objects);
        this.context=context;
        this.layRes=resource;
        this.arrayList=objects;

        inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=inflater.inflate(layRes,null);

        TextView textViewTitle=view.findViewById(R.id.textViewTitle);
        TextView textViewDes=view.findViewById(R.id.textViewDes);
        ImageView imageView=view.findViewById(R.id.imageView);
        AssignmentPojo pojo=arrayList.get(position);
        textViewDes.setText(pojo.getAssignment_message());
        textViewTitle.setText(pojo.getAssignment_title());

        Glide.with(context)
                .load(pojo.getAssignment_image_url())
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);
        return view;
    }
}
