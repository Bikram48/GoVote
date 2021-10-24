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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.govote.Model.User;

public class Signup_Fragment extends Fragment {
    private EditText mEmailId,mPassword,mRePassword;
    private Button mSignupBtn;
    private FragmentListener fragmentListener;
    private TextView loginRedirectTxtView;
    private ImageView mSignupBackImage;

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
        loginRedirectTxtView=(TextView) view.findViewById(R.id.loginRedirect);
        mSignupBackImage=(ImageView) view.findViewById(R.id.signupBackImage);
        mRePassword=(EditText) view.findViewById(R.id.reEnterPwdEditTxt);
        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailId=mEmailId.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
                String rePassword=mRePassword.getText().toString().trim();
                User user;
                if (emailId.equals("")|| password.equals("")||rePassword.equals("")) {
                    Toast.makeText(getContext(), "Please fill all the field", Toast.LENGTH_SHORT).show();
                }else if(!password.equals(rePassword)){
                    Toast.makeText(getContext(), "Password didn't matched", Toast.LENGTH_SHORT).show();
                }
                else {
                    user=new User(emailId,password,"1");
                    fragmentListener.createAccountClicked(user);
                    //Toast.makeText(getContext(), "invalid email or password", Toast.LENGTH_LONG).show();
                }
            }
        });
        loginRedirectTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((UserAuthActivity)getActivity()).replaceFragment(new Login_Fragment());
            }
        });

        mSignupBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((UserAuthActivity)getActivity()).replaceFragment(new Login_Fragment());
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