package com.example.govote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.govote.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Fragment extends Fragment {
    private EditText mEmailId,mPassword;
    private Button mLoginBtn;
    private TextView mSignupRedirect,mPwdResetTxt;
    private FragmentListener fragmentListener;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    FirebaseAuth mAuth;
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
        radioGroup=(RadioGroup) view.findViewById(R.id.radioGroup);
        mAuth=FirebaseAuth.getInstance();
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId=radioGroup.getCheckedRadioButtonId();
                radioButton=(RadioButton) radioGroup.findViewById(selectedId);
                final String value=radioButton.getText().toString();
                Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();
                String email=mEmailId.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
                User user;
                if(!email.equals("") && !password.equals("")){
                    user=new User(email,password,value);
                    fragmentListener.loginClicked(user);
                }else{
                    //Toast.makeText(getContext(), "Invalid credential", Toast.LENGTH_SHORT).show();
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