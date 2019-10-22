package com.taranita.myblog.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.taranita.myblog.R;
import com.taranita.myblog.storage.SharedPrefManager;
import com.taranita.myblog.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserProfile extends AppCompatActivity {
    private String ID, NAME, CREATED_DATE, EMAIL;
    private String appURL;
    Activity mContext = this;
    TextView mId, mName, mEmail, mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mId = findViewById(R.id.txt_Id);
        mName = findViewById(R.id.txt_Name);
        mEmail = findViewById(R.id.txt_Email);
        mDate = findViewById(R.id.txt_Date);

//        Intent data = getIntent();
//        EMAIL = data.getStringExtra("email");
        SharedPrefManager sharedPrefManager = new SharedPrefManager();
        EMAIL = sharedPrefManager.getEmail(mContext);
        appURL = "http://192.168.0.100/api/getUserDetail.php?email=" + EMAIL;

        getUserDetail();
    }

    private void getUserDetail(){

        if (EMAIL.isEmpty()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setMessage("Email cannot be empty");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
        else{
            StringRequest stringRequest = new StringRequest(Request.Method.GET, appURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        ID = jsonObject.getString("id");
                        NAME = jsonObject.getString("name");
                        EMAIL = jsonObject.getString("email");
                        CREATED_DATE = jsonObject.getString("created_date");

                        mId.setText(ID);
                        mName.setText(NAME);
                        mEmail.setText(EMAIL);
                        mDate.setText(CREATED_DATE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    AlertDialog.Builder alert;
                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null){
                        switch (response.statusCode){
                            case 400:
                                alert = new AlertDialog.Builder(mContext);
                                alert.setTitle("Error");
                                alert.setMessage("The server could not understand the request due to invalid syntax.");
                                alert.setCancelable(false);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alert.show();
                                break;
                            case 404:
                                alert = new AlertDialog.Builder(mContext);
                                alert.setTitle("Error");
                                alert.setMessage("The server can not find requested resource.");
                                alert.setCancelable(false);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alert.show();
                                break;
                            case 403:
                                alert = new AlertDialog.Builder(mContext);
                                alert.setTitle("Error");
                                alert.setMessage("The client does not have access rights to the content.");
                                alert.setCancelable(false);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alert.show();
                                break;
                        }
                    }
                    else{
                        alert = new AlertDialog.Builder(mContext);
                        alert.setTitle("Error");
                        alert.setMessage(error.toString());
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();
                    }

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("Accept", "Application/json;charset=UTF-8");

                    return params;
                }

//                @Override
//                public Map<String, String> getParams() throws AuthFailureError{
//                    HashMap<String,String> params = new HashMap<>();
//                    params.put("email", EMAIL);
//
//                    return params;
//                }
            };
            VolleySingleton.getInstance().addRequestQueue(stringRequest);
        }
    }

}
