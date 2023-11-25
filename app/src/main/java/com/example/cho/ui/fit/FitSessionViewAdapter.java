package com.example.cho.ui.fit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cho.R;
import com.example.cho.databinding.ListItemFitExerciseBinding;
import com.example.cho.databinding.ListItemFitSessionBinding;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FitSessionViewAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<FitSession> fitSessionList;
    private OnClickListener onClickListener;

    public FitSessionViewAdapter() {
        this.fitSessionList = new ArrayList<FitSession>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FitSessionViewAdapter.ViewHolder(ListItemFitSessionBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    private String getSummary(FitSession session){
        String summary = "";
        Map<String, Integer> counter = new HashMap<>();
        Collections.sort(session.subSessions, (FitSubSession o1,FitSubSession o2)->
                (o1.exercise.typename.compareTo(o2.exercise.typename)));
        for (FitSubSession subsession:session.subSessions
             ) {
            if(counter.containsKey(subsession.exercise.typename)){
                counter.put(subsession.exercise.typename,counter.get(subsession.exercise.typename)+subsession.sets);
            } else {
                counter.put(subsession.exercise.typename,subsession.sets);
            }
        }
        for (Map.Entry<String,Integer> entry : counter.entrySet()){
            summary += String.format("%s x %d\n",entry.getKey(),entry.getValue());
        }

        return summary.trim();
    }

    private List<FitSession> sort(List<FitSession> sessionList){
        Collections.sort(sessionList, new Comparator() {
            public int compare(Object o1, Object o2) {
                int sComp = ((FitSession)o1).date.compareTo(
                        ((FitSession)o2).date);
                if (sComp != 0) {
                    return sComp;
                }
                return ((FitSession)o1).time.compareTo(
                        ((FitSession)o2).time);
            }});
        Collections.reverse(sessionList);
        return sessionList;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FitSession session = fitSessionList.get(position);
        ViewHolder viewHolder= (ViewHolder) holder;

        final TextView txtView_datetime = viewHolder.binding.textFitSessionTime;
        final TextView txtView_summary = viewHolder.binding.textFitSessionSummary;

        txtView_datetime.setText(session.GetDate());
        txtView_summary.setText(getSummary(session));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("FitSessionViewAdapter","session selected");
                Bundle bundle = new Bundle();
                bundle.putBoolean("isNew",false);
                bundle.putSerializable("session",session);
                Navigation.findNavController(view).navigate(R.id.action_navigation_fit_to_sessionEditFragment,bundle);
                notifyDataSetChanged();
            }
        });
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position);
    }
    @Override
    public int getItemCount() {
        return fitSessionList.size();
    }

    public void updateList(final List<FitSession> fitSessionList) {
        this.fitSessionList.clear();
        this.fitSessionList.addAll(sort(fitSessionList));
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ListItemFitSessionBinding binding;
        public ViewHolder(@NonNull ListItemFitSessionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
