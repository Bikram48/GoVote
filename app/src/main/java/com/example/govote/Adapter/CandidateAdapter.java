package com.example.govote.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.govote.Model.Candidate;
import com.example.govote.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.ViewHolder> {
    private int mImageResourceId;

    private DatabaseReference votingRef,votingCountRef;
    private int count;
    private FirebaseAuth firebaseAuth;
    private List<Candidate> candidateList;
    private String elctionCat;
    private Context context;
    public CandidateAdapter(@NonNull Context context, List<Candidate> candidateList,String electionCat) {
        firebaseAuth=FirebaseAuth.getInstance();
        this.context=context;
        this.elctionCat=electionCat;
        this.candidateList=candidateList;
        votingCountRef=FirebaseDatabase.getInstance().getReference("VoteCount");
        votingRef= FirebaseDatabase.getInstance().getReference("Vote").child(firebaseAuth.getCurrentUser().getUid());
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.candidate_item, parent, false);
       ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Candidate candidate = candidateList.get(position);
            holder.candidateName.setText(candidate.getName());
            holder.candidateDescription.setText(candidate.getDescription());

            Picasso.get().load(candidate.getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.candidatePicture);
            holder.mVoteBtn.setSelected(candidate.isClicked());
            holder.mVoteBtn.setTag(position);
        }



    @Override
    public int getItemCount() {
        return candidateList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView candidateName,candidateDescription;
        private CircleImageView candidatePicture;
        private MaterialButton mVoteBtn;

        public ViewHolder(@NonNull View listItemView){
            super(listItemView);
            candidateName = (TextView) listItemView.findViewById(R.id.candidate_name);
            candidateDescription= (TextView) listItemView.findViewById(R.id.candidate_description);
            candidatePicture=(CircleImageView) listItemView.findViewById(R.id.candidatePicture);
            mVoteBtn=(MaterialButton) listItemView.findViewById(R.id.voteBtn);
            mVoteBtn.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View view) {
            int position=this.getAdapterPosition();
            Integer pos=(Integer) mVoteBtn.getTag();
            Candidate candidate=candidateList.get(position);
            Toast.makeText(context, candidateList.get(position).getName(), Toast.LENGTH_SHORT).show();
            if(candidate.isClicked()) {
                mVoteBtn.setText("Vote Now");
                mVoteBtn.setEnabled(true);
                candidate.setClicked(false);
            }else{
                mVoteBtn.setText("Voted");
                mVoteBtn.setEnabled(false);
                candidate.setClicked(true);
            }
            count=0;
            Map<String,String > voteInfo=new HashMap<>();
            voteInfo.put("candidateName",candidate.getName());
            voteInfo.put("election",elctionCat);
            votingRef.setValue(voteInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    Map<String,String > voteCountList=new HashMap<>();
                    count++;
                    //votingCountRef.child(candidate.getName()).setValue(voteCountList);
                    votingCountRef.child(elctionCat).child(candidate.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String voteCount= snapshot.child("count").getValue().toString();
                                int votes=Integer.parseInt(voteCount);
                                votes+=count;
                                voteCountList.put("count",String.valueOf(votes));
                                //voteCountList.put("election",elctionCat);
                                votingCountRef.child(elctionCat).child(candidate.getName()).setValue(voteCountList);
                            }
                            else {
                                Map<String,String > votecounter=new HashMap<>();
                                votecounter.put("count",String.valueOf(count));
                               // votecounter.put("election",elctionCat);
                                votingCountRef.child(elctionCat).child(candidate.getName()).setValue(votecounter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            });

        }
    }

}