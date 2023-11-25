package com.example.cho.ui.fit;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.OnReceiveContentListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cho.R;
import com.example.cho.databinding.ListItemFitSubsessionBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class FitSubsessionViewAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<FitSubSession> subsessionList;
    private OnConfirmListener onConfirmListener;
    private OnDeleteListener onDeleteListener;
    private int selected_index = -1;
    private Activity activity;
    public FitSubsessionViewAdapter(Activity activity) {
        this.subsessionList = new ArrayList<FitSubSession>();
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ListItemFitSubsessionBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false));
    }



    /**
     * Hides the soft keyboard
     */

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FitSubSession subsession = subsessionList.get(position);
        ViewHolder viewHolder= (ViewHolder) holder;
        final TextView txtView_name = viewHolder.binding.textFitSubsessionExerciseName;
        final TextView txtView_typename = viewHolder.binding.textFitSubsessionExerciseType;
        final EditText inputWeight = viewHolder.binding.exerciseEditWeightInput;
        final EditText inputSets = viewHolder.binding.exerciseEditSetsInput;
        final EditText inputReps = viewHolder.binding.exerciseEditRepsInput;
        final View menu = viewHolder.binding.subsessionEditMenu;
        final TextView details = viewHolder.binding.subsessionDetails;

        txtView_name.setText(subsession.exercise.name);
        txtView_typename.setText(subsession.exercise.typename);
        inputWeight.setText(String.valueOf(subsession.weight));
        inputSets.setText(String.valueOf(subsession.sets));
        inputReps.setText(String.valueOf(subsession.reps));

        if (subsession.weight!=0){
            details.setText(String.format("%.2f kg %d 组 x %d",subsession.weight,subsession.sets,subsession.reps));
        } else {
            details.setText(String.format("%d 组 x %d",subsession.sets,subsession.reps));
        }

        // appearance
        if (position==selected_index){
            menu.setVisibility(View.VISIBLE);
            details.setVisibility(View.GONE);
        } else {
            menu.setVisibility(View.GONE);
            details.setVisibility(View.VISIBLE);
        }

        if (position>0 && subsessionList.get(position-1).exercise.typename.equals(subsession.exercise.typename)){
            txtView_typename.setVisibility(View.GONE);
        }
        if (position>0 && subsessionList.get(position-1).exercise.name.equals(subsession.exercise.name)){
            txtView_name.setVisibility(View.GONE);
        }

        // edittext convenience
        inputWeight.setOnFocusChangeListener(new editTextOnFocusChangeListener());
        inputSets.setOnFocusChangeListener(new editTextOnFocusChangeListener());
        inputReps.setOnFocusChangeListener(new editTextOnFocusChangeListener());


        final ImageView imgView_delete = viewHolder.binding.subsessionDelete;
        imgView_delete.setOnClickListener(new View.OnClickListener() {
            // delete button is clicked
                @Override
                public void onClick(View view) {
                    int position = viewHolder.getAdapterPosition();
                    selected_index =-1;
                    subsessionList.remove(position);
                    onDeleteListener.onClick(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, subsessionList.size());
                }
            }
        );

        final ImageView imgView_confirm = viewHolder.binding.subsessionConfirm;
        imgView_confirm.setOnClickListener(new View.OnClickListener() {
                  // confirm button is clicked
                  @Override
                  public void onClick(View view) {
                      view.requestFocus();
                      subsession.weight = Float.parseFloat(getInputString(inputWeight));
                      subsession.sets = Integer.parseInt(getInputString(inputSets));
                      subsession.reps = Integer.parseInt(getInputString(inputReps));
                      onConfirmListener.onClick(viewHolder.getAdapterPosition(), subsession);
                      selected_index =-1;
                      notifyDataSetChanged();
                  }
              }
        );

        final ImageView imgView_addSet = viewHolder.binding.subsessionAddSet;
        imgView_addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputSets.setText(String.valueOf(Integer.parseInt(getInputString(inputSets))+1));
            }
        });

        details.setOnClickListener(new View.OnClickListener() {
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
    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }
    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public interface OnConfirmListener {
        void onClick(int position, FitSubSession subsession);
    }
    public interface OnDeleteListener {
        void onClick(int position);
    }


    @Override
    public int getItemCount() {
        return subsessionList.size();
    }

    private List<FitSubSession> sort(List<FitSubSession> subsessionList){
        Collections.sort(subsessionList, new Comparator() {
            public int compare(Object o1, Object o2) {
                        int sComp = ((FitSubSession)o1).exercise.typename.compareTo(
                                ((FitSubSession)o2).exercise.typename);
                        if (sComp != 0) {
                            return sComp;
                        }
                        return ((FitSubSession)o1).exercise.name.compareTo(
                                ((FitSubSession)o2).exercise.name);
                    }});
        return subsessionList;
    }

    public void updateList(final List<FitSubSession> subsessionList) {
        this.subsessionList.clear();
        this.subsessionList.addAll(sort(subsessionList));
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ListItemFitSubsessionBinding binding;
        public ViewHolder(@NonNull ListItemFitSubsessionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
