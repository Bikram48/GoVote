package com.example.govote;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.net.URI;

public class AddElectionFragment extends Fragment {
    private EditText electionName;
    private AppCompatButton chooseBannerBtn,submitBtn;
    private EditText pictureName;
    public static final int PICK_IMAGE_REQUEST=1;
    private URI mImageURI;
    public AddElectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_election, container, false);
        electionName=(EditText) view.findViewById(R.id.electionName);
        chooseBannerBtn=(AppCompatButton) view.findViewById(R.id.chooseBannerBtn);
        pictureName=(EditText) view.findViewById(R.id.pictureName);
        submitBtn=(AppCompatButton) view.findViewById(R.id.submitBtn);

        chooseBannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=electionName.getText().toString();
            }
        });
        return view;
    }

    private void openFileChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

}