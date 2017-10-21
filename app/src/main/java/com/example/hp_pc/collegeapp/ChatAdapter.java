package com.example.hp_pc.collegeapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HP-PC on 06-08-2017.
 */

public class ChatAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<MessagePojo> arrayList;
    private LayoutInflater inflater;
    private String loggedInUserName="";
    public ChatAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<MessagePojo> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.arrayList=objects;
        inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        SharedPreferences preferences=context.getSharedPreferences("myFile",Context.MODE_PRIVATE);
        String info=preferences.getString("info","");
        if(!info.equals(""))
        {
            try {
                JSONObject object=new JSONObject(info);
                loggedInUserName=object.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MessagePojo pojo=arrayList.get(position);
        View view;
        if(loggedInUserName.equals(pojo.getName()))
            view=inflater.inflate(R.layout.chat_item_self,null);
        else
        view=inflater.inflate(R.layout.chat_item_other,null);
        TextView message=view.findViewById(R.id.message);
        TextView timeStamp=view.findViewById(R.id.timestamp);

        message.setText(pojo.getName()+"\n"+pojo.getMessahe());
        timeStamp.setText(pojo.getAdded_date());
        return view;
    }
}
