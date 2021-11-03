package com.nuryadincjr.todolist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nuryadincjr.todolist.R;
import com.nuryadincjr.todolist.activity.ActionsActivity;
import com.nuryadincjr.todolist.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.swipeRefresh.setColorSchemeResources(R.color.orange);
        binding.swipeRefresh.setOnRefreshListener(() -> {
            binding.swipeRefresh.setRefreshing(false);
        });

        binding.fab.setOnClickListener(v -> startActivity(new Intent(getContext(), ActionsActivity.class)));

        return root;
    }
}