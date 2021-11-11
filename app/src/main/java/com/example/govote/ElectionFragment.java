package com.example.govote;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.govote.Adapter.OtherElectionAdapter;
import com.example.govote.Adapter.RunningElectionAdapter;
import com.example.govote.Model.Election;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ElectionFragment extends Fragment implements RunningElectionAdapter.ClickListener {
    private List<Election> electionList=new ArrayList<>();
    private List<Election> otherElectionList=new ArrayList<>();
    private DatabaseReference userReference;
    RunningElectionAdapter electionAdapter;
    OtherElectionAdapter otherElectionAdapter;
    private RecyclerView recyclerView,otherElectionRv;
    DatabaseReference databaseReference;
    public ElectionFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_election, container, false);
        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerView);
        otherElectionRv=(RecyclerView) view.findViewById(R.id.otherElectionRv);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            userReference = FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Election");
        databaseReference.orderByChild("electionType").equalTo("other")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot3) {
                        for (DataSnapshot snapshot2 : snapshot3.getChildren()) {
                            Log.d("election_fragment", "onDataChange: " + snapshot2.getKey());
                            Election election=snapshot2.getValue(Election.class);
                            electionList.add(election);
                        }
                        electionAdapter=new RunningElectionAdapter(getContext(),electionList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(electionAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        databaseReference.orderByChild("electionType").equalTo("sga")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                                Log.d("election_fragment", "onDataChange: " + snapshot2.getKey());
                                Election election = snapshot2.getValue(Election.class);
                                otherElectionList.add(election);
                            }
                            otherElectionAdapter = new OtherElectionAdapter(getContext(),otherElectionList);
                            otherElectionRv.setLayoutManager(new LinearLayoutManager(getActivity()));
                            otherElectionRv.setAdapter(otherElectionAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        /*
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String email = snapshot.child("email").getValue().toString().trim();
                    Log.d("election_fragment", "email: "+email);
                    String[] splitter = email.split("[.]", 0);
                    Log.d("election_fragment", "size: "+splitter.length);
                    if (splitter.length == 3) {
                        String second_last = splitter[1];
                        String last = splitter[2];
                        if (second_last.equals("mcneese") && last.equals("edu")) {
                            databaseReference = FirebaseDatabase.getInstance().getReference("Election");
                            databaseReference.orderByChild("electionType").equalTo("sga")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot3) {
                                            for (DataSnapshot snapshot2 : snapshot3.getChildren()) {
                                                Log.d("election_fragment", "onDataChange: " + snapshot2.getKey());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }
                    }
                    if(splitter.length==2){
                        String last_word = splitter[1];
                        Log.d("election_fragment", "lastword: "+last_word);
                        if (last_word.equals("com")) {
                            databaseReference = FirebaseDatabase.getInstance().getReference("Election");
                            databaseReference.orderByChild("electionType").equalTo("other")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot3) {
                                            for (DataSnapshot snapshot2 : snapshot3.getChildren()) {
                                                Log.d("election_fragment", "onDataChange: " + snapshot2.getKey());
                                                Election election=snapshot2.getValue(Election.class);
                                                electionList.add(election);
                                            }
                                            electionAdapter=new RunningElectionAdapter(getActivity(),electionList);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                            recyclerView.setAdapter(electionAdapter);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }

                }
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference= FirebaseDatabase.getInstance().getReference("Election");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */


        /*
        electionList.add(new Election("President Election"));
        electionList.add(new Election("Student Election"));
        electionList.add(new Election("Captain Election"));
        electionList.add(new Election("Headboy Election"));
        electionList.add(new Election("Headgirl Election"));
        electionList.add(new Election("Teacher Election"));
        electionList.add(new Election("Principal Election"));

         */

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onItemClick(int position) {
       // ((MainActivity)getActivity()).replaceElectionFragment();
        Intent intent = new Intent(getActivity(), CandidatesActivity.class);
        startActivity(intent);
    }
}