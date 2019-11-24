package com.madrecipes.app.madrecipes.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madrecipes.app.madrecipes.Data.RecipeContract.*;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

//Tay Hui Lin
//Team 6
//10178003J

//database helper
//sqliteassethelper is to get external database to the database folder in phone
public class DBHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "Recipe.db";
    private static final int DATABASE_VERSION = 1;

    //constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //get all recipe from database and put in a list
    public List<Recipe> getAllRecipe() {
        List<Recipe> recipes = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + RecipeEntry.TABLE_RECIPE + " ORDER BY " + RecipeEntry.COLUMN_ID + " ASC";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(Integer.parseInt(cursor.getString(0)));
                recipe.setRecipeName(cursor.getString(1));
                byte[] blob = cursor.getBlob(2);
                recipe.setRecipeImage(blob);
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }

        sqLiteDatabase.close();
        return recipes;
    }

    //get all steps from a recipe using id and put in a list
    public List<Steps> getAllSteps(String id) {
        List<Steps> steps = new ArrayList<>();

        String selectQuery = "SELECT * FROM " +
                StepsEntry.TABLE_STEPS + " WHERE " +
                StepsEntry.COLUMN_RECIPE_ID + " = " + id;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Steps step = new Steps();

                step.setId(Integer.parseInt(cursor.getString(0)));
                step.setStep(cursor.getString(1));
                step.setRecipeId(Integer.parseInt(cursor.getString(2)));
                steps.add(step);

            } while (cursor.moveToNext());
            cursor.close();
        } else {
            steps = null;
        }
        sqLiteDatabase.close();
        return steps;
    }

    //add a recipe to database's recipe table with recipename, steplist, and image
    public boolean addRecipe(String recipeName, List<String> stepList, byte[] image) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeEntry.COLUMN_NAME, recipeName);
        contentValues.put(RecipeEntry.COLUMN_IMAGE, image);

        long id = sqLiteDatabase.insert(RecipeEntry.TABLE_RECIPE, null, contentValues);

        addSteps(stepList, String.valueOf(id));

        sqLiteDatabase.close();

        if (id != -1) {
            return true;
        } else {
            return false;
        }
    }

    //add all steps from steplist to database's steps table
    //id is to use as foreign key
    public void addSteps(List<String> stepList, String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        for (int i = 0; i < stepList.size(); i++) {
            String step = stepList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(StepsEntry.COLUMN_STEPS, step);
            contentValues.put(StepsEntry.COLUMN_RECIPE_ID, id);
            sqLiteDatabase.insert(StepsEntry.TABLE_STEPS, null, contentValues);
        }
    }

    //delete recipe using id
    public void deleteRecipe(String id) {
        String query = "SELECT * FROM " + RecipeEntry.TABLE_RECIPE +
                " WHERE " + RecipeEntry.COLUMN_ID + " = " + id;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        deleteSteps(id);

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            sqLiteDatabase.delete(RecipeEntry.TABLE_RECIPE,
                    RecipeEntry.COLUMN_ID + " = ?",
                    new String[]{cursor.getString(0)});
            cursor.close();
        }
        sqLiteDatabase.close();
    }

    //delete step that has the id as foreign key
    public void deleteSteps(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(StepsEntry.TABLE_STEPS,
                StepsEntry.COLUMN_RECIPE_ID + " = ?",
                new String[]{id});
    }

    //get a single recipe using id
    public Recipe getRecipeByID(String id) {
        Recipe recipe = new Recipe();
        String selectQuery = "SELECT * FROM " +
                RecipeEntry.TABLE_RECIPE + " WHERE " +
                RecipeEntry.COLUMN_ID + " = " + id;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                recipe.setId(Integer.parseInt(cursor.getString(0)));
                recipe.setRecipeName(cursor.getString(1));
                byte[] blob = cursor.getBlob(2);
                recipe.setRecipeImage(blob);
            } while (cursor.moveToNext());
        }

        sqLiteDatabase.close();
        return recipe;
    }

    //update existing recipe
    //update recipe name and image
    //delete all steps associated with the id
    //add the new list of steps with id as foreign key
    public void updateRecipe(Recipe recipe, List<String> stepList) {
        String id = Integer.toString(recipe.getId());

        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeEntry.COLUMN_NAME, recipe.getRecipeName());
        contentValues.put(RecipeEntry.COLUMN_IMAGE, recipe.getRecipeImage());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.update(RecipeEntry.TABLE_RECIPE, contentValues, RecipeEntry.COLUMN_ID + " = ?", new String[]{id});

        sqLiteDatabase.delete(StepsEntry.TABLE_STEPS,
                StepsEntry.COLUMN_RECIPE_ID + " = ?",
                new String[]{id});

        addSteps(stepList, id);

        sqLiteDatabase.close();
    }
}
