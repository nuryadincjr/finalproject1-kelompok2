package com.nuryadincjr.todolist.activity;

import static com.nuryadincjr.todolist.util.AppExecutors.getInstance;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

public class ActionsActivity extends AppCompatActivity {

    private ActivityActionsBinding binding;
    private AdapterPreference adapterPreference;
    private ToDoDatabases databases;
    private ToDo toDo, data, dataView;
    private Menu menu;
    private boolean isPin, isArchip, isDelete, isInput;

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

        onInputListener(binding.etDescription);
        onInputListener(binding.etTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        menu.findItem(R.id.itemRestore).setVisible(false);
        this.menu = menu;

        if(data == null) menu.findItem(R.id.itemDeleteFix).setVisible(false);
        if(dataView != null) {
            menu.findItem(R.id.itemArsip).setVisible(false);
            menu.findItem(R.id.itemPin).setVisible(false);
            menu.findItem(R.id.itemDelete).setVisible(false);
            menu.findItem(R.id.itemDeleteFix).setVisible(true);
            menu.findItem(R.id.itemRestore).setVisible(true);
            menu.findItem(R.id.itemShare).setVisible(false);
        }

        isEdited(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.itemDelete:
                isDelete = true;
                onBackPressed();
                return true;
            case R.id.itemDeleteFix:
                if(data == null) data = dataView;
                getInstance().diskID().execute(() -> databases.toDoDao().delete(data));
                finish();
                return true;
            case R.id.itemRestore:
                dataView.setDelete(false);
                getInstance().diskID().execute(() -> databases.toDoDao().update(dataView));
                finish();
                return true;
            case R.id.itemShare:
                if(!data.getTitle().equals("") && !data.getDetails().equals(""))
                    adapterPreference.shareData("Title: " + data.getTitle() + "\n\n" + data.getDetails());
                else if(data.getTitle().equals("")) adapterPreference.shareData(data.getDetails());
                else if(data.getDetails().equals("")) adapterPreference.shareData(data.getTitle());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onInputListener(TextView view) {
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isInput = i2 > 0;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isEdited(menu);
            }
        });
    }

    private void isEdited(Menu menu) {
        if (isInput) {
            menu.findItem(R.id.itemDelete).setEnabled(true);
            menu.findItem(R.id.itemShare).setEnabled(true);
        } else {
            menu.findItem(R.id.itemDelete).setEnabled(false);
            menu.findItem(R.id.itemShare).setEnabled(false);
        }

        getTonggolButton(menu, R.id.itemArsip, R.layout.btn_archive,
                R.id.tbArchive, isArchip, Constaint.IS_ARCHIVE);
        getTonggolButton(menu, R.id.itemPin, R.layout.btn_pin,
                R.id.tb_Pin, isPin, Constaint.IS_PIN);
    }

    private void isData() {
        if(data != null) {
            getSupportActionBar().setTitle(Constaint.TITLE_CHANGE);
            String titile = data.getTitle();
            String description = data.getDetails();
            binding.etTitle.setText(titile);
            binding.etDescription.setText(description);
            isPin = data.isPin();
            isArchip = data.isArcip();
            isInput = !titile.isEmpty() || !description.isEmpty();

        } else if(dataView != null) {
            getSupportActionBar().setTitle(Constaint.TITLE_VIW_ONLY);
            binding.etTitle.setText(dataView.getTitle());
            binding.etDescription.setText(dataView.getDetails());
            binding.etTitle.setFocusable(false);
            binding.etDescription.setFocusable(false);

            isEditlable(binding.etTitle);
            isEditlable(binding.etDescription);
        } else getSupportActionBar().setTitle(Constaint.TITLE_ADD);
    }

    private void isEditlable(EditText editText) {
        editText.setOnClickListener(v -> Snackbar.make(v,
                getString(R.string.str_isedit), Snackbar.LENGTH_LONG)
                .setAction(Constaint.TITLE_RESTORE, view -> {
                    dataView.setDelete(false);
                    getInstance().diskID().execute(() -> databases.toDoDao().update(dataView));
                    finish();
                }).show());
    }

    private void actionCreateUpdate() {
        String titile = binding.etTitle.getText().toString();
        String description = binding.etDescription.getText().toString();

        if(isDelete) {
            isPin = false;
            isArchip = false;
        }

        toDo = new ToDo(0, titile, description, isPin, isArchip, isDelete, Constaint.time());

        if(dataView == null && (!titile.isEmpty() || !description.isEmpty())) {
            if(data != null) {
                toDo.setUid(data.getUid());

                if(!data.equals(toDo)) {
                    getInstance().diskID().execute(() -> databases.toDoDao().update(toDo));
                    Toast.makeText(this, getString(R.string.str_message_change), Toast.LENGTH_SHORT).show();
                }
            } else{
                getInstance().diskID().execute(() -> databases.toDoDao().insert(toDo));
                Toast.makeText(this, getString(R.string.str_message_save), Toast.LENGTH_SHORT).show();
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

        Log.d("LIA", String.valueOf(isInput));
        if(isInput) {
            tonggle.setEnabled(true);
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
        } else{
            tonggle.setEnabled(false);
        }
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