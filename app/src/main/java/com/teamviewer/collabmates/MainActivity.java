package com.teamviewer.collabmates;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private Map<Integer, Fragment> fragmentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        fragmentMap = new HashMap<>();
        fragmentMap.put(R.id.menu_feed, new FeedFragment());
        fragmentMap.put(R.id.menu_create, new CreateFragment());
        fragmentMap.put(R.id.menu_deals, new DealsFragment());
        fragmentMap.put(R.id.menu_withdraw, new WithdrawFragment());
        fragmentMap.put(R.id.menu_profile, new ProfileFragment());

        setDefaultFragment(fragmentMap.get(R.id.menu_feed));

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = fragmentMap.get(item.getItemId());

            if (selectedFragment != null) {
                if (item.getItemId() == R.id.menu_create) {
                    selectedFragment = new CreateFragment();
                }

                setDefaultFragment(selectedFragment);
            }

            return true;
        });
    }

    private void setDefaultFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout1, fragment);
        fragmentTransaction.commit();
    }
}
