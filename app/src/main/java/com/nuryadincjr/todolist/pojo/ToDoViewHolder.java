package com.nuryadincjr.todolist.pojo;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.nuryadincjr.todolist.data.ToDo;
import com.nuryadincjr.todolist.databinding.ListItemBinding;

class ToDoViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {

    private final ToDoAdapter toDoAdapter;
    private final ListItemBinding binding;

    public ToDoViewHolder(ToDoAdapter toDoAdapter, ListItemBinding binding) {
        super(binding.getRoot());
        this.toDoAdapter = toDoAdapter;
        this.binding = binding;

        binding.tvDetailTask.setOnClickListener(this);
        binding.tvTitleTask.setOnClickListener(this);
        binding.tvDetailTask.setOnLongClickListener(this);
        binding.tvTitleTask.setOnLongClickListener(this);
    }

    public void setDataToView(ToDo toDo) {
        binding.tvTitleTask.setText(toDo.getTitle());
        binding.tvDetailTask.setText(toDo.getDetails());
        binding.tvEdited.setText(toDo.getLatestEdited());
    }

    @Override
    public void onClick(View view) {
        if (toDoAdapter.itemClickListener != null)
            toDoAdapter.itemClickListener.onClick(view, getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View view) {
        if (toDoAdapter.itemClickListener != null)
            toDoAdapter.itemClickListener.onLongClick(view, getAdapterPosition());
        return true;
    }
}