package com.madrecipes.app.madrecipes.Data;

import android.provider.BaseColumns;

//Tay Hui Lin
//Team 6
//10178003J

//variables hold database table and column name
public class RecipeContract {

    private RecipeContract() {}

    public static final class RecipeEntry implements BaseColumns {
        public static final String TABLE_RECIPE = "TestRecipe";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_IMAGE = "Image";
    }

    public static final class StepsEntry implements BaseColumns {
        public static final String TABLE_STEPS = "TestSteps";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_STEPS = "Steps";
        public static final String COLUMN_RECIPE_ID = "RecipeID";
    }
}
