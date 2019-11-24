package com.madrecipes.app.madrecipes.StepsScreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madrecipes.app.madrecipes.R;
import com.madrecipes.app.madrecipes.Data.Recipe;
import com.madrecipes.app.madrecipes.Data.Steps;

import java.util.List;

//Tay Hui Lin
//Team 6
//10178003J

//adapter
public class StepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Steps> stepsList;
    private Recipe recipe;

    //constructor
    public StepsAdapter(Context context, List<Steps> stepsList, Recipe recipe) {
        this.context = context;
        this.stepsList = stepsList;
        this.recipe = recipe;
    }

    //set up layout for each row
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == 1) { //if row is the first row, use header layour
             view = layoutInflater.inflate(R.layout.layout_steps_header, parent, false);
            return new HeaderViewHolder(view);
        } else { //use layout for steps
            view = layoutInflater.inflate(R.layout.layout_step, parent, false);
            return new StepViewHolder(view);
        }
    }

    //set data for row
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) { //if row is first, set recipe name and image
            String name = recipe.getRecipeName();
            byte[] image = recipe.getRecipeImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            ((HeaderViewHolder) holder).recipeName.setText(name);
            ((HeaderViewHolder) holder).imageRecipe.setImageBitmap(bitmap);
            return;
        } else { //if not, set steps of recipe
            Steps steps = stepsList.get(position - 1);
            String stepName = steps.getStep();
            ((StepViewHolder) holder).step.setText(stepName);
        }
    }

    //get size for steps
    //+ 1 because of adding header
    @Override
    public int getItemCount() {
        return stepsList.size() + 1;
    }

    //get view type depending on the row position
    @Override
    public int getItemViewType(int position) {
        if (position == 0) { //if first row
            return 1;
        } else { //if second row
            return 2;
        }
    }
}
