package com.example.cho.ui.home;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cho.R;
import com.kizitonwose.calendar.view.ViewContainer;

public class MonthViewContainer extends ViewContainer {
    public ViewGroup dayTitlesContainer;
    public TextView yearTitleContainer;
    public TextView monthTitleContainer;

    public MonthViewContainer(View view) {
        super(view);
        monthTitleContainer = view.findViewById(R.id.calendar_month_title);
        yearTitleContainer = view.findViewById(R.id.calendar_year_title);
        dayTitlesContainer = (ViewGroup) view.findViewById(R.id.calendar_day_titles);
    }
}