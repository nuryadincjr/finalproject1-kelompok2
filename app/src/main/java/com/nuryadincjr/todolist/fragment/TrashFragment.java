package com.nuryadincjr.todolist.fragment;

import android.widget.PopupMenu;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.nuryadincjr.todolist.R;
import com.nuryadincjr.todolist.pojo.*;
import com.nuryadincjr.todolist.util.*;
import com.nuryadincjr.todolist.data.*;
import com.nuryadincjr.todolist.interfaces.*;
import com.nuryadincjr.todolist.activity.ActionsActivity;
import com.nuryadincjr.todolist.databinding.FragmentTrashBinding;

import java.util.List;

public class TrashFragment extends Fragment implements AdatperPreference {

    private FragmentTrashBinding binding;
    private ToDoDatabases databases;
    private AdapterPreference adapterPreference;

    public TrashFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTrashBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);

        databases = ToDoDatabases.getInstance(getContext());
        adapterPreference = AdapterPreference.getInstance(getContext());

        binding.swipeRefresh.setOnRefreshListener(() -> {
            getData();
            binding.swipeRefresh.setRefreshing(false);
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_format, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.actFormat:
                AppExecutors.getInstance().diskID().execute(() -> databases.toDoDao().deleteAllTrash());
                getData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        AppExecutors.getInstance().mainThread().execute(() -> {
            List<ToDo> toDoList = databases.toDoDao().getAllTrash();
            getActivity().runOnUiThread(() -> {
                getAdapters(toDoList, binding.rvToDo, 1);
            });
        });
    }

    @Override
    public void getAdapters(List<ToDo> list, RecyclerView view, int spanCount) {
        ToDoAdapter toDoAdapter = new ToDoAdapter(list);
        view.setLayoutManager(new GridLayoutManager(view.getContext(), spanCount));
        view.setAdapter(toDoAdapter);
        getAdapterClick(toDoAdapter, list);
    }

    private void getAdapterClick(ToDoAdapter adapter, List<ToDo> list) {
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

    @Override
    public void openMenuEditToolsClick(View view, ToDo toDo) {
        switch (view.getId()) {
            case R.id.tvTitleTask:
            case R.id.tvDetailTask:
                startActivity(new Intent(getContext(), ActionsActivity.class)
                        .putExtra(Constaint.TITLE_VIW_ONLY, toDo));
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

    @Override
    public void openMenuEditPopup(View view, ToDo toDo) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_restore, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.actRestore:
                    toDo.setDelete(false);
                    AppExecutors.getInstance().diskID().execute(() -> databases.toDoDao().update(toDo));
                    break;
                case R.id.actDelete:
                    AppExecutors.getInstance().diskID().execute(() -> databases.toDoDao().delete(toDo));
                    break;
            }
            getData();
            return true;
        });
        popupMenu.show();
    }
}