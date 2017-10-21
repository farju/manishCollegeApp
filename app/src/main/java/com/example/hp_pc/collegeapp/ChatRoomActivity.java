package com.example.hp_pc.collegeapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<MessagePojo> arrayList=new ArrayList<>();
    private ChatAdapter chatAdapter;
    private EditText messageEditText;
    private String student_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        init();
        chatAdapter=new ChatAdapter(this,R.layout.chat_item_other,arrayList);
        listView.setAdapter(chatAdapter);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setStackFromBottom(true);
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        String info=preference.getString("info","");
        if(!info.equals("")){
            try {
                JSONObject object=new JSONObject(info);
                student_id=object.getString("student_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        HashMap<String,String> map=new HashMap<>();
        map.put("student_id",student_id);
        callGetChatRoomMessages(map);
    }

    private void callGetChatRoomMessages(final HashMap<String, String> map) {
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.show();

        StringRequest request=new StringRequest(Request.Method.POST, URLHelper.GETCHATROOMMESSAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        handleMessage(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChatRoomActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void handleMessage(String response) {
        try {
            JSONObject object=new JSONObject(response);
            JSONArray array=object.getJSONArray("item");
            for(int i=0;i<array.length();i++)
            {
                JSONObject arrayObject=array.getJSONObject(i);
                String message_id=arrayObject.getString("message_id");
                String name=arrayObject.getString("name");
                String message=arrayObject.getString("message");
                String added_date=arrayObject.getString("added_date");

                MessagePojo pojo=new MessagePojo();
                pojo.setAdded_date(added_date);
                pojo.setMessage_id(message_id);
                pojo.setMessahe(message);
                pojo.setName(name);

                arrayList.add(pojo);
            }
            chatAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        listView= (ListView) findViewById(R.id.listView);
        messageEditText= (EditText) findViewById(R.id.messageEditText);
    }

    public void doChat(View view) {
        String enterMessage=messageEditText.getText().toString();
        if(enterMessage.equals(""))
            Toast.makeText(this, "Enter Some Text", Toast.LENGTH_SHORT).show();
        else
        {
            HashMap<String,String> map=new HashMap<>();
            map.put("sender_id",student_id);
            map.put("message",enterMessage);
            callDoChatService(map);
        }
    }

    private void callDoChatService(final HashMap<String, String> map) {

        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.show();
        StringRequest request=new StringRequest(Request.Method.POST, URLHelper.DOCHAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        handleChat(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChatRoomActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void handleChat(String response) {
        try {
            JSONObject object = new JSONObject(response);
            String result=object.getString("result");
            if(result.equals("1")){
                Toast.makeText(this, "Sent", Toast.LENGTH_SHORT).show();
                messageEditText.setText("");
                chatAdapter.notifyDataSetChanged();
            }
            else
                Toast.makeText(this, "Not Sent", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}