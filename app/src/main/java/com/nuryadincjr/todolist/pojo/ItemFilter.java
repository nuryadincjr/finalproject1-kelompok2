package com.nuryadincjr.todolist.pojo;

import android.widget.Filter;

import com.nuryadincjr.todolist.data.ToDo;

import java.util.*;

public class ItemFilter extends Filter {
    private final ToDoAdapter toDoAdapter;

    public ItemFilter(ToDoAdapter toDoAdapter) {
        this.toDoAdapter = toDoAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        List<ToDo> filterList = new ArrayList<>();
        if (constraint.toString().isEmpty()) {
            filterList.addAll(toDoAdapter.filteredData);
        } else {
            for (ToDo toDo: toDoAdapter.filteredData) {
                if (toDo.getTitle().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                        toDo.getDetails().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    filterList.add(toDo);
                }
            }
        }

        FilterResults results = new FilterResults();
        results.values = filterList;
        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        toDoAdapter.data.clear();
        toDoAdapter.data.addAll((Collection<? extends ToDo>) results.values);
        toDoAdapter.notifyDataSetChanged();
    }
}