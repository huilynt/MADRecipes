package com.madrecipes.app.madrecipes.AddScreen;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.madrecipes.app.madrecipes.R;
import com.madrecipes.app.madrecipes.TouchHelper.ItemTouchHelperViewHolder;

//Tay Hui Lin
//Team 6
//10178003J

//viewholder for recyclerview that displays steplist
public class AddStepsViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
    public TextView addStep;
    public ImageView handleView;

    //constructor for viewholder
    public AddStepsViewHolder(View itemView) {
        super(itemView);
        addStep = itemView.findViewById(R.id.tvStep);
        handleView = itemView.findViewById(R.id.handle);
    }

    //change item background to light grey when swiped or reordered
    @Override
    public void onItemSelected() {
        itemView.setBackgroundColor(Color.LTGRAY);
    }

    //change item background to none when action is released
    @Override
    public void onItemClear() {
        itemView.setBackgroundColor(0);
    }
}
