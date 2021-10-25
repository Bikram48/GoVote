package com.example.govote.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.govote.Model.VoteResult;
import com.example.govote.R;
import com.example.govote.VoterInfoActivity;
import com.github.mikephil.charting.charts.PieChart;

import java.util.List;

public class VoterInfoAdapter extends RecyclerView.Adapter<VoterInfoAdapter.ViewHolder> {
    private List<String> voterInfo;
    private Context context;
    public VoterInfoAdapter(Context context, List<String> voterInfo){
        this.voterInfo=voterInfo;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.voterinfoitem,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.voterEmail.setText(voterInfo.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return voterInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView voterEmail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            voterEmail=itemView.findViewById(R.id.voterEmail);
        }
    }
}
