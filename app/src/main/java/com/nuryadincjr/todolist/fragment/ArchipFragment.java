package com.nuryadincjr.todolist.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.nuryadincjr.todolist.R;
import com.nuryadincjr.todolist.databinding.FragmentArchipBinding;

public class ArchipFragment extends Fragment {
    private FragmentArchipBinding binding;

    public ArchipFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArchipBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.swipeRefresh.setColorSchemeResources(R.color.orange);
        binding.swipeRefresh.setOnRefreshListener(() -> {
            binding.swipeRefresh.setRefreshing(false);
        });

        return root;
    }
}