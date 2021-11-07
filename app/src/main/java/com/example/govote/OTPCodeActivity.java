package com.example.govote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OTPCodeActivity extends AppCompatActivity implements View.OnClickListener {
    private AppCompatButton mSubmitBtn;
    private PinView mOtpCode;
    private String phoneNumber,email,password,role;
    private String otpcode;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpcode);
        mOtpCode = findViewById(R.id.otpcode);
        mAuth=FirebaseAuth.getInstance();
        phoneNumber = getIntent().getStringExtra("phone_number");
        Log.d("practice", "onCreate: " + phoneNumber);
        otpcode = getIntent().getStringExtra("otp_code");
        Log.d("practice", "code: "+otpcode);
        email=getIntent().getStringExtra("email");
        password=getIntent().getStringExtra("password");
        role=getIntent().getStringExtra("role");
        mSubmitBtn = findViewById(R.id.submitBtn);
        mSubmitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(OTPCodeActivity.this, MainActivity.class);
        String getOtpcode = mOtpCode.getText().toString();
        //   Toast.makeText(this, mOtpCode.getText().toString(), Toast.LENGTH_SHORT).show();
        if (otpcode != null) {
            //PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(otpcode, getOtpcode);

            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(otpcode, getOtpcode);
            FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        userLogin();
                    } else {
                        Toast.makeText(OTPCodeActivity.this, "Enter the correct otp code", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            }

    }

    public void userLogin(){
        //databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(role.equals("User")) {
                                checkUserRole(mAuth.getCurrentUser().getUid());
                            }
                        }else{
                            Toast.makeText(OTPCodeActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }
    private void checkUserRole(String userId){
        Log.d("userid", userId);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        Query query=databaseReference.child("Users").child(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("isUser")){
                    Log.d("userid", snapshot.getKey());
                    startActivity(new Intent(OTPCodeActivity.this, MainActivity.class));
                    /*
                    if(mAuth.getCurrentUser().isEmailVerified()) {
                        startActivity(new Intent(UserAuthActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(UserAuthActivity.this, "Please verify your email first", Toast.LENGTH_SHORT).show();
                    }

                     */
                }
                else{
                    Toast.makeText(OTPCodeActivity.this, "No user found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}