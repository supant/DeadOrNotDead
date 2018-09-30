package com.example.antoine.deadornotdead.recyclingReponse;


/**
 * Created by Antoine on 24/12/2017.
 */


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.antoine.deadornotdead.R;

import java.util.List;


public class ReponseAdapter extends RecyclerView.Adapter<ReponseAdapter.MyViewHolder> {

        private List<Reponse> repList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView reponse;

            public MyViewHolder(View view) {
                super(view);
                reponse = (TextView) view.findViewById(R.id.reponse);
            }
    }


    public ReponseAdapter(List<Reponse> repList) {
        this.repList = repList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reponse_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Reponse rep = repList.get(position);
        holder.reponse.setText(rep.getValeurReponse());
    }

    @Override
    public int getItemCount() {
        return repList.size();
    }

    public void clear() {
        int size = repList.size();
        repList.clear();
        notifyItemRangeRemoved(0, size);
    }


}
