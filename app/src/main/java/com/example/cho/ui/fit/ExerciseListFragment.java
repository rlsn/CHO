package com.example.cho.ui.fit;

import static androidx.navigation.Navigation.findNavController;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.cho.MainActivity;
import com.example.cho.R;
import com.example.cho.databinding.FragmentExerciseListBinding;

import java.util.ArrayList;
import java.util.List;


public class ExerciseListFragment extends Fragment {
    private Context mContext;
    private ViewGroup mContainer;

    private FitViewModel mFitViewModel;
    private FragmentExerciseListBinding binding;

    public ExerciseListFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFitViewModel = new ViewModelProvider(getActivity()).get(FitViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentExerciseListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.mContainer = container;

        final RecyclerView exercisesView = binding.fitExercises;


        FitExerciseViewAdapter fitExerciseViewAdapter = new FitExerciseViewAdapter(getActivity(),getArguments().getBoolean("manage"));
        exercisesView.setAdapter(fitExerciseViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        exercisesView.setLayoutManager(layoutManager);
        mFitViewModel.getFitExerciseList().observe(getViewLifecycleOwner(), new Observer<List<FitExercise>>() {
            @Override
            public void onChanged(List<FitExercise> fitExerciseList) {
                fitExerciseViewAdapter.updateList(fitExerciseList);
            }
        });
        exercisesView.addItemDecoration(
                new DividerItemDecoration(
                        mContext,
                        layoutManager.getOrientation()
                )
        );

        fitExerciseViewAdapter.setOnClickListener(new FitExerciseViewAdapter.OnClickListener() {
            // exercise selected
            @Override
            public void onClick(int position) {
                FitExercise exercise = mFitViewModel.getFitExerciseList().getValue().get(position);
                mFitViewModel.updateFitExercise(exercise);

                FitSession session = (FitSession) getArguments().getSerializable("session");

                FitSubSession subSession = new FitSubSession(exercise, exercise.recent_sets,exercise.recent_reps,exercise.recent_weight);
                session.AddSubSession(subSession);

                session.id =mFitViewModel.add_update_FitSession(session);
                Navigation.findNavController(mContainer).popBackStack();
            }
        });
        return root;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.add_menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_add) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isNew",true);
            Navigation.findNavController(mContainer).navigate(
                    R.id.action_navigation_fit_exercise_list_to_exerciseEditFragment,bundle);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).hideNav();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}