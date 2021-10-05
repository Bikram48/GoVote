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
import com.example.govote.Model.VoteInfo;
import com.example.govote.R;

import java.util.List;

public class VotingResultAdapter extends RecyclerView.Adapter<VotingResultAdapter.ViewHolder> {
    private List<VoteInfo> voteInfoList;
    Context context;
    public VotingResultAdapter(Context context, List<VoteInfo> voteInfoList){
        this.context=context;
        this.voteInfoList=voteInfoList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.voteresult_list_item,parent,false);
        VotingResultAdapter.ViewHolder viewHolder=new VotingResultAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int rowPos=holder.getAdapterPosition();
        if(rowPos==0){
            holder.txtPosition.setBackgroundResource(R.drawable.table_header_border);
            holder.txtCandidate.setBackgroundResource(R.drawable.table_header_border);
            holder.txtVoter.setBackgroundResource(R.drawable.table_header_border);
            holder.txtVoterCount.setBackgroundResource(R.drawable.table_header_border);

            holder.txtPosition.setText("Position");
            holder.txtCandidate.setText("Candidate");
            holder.txtVoter.setText("Voter");
            holder.txtVoterCount.setText("Total Votes");
        }else {
            holder.txtPosition.setBackgroundResource(R.drawable.table_content_bg);
            holder.txtCandidate.setBackgroundResource(R.drawable.table_content_bg);
            holder.txtVoter.setBackgroundResource(R.drawable.table_content_bg);
            holder.txtVoterCount.setBackgroundResource(R.drawable.table_content_bg);

            VoteInfo voteInfo = voteInfoList.get(position);
            holder.txtPosition.setText(voteInfo.getPosition());
            holder.txtCandidate.setText(voteInfo.getCandidateName());
            holder.txtVoter.setText(voteInfo.getVoterName());
            holder.txtVoterCount.setText(String.valueOf(voteInfo.getVoteCount()));
        }
    }

    @Override
    public int getItemCount() {
        return voteInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtPosition,txtCandidate,txtVoter,txtVoterCount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPosition=(TextView) itemView.findViewById(R.id.txtPosition);
            txtCandidate=(TextView) itemView.findViewById(R.id.txtCandidate);
            txtVoter=(TextView) itemView.findViewById(R.id.txtVoter);
            txtVoterCount=(TextView) itemView.findViewById(R.id.txtVoteCount);
        }


    }

}
