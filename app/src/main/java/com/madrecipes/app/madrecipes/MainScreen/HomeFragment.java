package com.madrecipes.app.madrecipes.MainScreen;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.madrecipes.app.madrecipes.R;
import com.madrecipes.app.madrecipes.AddScreen.AddActivity;
import com.madrecipes.app.madrecipes.Data.DBHelper;
import com.madrecipes.app.madrecipes.Data.Recipe;

import java.util.ArrayList;
import java.util.List;

//Tay Hui Lin
//Team 6
//10178003J

//home page to display all recipes
public class HomeFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {
    private View view;
    private RecyclerView recyclerView;
    private DBHelper dbHelper;
    private List<Recipe> recipeList = new ArrayList<>();
    private ItemAdapter itemAdapter;
    private SearchView searchView;
    private MenuItem searchMenuItem;

    private static final String TAG = "HomeFragment";

    //on create set layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.home_layout, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    //after view is created
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    //when view is resumed after stop
    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    //initialize
    private void init() {
        recyclerView = view.findViewById(R.id.lvRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        dbHelper = new DBHelper(getActivity());

        recipeList.addAll(dbHelper.getAllRecipe());

        itemAdapter = new ItemAdapter(getActivity(), recipeList);

        recyclerView.setAdapter(itemAdapter);

        floatingButton();
    }

    //floating action button
    private void floatingButton() {
        final FloatingActionButton floatingActionButton = view.findViewById(R.id.fabCreateRecipe);
        floatingActionButton.setOnClickListener(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        floatingActionButton.show();
                        break;
                    default:
                        floatingActionButton.hide();
                        break;
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    //refresh recycler view
    //by clearing the list and adding back the steps
    private void refresh() {
        recipeList.clear();
        recipeList.addAll(dbHelper.getAllRecipe());
        itemAdapter.notifyDataSetChanged();
    }

    //click on a recipe to go to recipe page
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.fabCreateRecipe) {
            Intent intent = new Intent(getActivity(), AddActivity.class);
            this.startActivity(intent);
        }
    }

    //create top menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

        //search view
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getActivity().getComponentName());
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchableInfo);
        searchView.setOnQueryTextListener(this);
    }

    //when icon in menu is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //search query is entered by pressing enter
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    //filter and display recipe as user is typing in search view
    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Recipe> newList = new ArrayList<>();
        for (Recipe recipe : recipeList) {
            String name = recipe.getRecipeName().toLowerCase();
            if (name.contains(newText)) {
                newList.add(recipe);
            }
        }
        itemAdapter.setFilter(newList);
        return true;
    }
}
