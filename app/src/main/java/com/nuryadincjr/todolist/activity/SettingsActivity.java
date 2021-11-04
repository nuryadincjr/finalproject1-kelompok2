package com.nuryadincjr.todolist.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.nuryadincjr.todolist.R;
import com.nuryadincjr.todolist.data.ToDoDatabases;
import com.nuryadincjr.todolist.databinding.ActivitySettingsBinding;
import com.nuryadincjr.todolist.util.AppExecutors;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;
    private ToDoDatabases databases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databases = ToDoDatabases.getInstance(this);
        getData();
        getBtnListener(binding.btnDataCount);
        getBtnListener(binding.btnDataList);
        getBtnListener(binding.btnDataPin);
        getBtnListener(binding.btnDataArchive);
        getBtnListener(binding.btnDataTrash);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        AppExecutors.getInstance().mainThread().execute(() -> {
            String alldata = String.valueOf(databases.toDoDao().getAllToDo().size());
            String listdata = String.valueOf(databases.toDoDao().getAllToDoList().size());
            String pindata = String.valueOf(databases.toDoDao().getAllPin().size());
            String archipdata = String.valueOf(databases.toDoDao().getAllArcip().size());
            String trashdata = String.valueOf(databases.toDoDao().getAllTrash().size());

            runOnUiThread(() -> {
                binding.tvDataCount.setText(alldata);
                binding.tvDataList.setText(listdata);
                binding.tvDataPin.setText(pindata);
                binding.tvDataArchive.setText(archipdata);
                binding.tvDataTrash.setText(trashdata);
            });
        });
    }

    private void getBtnListener(ImageButton button) {
        button.setOnClickListener(view -> {
            switch (button.getId()) {
                case R.id.btnDataCount:
                    AppExecutors.getInstance().diskID().execute(() -> databases.toDoDao().deleteAll());
                    break;
                case R.id.btnDataList:
                    AppExecutors.getInstance().diskID().execute(() -> databases.toDoDao().deleteAllList());
                    break;
                case R.id.btnDataPin:
                    AppExecutors.getInstance().diskID().execute(() -> databases.toDoDao().deleteAllPin());
                    break;
                case R.id.btnDataArchive:
                    AppExecutors.getInstance().diskID().execute(() -> databases.toDoDao().deleteAllArchip());
                    break;
                case R.id.btnDataTrash:
                    AppExecutors.getInstance().diskID().execute(() -> databases.toDoDao().deleteAllTrash());
                    break;
            }
            getData();
        });
    }
}