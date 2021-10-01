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

import com.example.govote.Adapter.RunningElectionAdapter;
import com.example.govote.Model.Election;

import java.util.ArrayList;
import java.util.List;


public class ElectionFragment extends Fragment implements RunningElectionAdapter.ClickListener {
    private List<Election> electionList=new ArrayList<>();
    RunningElectionAdapter electionAdapter;
    private RecyclerView recyclerView,upcomingELectionRV;
    public ElectionFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_election, container, false);
        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerView);
        upcomingELectionRV=(RecyclerView) view.findViewById(R.id.upcomingElectionRV);
        electionList.add(new Election("President Election"));
        electionList.add(new Election("Student Election"));
        electionList.add(new Election("Captain Election"));
        electionList.add(new Election("Headboy Election"));
        electionList.add(new Election("Headgirl Election"));
        electionList.add(new Election("Teacher Election"));
        electionList.add(new Election("Principal Election"));
        electionAdapter=new RunningElectionAdapter(this,electionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        upcomingELectionRV.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(electionAdapter);
        upcomingELectionRV.setAdapter(electionAdapter);
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