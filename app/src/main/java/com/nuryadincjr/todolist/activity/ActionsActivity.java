package com.nuryadincjr.todolist.activity;

import static com.nuryadincjr.todolist.util.AppExecutors.getInstance;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.nuryadincjr.todolist.R;
import com.nuryadincjr.todolist.data.ToDo;
import com.nuryadincjr.todolist.data.ToDoDatabases;
import com.nuryadincjr.todolist.databinding.ActivityActionsBinding;
import com.nuryadincjr.todolist.pojo.Constaint;
import com.nuryadincjr.todolist.util.AdapterPreference;

import java.util.Date;

public class ActionsActivity extends AppCompatActivity {

    private ActivityActionsBinding binding;
    private ToDoDatabases databases;
    private AdapterPreference adapterPreference;
    private ToDo toDo, data, dataView;
    private boolean isPin;
    private boolean isArchip;
    private boolean isDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding = ActivityActionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databases = ToDoDatabases.getInstance(this);
        getAdapterPreference();


        data = getIntent().getParcelableExtra(Constaint.TITLE_CHANGE);
        dataView = getIntent().getParcelableExtra(Constaint.TITLE_VIW_ONLY);

        isData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        menu.findItem(R.id.actRestore).setVisible(false);

        if(data == null) menu.findItem(R.id.actDeleteFix).setVisible(false);
        if(dataView != null) {
            menu.findItem(R.id.actArsip).setVisible(false);
            menu.findItem(R.id.actPin).setVisible(false);
            menu.findItem(R.id.actDelete).setVisible(false);
            menu.findItem(R.id.actDeleteFix).setVisible(true);
            menu.findItem(R.id.actRestore).setVisible(true);
            menu.findItem(R.id.actShare).setVisible(false);
        }

        getTonggolButton(menu, R.id.actArsip, R.layout.btn_archive,
                R.id.btn_archive, isArchip, Constaint.IS_ARCHIVE);
        getTonggolButton(menu, R.id.actPin, R.layout.btn_pin,
                R.id.btn_pin, isPin, Constaint.IS_PIN);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.actDelete:
                isDelete = true;
                onBackPressed();
                return true;
            case R.id.actDeleteFix:
                if(data == null) data = dataView;
                getInstance().diskID().execute(() -> databases.toDoDao().delete(data));
                finish();
                return true;
            case R.id.actRestore:
                dataView.setDelete(false);
                getInstance().diskID().execute(() -> databases.toDoDao().update(dataView));
                finish();
                return true;
            case R.id.actShare:
                if(!data.getTitle().equals("") && !data.getDetails().equals(""))
                    adapterPreference.shareData("Title: " + data.getTitle() + "\n\n" + data.getDetails());
                else if(data.getTitle().equals("")) adapterPreference.shareData(data.getDetails());
                else if(data.getDetails().equals("")) adapterPreference.shareData(data.getTitle());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void isData() {
        if(data != null) {
            getSupportActionBar().setTitle(Constaint.TITLE_CHANGE);
            binding.tvTitle.setText(data.getTitle());
            binding.tvDescription.setText(data.getDetails());
            isPin = data.isPin();
            isArchip = data.isArcip();

        } else if(dataView != null) {
            getSupportActionBar().setTitle(Constaint.TITLE_VIW_ONLY);
            binding.tvTitle.setText(dataView.getTitle());
            binding.tvDescription.setText(dataView.getDetails());
            binding.tvTitle.setFocusable(false);
            binding.tvDescription.setFocusable(false);

            isEditlable(binding.tvTitle);
            isEditlable(binding.tvDescription);
        } else
            getSupportActionBar().setTitle(Constaint.TITLE_ADD);

    }

    private void isEditlable(EditText editText) {
        editText.setOnClickListener(v -> Snackbar.make(v,
                "Tidak dapat mengedit dalam sampah", Snackbar.LENGTH_LONG)
                .setAction(Constaint.TITLE_RESTORE, view -> {
                    dataView.setDelete(false);
                    getInstance().diskID().execute(() -> databases.toDoDao().update(dataView));
                    finish();
                }).show());
    }

    private void actionCreateUpdate() {
        String titile = binding.tvTitle.getText().toString();
        String description = binding.tvDescription.getText().toString();

        if(isDelete) {
            isPin = false;
            isArchip = false;
        }

        toDo = new ToDo(0, titile, description, isPin, isArchip, isDelete, time());

        if(dataView == null && (!titile.equals("") || !description.equals(""))) {
            if(data != null) {
                toDo.setUid(data.getUid());

                if(!data.equals(toDo)) {
                    getInstance().diskID().execute(() -> databases.toDoDao().update(toDo));
                    Toast.makeText(this, "Data diubah", Toast.LENGTH_SHORT).show();
                }

            } else{
                getInstance().diskID().execute(() -> databases.toDoDao().insert(toDo));
                Toast.makeText(this, "Data disimpan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        actionCreateUpdate();
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getTonggolButton(Menu menu, int findItem, int actionView,
                                  int findViewById, boolean isBoolean, String item) {

        ToggleButton tonggle = menu.findItem(findItem)
                .setActionView(actionView)
                .getActionView()
                .findViewById(findViewById);

        tonggle.setChecked(isBoolean);

        tonggle.setOnCheckedChangeListener((compoundButton, b) -> {
            if(item.equals(Constaint.IS_PIN)) {
                isPin = b;
                isArchip = false;

                String message = "Data disematkan";
                if(!isPin) message = "Sematan dilepas";

                Snackbar.make(compoundButton, message, Snackbar.LENGTH_LONG)
                        .setAction(Constaint.TITLE_RESTORE, null).show();

            } else if(item.equals(Constaint.IS_ARCHIVE)){
                isArchip = b;
                String message = "Data diarsipkan";
                if(isPin) {
                    message = "Data diarsipkan dan tidak dipasangi pin";
                    isPin = false;
                }

                if(!isArchip) message = "Data batal diarsipkan";
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    private String time() {
        return DateFormat.format(Constaint.DATE_FORMAT, new Date()).toString();
    }

    private void getAdapterPreference() {
        adapterPreference = new AdapterPreference(this) {
            @Override
            public void openMenuEditPopup(View view, ToDo toDo) {
            }

            @Override
            public void shareData(String value) {
                super.shareData(value);
            }
        };
    }
}