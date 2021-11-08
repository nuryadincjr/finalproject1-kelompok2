package com.nuryadincjr.todolist.activity;

import static com.nuryadincjr.todolist.util.AppExecutors.getInstance;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.nuryadincjr.todolist.R;
import com.nuryadincjr.todolist.data.ToDoDatabases;
import com.nuryadincjr.todolist.databinding.ActivitySettingsBinding;

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

        getBtnListener(binding.btnDataCount);
        getBtnListener(binding.btnDataList);
        getBtnListener(binding.btnDataPin);
        getBtnListener(binding.btnDataArchive);
        getBtnListener(binding.btnDataTrash);
    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
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
        getInstance().mainThread().execute(() -> {
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
        button.setOnClickListener(view -> getInstance().diskID().execute(() -> {
            switch (button.getId()) {
                case R.id.btnDataCount:
                    getDeleteOf(databases.toDoDao().deleteAll());
                    break;
                case R.id.btnDataList:
                    getDeleteOf(databases.toDoDao().deleteAllList());
                    break;
                case R.id.btnDataPin:
                    getDeleteOf(databases.toDoDao().deleteAllPin());
                    break;
                case R.id.btnDataArchive:
                    getDeleteOf(databases.toDoDao().deleteAllArchip());
                    break;
                case R.id.btnDataTrash:
                    getDeleteOf(databases.toDoDao().deleteAllTrash());
                    break;
            }
        }));
    }

    private void getDeleteOf(int deleteOf) {
        if (deleteOf != 0) getData();
    }
}