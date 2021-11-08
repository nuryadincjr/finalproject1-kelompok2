package com.nuryadincjr.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.*;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.nuryadincjr.todolist.activity.*;
import com.nuryadincjr.todolist.fragment.*;
import com.google.android.material.navigation.NavigationView;
import com.nuryadincjr.todolist.databinding.ActivityMainBinding;
import com.nuryadincjr.todolist.pojo.Constaint;

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
            binding.navigationView.setCheckedItem(R.id.homeMenu);
            getFragmentPage(new HomeFragment(),this);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeMenu:
                getFragmentPage(new HomeFragment(), this);
                getSupportActionBar().setTitle("My to do list");
                break;
            case R.id.archipMenu:
                getFragmentPage(new ArchipFragment(), this);
                getSupportActionBar().setTitle("Arsip");
                break;
            case R.id.trashMenu:
                getSupportActionBar().setTitle("Sampah");
                getFragmentPage(new TrashFragment(), this);
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

    public static boolean getFragmentPage(Fragment fragment, Context context) {
        if (fragment != null) {
            ((FragmentActivity)context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return true;
    }
}