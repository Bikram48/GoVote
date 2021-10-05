package com.example.govote;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.govote.Adapter.VotingResultAdapter;
import com.example.govote.Model.User;
import com.example.govote.Model.VoteInfo;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VoteResultFragment extends Fragment {
    private PieChart pieChart;

    private RecyclerView voteResultDisplayerRV;
    private DatabaseReference databaseReference;
    private DatabaseReference voterReference;
    private String voterName;
    private int total;
    public VoteResultFragment() {
        // Required empty public constructor
        databaseReference=FirebaseDatabase.getInstance().getReference("Vote");
        voterReference=FirebaseDatabase.getInstance().getReference("Users");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_vote_result, container, false);
        voteResultDisplayerRV=view.findViewById(R.id.voteResultRecyclerView);
        pieChart=view.findViewById(R.id.barChart);
        ArrayList<PieEntry> pieEntries=new ArrayList<PieEntry>();
        FirebaseDatabase.getInstance().getReference("VoteCount")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                        if(snapshot2.exists()){
                            for(DataSnapshot snapshot:snapshot2.getChildren()){
                                String voteCount=snapshot.child("count").getValue().toString();
                                int counts=Integer.parseInt(voteCount);
                                pieEntries.add(new PieEntry(counts,snapshot.getKey()));
                            }
                        }

                        PieDataSet barDataSet=new PieDataSet(pieEntries,"VoteResult");
                        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        barDataSet.setValueTextColor(Color.BLACK);
                        barDataSet.setValueTextSize(16f);
                        PieData pieData=new PieData(barDataSet);
                        pieChart.setData(pieData);
                        pieChart.getDescription().setEnabled(false);
                        pieChart.setCenterText("Voting Result");
                        pieChart.animate();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        getInfo();
        return view;
    }


    private List<VoteInfo> getInfo(){
        List<VoteInfo> voteInfoList=new ArrayList<>();
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        /*
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
         */
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {

                    VoteInfo voteInfo=new VoteInfo();
                    FirebaseDatabase.getInstance().getReference("VoteCount")
                            .child(dataSnapshot.child("candidateName").getValue().toString())
                            .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            if(snapshot2.exists()){
                                String counter=snapshot2.child("count").getValue().toString();
                                voteInfo.setVoteCount(Integer.parseInt(counter));
                                Log.d("totalcount", "count: "+total);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    voterReference.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            voterName=snapshot1.child("email").getValue().toString();
                            voteInfo.setVoterName(snapshot1.child("email").getValue().toString());
                            voteInfo.setCandidateName(dataSnapshot.child("candidateName").getValue().toString());
                            voteInfo.setPosition(dataSnapshot.child("election").getValue().toString());
                            voteInfoList.add(voteInfo);
                            Log.d("allvalues", "onDataChange: "+voterName);
                            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
                            voteResultDisplayerRV.setLayoutManager(linearLayoutManager);
                            VotingResultAdapter votingResultAdapter=new VotingResultAdapter(getContext(),voteInfoList);
                            voteResultDisplayerRV.setAdapter(votingResultAdapter);
                           // Log.d("allvalues", "onDataChange: "+snapshot1.child("email").getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return voteInfoList;
    }
}