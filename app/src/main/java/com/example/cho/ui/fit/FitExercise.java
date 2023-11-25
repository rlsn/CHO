package com.example.cho.ui.fit;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
@Entity(tableName = "exercise_table")
public class FitExercise implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    @Expose
    public String name = "";
    @Expose
    public String typename = "";

    public float recent_weight = 10;
    public int recent_sets = 4;
    public int recent_reps = 12;
    public FitExercise(){}

    @Ignore
    public FitExercise(String name, String typename, float recent_weight, int recent_sets, int recent_reps){
        this.name = name;
        this.typename = typename;
        this.recent_weight = recent_weight;
        this.recent_sets = recent_sets;
        this.recent_reps = recent_reps;

    }
}
