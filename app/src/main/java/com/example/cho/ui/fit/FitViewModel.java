package com.example.cho.ui.fit;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.cho.database.MyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FitViewModel extends AndroidViewModel {

    private final MyRepository mRepository;
    private final LiveData<List<FitSession>> mFitSessions;
    private final LiveData<List<FitExercise>> mFitExercises;

    public FitViewModel(Application app) {
        super(app);
        mRepository = new MyRepository(app);
        mFitExercises = mRepository.getAllExercies();
        mFitSessions = mRepository.getAllSessions();

//        initialize_database();
    }

    private void initialize_database(){
        mRepository.deleteAllSessions();
    }

    public  LiveData<List<FitSession>> getFitSessionList(){
        return mFitSessions;
    }

    public  LiveData<List<FitExercise>> getFitExerciseList(){
        return mFitExercises;
    }

    public long add_update_FitSession(FitSession session){
        long ses_id = mRepository.insertSession(session);
        return ses_id;
    }

    public void removeFitSession(FitSession session){
        mRepository.deleteSession(session);
    }

    public boolean addFitExercise(FitExercise exercise){
        List<FitExercise> fitExercises = mFitExercises.getValue();
        for (FitExercise ex:fitExercises
        ) {
            if (ex.name.equals(exercise.name)){
                return false;
            }
        }
        mRepository.insertExercise(exercise);
        return true;
    };

    public void updateFitExercise(FitExercise exercise){
        mRepository.updateExercise(exercise);
    }

    public boolean setFitExercise(int position, FitExercise exercise){
        List<FitExercise> fitExercises = mFitExercises.getValue();
        for (int i=0; i<fitExercises.size();i++) {
            if (i!=position && fitExercises.get(i).name.equals(exercise.name)){
                return false;
            }
        }
        fitExercises.set(position, exercise);
        mRepository.updateExercise(exercise);
        return true;
    }

    public void removeFitExercise(int position){
        FitExercise exercise = mFitExercises.getValue().get(position);
        mRepository.deleteExercise(exercise);
    }
}