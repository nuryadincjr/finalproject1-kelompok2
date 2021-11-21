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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.nuryadincjr.todolist.R;
import com.nuryadincjr.todolist.activity.ActionsActivity;
import com.nuryadincjr.todolist.data.ToDo;
import com.nuryadincjr.todolist.data.ToDoDatabases;
import com.nuryadincjr.todolist.databinding.FragmentArchipBinding;
import com.nuryadincjr.todolist.pojo.Constaint;
import com.nuryadincjr.todolist.util.AdapterPreference;
import com.nuryadincjr.todolist.util.LocalPreference;

import java.util.List;

public class ArchipFragment extends Fragment {

    private FragmentArchipBinding binding;
    private ToDoDatabases databases;
    private AdapterPreference adapterPreference;
    private LocalPreference localPreference;
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

        if(savedInstanceState != null) {
            String titleBar = savedInstanceState.getString(Constaint.TITLE_BAR);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(titleBar);
        }

        databases = ToDoDatabases.getInstance(getContext());
        localPreference = LocalPreference.getInstance(getContext());

        binding.swipeRefresh.setColorSchemeResources(R.color.orange);
        binding.swipeRefresh.setOnRefreshListener(() -> {
            getData();
            binding.swipeRefresh.setRefreshing(false);
        });

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
        outState.putString(Constaint.TITLE_BAR, getString(R.string.str_archip_menu));
        super.onSaveInstanceState(outState);
    }

    private void getData() {
        isView = localPreference.getPreferences().getBoolean(Constaint.IS_VIEW, isView);
        if(isView) spanCount = 2;
        else spanCount = 1;

        getInstance().mainThread().execute(() -> {
            List<ToDo> toDoList = databases.toDoDao().getAllArcip();
            getActivity().runOnUiThread(() -> adapterPreference
                    .getAdapters(toDoList, binding.rvTodo, spanCount));
        });
    }

    private void getAdapterPreference() {
        adapterPreference = new AdapterPreference(getContext()) {
            @Override
            public void openMenuEditPopup(View view, ToDo toDo) {
                PopupMenu menu = new PopupMenu(view.getContext(), view);
                menu.getMenuInflater().inflate(R.menu.menu_edit, menu.getMenu());
                menu.getMenu().findItem(R.id.itemRestore).setVisible(false);

                if(toDo.isPin()) menu.getMenu().findItem(R.id.itemPin)
                        .setTitle(getString(R.string.str_unpin));
                else if(toDo.isArcip()) menu.getMenu().findItem(R.id
                        .itemArsip).setTitle(getString(R.string.str_unarchip));

                menu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.itemPin:
                            if(toDo.isPin()) getPopupSelected(toDo, false, false, false);
                            else getPopupSelected(toDo, true, false, false);
                            break;
                        case R.id.itemArsip:
                            if(toDo.isArcip()) getPopupSelected(toDo, false, false, false);
                            else getPopupSelected(toDo, false, false, true);
                            break;
                        case R.id.itemDelete:
                            getPopupSelected(toDo, false, true, false);
                            break;
                        case R.id.itemDeleteFix:
                            deleteFix(toDo);
                            break;
                        case R.id.itemShare:
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