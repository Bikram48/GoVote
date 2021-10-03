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
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CandidateAdapter extends ArrayAdapter<Candidate> {
    private int mImageResourceId;
    private TextView candidateName,candidateDescription;
    private CircleImageView candidatePicture;
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
        candidateName = (TextView) listItemView.findViewById(R.id.candidate_name);
        candidateName.setText(candidate.getName());
        candidateDescription= (TextView) listItemView.findViewById(R.id.candidate_description);
        candidateDescription.setText(candidate.getDescription());
        candidatePicture=(CircleImageView) listItemView.findViewById(R.id.candidatePicture);
        Picasso.get().load(candidate.getImageUrl())
                .fit()
                .centerCrop()
                .into(candidatePicture);
        return listItemView;
    }
}