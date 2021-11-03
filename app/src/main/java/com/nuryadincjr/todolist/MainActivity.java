package com.nuryadincjr.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.nuryadincjr.todolist.activity.AboutActivity;
import com.nuryadincjr.todolist.activity.SearchActivity;
import com.nuryadincjr.todolist.activity.SettingsActivity;
import com.nuryadincjr.todolist.databinding.ActivityMainBinding;
import com.nuryadincjr.todolist.fragment.ArchipFragment;
import com.nuryadincjr.todolist.fragment.HomeFragment;
import com.nuryadincjr.todolist.fragment.TrashFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        new ActionBarDrawerToggle(this,
                binding.drawerLayout, binding.toolbar,
                R.string.navigation_open, R.string.navigation_close).syncState();

        binding.navigationView.setNavigationItemSelectedListener(this);
        if(savedInstanceState == null) {
            getFragmentPage(new HomeFragment());
            binding.navigationView.setCheckedItem(R.id.homeMenu);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actView:
                layoutSeting();
                return true;
            case R.id.actSearch:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeMenu:
                getFragmentPage(new HomeFragment());
                getSupportActionBar().setTitle("My to do list");
                binding.navigationView.setCheckedItem(R.id.homeMenu);
                break;
            case R.id.archipMenu:
                getFragmentPage(new ArchipFragment());
                getSupportActionBar().setTitle("Arsip");
                binding.navigationView.setCheckedItem(R.id.archipMenu);
                break;
            case R.id.trashMenu:
                getFragmentPage(new TrashFragment());
                getSupportActionBar().setTitle("Sampah");
                binding.navigationView.setCheckedItem(R.id.trashMenu);
                break;
            case R.id.settingsMenu:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.aboutMenu:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean getFragmentPage(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return true;
    }

    private void layoutSeting() {
//        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
//        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return toDoAdapter.getItemViewType(position);
//            }
//        });
//        binding.rvToDo.setLayoutManager(mLayoutManager);
//        binding.rvToDo.setAdapter(toDoAdapter);
    }
}