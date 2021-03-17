package com.bunoza.top10downloads;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView tvName;
    private TextView tvArtist;
    private TextView tvDate;
    private ImageView ivIcon;
    private ItemClickListener clickListener;


    public ViewHolder(@NonNull View itemView, ItemClickListener listener ) {
        super(itemView);
        this.clickListener = listener;
        tvName =  itemView.findViewById(R.id.tvName);
        tvArtist = itemView.findViewById(R.id.tvArtist);
        tvDate = itemView.findViewById(R.id.tvDate);
        ivIcon = itemView.findViewById(R.id.ivIcon);

        itemView.setOnClickListener(this);

    }

    public void setName(String name){
        tvName.setText(name);
    }
    public void setArtist(String name){
        tvArtist.setText(name);
    }
    public void setDate(String name){
        tvDate.setText(name);
    }
    public void setIcon(String name){
        Picasso.get().load(name).into(ivIcon);
    }

    @Override
    public void onClick(View v) {
        clickListener.onItemClick(getAdapterPosition());
    }
}
