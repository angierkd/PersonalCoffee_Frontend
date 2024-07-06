package com.example.personalcoffee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.personalcoffee.Fragment.FriendListFragment;
import com.example.personalcoffee.Fragment.MyRecipeFragment;
import com.example.personalcoffee.Fragment.RecommendFragment;
import com.example.personalcoffee.databinding.ActivityLoginBinding;
import com.example.personalcoffee.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Fragment selectorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyRecipeFragment()).commit();

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.myrecipe:
                        selectorFragment = new MyRecipeFragment();
                        break;
                    case R.id.friendlist:
                        selectorFragment = new FriendListFragment();
                        break;
                    case R.id.recommend:
                        selectorFragment = new RecommendFragment();
                        break;

                }

                if(selectorFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, selectorFragment).commit();
                }
                return true;
            }
        });

    }
}