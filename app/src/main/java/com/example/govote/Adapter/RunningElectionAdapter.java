package com.example.govote.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class RunningElectionAdapter extends RecyclerView.Adapter<RunningElectionAdapter.ViewHolder>  {
    private List<Election> electionList;
    ClickListener clickListener;
    private DatabaseReference userReference;
    Context context;
    public RunningElectionAdapter(Context context,List<Election> electionList){
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
        private ClickListener clickListener;
        private TextView mElectionTitle;
        private ImageView electionBannerImg;
        public ViewHolder(@NonNull View itemView,ClickListener clickListener) {
            super(itemView);
            this.clickListener=clickListener;
            mElectionTitle=(TextView) itemView.findViewById(R.id.electionTitle);
            electionBannerImg=(ImageView) itemView.findViewById(R.id.electionBannerImg);
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
                        if (splitter.length == 3) {
                            String second_last = splitter[1];
                            String last = splitter[2];
                            if (second_last.equals("mcneese") && last.equals("edu")) {
                                Log.d("hello", "Hello: ");
                                int position=getAdapterPosition();
                                Election election=electionList.get(position);
                                Intent intent=new Intent(context,CandidatesActivity.class);
                                intent.putExtra("cat",election.getName());
                                intent.putExtra("imageurl",election.getImageUrl());
                                context.startActivity(intent);
                            }else{

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //this.clickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface ClickListener{
        void onItemClick(int position);
    }
}
