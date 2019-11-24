package com.madrecipes.app.madrecipes.MainScreen;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.madrecipes.app.madrecipes.R;

//Tay Hui Lin
//Team 6
//10178003J

//recipe view holder
public class ItemViewHolder extends RecyclerView.ViewHolder{
    public TextView item;
    public ImageView itemImage;

    //constructor
    public ItemViewHolder(View itemView) {
        super(itemView);
        item = itemView.findViewById(R.id.tvRecipeName);
        itemImage = itemView.findViewById(R.id.imageRecipe);
    }
}
