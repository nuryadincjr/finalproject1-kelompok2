package com.nuryadincjr.todolist.util;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nuryadincjr.todolist.R;
import com.nuryadincjr.todolist.data.ToDo;
import com.nuryadincjr.todolist.data.ToDoDatabases;
import com.nuryadincjr.todolist.interfaces.ItemClickListener;
import com.nuryadincjr.todolist.pojo.Constaint;
import com.nuryadincjr.todolist.pojo.ToDoAdapter;

import java.util.List;

public abstract class AdapterPreference {

    private static ToDoDatabases databases;
    private final Context context;
    private final LocalPreference localPreference;
    private ToDoAdapter toDoAdapter;

    public AdapterPreference(Context context) {
        this.context = context;
        localPreference = LocalPreference.getInstance(context);
        databases = ToDoDatabases.getInstance(context);
    }

    public void getAdapters(List<ToDo> list, RecyclerView view, int spanCount) {
        this.toDoAdapter = new ToDoAdapter(list);
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

    public void openMenuEditToolsClick(View view, ToDo toDo){ }

    private void openMenuEditToolsLongClick(View view, ToDo toDo) {
        switch (view.getId()) {
            case R.id.tvTitleTask:
            case R.id.tvDetailTask:
                openMenuEditPopup(view, toDo);
                break;
        }
    }

    public abstract void openMenuEditPopup(View view, ToDo toDo);

    public void getPopupSelected(ToDo toDo, boolean isPin, boolean isDelete, boolean isArchive) {
        toDo.setPin(isPin);
        toDo.setDelete(isDelete);
        toDo.setArcip(isArchive);
        AppExecutors.getInstance().diskID().execute(() -> {
            int result = databases.toDoDao().update(toDo);
            if(result != 0) setData();
        });
    }

    public void deleteFix(ToDo toDo) {
        AppExecutors.getInstance().diskID().execute(() -> {
            int result = databases.toDoDao().delete(toDo);
            if(result != 0) setData();
        });
    }

    public void shareData(String value) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, value);
        intent.setType("text/plain");

        if(intent.resolveActivity(context.getPackageManager()) != null) context.startActivity(intent);
    }

    public void getOnCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater, boolean isView) {
        inflater.inflate(R.menu.menu_view, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.itemSearch).getActionView();
        searchView.setQueryHint(context.getString(R.string.str_search_hint));
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                toDoAdapter.getFilter().filter(s);
                return false;
            }
        });

        ToggleButton tonggle = menu.findItem(R.id.itemView)
                .setActionView(R.layout.btn_view)
                .getActionView().findViewById(R.id.tbView);
        tonggle.setChecked(isView);
        tonggle.setOnCheckedChangeListener((compoundButton, b) -> {
            localPreference.getEditor().putBoolean(Constaint.IS_VIEW, b).apply();
            setData();
        });
    }

    public void setData() {}
}