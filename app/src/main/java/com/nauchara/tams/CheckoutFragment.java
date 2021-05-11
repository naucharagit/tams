package com.nauchara.tams;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.nauchara.tams.LoginActivity.stat_modification;
import static com.nauchara.tams.LoginActivity.stat_password;
import static com.nauchara.tams.LoginActivity.stat_username;
import static com.nauchara.tams.MainActivity.TAG;
import static com.nauchara.tams.MainActivity.strOPH;

public class CheckoutFragment extends Fragment {

    TextView tvheader, tvsubheader;
    Button btncheckout;
    AlertDialog alertDialog;
    private RequestQueue requestQueue;
    DatabaseHelper databaseHelper;
    List<OpnameDModel> listAsset;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        tvheader = view.findViewById(R.id.tv_header);
        btncheckout = view.findViewById(R.id.btn_checkout);
        tvheader.setText("Scanning Assets OPN."+strOPH);

        CookieHandler.setDefault(new CookieManager()); //Important to save cookies by me

        databaseHelper = new DatabaseHelper(getActivity());

        if(databaseHelper.getStatusHeader(Integer.parseInt(strOPH)) != 2) {
            btncheckout.setBackgroundResource(R.drawable.bg_button_checkout);
            btncheckout.setText("CHECKOUT");
        }
        else {
            btncheckout.setBackgroundResource(R.drawable.bg_button_checkout_done);
            btncheckout.setText("DONE");
        }

        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (databaseHelper.getStatusHeader(Integer.parseInt(strOPH)) != 2) {
                    alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("CHECKOUT")
                            .setMessage("Are you sure want to check out?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    btncheckout.setBackgroundResource(R.drawable.bg_button_checkout_done);
                                    btncheckout.setText("DONE");
                                    String strJSON = "[\n" +
                                            "    \"login\",\n" +
                                            "    null,\n" +
                                            "    null,\n" +
                                            "    [\n" +
                                            "        {\n" +
                                            "            \"login\" : \"" + stat_username + "\",\n" +
                                            "            \"password\" : \"" + stat_password + "\"\n" +
                                            "        }\n" +
                                            "    ],\n" +
                                            "    null\n" +
                                            "]";

                                    Submit(strJSON, stat_username, stat_password, view);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        return view;

    }

    private void Submit(String data, String username, String password, View v) {
        final String savedata = data;
        String URL = "http://10.15.1.60/api";

        requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);

                    JSONObject oresult  = objres.getJSONObject("result");
                    String strstatus = oresult.getString("status");
                    String strdata = oresult.getString("data");
                    String strmodif = oresult.getString("modification");

                    if(strdata == "true") {

                        //synchronize data
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        Handler handler = new Handler(Looper.getMainLooper());

                        executor.execute(new Runnable() {
                            @Override
                            public void run() {

                                synchronizeToServer();

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d(TAG,"Sync done!");
                                    }
                                },1000);
                            }
                        });
                    }
                    else {
                        Toast.makeText(getContext(), "Error on server 1!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
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

    private void synchronizeToServer() {

        databaseHelper = new DatabaseHelper(getContext());
        listAsset = databaseHelper.getOPDetail(Integer.parseInt(strOPH), 1);

        for(OpnameDModel opnameDModel : listAsset) {
            Log.d(TAG, "Asset ID ="+opnameDModel.getOpd_id()+" Asset Name = "+opnameDModel.getOpd_itemname());
            //Flag detail
            String strJSON = "[\n" +
                    "\t\"apply\",\n" +
                    "\t1,\n" +
                    "\t14,\n" +
                    "\t[{\n" +
                    "\t\t\t\"fields\": [\"id\", \"deleted\", \"master_id\", \"master_rec_id\", \"asset_name\", \"new_asset_room\"],\n" +
                    "\t\t\t\"expanded\": false,\n" +
                    "\t\t\t\"data\": {\n" +
                    "\t\t\t\t\"1\": {\n" +
                    "\t\t\t\t\t\"record\": [\n" +
                    "\t\t\t\t\t\t"+opnameDModel.getOpd_id()+",\n" +
                    "\t\t\t\t\t\t0,\n" +
                    "\t\t\t\t\t\t13,\n" +
                    "\t\t\t\t\t\t"+opnameDModel.getOpd_value1()+",\n" +
                    "\t\t\t\t\t\t"+opnameDModel.getOpd_value2()+",\n" +
                    "\t\t\t\t\t\t"+opnameDModel.getOpd_location()+",\n" +
                    "\t\t\t\t\t\t[\n" +
                    "\t\t\t\t\t\t\t2,\n" +
                    "\t\t\t\t\t\t\t{},\n" +
                    "\t\t\t\t\t\t\t\"1\"\n" +
                    "\t\t\t\t\t\t]\n" +
                    "\t\t\t\t\t],\n" +
                    "\t\t\t\t\t\"details\": {},\n" +
                    "\t\t\t\t\t\"old_record\": [\n" +
                    "\t\t\t\t\t\t"+opnameDModel.getOpd_id()+",\n" +
                    "\t\t\t\t\t\t0,\n" +
                    "\t\t\t\t\t\t13,\n" +
                    "\t\t\t\t\t\t"+opnameDModel.getOpd_value1()+",\n" +
                    "\t\t\t\t\t\t"+opnameDModel.getOpd_value2()+",\n" +
                    "\t\t\t\t\t\tnull,\n" +
                    "\t\t\t\t\t\t[\n" +
                    "\t\t\t\t\t\t\tnull,\n" +
                    "\t\t\t\t\t\t\t{},\n" +
                    "\t\t\t\t\t\t\t\"1\"\n" +
                    "\t\t\t\t\t\t]\n" +
                    "\t\t\t\t\t]\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t}\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"__edit_record_version\": 0\n" +
                    "\t\t}\n" +
                    "\t],\n" +
                    "\t"+stat_modification+"\n" +
                    "]";
            SubmitDetail(strJSON);
        }

        String strJSON2 = "[\n" +
                "\t\"apply\",\n" +
                "\t1,\n" +
                "\t13,\n" +
                "\t[{\n" +
                "\t\t\t\"fields\": [\n" +
                "\t\t\t\t\"id\",\n" +
                "\t\t\t\t\"status\"\n" +
                "\t\t\t],\n" +
                "\t\t\t\"expanded\": false,\n" +
                "\t\t\t\"data\": {\n" +
                "\t\t\t\t\"1\": {\n" +
                "\t\t\t\t\t\"record\": [\n" +
                "\t\t\t\t\t\t"+strOPH+",\n" +
                "\t\t\t\t\t\t2,\n" +
                "\t\t\t\t\t\t[\n" +
                "\t\t\t\t\t\t\t2,\n" +
                "\t\t\t\t\t\t\t{},\n" +
                "\t\t\t\t\t\t\t\"1\"\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t],\n" +
                "\t\t\t\t\t\"details\": {},\n" +
                "\t\t\t\t\t\"old_record\": [\n" +
                "\t\t\t\t\t\t"+strOPH+",\n" +
                "\t\t\t\t\t\t1,\n" +
                "\t\t\t\t\t\t[\n" +
                "\t\t\t\t\t\t\tnull,\n" +
                "\t\t\t\t\t\t\t{},\n" +
                "\t\t\t\t\t\t\t\"1\"\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"__edit_record_version\": 0\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t"+stat_modification+"\n" +
                "]";
        SubmitHeader(strJSON2);
    }

    private void SubmitHeader(String data) {
        final String savedata = data;
        String URL = "http://10.15.1.60/api";

        requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);

                    JSONObject oresult  = objres.getJSONObject("result");
                    String strstatus = oresult.getString("status");

                    Log.d(TAG, "Status Header = "+strstatus);

                    databaseHelper = new DatabaseHelper(getContext());
                    databaseHelper.closeHeader(Integer.parseInt(strOPH));

                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
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

    private void SubmitDetail(String data) {
        final String savedata = data;
        String URL = "http://10.15.1.60/api";

        requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);

                    JSONObject oresult  = objres.getJSONObject("result");
                    String strstatus = oresult.getString("status");

                    Log.d(TAG, "Status Detail = "+strstatus);
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
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
