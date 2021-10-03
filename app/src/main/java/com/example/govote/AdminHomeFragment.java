package com.example.govote;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class AdminHomeFragment extends Fragment {
    private FloatingActionButton mAddElectionBtn,mAddCandidateBtn;
    public AdminHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_admin_home, container, false);
        mAddElectionBtn=(FloatingActionButton) view.findViewById(R.id.floatingActionButton1);
        mAddCandidateBtn=(FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        mAddElectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(),AddElectionActivity.class));
                //((AdminDashboard)getActivity()).replaceFragments(new AddElectionFragment());
            }
        });

        mAddCandidateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(),AddCandidateActivity.class));
            }
        });
        return view;
    }
}