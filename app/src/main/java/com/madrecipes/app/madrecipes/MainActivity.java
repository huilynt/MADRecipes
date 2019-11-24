package com.madrecipes.app.madrecipes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.madrecipes.app.madrecipes.MainScreen.HomeFragment;
import com.madrecipes.app.madrecipes.ShoppingList.ShopListFragment;

//Muhd Ikmal Hakim Bin Abdullah
//Team 6
//10177616F

//contains bottom navigation bar and fragment to display the screens
public class MainActivity extends AppCompatActivity {

    //create the view and get manager for fragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

    //bottom navigation bar item select
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_home: //recipe page
                            selectedFragment = new HomeFragment();
                            break;

                        case R.id.nav_shoplist: //shopping list page
                            selectedFragment = new ShopListFragment();
                            break;

                        case R.id.nav_timer: //timer page
                            selectedFragment = new TimerFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

}
