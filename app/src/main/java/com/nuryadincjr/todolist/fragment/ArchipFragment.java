package com.nuryadincjr.todolist.fragment;

import android.os.Bundle;
import android.view.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.nuryadincjr.todolist.R;
import com.nuryadincjr.todolist.data.*;
import com.nuryadincjr.todolist.util.*;
import com.nuryadincjr.todolist.pojo.Constaint;
import com.nuryadincjr.todolist.databinding.FragmentArchipBinding;

import java.util.List;

public class ArchipFragment extends Fragment {

    private FragmentArchipBinding binding;
    private ToDoDatabases databases;
    private LocalPreference localPreference;
    private OptionMenuPreference optionMenuPreference;
    private AdapterPreference adapterPreference;
    private boolean isView;
    private int spanCount;

    public ArchipFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArchipBinding.inflate(inflater, container, false);
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
            List<ToDo> toDoList = databases.toDoDao().getAllArcip();
            getActivity().runOnUiThread(() -> adapterPreference
                    .getAdapters(toDoList, binding.rvToDo, spanCount));
        });
    }
}