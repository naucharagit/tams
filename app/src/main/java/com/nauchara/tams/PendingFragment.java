package com.nauchara.tams;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.nauchara.tams.MainActivity.strOPH;
import static com.nauchara.tams.MainActivity.TAG;

public class PendingFragment extends Fragment {

    public static DatabaseHelper databaseHelper;
    public static ListView lvassetpendingList;
    public static ArrayAdapter assetArrayAdapter;

    TextView tvheader, tvsubheader;
    EditText et_search;
    RecyclerView rcyscan;
    public List<OpnameDModel> data_list = new ArrayList<>();
    OpnameDPendingAdapter opnameDAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pending, container, false);

        tvheader = view.findViewById(R.id.tv_header);
        tvsubheader = view.findViewById(R.id.tv_subheader);
        et_search = view.findViewById(R.id.et_search);
        lvassetpendingList = view.findViewById(R.id.lv_assetpendingList);
        rcyscan = view.findViewById(R.id.rcy_scan);

        tvheader.setText("Scanning Assets OPN."+strOPH);

        updateTable(et_search.getText().toString());

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Log.d(TAG, "type : " + s.toString());
                    assetArrayAdapter = new ArrayAdapter<OpnameDModel>(getActivity(), android.R.layout.simple_list_item_1, databaseHelper.getOPDetailSearch(Integer.parseInt(strOPH), -1, s.toString())) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            // Cast the list view each item as text view
                            TextView item = (TextView) super.getView(position, convertView, parent);

                            item.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);

                            // Set the list view item's text color
                            item.setTextColor(Color.parseColor("#DFDFDF"));

                            // return the view
                            return item;
                        }
                    };
                    updateTable(s.toString());
                }
                catch (Exception e) {
                    Log.d(TAG,"Error on enter. "+e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;

    }

    private void updateTable(String strsearch) {
        databaseHelper = new DatabaseHelper(getContext());
        rcyscan.setLayoutManager(new LinearLayoutManager(getContext()));
        data_list = databaseHelper.getOPDetailSearch(Integer.parseInt(strOPH), -1, strsearch);
        opnameDAdapter = new OpnameDPendingAdapter(getContext(), data_list);
        rcyscan.setAdapter(opnameDAdapter);
        opnameDAdapter.notifyDataSetChanged();
    }
}