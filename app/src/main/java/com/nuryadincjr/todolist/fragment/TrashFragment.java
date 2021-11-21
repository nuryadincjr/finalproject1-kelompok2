package com.nuryadincjr.todolist.fragment;

import static com.nuryadincjr.todolist.util.AppExecutors.getInstance;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.nuryadincjr.todolist.R;
import com.nuryadincjr.todolist.activity.ActionsActivity;
import com.nuryadincjr.todolist.data.ToDo;
import com.nuryadincjr.todolist.data.ToDoDatabases;
import com.nuryadincjr.todolist.databinding.FragmentTrashBinding;
import com.nuryadincjr.todolist.pojo.Constaint;
import com.nuryadincjr.todolist.util.AdapterPreference;

import java.util.List;

public class TrashFragment extends Fragment {

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

        if(savedInstanceState != null) {
            String titleBar = savedInstanceState.getString(Constaint.TITLE_BAR);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(titleBar);
        }

        databases = ToDoDatabases.getInstance(getContext());

        binding.swipeRefresh.setColorSchemeResources(R.color.orange);
        binding.swipeRefresh.setOnRefreshListener(() -> {
            getData();
            binding.swipeRefresh.setRefreshing(false);
        });

        getAdapterPreference();
        return root;
    }

    @Override
    public void onResume() {
        getData();
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_format, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemFormat) {
            getInstance().diskID().execute(() -> databases.toDoDao().deleteAllTrash());
            getData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(Constaint.TITLE_BAR, getString(R.string.str_trash_menu));
        super.onSaveInstanceState(outState);
    }

    private void getData() {
        getInstance().mainThread().execute(() -> {
            List<ToDo> toDoList = databases.toDoDao().getAllTrash();
            getActivity().runOnUiThread(() ->
                    adapterPreference.getAdapters(toDoList, binding.rvTodo, 1));
        });
    }

    private void getAdapterPreference() {
        adapterPreference = new AdapterPreference(getContext()) {
            @Override
            public void openMenuEditPopup(View view, ToDo toDo) {
                PopupMenu menu = new PopupMenu(view.getContext(), view);
                menu.getMenuInflater().inflate(R.menu.menu_restore, menu.getMenu());

                if(toDo.isPin()) menu.getMenu().findItem(R.id.itemPin)
                        .setTitle(getString(R.string.str_unpin));
                else if(toDo.isArcip()) menu.getMenu().findItem(R.id.itemArsip)
                        .setTitle(getString(R.string.str_unarchip));

                menu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.itemRestore:
                        getPopupSelected(toDo, false, false, false);
                        break;
                    case R.id.itemDeleteFix:
                        deleteFix(toDo);
                        break;
                    }
                    return true;
                });
                menu.show();
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

            @Override
            public void setData() {
                getData();
            }
        };
    }
}


