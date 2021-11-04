package com.nuryadincjr.todolist.util;

import android.content.Context;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;

import com.nuryadincjr.todolist.R;
import com.nuryadincjr.todolist.pojo.Constaint;

public class OptionMenuPreference {

    private AdapterPreference adapterPreference;
    private static OptionMenuPreference instance;
    private LocalPreference localPreference;

    public OptionMenuPreference(Context context) {
        localPreference = LocalPreference.getInstance(context);
        adapterPreference = AdapterPreference.getInstance(context);
    }

    public static OptionMenuPreference getInstance(Context context) {
        if(instance == null) {
            instance = new OptionMenuPreference(context);
        }
        return instance;
    }

    public void getOnCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater, boolean isView) {
        inflater.inflate(R.menu.menu_view, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.actSearch).getActionView();
        searchView.setQueryHint("Type here to search");
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapterPreference.getToDoAdapter().getFilter().filter(s);
                return false;
            }
        });

        ToggleButton tonggle = menu.findItem(R.id.actView)
                .setActionView(R.layout.btn_view)
                .getActionView().findViewById(R.id.btn_view);
        tonggle.setChecked(isView);
        tonggle.setOnCheckedChangeListener((compoundButton, b) -> {
            localPreference.getEditor().putBoolean(Constaint.IS_VIEW, b).apply();
//            getdata()
        });
    }
}