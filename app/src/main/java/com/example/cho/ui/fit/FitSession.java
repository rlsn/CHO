package com.example.cho.ui.fit;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Entity(tableName = "session_table")
public class FitSession implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public LocalDate date = LocalDate.now();

    public LocalTime time = LocalTime.now();

    public ArrayList<FitSubSession> subSessions = new ArrayList<>();

    @Ignore
    public MutableLiveData<ArrayList<FitSubSession>> mSubSessions = new MutableLiveData<>();


    public FitSession(){}

    public void AddSubSession(FitExercise exercise, int sets,int reps,float weight){
        subSessions.add(new FitSubSession(exercise,sets,reps,weight));
        mSubSessions.setValue((subSessions));
    }
    public void AddSubSession(FitSubSession subSession){
        subSessions.add(subSession);
        mSubSessions.setValue((subSessions));
    }

    public void SetSubSession(int pos, FitSubSession subSession){
        subSessions.set(pos, subSession);
        mSubSessions.setValue((subSessions));
    }

    public void RemoveSubSession(int pos){
        subSessions.remove(pos);
        mSubSessions.setValue((subSessions));
    }

    public MutableLiveData<ArrayList<FitSubSession>> GetSubSessions(){
        mSubSessions.setValue((subSessions));
        return mSubSessions;
    }

    public String GetDateTime(){
        return GetDate() +" " +GetTime();
    }
    public String GetDate(){
        return date.format(DateTimeFormatter.ofPattern("M月 d日 - yyyy"));
    }
    public String GetTime(){
        return time.format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
    }
    @Override
    public boolean equals(Object ses){
        return this.id==((FitSession)ses).id;
    }

    @Override
    public int hashCode(){
        return Long.hashCode(this.id);
    }

    public static String jsonify_subsessions(ArrayList<FitSubSession> subs){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        return gson.toJson(subs);
    }

    public static ArrayList<FitSubSession> subsessions_fromJson(String s){
        Gson gson = new Gson();
        Type listOfMyClassObject = new TypeToken<ArrayList<FitSubSession>>() {}.getType();
        return gson.fromJson(s, listOfMyClassObject);
    }
}
