package com.example.cho.ui.fit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cho.R;
import com.example.cho.databinding.FragmentExerciseEditBinding;

import java.util.Objects;


public class ExerciseEditFragment extends Fragment {
    private Context mContext;
    private ViewGroup mContainer;
    private FitExercise mExercise;
    private int editIndex;
    private Boolean isCreatingNew;
    private FitViewModel mFitViewModel;

    private FragmentExerciseEditBinding binding;
    public ExerciseEditFragment() {
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
        binding = FragmentExerciseEditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.mContainer = container;

        mFitViewModel = new ViewModelProvider(getActivity()).get(FitViewModel.class);


        isCreatingNew = getArguments().getBoolean("isNew");
        if (isCreatingNew){
            mExercise = new FitExercise();
        } else {
            editIndex = getArguments().getInt("index");
            mExercise = mFitViewModel.getFitExerciseList().getValue().get(editIndex);
        }

        final TextView inputName = binding.exerciseEditNameInput;
        final TextView inputType = binding.exerciseEditTypeInput;
        if (mExercise!=null){
            inputName.setText(mExercise.name);
            inputType.setText(mExercise.typename);
        }

        final Button saveExercise = binding.saveExercise;
        saveExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExercise.name = inputName.getText().toString();
                mExercise.typename = inputType.getText().toString();

                if(mExercise.name.equals("") || mExercise.typename.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.fit_exercise_edit_false_dialog);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                } else {
                    if (isCreatingNew){
                        if (mFitViewModel.addFitExercise(mExercise)){
                            Navigation.findNavController(v).popBackStack();
                        } else {
                            show_repeat_dialog();
                        }
                    } else {
                        if (mFitViewModel.setFitExercise(editIndex,mExercise)){
                            Navigation.findNavController(v).popBackStack();
                        } else {
                            show_repeat_dialog();
                        }
                    }
                }
            }
        });

        return root;
    }

    private void show_repeat_dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.fit_exercise_edit_repeat_dialog);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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
                    mFitViewModel.removeFitExercise(editIndex);
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}