package com.madrecipes.app.madrecipes.StepsScreen;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.madrecipes.app.madrecipes.R;

//Tay Hui Lin
//Team 6
//10178003J

//header view holder to show recipe name and image
public class HeaderViewHolder extends RecyclerView.ViewHolder {
    public TextView recipeName;
    public ImageView imageRecipe;

    //constructor
    public HeaderViewHolder(View itemView) {
        super(itemView);
        recipeName = itemView.findViewById(R.id.tvRecipeName);
        imageRecipe = itemView.findViewById(R.id.imageRecipe);
    }
}
