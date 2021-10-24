package com.example.govote;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class PasswordReset_Fragment extends Fragment {
    private EditText mEmailId;
    private Button mPwdResetBtn;
    private TextView mLoginRedirector;
    private  PasswordReset_Fragment.FragmentListener fragmentListener;
    private ImageView resetBackImage;
    public PasswordReset_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_password_reset_, container, false);
        mEmailId=(EditText) view.findViewById(R.id.emailEditTxt);
        mPwdResetBtn=(Button) view.findViewById(R.id.forgetpwdBtn);
        resetBackImage=(ImageView) view.findViewById(R.id.resetBackImage);
        resetBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((UserAuthActivity)getActivity()).replaceFragment(new Login_Fragment());
            }
        });
        mPwdResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mEmailId.getText().toString().trim();
                AlertDialog.Builder passwordResetDialog=new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Are you sure you want to reset password?");
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        fragmentListener.passwordReset(email);
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                passwordResetDialog.show();
            }
        });
        mLoginRedirector=(TextView) view.findViewById(R.id.loginRedirect);
        mLoginRedirector.setOnClickListener(new View.OnClickListener() {
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
        if (context instanceof Login_Fragment.FragmentListener) {
            fragmentListener = (PasswordReset_Fragment.FragmentListener) context;
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
        void passwordReset(String email);
    }
}