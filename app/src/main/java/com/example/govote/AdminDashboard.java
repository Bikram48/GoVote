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

public class AdminDashboard extends AppCompatActivity {
    public static final String TAG="AdminDashboard";
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