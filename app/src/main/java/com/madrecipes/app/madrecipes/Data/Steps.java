package com.madrecipes.app.madrecipes.Data;

//Tay Hui Lin
//Team 6
//10178003J

//steps class
public class Steps {
    private int id;
    private String step;
    private int recipeId;

    //constructor
    public Steps() {}

    public Steps(int id, String step, int recipeId) {
        this.id = id;
        this.step = step;
        this.recipeId = recipeId;
    }

    //getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
