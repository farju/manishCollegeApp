package com.example.hp_pc.collegeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences preferences=getSharedPreferences("myFile",MODE_PRIVATE);
        if(preferences.getBoolean("isLogin",false))
        {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        init();
    }

    private void init() {
        editTextEmail= (EditText) findViewById(R.id.email);
        editTextPass= (EditText) findViewById(R.id.password);
    }
    public void login(View view)
    {
        String enterEmail=editTextEmail.getText().toString();
        String enterPass=editTextPass.getText().toString();

        if(enterEmail.equals("") || enterPass.equals(""))
            Toast.makeText(this, "All Fields Required", Toast.LENGTH_SHORT).show();
        else
        {
            HashMap<String,String> map= new HashMap<>();
            map.put("email",enterEmail);
            map.put("password",enterPass);
            callLoginService(map);
        }
    }

    private void callLoginService(final HashMap<String, String> map) {

        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();

        StringRequest request=new StringRequest(Request.Method.POST, URLHelper.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        handleLogin(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void handleLogin(String response) {
        try {
            JSONObject object=new JSONObject(response);
            if(object.getString("result").equals("1"))
            {
                Toast.makeText(this,object.getString("msg"), Toast.LENGTH_SHORT).show();
                JSONObject infoObject=object.getJSONObject("info");
                SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
                SharedPreferences.Editor editor=preference.edit();
                editor.putString("info",infoObject.toString());
                editor.putBoolean("isLogin",true);
                editor.commit();

                startActivity(new Intent(this,MainActivity.class));
                finish();
            }
            else
                Toast.makeText(this,object.getString("msg"), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void register(View v)
    {
        Intent i=new Intent(this,RegisterActivity.class);
        startActivity(i);
    }
}
