package com.example.cho.ui.meal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cho.databinding.FragmentMealBinding;

public class MealFragment extends Fragment {

    private FragmentMealBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MealViewModel mealViewModel =
                new ViewModelProvider(this).get(MealViewModel.class);

        binding = FragmentMealBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMeal;
        mealViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}