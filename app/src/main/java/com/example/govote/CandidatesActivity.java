package com.example.govote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.govote.Adapter.CandidateAdapter;
import com.example.govote.Adapter.RunningElectionAdapter;
import com.example.govote.Model.Candidate;
import com.example.govote.Model.Election;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CandidatesActivity extends AppCompatActivity {
    List<Candidate> candidateList;
    ListView listView;
    CandidateAdapter candidateAdapter;
    DatabaseReference databaseReference;
    private String electionCat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidates);
        listView=findViewById(R.id.listView);
        electionCat=getIntent().getStringExtra("cat").trim().toString();
        Log.d("intentdata", electionCat);
        candidateList=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("Candidate");
        databaseReference.orderByChild("electionCat").equalTo(electionCat).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Candidate candidate = dataSnapshot.getValue(Candidate.class);
                        candidateList.add(candidate);
                    }
                    candidateAdapter = new CandidateAdapter(CandidatesActivity.this, candidateList);
                    listView.setAdapter(candidateAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}