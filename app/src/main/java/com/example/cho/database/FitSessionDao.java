package com.example.cho.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cho.ui.fit.FitExercise;
import com.example.cho.ui.fit.FitSession;
import com.example.cho.ui.fit.FitSubSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Dao
public interface FitSessionDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(FitSession session);

    @Query("DELETE FROM session_table")
    void deleteAll();

    @Query("SELECT * FROM session_table ORDER BY date DESC, time DESC LIMIT 30")
    LiveData<List<FitSession>> getSessions();

    @Query("SELECT * FROM session_table WHERE date like :date ORDER BY date DESC, time DESC")
    List<FitSession> getSessionsOfMonth(String date);

    @Update
    void update(FitSession FitSession);

    @Delete
    void delete(FitSession Session);

    @Query("SELECT * from session_table WHERE id= :id")
    List<FitSession> getItemById(long id);

    @Query("UPDATE session_table SET subSessions = :newSubSessions WHERE id = :id")
    void updateSubSessions(long id, ArrayList<FitSubSession> newSubSessions);
}
