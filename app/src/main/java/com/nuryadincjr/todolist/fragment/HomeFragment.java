package com.nuryadincjr.todolist.fragment;

import static com.nuryadincjr.todolist.util.AppExecutors.getInstance;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.nuryadincjr.todolist.R;
import com.nuryadincjr.todolist.activity.ActionsActivity;
import com.nuryadincjr.todolist.data.ToDo;
import com.nuryadincjr.todolist.data.ToDoDatabases;
import com.nuryadincjr.todolist.databinding.FragmentHomeBinding;
import com.nuryadincjr.todolist.pojo.Constaint;
import com.nuryadincjr.todolist.util.AdapterPreference;
import com.nuryadincjr.todolist.util.LocalPreference;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ToDoDatabases databases;
    private LocalPreference localPreference;
    private AdapterPreference adapterPreference;
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

        binding.swipeRefresh.setColorSchemeResources(R.color.orange);
        binding.swipeRefresh.setOnRefreshListener(() -> {
            getData();
            binding.swipeRefresh.setRefreshing(false);
        });

        binding.fab.setOnClickListener(v -> startActivity(new Intent(getContext(), ActionsActivity.class)));

        getAdapterPreference();
        return root;
    }

    @Override
    public void onResume() {
        getData();
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        adapterPreference.getOnCreateOptionsMenu(menu, inflater, isView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(Constaint.TITLE_BAR, "My to do list");
        super.onSaveInstanceState(outState);
    }

    private void getData() {
        isView = localPreference.getPreferences().getBoolean(Constaint.IS_VIEW, isView);
        if(isView) spanCount = 2;
        else spanCount = 1;

        getInstance().mainThread().execute(() -> {
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

                adapterPreference.getAdapters(toDoListPin, binding.rvToDoPin, spanCount);
                adapterPreference.getAdapters(toDoList, binding.rvToDo, spanCount);
            });
        });
    }

    public void getAdapterPreference() {
        adapterPreference = new AdapterPreference(getContext()) {
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
                            deleteFix(toDo);
                            break;
                        case R.id.actShare:
                            if(!toDo.getTitle().equals("") && !toDo.getDetails().equals(""))
                                shareData("Title: " + toDo.getTitle() + "\n\n" + toDo.getDetails());
                            else if(toDo.getTitle().equals("")) shareData(toDo.getDetails());
                            else if(toDo.getDetails().equals("")) shareData(toDo.getTitle());
                            break;
                    }
                    return true;
                });
                menu.show();
            }

            @Override
            public void openMenuEditToolsClick(View view, ToDo toDo) {
                switch (view.getId()) {
                    case R.id.tvTitleTask:
                    case R.id.tvDetailTask:
                        startActivity(new Intent(getContext(), ActionsActivity.class)
                                .putExtra(Constaint.TITLE_CHANGE, toDo));
                        break;
                }
            }

            @Override
            public void setData() { getData(); }
        };
    }
}