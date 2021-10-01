package com.example.govote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserAuthActivity extends AppCompatActivity implements Signup_Fragment.FragmentListener,
        Login_Fragment.FragmentListener,PasswordReset_Fragment.FragmentListener {
     Fragment fragment;
     FragmentManager fragmentManager;
     FragmentTransaction fragmentTransaction;
     private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_auth);
        mAuth=FirebaseAuth.getInstance();
        addFragment();
    }

    public void addFragment(){
        fragment=new Login_Fragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragemtnContainer,fragment).addToBackStack(null).commit();
    }

    public void replaceFragment(){
        fragment=new Signup_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragemtnContainer,fragment).addToBackStack(null).commit();
    }

    public void passwordResetFragment(){
        fragment=new PasswordReset_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragemtnContainer,fragment).addToBackStack(null).commit();
    }

    @Override
    public void createAccountClicked(User user) {
        createUser(user.getEmail(),user.getPassword());
    }

    public void createUser(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                        }else{
                            Toast.makeText(UserAuthActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void loginClicked() {
    }
    @Override
    public void createAccountClicked() {

    }

    @Override
    public void loginClicked(User user) {
        userLogin(user.getEmail(),user.getPassword());
    }

    public void userLogin(String email,String password){
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(UserAuthActivity.this,MainActivity.class));
                        }else{
                            Toast.makeText(UserAuthActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void passwordReset(String email) {
       mAuth.sendPasswordResetEmail(email)
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       Toast.makeText(UserAuthActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                       fragment=new Login_Fragment();
                       getSupportFragmentManager().beginTransaction().replace(R.id.fragemtnContainer,fragment).addToBackStack(null).commit();
                   }
               });
    }
}