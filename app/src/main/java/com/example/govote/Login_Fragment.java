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
import android.widget.TextView;
import android.widget.Toast;

public class Login_Fragment extends Fragment {
    EditText mEmailId,mPassword;
    Button mLoginBtn;
    TextView mSignupRedirect,mPwdResetTxt;
    FragmentListener fragmentListener;
    public Login_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_login_, container, false);
        mEmailId=(EditText) view.findViewById(R.id.emailEditTxt);
        mPassword=(EditText) view.findViewById(R.id.pwdEditTxt);
        mLoginBtn=(Button) view.findViewById(R.id.loginBtn);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mEmailId.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
                User user;
                if(!email.equals("") && !password.equals("")){
                    user=new User(email,password);
                    fragmentListener.loginClicked(user);
                }else{
                    Toast.makeText(getContext(), "Invalid credential", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSignupRedirect=(TextView) view.findViewById(R.id.signupRedirect);
        mSignupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((UserAuthActivity)getActivity()).replaceFragment();
            }
        });
        mPwdResetTxt=(TextView) view.findViewById(R.id.forgetPwdTxt);
        mPwdResetTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((UserAuthActivity)getActivity()).passwordResetFragment();
            }
        });
        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Login_Fragment.FragmentListener) {
            fragmentListener = (Login_Fragment.FragmentListener) context;
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
        void createAccountClicked();
        void loginClicked(User user);
    }
}