package com.example.cho.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cho.ui.fit.FitExercise;
import com.example.cho.ui.fit.FitSession;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FitExerciseDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FitExercise exercise);

    @Query("DELETE FROM exercise_table")
    void deleteAll();

    @Update
    void update(FitExercise exercise);

    @Delete
    void delete(FitExercise exercise);

    @Query("SELECT * FROM exercise_table ORDER BY name ASC")
    LiveData<List<FitExercise>> getExercises();
}
