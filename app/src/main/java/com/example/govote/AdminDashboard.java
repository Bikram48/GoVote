package com.example.govote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.govote.Service.MyService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import papaya.in.sendmail.SendMail;

public class AdminDashboard extends AppCompatActivity {
    public static final String TAG="AdminDashboard";
    private DatabaseReference voteReference,electionReference,voteCountReference,userReference;
    private BottomNavigationView bottomNavigationView;
    private DatabaseReference databaseReference;
    private static final int JOB_ID=123;
    private Uri mImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        bottomNavigationView=findViewById(R.id.bottomNavigation);
    /*
        if(getIntent().getStringExtra("id")!=null) {
           // String message = getIntent().getStringExtra("isEnded");
            String uploadId = getIntent().getStringExtra("id");
            Log.d(TAG, "onCreate: " + uploadId);

                FirebaseDatabase.getInstance().getReference("Election").child(uploadId)
                        .child("isEnded").setValue("Y");






        }

     */
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new AdminHomeFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment=null;
                switch (item.getItemId()){
                    case R.id.dashboard:
                        fragment=new AdminHomeFragment();
                        break;
                    case  R.id.vote_result:
                        fragment=new VoteResultFragment();
                        break;
                    case R.id.profile:
                        fragment=new UserSettingFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                return true;
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if(extras != null){
            if(extras.containsKey("id"))
            {
                String uploadId=extras.getString("id");

                FirebaseDatabase.getInstance().getReference("Election").child(uploadId)
                        .child("isEnded").setValue("Y");

                Log.d(TAG, "onCreate: " + uploadId);
                if(uploadId!=null){
                    voteReference = FirebaseDatabase.getInstance().getReference("Vote");
                    voteCountReference=FirebaseDatabase.getInstance().getReference("VoteCount");
                    userReference=FirebaseDatabase.getInstance().getReference("Users");
                    electionReference=FirebaseDatabase.getInstance().getReference("Election")
                            .child(uploadId);
                    electionReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String electionName=snapshot.child("name").getValue().toString();
                            Log.d("MyReceiver", "election name: "+electionName);
                            voteReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                                        for(DataSnapshot snapshot2:snapshot1.getChildren()) {
                                            String election = snapshot2.getKey().toString();
                                            Log.d("MyReceiver", "election names: "+electionName);
                                            if (electionName.equals(election)) {
                                                String userid=snapshot1.getKey().toString();
                                                Log.d("MyReceiver", "Userid: "+userid);
                                                userReference.child(userid).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        String email=snapshot.child("email").getValue().toString();
                                                        Log.d("MyReceiver", "email: "+email);
                                                        voteCountReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                for(DataSnapshot snapshot3:snapshot.getChildren()){
                                                                    String name = snapshot3.getKey().toString();
                                                                    if(name.equals(electionName)){
                                                                        for (DataSnapshot snapshot4 : snapshot3.getChildren()) {
                                                                            String voteCount = snapshot4.child("count").getValue().toString();
                                                                            int counts = Integer.parseInt(voteCount);
                                                                            Log.d("MyReceiver", "count: "+counts);
                                                                            String election = snapshot3.getKey().toString();
                                                                            String candidate = snapshot4.getKey();
                                                                            SendMail mail = new SendMail("chandbikram001@gmail.com", "Bikram31@",
                                                                                    email,
                                                                                    "View "+election+" result",
                                                                                    election+" result \n  "+candidate+" : "+counts);
                                                                            mail.execute();
                                                                            //electionNameTxt.setTextSize(30);
                                                                            // typeAmountMap.put(candidates.get(i),Integer.parseInt(votes));
                                                                            //collecting the entries with label name

                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        }
    }

    public void replaceFragments(Fragment fragment) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment)
                .commit();
    }
}