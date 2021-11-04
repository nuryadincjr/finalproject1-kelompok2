package com.nuryadincjr.todolist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.nuryadincjr.todolist.R;
import com.nuryadincjr.todolist.util.*;
import com.nuryadincjr.todolist.data.*;
import com.nuryadincjr.todolist.pojo.Constaint;
import com.nuryadincjr.todolist.activity.ActionsActivity;
import com.nuryadincjr.todolist.databinding.FragmentHomeBinding;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ToDoDatabases databases;
    private LocalPreference localPreference;
    private AdapterPreference adapterPreference;
    private OptionMenuPreference optionMenuPreference;
    private boolean isView;
    private int spanCount;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);

        databases = ToDoDatabases.getInstance(getContext());
        localPreference = LocalPreference.getInstance(getContext());
        adapterPreference = AdapterPreference.getInstance(getContext());
        optionMenuPreference = OptionMenuPreference.getInstance(getContext());

        binding.swipeRefresh.setColorSchemeResources(R.color.orange);
        binding.swipeRefresh.setOnRefreshListener(() -> {
            getData();
            binding.swipeRefresh.setRefreshing(false);
        });

        binding.fab.setOnClickListener(v -> startActivity(new Intent(getContext(), ActionsActivity.class)));
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        optionMenuPreference.getOnCreateOptionsMenu(menu, inflater, isView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getData() {
        isView = localPreference.getPreferences().getBoolean(Constaint.IS_VIEW, isView);
        if(isView) spanCount = 2;
        else spanCount = 1;

        AppExecutors.getInstance().mainThread().execute(() -> {
            List<ToDo> toDoList = databases.toDoDao().getAllToDoList();
            List<ToDo> toDoListPin = databases.toDoDao().getAllPin();
            getActivity().runOnUiThread(() -> {
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

                adapterPreference.getAdapters(toDoList, binding.rvToDo, spanCount);
                adapterPreference.getAdapters(toDoListPin, binding.rvToDoPin, spanCount);
            });
        });
    }
}