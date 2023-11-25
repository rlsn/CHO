package com.example.cho.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cho.ui.fit.FitExercise;
import com.example.cho.ui.fit.FitSession;
import com.example.cho.ui.fit.FitSubSession;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class MyRepository {

    private FitExerciseDao mExerciseDao;
    private FitSessionDao mSessionDao;

    private LiveData<List<FitExercise>> mAllExercises;
    private LiveData<List<FitSession>> mAllSessions;
    private LiveData<List<FitSession>> mRecentSessions;

    private LiveData<List<FitSubSession>> mAllSubSessions;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public MyRepository(Application application) {
        MyDataBase db = MyDataBase.getDatabase(application);
        mExerciseDao = db.exerciseDao();
        mSessionDao = db.sessionDao();

        mAllExercises = mExerciseDao.getExercises();
        mAllSessions = mSessionDao.getSessions();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<FitExercise>> getAllExercies() {
        return mAllExercises;
    }
    public LiveData<List<FitSubSession>> getAllSubsessions() { return mAllSubSessions; }

    public LiveData<List<FitSession>> getAllSessions() {
        return mAllSessions;
    }
    public List<FitSession> getAllSessionsOfMonth(YearMonth yearMonth) {
        return mSessionDao.getSessionsOfMonth(yearMonth.toString()+"%");
    }

    public void deleteEverything(){
        MyDataBase.databaseWriteExecutor.execute(() -> {
            mExerciseDao.deleteAll();
            mSessionDao.deleteAll();
        });
    }

    public void deleteAllSessions(){
        MyDataBase.databaseWriteExecutor.execute(() -> {
            mSessionDao.deleteAll();
        });
    }

    private final MutableLiveData<Long> id = new MutableLiveData<>();
    public long insertSession(FitSession session) {

        List<FitSession> itemsFromDB = mSessionDao.getItemById(session.id);
        if (itemsFromDB.isEmpty())
            id.setValue(mSessionDao.insert(session));
        else{
            mSessionDao.updateSubSessions(session.id, session.subSessions);
            id.setValue(session.id);
        }

        return id.getValue();
    }

    public void updateSession(FitSession session) {
        MyDataBase.databaseWriteExecutor.execute(() -> {
            mSessionDao.update(session);
        });
    }

    public void deleteSession(FitSession session) {
        MyDataBase.databaseWriteExecutor.execute(() -> {
            mSessionDao.delete(session);
        });
    }


    public void insertExercise(FitExercise exercise) {
        MyDataBase.databaseWriteExecutor.execute(() -> {
            mExerciseDao.insert(exercise);
        });
    }

    public void updateExercise(FitExercise exercise) {
        MyDataBase.databaseWriteExecutor.execute(() -> {
            mExerciseDao.update(exercise);
        });
    }

    public void deleteExercise(FitExercise exercise){
        MyDataBase.databaseWriteExecutor.execute(() -> {
            mExerciseDao.delete(exercise);
        });
    }
}
