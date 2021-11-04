package com.nuryadincjr.todolist.util;

import android.content.*;
import android.view.View;
import android.widget.PopupMenu;

import androidx.recyclerview.widget.*;

import com.nuryadincjr.todolist.R;
import com.nuryadincjr.todolist.pojo.*;
import com.nuryadincjr.todolist.data.*;
import com.nuryadincjr.todolist.interfaces.*;
import com.nuryadincjr.todolist.activity.ActionsActivity;

import java.util.List;

public class AdapterPreference implements AdatperPreference {

    private static AdapterPreference instance;
    private static ToDoDatabases databases;
    private  Context context;
    private ToDoAdapter toDoAdapter;

    public AdapterPreference(Context context) {
        this.context = context;
        databases = ToDoDatabases.getInstance(context);
    }

    public static AdapterPreference getInstance(Context context) {
        if(instance == null) {
            instance = new AdapterPreference(context);
        }
        return instance;
    }

    @Override
    public void getAdapters(List<ToDo> list, RecyclerView view, int spanCount) {
        toDoAdapter = new ToDoAdapter(list);
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
                context.startActivity(new Intent(context, ActionsActivity.class)
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

    @Override
    public void openMenuEditPopup(View view, ToDo toDo) {
        PopupMenu menu = new PopupMenu(view.getContext(), view);
        menu.getMenuInflater().inflate(R.menu.menu_edit, menu.getMenu());
        menu.getMenu().findItem(R.id.actRestore).setVisible(false);

        if(toDo.isPin()) menu.getMenu().findItem(R.id.actPin).setTitle("Lepas sematan");
        else  if(toDo.isArcip()) menu.getMenu().findItem(R.id.actArsip).setTitle("Lepas arsipan");

        menu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.actPin:
                    if(toDo.isPin()) getPopupSelected(toDo, false, false, false);
                    else getPopupSelected(toDo, true, false, false);
                    break;
                case R.id.actArsip:
                    if(toDo.isArcip()) getPopupSelected(toDo, false, false, false);
                    else getPopupSelected(toDo, false, false, true);
                    break;
                case R.id.actDelete:
                    getPopupSelected(toDo, false, true, false);
                    break;
                case R.id.actDeleteFix:
                    AppExecutors.getInstance().mainThread().execute(() -> databases.toDoDao().delete(toDo));
                    break;
                case R.id.actShare:

                    if(!toDo.getTitle().equals("") && !toDo.getDetails().equals(""))
                        shareData("Title: " + toDo.getTitle() + "\n\n" + toDo.getDetails());
                    else if(toDo.getTitle().equals("")) shareData(toDo.getDetails());
                    else if(toDo.getDetails().equals("")) shareData(toDo.getTitle());
                    break;
            }
//            getData();
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

    public ToDoAdapter getToDoAdapter() {
        return toDoAdapter;
    }

    public void shareData(String value) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, value);
        intent.setType("text/plain");

        if(intent.resolveActivity(context.getPackageManager()) != null) context.startActivity(intent);
    }
}