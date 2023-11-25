package com.example.cho.ui.home;

import static com.kizitonwose.calendar.core.ExtensionsKt.daysOfWeek;
import static com.kizitonwose.calendar.core.ExtensionsKt.firstDayOfWeekFromLocale;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;

import java.time.DayOfWeek;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class MonthHeaderBinder implements MonthHeaderFooterBinder<MonthViewContainer> {
    @NonNull
    @Override
    public MonthViewContainer create(@NonNull View view) {
        return new MonthViewContainer(view);
    }

    @Override
    public void bind(MonthViewContainer container, CalendarMonth data) {
        List<DayOfWeek> daysOfWeek = daysOfWeek(firstDayOfWeekFromLocale());

        for (int index = 0; index < container.dayTitlesContainer.getChildCount(); index++) {
            View childView = container.dayTitlesContainer.getChildAt(index);
            if (childView instanceof TextView) {
                TextView textView = (TextView) childView;
                DayOfWeek dayOfWeek = daysOfWeek.get(index);
                String title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault());
                textView.setText(title);
            }
        }

        container.monthTitleContainer.setText(data.getYearMonth().getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
        container.yearTitleContainer.setText(String.valueOf(data.getYearMonth().getYear()));
    }
}