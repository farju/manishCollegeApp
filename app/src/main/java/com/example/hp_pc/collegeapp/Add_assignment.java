package com.example.hp_pc.collegeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Add_assignment extends AppCompatActivity {

    private EditText titleEditText,messageEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);
        init();
    }

    private void init() {
        titleEditText= (EditText) findViewById(R.id.title);
        messageEditText= (EditText) findViewById(R.id.message);
    }

    public void addAssignment(View view) {
        String ennterTitle=titleEditText.getText().toString();
        String entermessage=messageEditText.getText().toString();
        if(entermessage.equals("") || ennterTitle.equals(""))
            Toast.makeText(this, "All required", Toast.LENGTH_SHORT).show();
        else
        {
            SharedPreferences sharedPreference =getSharedPreferences("myFile",MODE_PRIVATE);
            String info=sharedPreference.getString("info","");
            if(!info.equals(""))
            {
                try {
                    JSONObject object=new JSONObject(info);
                    String college_id = object.getString("college_id");
                    String college_year = object.getString("college_year");
                    String college_branch = object.getString("college_branch");
                    String college_section = object.getString("college_section");

                    HashMap<String, String> map = new HashMap<>();
                    map.put("college_id", college_id);
                    map.put("college_year", college_year);
                    map.put("college_branch", college_branch);
                    map.put("college_section", college_section);
                    map.put("title", ennterTitle);
                    map.put("message", entermessage);
                    map.put("image", "");
                    
                    callAddAssignmentService(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void callAddAssignmentService(final HashMap<String, String> map) {
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.show();
        StringRequest request=new StringRequest(Request.Method.POST, URLHelper.ADDASSIGNMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        parseResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                Toast.makeText(Add_assignment.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void parseResponse(String response) {
        try {
            JSONObject object=new JSONObject(response);

            String result=object.getString("result");
            if(result.equals("1"))
            {
                startActivity(new Intent(this,MainActivity.class));
                finish();
            }
            Toast.makeText(this, object.getString("msg"), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
