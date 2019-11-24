package com.madrecipes.app.madrecipes.Data;

//Tay Hui Lin
//Team 6
//10178003J

//recipe class
public class Recipe {
    private int id;
    private String recipeName;
    private byte[] recipeImage;

    //constructor
    public Recipe() {}

    public Recipe(int id, String recipeName, byte[] recipeImage) {
        this.id = id;
        this.recipeName = recipeName;
        this.recipeImage = recipeImage;
    }

    //getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public byte[] getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(byte[] recipeImage) {
        this.recipeImage = recipeImage;
    }
}
