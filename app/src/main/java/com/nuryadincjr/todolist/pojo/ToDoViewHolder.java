package com.nuryadincjr.todolist.pojo;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.nuryadincjr.todolist.data.ToDo;
import com.nuryadincjr.todolist.databinding.ListItemBinding;

class ToDoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ToDoAdapter toDoAdapter;
    private final ListItemBinding binding;

    public ToDoViewHolder(ToDoAdapter toDoAdapter, ListItemBinding binding) {
        super(binding.getRoot());
        this.toDoAdapter = toDoAdapter;
        this.binding = binding;

        binding.btnTaskCompleted.setOnClickListener(this);
        binding.tvDetailTask.setOnClickListener(this);
    }

    public void setDataToView(ToDo toDo) {
        binding.tvDetailTask.setText(toDo.getTodolist());
    }

    @Override
    public void onClick(View view) {
        if (toDoAdapter.itemClickListener != null)
            toDoAdapter.itemClickListener.onClick(view, getAdapterPosition());
    }
}
