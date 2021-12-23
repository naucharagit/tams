package com.nauchara.tams;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class OpnameScanAdapter extends RecyclerView.Adapter<OpnameScanAdapter.ViewHolder> {

    private Context context;
    private List<OpnameScanData> my_data;

    public OpnameScanAdapter(Context context, List<OpnameScanData> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_opnamescan, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvopnamescanassetname.setText(my_data.get(position).getNamescan());
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvopnamescanassetname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvopnamescanassetname = (TextView)itemView.findViewById(R.id.tv_opnamescan_assetname);
        }
    }
}
