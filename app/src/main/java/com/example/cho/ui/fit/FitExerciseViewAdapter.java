package com.example.cho.ui.fit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cho.R;
import com.example.cho.databinding.ListItemFitExerciseBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FitExerciseViewAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<FitExercise> fitExerciseList;
    private OnClickListener onClickListener;
    private int selected_index = -1;
    private Activity activity;
    private boolean manage;
    public FitExerciseViewAdapter(Activity activity, boolean manage) {
        this.fitExerciseList = new ArrayList<FitExercise>();
        this.manage = manage;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ListItemFitExerciseBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FitExercise exercise = fitExerciseList.get(position);
        ViewHolder viewHolder= (ViewHolder) holder;

        final TextView txtView_name = viewHolder.binding.textFitExerciseName;
        final TextView txtView_typename = viewHolder.binding.textFitExerciseType;
        final EditText inputWeight = viewHolder.binding.exerciseEditWeightInput;
        final EditText inputSets = viewHolder.binding.exerciseEditSetsInput;
        final EditText inputReps = viewHolder.binding.exerciseEditRepsInput;
        final View menutxt = viewHolder.binding.exerciseDetailText;
        final View menubutton = viewHolder.binding.exerciseDetailButtons;

        txtView_name.setText(exercise.name);
        txtView_typename.setText(exercise.typename);
        inputWeight.setText(String.valueOf(exercise.recent_weight));
        inputSets.setText(String.valueOf(exercise.recent_sets));
        inputReps.setText(String.valueOf(exercise.recent_reps));

        if (position==selected_index){
            if (manage){
                menutxt.setVisibility(View.GONE);
            } else {
                menutxt.setVisibility(View.VISIBLE);
            }
            menubutton.setVisibility(View.VISIBLE);
        } else {
            menutxt.setVisibility(View.GONE);
            menubutton.setVisibility(View.GONE);
        }

        if (position>0 && fitExerciseList.get(position-1).typename.equals(exercise.typename)){
            txtView_typename.setVisibility(View.GONE);
        } else {
            txtView_typename.setVisibility(View.VISIBLE);
        }

        inputWeight.setOnFocusChangeListener(new editTextOnFocusChangeListener());
        inputSets.setOnFocusChangeListener(new editTextOnFocusChangeListener());
        inputReps.setOnFocusChangeListener(new editTextOnFocusChangeListener());

        final ImageView imgView_edit = viewHolder.binding.fitExerciseEdit;
        imgView_edit.setOnClickListener(new View.OnClickListener() {
            // edit button is clicked
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isNew",false);
                    bundle.putInt("index",holder.getAdapterPosition());
                    Navigation.findNavController(view).navigate(
                            R.id.action_navigation_fit_exercise_list_to_exerciseEditFragment,bundle);
                }
            }
        );

        final ImageView imgView_select = viewHolder.binding.fitExerciseSelect;
        if (manage){
            imgView_select.setVisibility(View.GONE);
        } else {
            imgView_select.setOnClickListener(new View.OnClickListener() {
                // select button is clicked
                @Override
                public void onClick(View view) {
                    exercise.recent_weight = Float.parseFloat(getInputString(inputWeight));
                    exercise.recent_sets = Integer.parseInt(getInputString(inputSets));
                    exercise.recent_reps = Integer.parseInt(getInputString(inputReps));
                    onClickListener.onClick(viewHolder.getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }

        txtView_name.setOnClickListener(new View.OnClickListener() {
            // item is clicked
            @Override
            public void onClick(View view) {
                if (selected_index==viewHolder.getAdapterPosition()){
                    selected_index =-1;
                } else {
                    selected_index = viewHolder.getAdapterPosition();
                }
                notifyDataSetChanged();
            }
        });
    }


    public class editTextOnFocusChangeListener implements View.OnFocusChangeListener{
        @Override
        public void onFocusChange(View view, boolean b) {
            EditText v= (EditText)view;
            InputMethodManager imm =  (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (b){
                v.setHint(v.getText());
                v.setText("");
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            } else {
                if (v.getText().toString().equals("")) {
                    v.setText(v.getHint());
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private String getInputString(EditText v){
        String s = v.getText().toString();
        if (s.equals("")){
            return v.getHint().toString();
        }
        return s;
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position);
    }
    @Override
    public int getItemCount() {
        return fitExerciseList.size();
    }


    private List<FitExercise> sort(List<FitExercise> subsessionList){
        Collections.sort(subsessionList, new Comparator() {
            public int compare(Object o1, Object o2) {
                int sComp = ((FitExercise)o1).typename.compareTo(
                        ((FitExercise)o2).typename);
                if (sComp != 0) {
                    return sComp;
                }
                return ((FitExercise)o1).name.compareTo(
                        ((FitExercise)o2).name);
            }});
        return subsessionList;
    }

    public void updateList(final List<FitExercise> fitExerciseList) {
        this.fitExerciseList.clear();
        this.fitExerciseList.addAll(sort(fitExerciseList));
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ListItemFitExerciseBinding binding;
        public ViewHolder(@NonNull ListItemFitExerciseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
