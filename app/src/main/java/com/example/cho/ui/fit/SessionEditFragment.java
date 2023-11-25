package com.example.cho.ui.fit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cho.MainActivity;
import com.example.cho.R;
import com.example.cho.databinding.FragmentSessionEditBinding;

import java.util.ArrayList;
import java.util.List;


public class SessionEditFragment extends Fragment {
    private Context mContext;
    private ViewGroup mContainer;
    private FitSession mSession;
    private int editIndex;
    private long sessionId;
    private Boolean isCreatingNew;
    private Boolean newCreated = true;

    private FitViewModel mFitViewModel;

    private FragmentSessionEditBinding binding;
    public SessionEditFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSessionEditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.mContainer = container;

        mFitViewModel = new ViewModelProvider(getActivity()).get(FitViewModel.class);


        isCreatingNew = getArguments().getBoolean("isNew");
        if (isCreatingNew){
            if (newCreated){
                newCreated=false;
                mSession = new FitSession();
            }
        } else {
            mSession = (FitSession) getArguments().getSerializable("session");
        }

        final TextView txtSessionDate = binding.textFitEditSessionDate;
        txtSessionDate.setText(mSession.GetDate());

        final TextView txtSessionTime = binding.textFitEditSessionTime;
        txtSessionTime.setText(mSession.GetTime());

        final Button addSubsession = binding.addSubsession;
        addSubsession.setOnClickListener(new View.OnClickListener() {
            // add button pressed
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();;
                bundle.putSerializable("session", mSession);
                Navigation.findNavController(v).navigate(R.id.action_sessionEditFragment_to_exerciseListFragment, bundle);
            }
        });

        final RecyclerView subsessionsView = binding.fitSubsessions;
        FitSubsessionViewAdapter fitSubsessionViewAdapter = new FitSubsessionViewAdapter(getActivity());
        subsessionsView.setAdapter(fitSubsessionViewAdapter);
        subsessionsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSession.GetSubSessions().observe(getViewLifecycleOwner(), new Observer<List<FitSubSession>>() {
            @Override
            public void onChanged(List<FitSubSession> subsessionList) {
                fitSubsessionViewAdapter.updateList(subsessionList);
            }
        });

        fitSubsessionViewAdapter.setOnDeleteListener(new FitSubsessionViewAdapter.OnDeleteListener() {
            // delete pressed
            @Override
            public void onClick(int position) {
                mSession.RemoveSubSession(position);
                mSession.id =mFitViewModel.add_update_FitSession(mSession);
            }
        });

        fitSubsessionViewAdapter.setOnConfirmListener(new FitSubsessionViewAdapter.OnConfirmListener() {
            // confirm pressed
            @Override
            public void onClick(int position, FitSubSession subsession) {
                mSession.SetSubSession(position, subsession);
                mSession.id = mFitViewModel.add_update_FitSession(mSession);
            }
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!isCreatingNew){
            inflater.inflate(R.menu.delete_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.fit_delete_exercise_dialog);
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
//                    mFitViewModel.removeFitSession(editIndex);
                    mFitViewModel.removeFitSession(mSession);
                    Navigation.findNavController(mContainer).popBackStack();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    dialog.dismiss();
                }
            });
            builder.create().show();
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

    @Override
    public void onDestroy() {
        if (isCreatingNew && mSession.subSessions.size()==0) {
            mFitViewModel.removeFitSession(mSession);
        }
        super.onDestroy();
    }
}