package com.nuryadincjr.todolist.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nuryadincjr.todolist.R;
import com.nuryadincjr.todolist.databinding.ActivityAboutBinding;
import com.nuryadincjr.todolist.util.AdapterPreference;

public class AboutActivity extends AppCompatActivity {

    private ActivityAboutBinding binding;
    private AdapterPreference adapterPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adapterPreference = AdapterPreference.getInstance(this);

        getPortofoloi(binding.tvPortofolo1, "https://github.com/amrimarihotjati");
        getPortofoloi(binding.tvPortofolo2, "https://github.com/nuryadincjr");
    }

    private void getPortofoloi(TextView view, String link) {
        view.setOnClickListener(v -> adapterPreference.shareData(link));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}