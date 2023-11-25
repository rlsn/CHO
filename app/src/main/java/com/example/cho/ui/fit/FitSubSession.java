package com.example.cho.ui.fit;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class FitSubSession implements Serializable {

    @Expose
    public FitExercise exercise;
    @Expose
    public float weight;
    @Expose
    public int sets;
    @Expose
    public int reps;

    public FitSubSession(){}

    public FitSubSession(FitExercise exercise, int sets, int reps, float weight){
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }
}
