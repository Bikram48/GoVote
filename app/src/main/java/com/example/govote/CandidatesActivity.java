package com.example.govote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.govote.Adapter.CandidateAdapter;
import com.example.govote.Model.Candidate;

import java.util.ArrayList;
import java.util.List;

public class CandidatesActivity extends AppCompatActivity {
    List<Candidate> candidateList;
    ListView listView;
    CandidateAdapter candidateAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidates);
        listView=findViewById(R.id.listView);
        candidateList=new ArrayList<>();
        candidateList.add(new Candidate("Donald trump","President of United States"));
        candidateList.add(new Candidate("Joe Biden","Senator of United States"));
        candidateList.add(new Candidate("Bikram Chand","Student of British"));
        candidateList.add(new Candidate("Mithun Chand","Student of Canvas"));
        candidateAdapter=new CandidateAdapter(CandidatesActivity.this,candidateList);
        listView.setAdapter(candidateAdapter);
    }
}