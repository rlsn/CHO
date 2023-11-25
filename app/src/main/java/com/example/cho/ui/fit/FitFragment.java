package com.example.cho.ui.fit;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cho.MainActivity;
import com.example.cho.R;
import com.example.cho.databinding.FragmentFitBinding;

import java.util.ArrayList;
import java.util.List;

public class FitFragment extends Fragment {

    private FragmentFitBinding binding;
    private Context mContext;
    private ViewGroup mContainer;
    private FitViewModel mFitViewModel;

    public FitFragment(){
        setHasOptionsMenu(true);
    }

    // Initialise it from onAttach()
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mFitViewModel = new ViewModelProvider(getActivity()).get(FitViewModel.class);
        this.mContainer = container;

        binding = FragmentFitBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Button addSession = binding.addFitSession;
        ExerciseListFragment exerciseListFragment = new ExerciseListFragment();
        addSession.setOnClickListener(new OnClickListener() {
            // add new session
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isNew",true);
                Navigation.findNavController(v).navigate(R.id.action_navigation_fit_to_sessionEditFragment,bundle);
            }
        });

        final RecyclerView fitSessionsView = binding.fitSessions;
        FitSessionViewAdapter fitSessionViewAdapter = new FitSessionViewAdapter();
        fitSessionsView.setAdapter(fitSessionViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        fitSessionsView.setLayoutManager(linearLayoutManager);
        mFitViewModel.getFitSessionList().observe(getViewLifecycleOwner(), new Observer<List<FitSession>>() {
            @Override
            public void onChanged(List<FitSession> fitSessionList) {
                fitSessionViewAdapter.updateList(fitSessionList);
                fitSessionsView.scrollToPosition(0);
            }
        });

        return root;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fit_menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_fit_manage_exercise) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("manage",true);
            Navigation.findNavController(mContainer).navigate(R.id.action_navigation_fit_to_exerciseListFragment,bundle);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).showNav();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}