package com.madrecipes.app.madrecipes.StepsScreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.madrecipes.app.madrecipes.Data.DBHelper;
import com.madrecipes.app.madrecipes.Data.Recipe;
import com.madrecipes.app.madrecipes.Data.Steps;
import com.madrecipes.app.madrecipes.EditScreen.EditActivity;
import com.madrecipes.app.madrecipes.R;

import java.util.ArrayList;
import java.util.List;

//Tay Hui Lin
//Team 6
//10178003J

//show a recipe with steps
public class RecipeActivity extends AppCompatActivity {
    private List<Steps> stepsList = new ArrayList<>();
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private StepsAdapter stepsAdapter;
    private String id;

    //oncreate
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        init();
    }

    //when activity is resume after stop
    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    //inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_steps, menu);
        return true;
    }

    //when icon of menu is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete: //click on bin icon
                confirmDelete();
                return true;
            case R.id.edit: //click on pencil icon
                editData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //dialog to confirm delete recipe
    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setMessage("Delete recipe?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    //open edit page to edit the recipe
    private void editData() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("recipeID", id);
        startActivity(intent);
    }

    //initializer
    private void init() {
        recyclerView = findViewById(R.id.lvRecyclerView);
        id = Integer.toString(getIntent().getExtras().getInt("id"));
        dbHelper = new DBHelper(this);
        getSteps();
    }

    //get all steps of the recipe
    private void getSteps() {
        if (dbHelper.getAllSteps(id) != null && dbHelper.getRecipeByID(id) != null ) {
            setStepsAdapter();
        } else {
            return;
        }
    }

    //set adapter of recyclerview
    private void setStepsAdapter() {
        stepsList.addAll(dbHelper.getAllSteps(id));
        Recipe recipe = dbHelper.getRecipeByID(id);

        stepsAdapter = new StepsAdapter(this, stepsList, recipe);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(stepsAdapter);
    }

    //delete recipe
    private void deleteData() {
        String id = Integer.toString(getIntent().getExtras().getInt("id"));
        dbHelper.deleteRecipe(id);
        finish();
        Toast("Recipe deleted!");
    }

    //refresh the steps
    //used for after updating recipe
    private void refresh() {
        stepsList.clear();
        setStepsAdapter();
    }

    //toast message
    private void Toast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }
}
