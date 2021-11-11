package com.example.govote;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.airbnb.lottie.L;
import com.google.firebase.auth.FirebaseAuth;

public class UserSettingFragment extends Fragment {
    private LinearLayout mLogoutBtn,mAboutUsBtn,mProfileEditBtn;

    public UserSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user_setting, container, false);
        mLogoutBtn=(LinearLayout) view.findViewById(R.id.logoutBtn);
        mAboutUsBtn=(LinearLayout) view.findViewById(R.id.aboutBtn);
        mProfileEditBtn=(LinearLayout) view.findViewById(R.id.profileEdit);
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(),UserAuthActivity.class));
            }
        });
        mAboutUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),AboutUs
                .class));
            }
        });
        return view;
    }
}