package com.example.govote.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.govote.AdminDashboard;
import com.example.govote.Model.Election;
import com.example.govote.Model.VoteResult;
import com.example.govote.R;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import papaya.in.sendmail.SendMail;

public class MyReceiver extends BroadcastReceiver {
    private DatabaseReference voteReference,electionReference,voteCountReference,userReference;
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        String text = bundle.getString("message");
        String uploadId=bundle.getString("uploadId");
        Log.d("AdminDashboard", "onReceive: "+text+" uploadid: "+uploadId);
        /*
        if(text!=null&&uploadId!=null){
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
                                                            String name = snapshot1.getKey().toString();
                                                            if(name.equals(electionName)){
                                                                for (DataSnapshot snapshot4 : snapshot3.getChildren()) {
                                                                    String voteCount = snapshot2.child("count").getValue().toString();
                                                                    int counts = Integer.parseInt(voteCount);
                                                                    String election = snapshot1.getKey().toString();
                                                                    String candidate = snapshot2.getKey();
                                                                    SendMail mail = new SendMail("chandbikram001@gmail.com", "Bikram31@",
                                                                            email,
                                                                            "View "+election+" result",
                                                                            election+"result \n  "+candidate+" : "+counts);
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
        */
        //String date = bundle.getString("date");
        //String time=bundle.getString("time");

        //Click on Notification
        Intent intent1 = new Intent(context, AdminDashboard.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
       // intent1.putExtra("isEnded", "true");
        intent1.putExtra("id",uploadId);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity
                    (context, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        }
        else
        {
            pendingIntent = PendingIntent.getActivity
                    (context, 1, intent1, PendingIntent.FLAG_ONE_SHOT);
        }
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001");

        @SuppressLint("RemoteViewLayout") RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        contentView.setImageViewResource(R.id.icon, R.drawable.ic_launcher_background);
        contentView.setTextViewText(R.id.task_name, text);
        //contentView.setTextViewText(R.id.time, time);
        mBuilder.setSmallIcon(R.drawable.ic_launcher_background);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(true);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setOnlyAlertOnce(true);
        //mBuilder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH;
        mBuilder.setContent(contentView);
        mBuilder.setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        Notification notification = mBuilder.build();
        notificationManager.notify(1, notification);
    }
}