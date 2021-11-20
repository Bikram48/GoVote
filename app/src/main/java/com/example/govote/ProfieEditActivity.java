package com.example.govote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ProfieEditActivity extends AppCompatActivity {
    private TextInputEditText emailEditTxt;
    private AppCompatButton profileEditBtn;
    private FloatingActionButton backBtn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profie_edit);
        emailEditTxt=(TextInputEditText) findViewById(R.id.emailEditTxt);
        profileEditBtn=(AppCompatButton) findViewById(R.id.updateProfileBtn);
        backBtn=(FloatingActionButton) findViewById(R.id.back_btn);
        mAuth=FirebaseAuth.getInstance();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            emailEditTxt.setText(firebaseUser.getEmail());
        }
        profileEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailEditTxt.getText() != null) {
                    SharedPreferences sh = getSharedPreferences("UserLoginDetail", Context.MODE_PRIVATE);
                    String email = sh.getString("email", "");
                    String password = sh.getString("password", "");
                    Log.d("ProfileEdit", "email: " + email + " password " + password);
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email, password); // Current Login Credentials \\
                    // Prompt the user to re-provide their sign-in credentials
                    firebaseUser.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //Log.d(TAG, "User re-authenticated.");
                                    //Now change your email address \\
                                    //----------------Code for Changing Email Address----------\\
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    user.updateEmail(emailEditTxt.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        FirebaseDatabase.getInstance().getReference("Users")
                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                .child("email").setValue(emailEditTxt.getText().toString())
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        mAuth.getCurrentUser()
                                                                                .sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                Toast.makeText(ProfieEditActivity.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                                                                                FirebaseAuth.getInstance().signOut();
                                                                                startActivity(new Intent(ProfieEditActivity.this,UserAuthActivity.class));
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.d("UserAuthActivity", "onFailure: Email not sent "+e.getMessage());
                                                                            }
                                                                        });
                                                                       // Toast.makeText(ProfieEditActivity.this, "Email Updated Successfully!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                    }
                                                }
                                            });
                                    //----------------------------------------------------------\\
                                }
                            });
                }else{
                    Toast.makeText(ProfieEditActivity.this, "Please fill the field", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}