package com.nuryadincjr.todolist.data;

import androidx.room.*;

import static androidx.room.OnConflictStrategy.REPLACE;

import java.util.List;

@Dao
public interface ToDoDao {
    @Query("SELECT * FROM toDo")
    List<ToDo> getAllToDo();

    @Query("SELECT * FROM toDo WHERE is_pin = 0 AND is_arcip = 0 AND is_delete = 0 ORDER BY latest_edited DESC")
    List<ToDo> getAllToDoList();

    @Query("SELECT * FROM toDo WHERE is_delete = 1 ORDER BY latest_edited DESC")
    List<ToDo> getAllTrash();

    @Query("SELECT * FROM toDo WHERE is_arcip = 1 ORDER BY latest_edited DESC")
    List<ToDo> getAllArcip();

    @Query("SELECT * FROM toDo WHERE is_pin = 1 ORDER BY latest_edited DESC")
    List<ToDo> getAllPin();

    @Insert(onConflict = REPLACE)
    Long insert(ToDo toDo);

    @Update
    int update(ToDo toDo);

    @Delete
    int delete(ToDo toDo);

    @Query("DELETE FROM todo")
    int deleteAll();

    @Query("DELETE FROM todo where is_pin = 0 AND is_arcip = 0 AND is_delete = 0")
    int deleteAllList();

    @Query("DELETE FROM todo where is_pin = 1")
    int deleteAllPin();

    @Query("DELETE FROM todo where is_arcip = 1")
    int deleteAllArchip();

    @Query("DELETE FROM todo where is_delete = 1")
    int deleteAllTrash();
}