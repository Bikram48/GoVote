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
        scheduleJob();
        FirebaseDatabase.getInstance().getReference("Election").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    scheduleJob();
                }else{
                    cancelJob();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
    public void scheduleJob(){
        ComponentName componentName=new ComponentName(this, MyService.class);
        String dateandtime="05-10-2021"+" "+"9:31";
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:MM");
        try {
            Date date1 = formatter.parse(dateandtime);
            JobInfo jobInfo=new JobInfo.Builder(JOB_ID,componentName)
                    .setPersisted(true)
                    .setRequiresCharging(false)
                    .setPeriodic(15*60*1000)
                    .build();
            Log.d(TAG, "scheduleJob: "+date1);
            JobScheduler jobScheduler=(JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            int resultCode=jobScheduler.schedule(jobInfo);
            if(resultCode==JobScheduler.RESULT_SUCCESS)
                Log.d(TAG, "Job Scheduled..");
            else
                System.out.println("Job Scheduler failed..");
        }catch (ParseException e){

        }


    }

    public void cancelJob(){
        JobScheduler jobScheduler=(JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(JOB_ID);
        Log.d(TAG, "Job has been canceled ");
    }


    public void replaceFragments(Fragment fragment) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment)
                .commit();
    }
}