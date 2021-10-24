package com.example.govote.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.govote.Model.VoteResult;
import com.example.govote.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class VoteResultDisplayAdapter extends RecyclerView.Adapter<VoteResultDisplayAdapter.ViewHolder> {
    private List<VoteResult> voteResults;
    private Context context;

    public VoteResultDisplayAdapter(Context context, List<VoteResult> voteResults){
        this.voteResults=voteResults;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.election_result_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<PieEntry> pieEntries=new ArrayList<PieEntry>();
        pieEntries.add(new PieEntry(voteResults.get(position).getVoteCount(),voteResults.get(position).getCandidate()));
        PieDataSet barDataSet=new PieDataSet(pieEntries,"votecount");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        PieData pieData=new PieData(barDataSet);
        holder.pieChart.setData(pieData);
        holder.pieChart.getDescription().setEnabled(false);
        holder.pieChart.setCenterText(voteResults.get(position).getElection());
        holder.pieChart.animate();
    }

    @Override
    public int getItemCount() {
        return voteResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private PieChart pieChart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pieChart=itemView.findViewById(R.id.barChart);
        }
    }
}
