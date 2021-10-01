package com.example.govote.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.govote.Model.Candidate;
import com.example.govote.R;

import java.util.List;

public class CandidateAdapter extends ArrayAdapter<Candidate> {
    private int mImageResourceId;

    public CandidateAdapter(@NonNull Context context, List<Candidate> wordList) {
        super(context, 0, wordList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.candidate_item, parent, false);
        }
        Candidate candidate = getItem(position);
        TextView miworkWord = (TextView) listItemView.findViewById(R.id.candidate_name);
        miworkWord.setText(candidate.getName());
        TextView defaultWord = (TextView) listItemView.findViewById(R.id.candidate_description);
        defaultWord.setText(candidate.getDescription());
        return listItemView;
    }
}