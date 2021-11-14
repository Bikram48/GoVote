package com.example.govote.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.govote.CandidatesActivity;
import com.example.govote.Model.Election;
import com.example.govote.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OtherElectionAdapter extends RecyclerView.Adapter<OtherElectionAdapter.ViewHolder> {
    private List<Election> electionList;
    RunningElectionAdapter.ClickListener clickListener;
    private DatabaseReference userReference;
    Context context;
    public OtherElectionAdapter(Context context,List<Election> electionList){
        this.electionList=electionList;
        this.context=context;
        this.clickListener=clickListener;
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            userReference = FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.running_election_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view,this.clickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Election currentElection=electionList.get(position);
        Log.d("boy", currentElection.getImageUrl());
        holder.mElectionTitle.setText(currentElection.getName());
        holder.electionDeadline.setText("Deadline: "+currentElection.getEndDate());
        Picasso.get().load(currentElection.getImageUrl())
                .fit()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.electionBannerImg);
    }

    @Override
    public int getItemCount() {
        return electionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RunningElectionAdapter.ClickListener clickListener;
        private TextView mElectionTitle;
        private ImageView electionBannerImg;
        private TextView electionDeadline;
        public ViewHolder(@NonNull View itemView, RunningElectionAdapter.ClickListener clickListener) {
            super(itemView);
            this.clickListener=clickListener;
            mElectionTitle=(TextView) itemView.findViewById(R.id.electionTitle);
            electionBannerImg=(ImageView) itemView.findViewById(R.id.electionBannerImg);
            electionDeadline=(TextView) itemView.findViewById(R.id.electionDeadline);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String email = snapshot.child("email").getValue().toString().trim();
                        Log.d("election_fragment", "email: "+email);
                        String[] splitter = email.split("[.]", 0);
                        Log.d("election_fragment", "size: "+splitter.length);
                        if (splitter.length == 2) {
                            String last_word = splitter[1];
                            if (last_word.equals("com")) {
                                Log.d("hello", "Hello: ");
                                int position=getAdapterPosition();
                                Election election=electionList.get(position);
                                Intent intent=new Intent(context,CandidatesActivity.class);
                                intent.putExtra("cat",election.getName());
                                intent.putExtra("imageurl",election.getImageUrl());
                                context.startActivity(intent);
                            }else{
                                Log.d("checkdata", "milyo: ");
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setMessage("Sorry you can't vote on this election.");
                                builder1.setCancelable(true);

                                builder1.setNegativeButton(
                                        "Close",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                                //Toast.makeText(context, "Sorry you can't vote in this election.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}
