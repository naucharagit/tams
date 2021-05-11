package com.nauchara.tams;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static com.nauchara.tams.MainActivity.strOPH;

public class OpnameAdapter extends RecyclerView.Adapter<OpnameAdapter.ViewHolder> {

    private Context context;
    private List<OpnameHModel> my_data;
    private RecyclerView recyclerView;
    private OpnameAdapter adapter;

    JSONObject jsonObject;
    JSONArray jsonArray;
    String JSON_STRING;

    DatabaseHelper databaseHelper;

    public OpnameAdapter(Context context, List<OpnameHModel> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    @NonNull
    @Override
    public OpnameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_opname, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OpnameAdapter.ViewHolder holder, final int position) {
        holder.tvdate.setText(my_data.get(position).getOph_date());
        holder.tvlocation.setText(my_data.get(position).getOph_title());
        holder.tvtotal.setText(my_data.get(position).getOph_status4());

        databaseHelper = new DatabaseHelper(context);

        int intAssetExist = databaseHelper.getCountDetailExist(my_data.get(position).getOph_id());
        int intAssetCount = databaseHelper.getCountDetail(my_data.get(position).getOph_id());
        int intIsClosed = my_data.get(position).getOph_status1();
        String strStatus = "";
        if(intAssetExist <= 0) {
            holder.btnsubmit.setBackgroundResource(R.drawable.bg_button_new);
            strStatus = "NEW";
        }
        else if (intAssetExist < intAssetCount) {
            holder.btnsubmit.setBackgroundResource(R.drawable.bg_button_progress);
            strStatus = "PROG";
        }
        else if (intAssetExist >= intAssetCount) {
            holder.btnsubmit.setBackgroundResource(R.drawable.bg_button_full);
            holder.tvtotal.setTextColor(R.drawable.bg_textview);
            strStatus = "FULL";
        }

        if(intIsClosed==2) {
            holder.btnsubmit.setBackgroundResource(R.drawable.bg_button_done);
            strStatus = "FINISH";
        }

        if(strStatus == "NEW") {
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(100); //You can manage the blinking time with this parameter
            anim.setStartOffset(50);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            holder.btnsubmit.startAnimation(anim);
        }

        holder.btnsubmit.setText(strStatus);

        int i = position;
        holder.btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key", String.valueOf(my_data.get(i).getOph_id()));
                ScanFragment scanFragment = new ScanFragment();
                scanFragment.setArguments(bundle);

                strOPH = String.valueOf(my_data.get(i).getOph_id());

                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, new ScanFragment()).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvdate, tvlocation, tvtotal;
        Button btnsubmit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvdate = itemView.findViewById(R.id.tv_date);
            tvlocation = itemView.findViewById(R.id.tv_location);
            tvtotal = itemView.findViewById(R.id.tv_total);
            btnsubmit = itemView.findViewById(R.id.btn_submit);
        }
    }
}