package com.example.govote.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.govote.Model.Election;
import com.example.govote.R;

import java.util.List;

public class RunningElectionAdapter extends RecyclerView.Adapter<RunningElectionAdapter.ViewHolder>  {
    private List<Election> electionList;
    ClickListener clickListener;
    Context context;
    public RunningElectionAdapter(ClickListener clickListener,List<Election> electionList){
        this.electionList=electionList;
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
        String title=electionList.get(position).getName();
        holder.mElectionTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return electionList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ClickListener clickListener;
        private TextView mElectionTitle;
        public ViewHolder(@NonNull View itemView,ClickListener clickListener) {
            super(itemView);
            this.clickListener=clickListener;
            mElectionTitle=(TextView) itemView.findViewById(R.id.electionTitle);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            Log.d("hello", "Hello: ");
            this.clickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface ClickListener{
        void onItemClick(int position);
    }
}
