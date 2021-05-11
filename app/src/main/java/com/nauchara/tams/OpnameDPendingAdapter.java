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

public class OpnameDPendingAdapter extends RecyclerView.Adapter<OpnameDPendingAdapter.ViewHolder> {

    private Context context;
    private List<OpnameDModel> my_data;
    AlertDialog alertDialog;

    public OpnameDPendingAdapter(Context context, List<OpnameDModel> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_opnamescan_pending, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvopnamescanassetname.setText(my_data.get(position).getOpd_itemname());

        int i = position;
        holder.btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog = new AlertDialog.Builder(v.getContext())
                        .setTitle("FIND ASSET")
                        .setMessage("" + my_data.get(i).getOpd_itemname() + " must be somewhere!")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(v.getContext(), "Process..", Toast.LENGTH_SHORT).show();
                                //nothing to do
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvopnamescanassetname;
        Button btnclose;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvopnamescanassetname = itemView.findViewById(R.id.tv_opnamescan_assetname);
            btnclose = itemView.findViewById(R.id.btn_close);
        }
    }
}
