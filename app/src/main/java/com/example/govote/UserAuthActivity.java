package com.example.govote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.govote.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UserAuthActivity extends AppCompatActivity implements Signup_Fragment.FragmentListener,
        Login_Fragment.FragmentListener,PasswordReset_Fragment.FragmentListener {
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<String > errors=new ArrayList<>();
     private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_auth);
        mAuth=FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(UserAuthActivity.this);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){

            startActivity(new Intent(UserAuthActivity.this,MainActivity.class));
        }

        addFragment();
    }

    public void addFragment(){
        fragment=new Login_Fragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragemtnContainer,fragment).addToBackStack(null).commit();
    }

    public void replaceFragment(Fragment fragment){
       // fragment=new Signup_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragemtnContainer,fragment).addToBackStack(null).commit();
    }

    public void passwordResetFragment(){
        fragment=new PasswordReset_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragemtnContainer,fragment).addToBackStack(null).commit();
    }

    @Override
    public void createAccountClicked(User user) {
        createUser(user.getEmail(),user.getPassword(),user.getPhoneNumber());
    }

    public void createUser(String email,String password,String phone){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mAuth.getCurrentUser()
                                    .sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    Map<String,String> userInfo=new HashMap<>();
                                    userInfo.put("email",email);
                                    userInfo.put("password",password);
                                    userInfo.put("isUser","1");
                                    userInfo.put("phone",phone);
                                    userInfo.put("isVerified","true");
                                    databaseReference.setValue(userInfo);
                                    Toast.makeText(UserAuthActivity.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("UserAuthActivity", "onFailure: Email not sent "+e.getMessage());
                                }
                            });

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

            userLogin(user);

    }


    public void userLogin(User user){
        //databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    mAuth.signInWithEmailAndPassword(user.getEmail(),user.getPassword())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        if(user.getUserRole().equals("Admin")){
                                            Log.d("user_role", "admin or user: ");
                                            checkAdmin(mAuth.getCurrentUser().getUid());
                                        }
                                        if(user.getUserRole().equals("User")) {
                                            checkUserRole(mAuth.getCurrentUser().getUid(),user);
                                        }
                                    }else{
                                        Toast.makeText(UserAuthActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

    }
    private void checkUserRole(String userId,User user){
        Log.d("userid", userId);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        Query query=databaseReference.child("Users").child(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("isUser")){
                    Log.d("userid", snapshot.getKey());
                    sendOtpCode(user.getEmail(),  user.getPhoneNumber(),user.getPassword(), user.getUserRole());
                    /*
                    if(mAuth.getCurrentUser().isEmailVerified()) {
                        startActivity(new Intent(UserAuthActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(UserAuthActivity.this, "Please verify your email first", Toast.LENGTH_SHORT).show();
                    }

                     */
                }
                else{
                    Toast.makeText(UserAuthActivity.this, "No user found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void sendOtpCode(String email,String phone,String password,String role){
        //progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+977"+phone, 60, TimeUnit.SECONDS, UserAuthActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //progressBar.setVisibility(View.GONE);
                // secretCodeSenderBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                //   progressBar.setVisibility(View.GONE);
                // secretCodeSenderBtn.setVisibility(View.VISIBLE);
                Toast.makeText(UserAuthActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                //progressBar.setVisibility(View.GONE);
                //secretCodeSenderBtn.setVisibility(View.VISIBLE);
                Intent intent=new Intent(UserAuthActivity.this, OTPCodeActivity.class);
                // Log.d(TAG, "onCodeSent: "+phone_number);
                //String number=phone_number.replace("-")
                intent.putExtra("email",email);
                intent.putExtra("phone_number",phone);
                intent.putExtra("password",password);
                intent.putExtra("role",role);
                intent.putExtra("otp_code",s);
                Log.d("UserAuthActivity", "onCodeSent: "+s);
                startActivity(intent);
            }
        });
    }

    private void checkAdmin(String userId){
        FirebaseDatabase.getInstance().getReference("Users").child(userId)
             .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("isAdmin")){
                    startActivity(new Intent(UserAuthActivity.this,AdminDashboard.class));
                }else{
                    Toast.makeText(UserAuthActivity.this, "No admin found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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