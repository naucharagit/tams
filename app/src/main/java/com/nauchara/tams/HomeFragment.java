package com.nauchara.tams;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private OpnameAdapter adapter;
    public static List<OpnameHModel> data_list = new ArrayList<>();

    DatabaseHelper databaseHelper;

    private RequestQueue requestQueue;

    TextView tvusername, tvdate, tvopnametask;
    String username, password, modification;

    AlertDialog alertDialog;

    int intResetDialog = 0;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);

        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.nav_home).setChecked(true);

        databaseHelper = new DatabaseHelper(getContext());

        CookieHandler.setDefault(new CookieManager()); //Important to save cookies by me

        tvusername = view.findViewById(R.id.tv_username);
        tvdate = view.findViewById(R.id.tv_date);
        tvopnametask = view.findViewById(R.id.tv_opnametask);
        username = getActivity().getIntent().getStringExtra("USERNAME");
        password = getActivity().getIntent().getStringExtra("PASSWORD");
        modification = getActivity().getIntent().getStringExtra("MODIFICATION");
        tvusername.setText("Username : "+username);

        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tvdate.setText("Date :"+date);

        data_list = databaseHelper.getOPHeader();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OpnameAdapter(getContext(), data_list);

        recyclerView.setAdapter(adapter);

        tvopnametask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intResetDialog++;

                if(intResetDialog == 10) {
                    alertDialog = new AlertDialog.Builder(v.getContext())
                            .setTitle("RESET DATA")
                            .setMessage("Are you sure want to reset data?")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    databaseHelper = new DatabaseHelper(v.getContext());
                                    databaseHelper.delHeader();
                                    databaseHelper.delDetailAll();
                                    databaseHelper.delLocationAll();
                                    data_list = databaseHelper.getOPHeader();
                                    recyclerView = view.findViewById(R.id.recycler_view);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    adapter = new OpnameAdapter(getContext(), data_list);

                                    recyclerView.setAdapter(adapter);
                                    Toast.makeText(v.getContext(), "Data Resetted!", Toast.LENGTH_SHORT).show();
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
}
