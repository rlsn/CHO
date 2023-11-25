package com.example.cho.ui.home;

import static com.kizitonwose.calendar.core.ExtensionsKt.daysOfWeek;
import static com.kizitonwose.calendar.core.ExtensionsKt.firstDayOfWeekFromLocale;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cho.MainActivity;
import com.example.cho.databinding.CalendarDayLayoutBinding;
import com.example.cho.databinding.FragmentHomeBinding;
import com.example.cho.ui.fit.FitSession;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.ViewContainer;

import java.time.DayOfWeek;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    HomeViewModel mHomeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mHomeViewModel =
                new ViewModelProvider(getActivity()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final CalendarView calendarView = binding.calenderHome;
        DayViewBinder dayViewBinder = new DayViewBinder(getContext(), mHomeViewModel);
        calendarView.setDayBinder(dayViewBinder);

        YearMonth currentMonth = YearMonth.now();
        YearMonth startMonth = currentMonth.minusMonths(100);  // Adjust as needed
        YearMonth endMonth = currentMonth.plusMonths(100);  // Adjust as needed

        DayOfWeek firstDayOfWeek = firstDayOfWeekFromLocale(); // Assuming this function is available

        calendarView.setup(startMonth, endMonth, firstDayOfWeek);
        calendarView.scrollToMonth(mHomeViewModel.currentMonth);

        MonthHeaderBinder monthHeaderBinder = new MonthHeaderBinder();
        calendarView.setMonthHeaderBinder(monthHeaderBinder);
        calendarView.setMonthScrollListener(new Function1<CalendarMonth, Unit>() {
            @Override
            public Unit invoke(CalendarMonth calendarMonth) {
                mHomeViewModel.updateFitSessionListOfMonth(calendarMonth.getYearMonth());
                return null;
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).showNav();
    }
}

