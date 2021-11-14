package com.example.govote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.govote.Adapter.VoterInfoAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VoterInfoActivity extends AppCompatActivity {
    private RecyclerView voterInfoRV;
    private DatabaseReference voterInfoRef;
    private VoterInfoAdapter voterInfoAdapter;
    private List<String> voterInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_info);
        voterInfoRV=findViewById(R.id.voterInfoRV);
        voterInfo=new ArrayList<>();
        voterInfoRef= FirebaseDatabase.getInstance().getReference("Users");
        voterInfoRef.orderByChild("isUser").equalTo("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        String role=snapshot1.child("isUser").getValue().toString();
                        if(role.equals("1")){
                            String email=snapshot1.child("email").getValue().toString();
                            voterInfo.add(email);
                        }

                    }
                    voterInfoAdapter=new VoterInfoAdapter(VoterInfoActivity.this,voterInfo);
                    voterInfoRV.setLayoutManager(new LinearLayoutManager(VoterInfoActivity.this));
                    voterInfoRV.setAdapter(voterInfoAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}