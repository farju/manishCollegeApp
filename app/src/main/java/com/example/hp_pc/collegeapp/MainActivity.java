package com.example.hp_pc.collegeapp;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    ListView listView;
    private ArrayList<AssignmentPojo> arrayList=new ArrayList<>();
    private AssignmentAdapter assignmentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        assignmentAdapter=new AssignmentAdapter(this,R.layout.assignment_list_item,arrayList);
        listView.setAdapter(assignmentAdapter);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setStackFromBottom(true);
        SharedPreferences sharedPreferences=getSharedPreferences("myFile",MODE_PRIVATE);
        String info=sharedPreferences.getString("info","");
        if(!info.equals(""))
        {
            try {
                JSONObject object=new JSONObject(info);
                String college_id=object.getString("college_id");
                String college_year = object.getString("college_year");
                String college_branch = object.getString("college_branch");
                String college_section = object.getString("college_section");

                HashMap<String,String> map=new HashMap<>();
                map.put("college_id",college_id);
                map.put("college_year", college_year);
                map.put("college_branch", college_branch);
                map.put("college_section", college_section);

                callGetAssignmentService(map);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void callGetAssignmentService(final HashMap<String, String> map) {
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.show();

        StringRequest request=new StringRequest(Request.Method.POST, URLHelper.GETCOLLEGEASSIGNMENT,
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
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

            JSONArray array=object.getJSONArray("item");
            for(int i=0;i<array.length();i++)
            {
                JSONObject arrObj=array.getJSONObject(i);


                String assignment_id = arrObj.getString("assignment_id");
                String assignment_title = arrObj.getString("assignment_title");
                String assignment_message = arrObj.getString("assignment_message");
                String assignment_image_url = arrObj.getString("assignment_image_url");

                AssignmentPojo pojo = new AssignmentPojo();
                pojo.setAssignment_id(assignment_id);
                pojo.setAssignment_image_url(assignment_image_url);
                pojo.setAssignment_message(assignment_message);
                pojo.setAssignment_title(assignment_title);

                arrayList.add(pojo);
            }
            assignmentAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this,ChatRoomActivity.class));
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        listView= (ListView) findViewById(R.id.listView);
    }
    public void AddAssignment(View view)
    {
        startActivity(new Intent(this,Add_assignment.class));
    }


}
