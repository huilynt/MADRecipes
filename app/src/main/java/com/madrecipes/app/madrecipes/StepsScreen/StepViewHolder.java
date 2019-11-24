package com.madrecipes.app.madrecipes.StepsScreen;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.madrecipes.app.madrecipes.R;

//Tay Hui Lin
//Team 6
//10178003J

//viewholder
public class StepViewHolder extends RecyclerView.ViewHolder {
    public TextView step;

    //constructor
    public StepViewHolder(View itemView) {
        super(itemView);
        step = itemView.findViewById(R.id.tvStep);
    }
}
