package com.example.govote;

import android.app.Fragment;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.govote.Adapter.CandidateAdapter;
import com.example.govote.Model.Candidate;

import java.util.ArrayList;
import java.util.List;

public class CandidateFragment extends Fragment {
    List<Candidate> candidateList;
    ListView listView;
    CandidateAdapter candidateAdapter;
    public CandidateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_candidate, container, false);
        listView=view.findViewById(R.id.listView);
        candidateList=new ArrayList<>();
        candidateList.add(new Candidate("Donald trump","President of United States"));
        candidateList.add(new Candidate("Joe Biden","Senator of United States"));
        candidateList.add(new Candidate("Bikram Chand","Student of British"));
        candidateList.add(new Candidate("Mithun Chand","Student of Canvas"));
        candidateList.add(new Candidate("Donald trump","President of United States"));
        candidateList.add(new Candidate("Joe Biden","Senator of United States"));
        candidateList.add(new Candidate("Bikram Chand","Student of British"));
        candidateList.add(new Candidate("Mithun Chand","Student of Canvas"));
      
        //listView.setAdapter(candidateAdapter);
        return view;
    }
}