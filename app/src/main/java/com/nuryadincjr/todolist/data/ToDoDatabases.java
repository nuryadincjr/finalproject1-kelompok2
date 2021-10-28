package com.nuryadincjr.todolist.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.nuryadincjr.todolist.pojo.Constaint;

@Database(entities = {ToDo.class}, version = 1)
public abstract class ToDoDatabases extends RoomDatabase {
    public abstract ToDoDao toDoDao();

    private static ToDoDatabases instance;

    public static ToDoDatabases getInstance(Context context) {
        if (instance == null) {
            synchronized (ToDoDatabases.class) {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        ToDoDatabases.class, Constaint.DATABASE_NAME).build();
            }
        }
        return instance;
    }
}