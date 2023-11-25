package com.example.cho.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cho.R;
import com.example.cho.database.MyRepository;
import com.example.cho.ui.fit.FitSession;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final MyRepository mRepository;
    public List<FitSession> SessionsOfMonth;
    public YearMonth currentMonth;
    public HomeViewModel(Application app) {
        super(app);
        mRepository = new MyRepository(app);
        SessionsOfMonth = mRepository.getAllSessionsOfMonth(YearMonth.now());
        currentMonth = YearMonth.now();
    }
    public  List<FitSession> updateFitSessionListOfMonth(YearMonth yearMonth){
        SessionsOfMonth = mRepository.getAllSessionsOfMonth(yearMonth);
        currentMonth = yearMonth;
        return SessionsOfMonth;
    }
}