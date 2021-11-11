package com.example.govote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.govote.Adapter.CandidateAdapter;
import com.example.govote.Adapter.RunningElectionAdapter;
import com.example.govote.Model.Candidate;
import com.example.govote.Model.Election;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CandidatesActivity extends AppCompatActivity {
    List<Candidate> candidateList;
    RecyclerView recyclerView;
    CandidateAdapter candidateAdapter;
    DatabaseReference databaseReference;
    private String electionCat;
    private String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidates);
        recyclerView=findViewById(R.id.recyclerView);
        electionCat=getIntent().getStringExtra("cat").trim().toString();
        imageUrl=getIntent().getStringExtra("imageurl").trim().toString();
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
                    candidateAdapter = new CandidateAdapter(CandidatesActivity.this, candidateList,electionCat);
                    recyclerView.setLayoutManager(new LinearLayoutManager(CandidatesActivity.this));
                    recyclerView.setAdapter(candidateAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}