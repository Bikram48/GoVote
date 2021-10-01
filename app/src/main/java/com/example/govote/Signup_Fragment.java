package com.example.govote;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Signup_Fragment extends Fragment {
    private EditText mEmailId,mPassword;
    private Button mSignupBtn;
    private FragmentListener fragmentListener;
    public Signup_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_signup_, container, false);
        mEmailId=(EditText) view.findViewById(R.id.emailEditTxt);
        mPassword=(EditText) view.findViewById(R.id.pwdEditTxt);
        mSignupBtn=(Button) view.findViewById(R.id.signupBtn);
        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailId=mEmailId.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
                User user;
                if (!mEmailId.equals("") && !mPassword.equals("")) {
                     user=new User(emailId,password);
                     fragmentListener.createAccountClicked(user);
                }
                else {
                    Toast.makeText(getContext(), "invalid email or password", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            fragmentListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener=null;
    }

    public interface FragmentListener{
        void createAccountClicked(User user);
        void loginClicked();
    }
}