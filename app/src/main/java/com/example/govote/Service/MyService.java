package com.example.govote.Service;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyService extends JobService {
    DatabaseReference databaseReference;
    private boolean jobCancelled;
    BackgroundTask backgroundTask;

    @Override
    public boolean onStartJob(@NonNull JobParameters job) {
        doBackgroundWork(job);
        return true;
    }

    private void doBackgroundWork(JobParameters job) {
        Log.d("AdminDashboard", "doBackgroundWork: Hello");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<=10;i++){
                    Log.d("AdminDashboard", "run: "+i);
                    if(jobCancelled){
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }
                jobFinished(job,false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(@NonNull JobParameters job) {
        jobCancelled=true;
        return true;
    }

    public static class BackgroundTask extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            return "Hello from background job";
        }
    }
}
