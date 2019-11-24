package com.madrecipes.app.madrecipes.MainScreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madrecipes.app.madrecipes.R;
import com.madrecipes.app.madrecipes.Data.Recipe;
import com.madrecipes.app.madrecipes.StepsScreen.RecipeActivity;

import java.util.ArrayList;
import java.util.List;

//Tay Hui Lin
//Team 6
//10178003J

//adapter for recipe recyclerview
public class    ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private Context context;
    private List<Recipe> recipeList;

    //constructor
    public ItemAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    //use layout for each row
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item, parent, false);

        return new ItemViewHolder(view);
    }

    //set details of recipe to recyclerview row
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        final Recipe recipe = recipeList.get(position);
        String name = recipe.getRecipeName();
        byte[] bytes = recipe.getRecipeImage();
        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        holder.item.setText(name.trim());
        holder.itemImage.setImageBitmap(bitmap);
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();

                        Intent intent = new Intent(context, RecipeActivity.class);
                        intent.putExtra("id", recipe.getId());
                        context.startActivity(intent);
                    }
                }
        );
    }

    //get size of recipe list
    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    //new list to filter recipes by search query
    public void setFilter(ArrayList<Recipe> newList) {
        recipeList = new ArrayList<>();
        recipeList.addAll(newList);
        notifyDataSetChanged();
    }
}
