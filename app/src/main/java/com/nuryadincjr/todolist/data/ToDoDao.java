package com.nuryadincjr.todolist.data;


import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ToDoDao {
    @Query("SELECT * FROM toDo")
    List<ToDo> getAllToDo();

    @Insert(onConflict = REPLACE)
    Long insert(ToDo toDo);

    @Update
    int update(ToDo toDo);

    @Delete
    int delete(ToDo toDo);
}
