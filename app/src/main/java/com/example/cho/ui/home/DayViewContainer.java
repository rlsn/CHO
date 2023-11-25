package com.example.cho.ui.home;

import android.view.View;
import android.widget.TextView;

import com.example.cho.databinding.CalendarDayLayoutBinding;
import com.kizitonwose.calendar.view.ViewContainer;

public class DayViewContainer extends ViewContainer {
    public TextView textView;

    public DayViewContainer(View view) {
        super(view);

        textView = CalendarDayLayoutBinding.bind(view).calendarDayText;
    }
}
