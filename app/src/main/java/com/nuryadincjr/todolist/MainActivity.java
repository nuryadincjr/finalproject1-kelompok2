package com.nuryadincjr.todolist;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nuryadincjr.todolist.data.ToDo;
import com.nuryadincjr.todolist.data.ToDoDatabases;
import com.nuryadincjr.todolist.databinding.ActivityMainBinding;
import com.nuryadincjr.todolist.pojo.Constaint;
import com.nuryadincjr.todolist.pojo.ToDoAdapter;
import com.nuryadincjr.todolist.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ToDoDatabases databases;
    private ToDoAdapter toDoAdapter;
    private List<ToDo> toDoList;
    private ToDo toDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toDo = new ToDo();
        toDoList = new ArrayList<>();
        databases = ToDoDatabases.getInstance(this);
        toDoAdapter = new ToDoAdapter(toDoList);

        binding.rvToDo.setAdapter(toDoAdapter);
        binding.rvToDo.setLayoutManager(new LinearLayoutManager(this));

        binding.swipeRefresh.setColorSchemeResources(android.R.color.holo_orange_dark);
        binding.swipeRefresh.setOnRefreshListener(() -> {
            getData();
            binding.swipeRefresh.setRefreshing(false);
        });

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actAdd) {
            dialogShows(Constaint.TITLE_ADD,
                    "What do you wont to do next?", toDo);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openMenuEditTools(View view, int position) {
        switch (view.getId()) {
            case R.id.btnTaskCompleted:
                deleteData(toDoList.get(position));
                break;
            case R.id.tvDetailTask:
                dialogShows(Constaint.TITLE_CHANGE,
                        "what would you change?", toDoList.get(position));
                break;
        }
    }

    private void dialogShows(String title, String message, ToDo toDo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);

        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        if(title.equals(Constaint.TITLE_ADD)) {
            builder.setPositiveButton("ADD", (dialog, which) -> {
                toDo.setTodolist(input.getText().toString());
                addData(toDo);
            });
        } else {
            input.setText(toDo.getTodolist());
            builder.setPositiveButton("CHANGE", (dialog, which) -> {
                toDo.setTodolist(input.getText().toString());
                updateData(toDo);
            });
        }

        builder.setView(input);
        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void getData() {
        AppExecutors.getInstance().mainThread().execute(() -> {
            toDoList = databases.toDoDao().getAllToDo();
            runOnUiThread(() -> {
                toDoAdapter = new ToDoAdapter(toDoList);
                binding.rvToDo.setLayoutManager(new LinearLayoutManager(this));
                binding.rvToDo.setAdapter(toDoAdapter);
                toDoAdapter.setItemClickListener(this::openMenuEditTools);
            });
        });
    }

    private void addData(ToDo toDo) {
        AppExecutors.getInstance().diskID().execute(() -> {
            Long result = databases.toDoDao().insert(toDo);
            crudData(toDo, 0, result, "menambah");
        });
    }

    private void updateData(ToDo toDo) {
        AppExecutors.getInstance().diskID().execute(() -> {
            int result = databases.toDoDao().update(toDo);
            crudData(toDo, result, 0, "mengubah");
        });
    }

    private void deleteData(ToDo toDo) {
        AppExecutors.getInstance().diskID().execute(() -> {
            int result = databases.toDoDao().delete(toDo);
            crudData(toDo, result, 0, "menghapus");
        });
    }

    private void crudData(ToDo toDo, int resultInt, long resultLong, String message) {
        int result = resultInt;
        if (resultLong != 0)
            result = (int) resultLong;

        final int finalResult = result;
        AppExecutors.getInstance().mainThread().execute(() -> {
            runOnUiThread(() -> {
                if (finalResult !=0) {
                    getData();
                    Toast.makeText(this, "Sukses "+ message +
                            toDo.getTodolist(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "Gagal " + message +
                            toDo.getTodolist(), Toast.LENGTH_SHORT).show();
            });
        });
    }
}