package com.nuryadincjr.todolist.pojo;

import android.text.format.DateFormat;

import java.util.Date;

public class Constaint {
    public static final int NUMBER_OF_THREADS = 3;
    public static final String DATABASE_NAME = "studentdb";
    public static final String DATE_FORMAT = "dd/MM/yyy hh:mm:ss a";
    public static final String TITLE_ADD = "Add new task";
    public static final String TITLE_BAR = "Title Bars";
    public static final String TITLE_CHANGE = "Change task";
    public static final String TITLE_VIW_ONLY = "View only";
    public static final String TITLE_RESTORE = "Pulihkan";
    public static final String IS_PIN = "PIN";
    public static final String IS_VIEW = "VIEW";
    public static final String IS_ARCHIVE = "ARCHIVE";

    public static String time() {
        return DateFormat.format(DATE_FORMAT, new Date()).toString();
    }
}