package com.nuryadincjr.todolist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.nuryadincjr.todolist.R;
import com.nuryadincjr.todolist.activity.ActionsActivity;
import com.nuryadincjr.todolist.data.ToDo;
import com.nuryadincjr.todolist.data.ToDoDatabases;
import com.nuryadincjr.todolist.databinding.FragmentHomeBinding;
import com.nuryadincjr.todolist.interfaces.ItemClickListener;
import com.nuryadincjr.todolist.pojo.Constaint;
import com.nuryadincjr.todolist.pojo.ToDoAdapter;
import com.nuryadincjr.todolist.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ToDoDatabases databases;
    private List<ToDo> toDoList;
    private List<ToDo> toDoListPin;
    private ToDoAdapter toDoAdapter;
    private ToDoAdapter toDoAdapterPin;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        databases = ToDoDatabases.getInstance(getContext());

        toDoList = new ArrayList<>();
        toDoListPin = new ArrayList<>();
        toDoAdapter = new ToDoAdapter(toDoList);
        toDoAdapterPin = new ToDoAdapter(toDoListPin);
        binding.rvToDo.setLayoutManager(new GridLayoutManager(getContext(), 1));
        binding.rvToDoPin.setLayoutManager(new GridLayoutManager(getContext(), 1));
        binding.rvToDo.setAdapter(toDoAdapter);
        binding.rvToDoPin.setAdapter(toDoAdapterPin);

        binding.swipeRefresh.setColorSchemeResources(R.color.orange);
        binding.swipeRefresh.setOnRefreshListener(() -> {
            getData();
            binding.swipeRefresh.setRefreshing(false);
        });

        binding.fab.setOnClickListener(v -> startActivity(new Intent(getContext(), ActionsActivity.class)));

        getData();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        AppExecutors.getInstance().mainThread().execute(() -> toDoList = databases.toDoDao().getAllToDoList());
        AppExecutors.getInstance().mainThread().execute(() -> toDoListPin = databases.toDoDao().getAllPin());

        toDoAdapter = new ToDoAdapter(toDoList);
        toDoAdapterPin = new ToDoAdapter(toDoListPin);

        if(toDoListPin.size() != 0) {
            binding.tvPin.setVisibility(View.VISIBLE);
            binding.rvToDoPin.setVisibility(View.VISIBLE);
            if(toDoList.size() != 0) binding.tvOther.setVisibility(View.VISIBLE);
            else binding.tvOther.setVisibility(View.GONE);
        } else {
            binding.tvPin.setVisibility(View.GONE);
            binding.tvOther.setVisibility(View.GONE);
            binding.rvToDoPin.setVisibility(View.GONE);
        }

        binding.rvToDoPin.setLayoutManager(new GridLayoutManager(getContext(), 1));
        binding.rvToDo.setLayoutManager(new GridLayoutManager(getContext(), 1));
        binding.rvToDoPin.setAdapter(toDoAdapterPin);
        binding.rvToDo.setAdapter(toDoAdapter);
        getAdapter(toDoAdapterPin, toDoListPin);
        getAdapter(toDoAdapter, toDoList);
        toDoAdapterPin.notifyDataSetChanged();
        toDoAdapter.notifyDataSetChanged();
    }

    private void getAdapter(ToDoAdapter adapter, List<ToDo> list) {
        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, final int position) {
                openMenuEditToolsClick(view, list.get(position));
            }

            @Override
            public void onLongClick(View view, final int position) {
                openMenuEditToolsLongClick(view, list.get(position));
            }
        });
    }

    private void openMenuEditToolsClick(View view, ToDo toDo) {
        switch (view.getId()) {
            case R.id.tvTitleTask:
            case R.id.tvDetailTask:
                startActivity(new Intent(getContext(), ActionsActivity.class)
                        .putExtra(Constaint.TITLE_CHANGE, toDo));
                break;
        }
    }

    private void openMenuEditToolsLongClick(View view, ToDo toDo) {
        switch (view.getId()) {
            case R.id.tvTitleTask:
            case R.id.tvDetailTask:
                openMenuEditPopup(view, toDo);
                break;
        }
    }

    public void openMenuEditPopup(View view, ToDo toDo) {
        PopupMenu menu = new PopupMenu(view.getContext(), view);
        menu.getMenuInflater().inflate(R.menu.menu_edit, menu.getMenu());
        menu.getMenu().findItem(R.id.actRestore).setVisible(false);

        if(toDo.isPin()) menu.getMenu().findItem(R.id.actPin).setTitle("Lepas sematan");

        menu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.actPin:
                    if(toDo.isPin()) getPopupSelected(toDo, false, false, false);
                    else getPopupSelected(toDo, true, false, false);
                    break;
                case R.id.actArsip:
                    getPopupSelected(toDo, false, false, true);
                    break;
                case R.id.actDelete:
                    getPopupSelected(toDo, false, true, false);
                    break;
                case R.id.actDeleteFix:
                    AppExecutors.getInstance().diskID().execute(() -> databases.toDoDao().delete(toDo));
                    break;
            }
            getData();
            return true;
        });
        menu.show();
    }

    private void getPopupSelected(ToDo toDo, boolean isPin, boolean isDelete, boolean isArchive) {
        toDo.setPin(isPin);
        toDo.setDelete(isDelete);
        toDo.setArcip(isArchive);
        AppExecutors.getInstance().diskID().execute(() -> databases.toDoDao().update(toDo));
    }
}