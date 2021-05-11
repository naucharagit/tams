package com.nauchara.tams;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import static com.nauchara.tams.MainActivity.strOPH;

public class OpnameDAdapter extends RecyclerView.Adapter<OpnameDAdapter.ViewHolder> {

    private Context context;
    private List<OpnameDModel> my_data;
    AlertDialog alertDialog;

    public OpnameDAdapter(Context context, List<OpnameDModel> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_opnamescan, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvopnamescanassetname.setText(my_data.get(position).getOpd_itemname());
        holder.tvopnamescanassetroom.setText(my_data.get(position).getOpd_location_desc());

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        if (databaseHelper.getStatusHeader(Integer.parseInt(strOPH)) == 2) {
            holder.btnclose.setBackgroundResource(R.drawable.bg_button_commit);
        }

        int i = position;
        holder.btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.btnclose.getText() != "X") {
                    if (databaseHelper.getStatusHeader(Integer.parseInt(strOPH)) != 2) {
                        alertDialog = new AlertDialog.Builder(v.getContext())
                                .setTitle("REMOVE LIST")
                                .setMessage("Are you sure want to delete asset " + my_data.get(i).getOpd_itemname() + "?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Toast.makeText(v.getContext(), "Process..", Toast.LENGTH_SHORT).show();
                                        DatabaseHelper databaseHelper = new DatabaseHelper(v.getContext());
                                        databaseHelper.deleteOPDetail(Integer.parseInt(strOPH), my_data.get(i).getOpd_tag(), 0, "");
                                        holder.btnclose.setText("X");
                                        holder.btnclose.setBackgroundResource(R.drawable.bg_button_closed);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                    else {
                        alertDialog = new AlertDialog.Builder(v.getContext())
                                .setTitle("INFO LIST")
                                .setMessage(my_data.get(i).getOpd_itemname() + " already summited!")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // nothing to do
                                    }
                                })
                                .show();
                    }
                }
                else {
                    alertDialog = new AlertDialog.Builder(v.getContext())
                            .setTitle("REMOVE LIST")
                            .setMessage(my_data.get(i).getOpd_itemname() + " already deleted!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // nothing to do
                                }
                            })
                            .show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvopnamescanassetname, tvopnamescanassetroom;
        Button btnclose;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvopnamescanassetname = itemView.findViewById(R.id.tv_opnamescan_assetname);
            tvopnamescanassetroom = itemView.findViewById(R.id.tv_opnamescan_assetroom);
            btnclose = itemView.findViewById(R.id.btn_close);
        }
    }
}