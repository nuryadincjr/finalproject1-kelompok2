package com.nuryadincjr.todolist.interfaces;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.nuryadincjr.todolist.data.ToDo;

import java.util.List;

public interface AdatperPreference {
    void getAdapters(List<ToDo> list, RecyclerView view, int spanCount);
    void openMenuEditToolsClick(View view, ToDo toDo);
    void openMenuEditPopup(View view, ToDo toDo);
}