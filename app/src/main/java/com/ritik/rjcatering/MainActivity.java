package com.ritik.rjcatering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ritik.rjcatering.fragments.AccountFragment;
import com.ritik.rjcatering.fragments.CartFragment;
import com.ritik.rjcatering.fragments.HomeFragment;
import com.ritik.rjcatering.fragments.OrdersFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    public static InterstitialAd interstitialAd1,interstitialAd2,interstitialAd3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottom_nav);

        if (FoodDetailsActivity.FOOD_DETAILS_CART) {
            FoodDetailsActivity.FOOD_DETAILS_CART = false;
            getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new CartFragment()).commit();

            bottomNavigationView.setSelectedItemId(R.id.nav_cart);
        } else if (PlacedActivity.ORDER_DETAILS) {
            PlacedActivity.ORDER_DETAILS = false;
            getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new OrdersFragment()).commit();

            bottomNavigationView.setSelectedItemId(R.id.nav_my_orders);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();

            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        break;

                    case R.id.nav_cart:
                        fragment = new CartFragment();
                        break;

                    case R.id.nav_my_orders:
                        fragment = new OrdersFragment();
                        break;

                    case R.id.nav_my_account:
                        fragment = new AccountFragment();
                        break;


                }

                getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
                return true;
            }
        });


    }
}