package com.skipp.mgnrega;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.CardViewHolder> {
    static int pos;
    View itemView;

    SharedPreferences sharedPreferences;
    //SharedPreferences.Editor editor = sharedPreferences.edit();

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.work, parent, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, final int position) {
        Info i = info.get(holder.getAdapterPosition());
        pos = holder.getAdapterPosition();
        CardViewHolder.workName.setText(i.nameofproject);
        holder.setIsRecyclable(false);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println("clicked "+MainActivity.array1.get(position));
                Details.click(holder.getAdapterPosition(),view.getContext());

                // editor.putString("ordernumber",MainActivity.orderno.get(position));
                // editor.apply();
                // System.out.println("orderno: "+MainActivity.orderno.get(position));*/
            }
        });

    }


    @Override
    public int getItemCount() {
        return info.size();
    }

    public static class Info {
        String nameofproject;
        Info(String n) {
            nameofproject = n;
        }
    }
    static List<Info> info;
    public WorkAdapter(List<Info> info) {
        this.info = info;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        static TextView workName;

        public CardViewHolder(View itemView) {
            super(itemView);
            workName = (TextView) itemView.findViewById(R.id.workname);

        }
    }

}
