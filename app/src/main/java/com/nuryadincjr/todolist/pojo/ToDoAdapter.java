package com.nuryadincjr.todolist.pojo;

import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nuryadincjr.todolist.data.ToDo;
import com.nuryadincjr.todolist.databinding.ListItemBinding;
import com.nuryadincjr.todolist.interfaces.ItemClickListener;

import java.util.*;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoViewHolder> implements Filterable {

    public List<ToDo> data;
    public List<ToDo> filteredData;
    public ItemFilter itemFilter;
    public ItemClickListener itemClickListener;

    public ToDoAdapter(List<ToDo> data) {
        this.data = data;
        this.filteredData = new ArrayList<>(data);
        this.itemFilter = new ItemFilter(this);
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemBinding binding = ListItemBinding
                .inflate(LayoutInflater.from(parent.getContext()),
                        parent, false);

        return new ToDoViewHolder(this, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        holder.setDataToView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

}