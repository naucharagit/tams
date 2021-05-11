package com.nauchara.tams;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private int intCountDetail = 0;
    private int intCountExist = 0;

    DatabaseHelper databaseHelper;

    public static List<OpnameData> data_list = new ArrayList<>();
    public static String stat_username;
    public static String stat_password;
    public static String stat_modification;

    private RequestQueue requestQueue;
    private TextView tvtext, tvstatus;
    private EditText etusername, etpassword;
    private Button btnlogin;

    private static String TAG = "MyWatch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CookieHandler.setDefault(new CookieManager());

        //Database
        databaseHelper = new DatabaseHelper(LoginActivity.this);

        getSupportActionBar().hide();

        tvtext = findViewById(R.id.tv_text);
        tvstatus = findViewById(R.id.tv_status);
        etusername = findViewById(R.id.et_username);
        etpassword = findViewById(R.id.et_password);

        btnlogin = findViewById(R.id.btn_login);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etusername.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter username", Toast.LENGTH_LONG).show();
                    etusername.requestFocus();
                }
                else if(etpassword.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_LONG).show();
                    etpassword.requestFocus();
                }
                else {

                    tvtext.setText("Synchronize DB ...");
                    etusername.setEnabled(false);
                    etpassword.setEnabled(false);
                    btnlogin.setEnabled(false);

                    String strJSON = "[\n" +
                            "    \"login\",\n" +
                            "    null,\n" +
                            "    null,\n" +
                            "    [\n" +
                            "        {\n" +
                            "            \"login\" : \"" + etusername.getText().toString() + "\",\n" +
                            "            \"password\" : \"" + etpassword.getText().toString() + "\"\n" +
                            "        }\n" +
                            "    ],\n" +
                            "    null\n" +
                            "]";

                    stat_username = etusername.getText().toString();
                    stat_password = etpassword.getText().toString();

                    Submit(strJSON, etusername.getText().toString(), etpassword.getText().toString(), v);
                }
            }
        });
    }

    private void Submit(String data, String username, String password, View v) {
        final String savedata = data;
        String URL = "http://10.15.1.60/api";

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);

                    JSONObject oresult  = objres.getJSONObject("result");
                    String strstatus = oresult.getString("status");
                    String strdata = oresult.getString("data");
                    String strmodif = oresult.getString("modification");

                    stat_modification = strmodif;

                    if(strdata == "true") {

                        //synchronize data
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        Handler handler = new Handler(Looper.getMainLooper());

                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                synchronizeData(username, password, strmodif);

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        GoOpname(stat_username,stat_password,stat_modification);
                                    }
                                },10000);
                            }
                        });
                    }
                    else {
                        //Toast.makeText(LoginActivity.this, "Wrong username/password!", Toast.LENGTH_LONG).show();
                        tvtext.setText("Wrong username/password");
                        //tvstatus.setVisibility(View.VISIBLE);
                        etusername.setEnabled(true);
                        etpassword.setEnabled(true);
                        btnlogin.setEnabled(true);
                        etusername.setText("");
                        etpassword.setText("");
                        etusername.requestFocus();
                    }
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(LoginActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return savedata == null ? null : savedata.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
    }

    private void synchronizeData(String strUsername, String strPassword, String strModification) {

        String strJSON = "[\n" +
                "    \"login\",\n" +
                "    null,\n" +
                "    null,\n" +
                "    [\n" +
                "        {\n" +
                "            \"login\" : \""+strUsername+"\",\n" +
                "            \"password\" : \""+strPassword+"\"\n" +
                "        }\n" +
                "    ],\n" +
                "    null\n" +
                "]";

        String strJSON2 = "[" +
                "\"open\"," +
                "1," +
                "17," +
                "{" +
                "\"__expanded\": false," +
                "\"__fields\": [\"id\", \"login\", \"name\"]," +
                "\"__order\": []," +
                "\"__filters\": [" +
                "[\"login\", 15, \""+strUsername+"\"]\n" +
                "]," +
                "\"__limit\": 0," +
                "\"__offset\": 0," +
                "\"__search\": []" +
                "}," +
                strModification +
                "]";

        getDataHeader(strJSON, strJSON2, strUsername, strPassword, strModification);
    }

    private void getDataHeader(String data, String data2, String strUsername, String strPassword, String strModification) {
        final String savedata = data;
        final String savedata2 = data2;
        String URL = "http://10.15.1.60/api";

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return savedata == null ? null : savedata.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
        };

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);
                    JSONObject oresult = objres.getJSONObject("result");

                    String strdata = oresult.getString("data");
                    JSONArray jsonArray = oresult.getJSONArray("data");

                    ArrayList<String> listdata = new ArrayList<String>();
                    JSONArray jArray = (JSONArray)jsonArray;
                    if(jArray != null) {
                        for (int i=0;i<jArray.length();i++){
                            listdata.add(jArray.getString(i));
                        }
                    }

                    String dataarray1 = listdata.get(0);

                    JSONArray jsonArray1 = new JSONArray(dataarray1);

                    ArrayList<String> listdata2 = new ArrayList<String>();
                    JSONArray jArray2 = (JSONArray)jsonArray1;
                    if(jArray2 != null) {
                        for (int i=0; i<jArray2.length();i++) {
                            listdata2.add(jArray2.getString(i));
                        }
                    }

                    String dataarray2 = listdata2.get(0);

                    JSONArray jsonArray2 = new JSONArray(dataarray2);

                    ArrayList<String> listdata3 = new ArrayList<String>();
                    JSONArray jArray3 = (JSONArray)jsonArray2;
                    if(jArray3 != null) {
                        for (int i=0; i<jArray3.length();i++) {
                            listdata3.add(jArray3.getString(i));
                        }
                    }

                    String struser_id = listdata3.get(0);
                    String struser_username = listdata3.get(1);
                    String struser_name = listdata3.get(2);

                    String strJSON3 = "[\n" +
                            "\t\"open\",\n" +
                            "\t1,\n" +
                            "\t13,\n" +
                            "\t{\n" +
                            "\t\t\"__expanded\": true,\n" +
                            "\t\t\"__fields\": [\"id\",\"opname_title\", \"order_date\", \"status\", \"opname_operator\"],\n" +
                            "\t\t\"__order\": [],\n" +
                            "\t\t\"__filters\": [\n" +
                            "\t\t\t[\"opname_operator_id\", 15, \""+struser_id+"\"]\n" +
                            "\t\t],\n" +
                            "\t\t\"__limit\": 0,\n" +
                            "\t\t\"__offset\": 0,\n" +
                            "\t\t\"__search\": []\n" +
                            "\t},\n" +
                            strModification +
                            "]";

                    SubmitHeader(strJSON3, strUsername, strPassword, strModification);
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return savedata2 == null ? null : savedata2.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
        requestQueue.add(stringRequest2);
    }

    private void SubmitHeader(String data, String strUsername, String strPassword, String strModification) {
        final String savedata = data;
        String URL = "http://10.15.1.60/api";

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);

                    JSONObject oresult  = objres.getJSONObject("result");

                    String strdata = oresult.getString("data");
                    JSONArray jsonArray = oresult.getJSONArray("data");

                    ArrayList<String> listdata = new ArrayList<String>();
                    JSONArray jArray = (JSONArray)jsonArray;
                    if(jArray != null) {
                        for (int i=0;i<jArray.length();i++){
                            listdata.add(jArray.getString(i));
                        }
                    }

                    String dataarray1 = listdata.get(0);
                    JSONArray jsonArray1 = new JSONArray(dataarray1);

                    ArrayList<String> listdata2 = new ArrayList<String>();
                    JSONArray jArray2 = (JSONArray)jsonArray1;

                    ArrayList<String> listdata3 = new ArrayList<String>();

                    if(jArray2 != null) {
                        for (int i=0;i<jArray2.length();i++) {
                            listdata2.add(jArray2.getString(i));

                            String dataarray2 = listdata2.get(0);
                            JSONArray jsonArray2 = new JSONArray(dataarray2);


                            JSONArray jArray3 = (JSONArray)jsonArray2;
                            if(jArray3 != null) {
                                for (int ii=0;i<jArray3.length();i++) {
                                    listdata3.add(jArray3.getString(i));
                                }
                            }
                        }
                    }

                    int idx = 0;
                    boolean SUCCESS0 = false;
                    boolean SUCCESS1 = false;

                    while(idx < jArray2.length()) {

                        JSONArray ja = jArray2.getJSONArray(idx);

                        String[] opnamedate;
                        opnamedate = ja.get(2).toString().split("T");

                        OpnameData opnameData = new OpnameData(
                                ja.get(0).toString(),
                                opnamedate[0],
                                ja.get(1).toString(),
                                ja.get(3).toString(),
                                ja.get(6).toString(),
                                ja.get(4).toString()
                        );
                        data_list.add(opnameData);

                        SUCCESS0 = databaseHelper.checkHeader(Integer.parseInt(ja.get(0).toString()));

                        if(SUCCESS0) {
                            tvtext.setText("Check "+ja.get(2).toString());
                        }
                        else {

                            tvtext.setText("Download Header..");

                            intCountDetail = databaseHelper.getCountDetail(Integer.parseInt(ja.get(0).toString()));
                            intCountExist = databaseHelper.getCountDetailExist(Integer.parseInt(ja.get(0).toString()));

                            OpnameHModel opnameHModel;
                            opnameHModel = new OpnameHModel(Integer.parseInt(ja.get(0).toString()), ja.get(1).toString(),
                                    ja.get(2).toString(),
                                    Integer.parseInt(ja.get(3).toString()),
                                    Integer.parseInt(ja.get(4).toString()),
                                    ja.get(5).toString(),
                                    ja.get(6).toString(),
                                    "Total Asset "+intCountExist+"/"+intCountDetail
                            );

                            SUCCESS1 = databaseHelper.addHeader(opnameHModel);

                            String strJSON4 = "[\n" +
                                    "\t\"open\",\n" +
                                    "\t1,\n" +
                                    "\t14,\n" +
                                    "\t{\n" +
                                    "\t\t\"__expanded\": true,\n" +
                                    "\t\t\"__fields\": [\"id\", \"master_rec_id\", \"asset_name\", \"booked_asset_room_id\", \"asset_tag_number\"],\n" +
                                    "\t\t\"__order\": [],\n" +
                                    "\t\t\"__filters\": [\n" +
                                    "\t\t\t[\"master_rec_id\", 15, \""+ Integer.parseInt(ja.get(0).toString())+"\"]\n" +
                                    "\t\t],\n" +
                                    "\t\t\"__limit\": 0,\n" +
                                    "\t\t\"__offset\": 0,\n" +
                                    "\t\t\"__search\": []\n" +
                                    "\t},\n" +
                                    "\t"+strModification+"\n" +
                                    "]";
                            SubmitDetail(strJSON4, strUsername, strPassword, strModification);

                            Log.i(TAG, opnameHModel.toString());

                        }
                        idx++;
                    }
                    Log.i(TAG, "Success = "+SUCCESS1);
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return savedata == null ? null : savedata.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
    }

    private void SubmitLocation(String data, String strUsername, String strPassword, String strModification) {

        final String savedata = data;
        String URL = "http://10.15.1.60/api";

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);

                    JSONObject oresult  = objres.getJSONObject("result");

                    String strdata = oresult.getString("data");
                    JSONArray jsonArray = oresult.getJSONArray("data");

                    ArrayList<String> listdata = new ArrayList<String>();
                    JSONArray jArray = (JSONArray)jsonArray;
                    if(jArray != null) {
                        for (int i=0;i<jArray.length();i++){
                            listdata.add(jArray.getString(i));
                        }
                    }

                    String dataarray1 = listdata.get(0);
                    JSONArray jsonArray1 = new JSONArray(dataarray1);

                    ArrayList<String> listdata2 = new ArrayList<String>();
                    JSONArray jArray2 = (JSONArray)jsonArray1;

                    ArrayList<String> listdata3 = new ArrayList<String>();

                    if(jArray2 != null) {
                        for (int i=0;i<jArray2.length();i++) {
                            listdata2.add(jArray2.getString(i));

                            String dataarray2 = listdata2.get(0);
                            JSONArray jsonArray2 = new JSONArray(dataarray2);

                            JSONArray jArray3 = (JSONArray)jsonArray2;
                            if(jArray3 != null) {
                                for (int ii=0;i<jArray3.length();i++) {
                                    listdata3.add(jArray3.getString(i));
                                }
                            }
                        }
                    }

                    int idx = 0;
                    boolean SUCCESS0 = false;
                    boolean SUCCESS1 = false;
                    while(idx < jArray2.length()) {

                        JSONArray ja = jArray2.getJSONArray(idx);
                        DatabaseHelper databaseHelper = new DatabaseHelper(LoginActivity.this);
                        SUCCESS0 = databaseHelper.checkLocation(Integer.parseInt(ja.get(0).toString()));

                        if(SUCCESS0) {
                            tvtext.setText("Preparing.. (wait)");
                            Log.i(TAG, "Location : "+ja.get(4).toString());
                        }
                        else {

                            tvtext.setText("Download Location..");

                            LocationModel locationModel;
                            locationModel = new LocationModel(Integer.parseInt(ja.get(0).toString()),
                                    Integer.parseInt(ja.get(1).toString()),
                                    ja.get(2).toString(), Integer.parseInt(ja.get(3).toString()),
                                    ja.get(4).toString(),ja.get(5).toString(),ja.get(6).toString());
                            Log.i(TAG, "Location : "+locationModel.toString());
                            SUCCESS1 = databaseHelper.addLocation(locationModel);

                        }


                        idx++;
                    }

                    Log.i(TAG, "Success Location : "+SUCCESS1);
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return savedata == null ? null : savedata.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
    }

    private void GoOpname(String strUsername, String strPassword, String strModification) {

        Intent sendData = new Intent(LoginActivity.this, MainActivity.class);
        sendData.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sendData.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendData.putExtra("USERNAME", strUsername);
        sendData.putExtra("PASSWORD", strPassword);
        sendData.putExtra("MODIFICATION", strModification);
        startActivity(sendData);

    }

    private void SubmitDetail(String data, String strUsername, String strPassword, String strModification) {

        final String savedata = data;
        String URL = "http://10.15.1.60/api";

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);

                    JSONObject oresult  = objres.getJSONObject("result");

                    String strdata = oresult.getString("data");
                    JSONArray jsonArray = oresult.getJSONArray("data");

                    ArrayList<String> listdata = new ArrayList<String>();
                    JSONArray jArray = (JSONArray)jsonArray;
                    if(jArray != null) {
                        for (int i=0;i<jArray.length();i++){
                            listdata.add(jArray.getString(i));
                        }
                    }

                    String dataarray1 = listdata.get(0);
                    JSONArray jsonArray1 = new JSONArray(dataarray1);

                    ArrayList<String> listdata2 = new ArrayList<String>();
                    JSONArray jArray2 = (JSONArray)jsonArray1;

                    ArrayList<String> listdata3 = new ArrayList<String>();

                    if(jArray2 != null) {
                        for (int i=0;i<jArray2.length();i++) {
                            listdata2.add(jArray2.getString(i));

                            String dataarray2 = listdata2.get(0);
                            JSONArray jsonArray2 = new JSONArray(dataarray2);


                            JSONArray jArray3 = (JSONArray)jsonArray2;
                            if(jArray3 != null) {
                                for (int ii=0;i<jArray3.length();i++) {
                                    listdata3.add(jArray3.getString(i));
                                }
                            }
                        }
                    }

                    int idx = 0;
                    boolean SUCCESS0 = false;
                    boolean SUCCESS1 = false;
                    while(idx < jArray2.length()) {

                        JSONArray ja = jArray2.getJSONArray(idx);
                        DatabaseHelper databaseHelper = new DatabaseHelper(LoginActivity.this);
                        SUCCESS0 = databaseHelper.checkDetail(Integer.parseInt(ja.get(0).toString()));

                        if(SUCCESS0) {
                            tvtext.setText("Check "+ja.get(3).toString());
                        }
                        else {

                            tvtext.setText("Download Detail..");
                            //Log.i(TAG, "Opname detail : "+ja.get(3).toString());
                            OpnameDModel opnameDModel;
                            opnameDModel = new OpnameDModel(Integer.parseInt(ja.get(0).toString()),
                                    Integer.parseInt(ja.get(1).toString()),
                                    Integer.parseInt(ja.get(2).toString()),
                                    -1,
                                    ja.get(3).toString(),
                                    Integer.parseInt(ja.get(4).toString()), ja.get(5).toString(), 0, "");

                            Log.i(TAG, "Opname detail : "+opnameDModel.toString());
                            SUCCESS1 = databaseHelper.addDetail(opnameDModel);
                        }
                        idx++;
                    }

                    Log.i(TAG, "Success Detail : "+SUCCESS1);

                    String strJSON5 = "[\n" +
                            "\t\"open\",\n" +
                            "\t1,\n" +
                            "\t9,\n" +
                            "\t{\n" +
                            "\t\t\"__master_id\": 13,\n" +
                            "\t\t\"__master_rec_id\": 4,\n" +
                            "\t\t\"__expanded\": true,\n" +
                            "\t\t\"__fields\": [\"id\", \"buildings\", \"description\", \"room_floor\", \"room\"],\n" +
                            "\t\t\"__order\": [],\n" +
                            "\t\t\"__filters\": [],\n" +
                            "\t\t\"__limit\": 0,\n" +
                            "\t\t\"__offset\": 0\n" +
                            "\t},\n" +
                            "\t"+strModification+"\n" +
                            "]";

                    SubmitLocation(strJSON5, strUsername, strPassword, strModification);
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return savedata == null ? null : savedata.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
    }

}