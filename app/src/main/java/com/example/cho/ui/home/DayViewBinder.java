package com.example.cho.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import com.example.cho.R;
import com.example.cho.ui.fit.FitSession;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.MonthDayBinder;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DayViewBinder implements MonthDayBinder<DayViewContainer> {

    Context mContext;
    HomeViewModel mViewModel;
    public DayViewBinder(Context context, HomeViewModel viewModel){
        mContext = context;
        mViewModel = viewModel;
    }

    @NonNull
    @Override
    public DayViewContainer create(@NonNull View view) {
        return new DayViewContainer(view);
    }

    @Override
    public void bind(@NonNull DayViewContainer container, CalendarDay data) {
        container.textView.setText(String.valueOf(data.getDate().getDayOfMonth()));
        if (data.getPosition() == DayPosition.MonthDate) {
            container.textView.setTextColor(Color.BLACK);


            if (!YearMonth.from(mViewModel.currentMonth).equals(YearMonth.from(data.getDate()))){
                mViewModel.updateFitSessionListOfMonth(YearMonth.from(data.getDate()));
            }

            if (mViewModel.SessionsOfMonth!=null){
                for(FitSession session: mViewModel.SessionsOfMonth){
                    if(data.getDate().equals(session.date)) {
                        container.textView.setBackgroundColor(mContext.getColor(R.color.fit_session_base));

                        container.getView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.i("DayViewBinder","clicked on "+data.getDate().toString());
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("isNew",false);
                                bundle.putSerializable("session",session);
                                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_sessionEditFragment,bundle);
                            }
                        });

                    }
                }
            }

            if (data.getDate().equals(LocalDate.now())) {
                container.textView.setBackgroundColor(mContext.getColor(R.color.fit_subsession_base));
            }
        } else {
            container.textView.setTextColor(Color.GRAY);
        }
    }
}
