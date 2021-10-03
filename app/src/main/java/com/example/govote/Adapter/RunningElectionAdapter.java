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
import com.squareup.picasso.Picasso;

import java.util.List;

public class RunningElectionAdapter extends RecyclerView.Adapter<RunningElectionAdapter.ViewHolder>  {
    private List<Election> electionList;
    ClickListener clickListener;
    Context context;
    public RunningElectionAdapter(Context context,List<Election> electionList){
        this.electionList=electionList;
        this.context=context;
        this.clickListener=clickListener;
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
            Log.d("hello", "Hello: ");
            int position=this.getAdapterPosition();
            Election election=electionList.get(position);
            Intent intent=new Intent(context,CandidatesActivity.class);
            intent.putExtra("cat",election.getName());
            context.startActivity(intent);
            //this.clickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface ClickListener{
        void onItemClick(int position);
    }
}
