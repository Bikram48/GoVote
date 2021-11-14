package com.example.govote.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.govote.Model.Election;
import com.example.govote.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdminAllElectionAdapter extends RecyclerView.Adapter<AdminAllElectionAdapter.ViewHolder> {
    private List<Election> electionList;
    private Context context;
    public AdminAllElectionAdapter(List<Election> electionList,Context context){
        this.electionList=electionList;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_all_election_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.electionName.setText(electionList.get(position).getName());
        holder.electionEndDate.setText(electionList.get(position).getEndDate());
        holder.deleteBtn.setTag(position);
    }

    @Override
    public int getItemCount() {
        return electionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView electionName;
        private TextView electionEndDate;
        private MaterialButton deleteBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteBtn=itemView.findViewById(R.id.electionDeleteBtn);
            electionName=itemView.findViewById(R.id.electionTitle);
            electionEndDate=itemView.findViewById(R.id.electionEndDate);
            deleteBtn.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            Integer pos=(Integer) deleteBtn.getTag();
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Are you sure you want to remove it.");
            builder1.setCancelable(true);

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder1.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase.getInstance().getReference("Election")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for(DataSnapshot snapshot1:snapshot.getChildren()){

                                                if(snapshot1.child("name").getValue().toString().equals(electionList.get(pos).getName())){
                                                    Log.d("kamaal", "onDataChange: "+snapshot1.getKey());
                                                    FirebaseDatabase.getInstance().getReference("Election")
                                                            .child(snapshot1.getKey()).removeValue();

                                                }
                                            }
                                            electionList.remove(getAdapterPosition());
                                            notifyItemRemoved(getAdapterPosition());
                                            notifyItemRangeChanged(getAdapterPosition(),electionList.size());
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
            //Toast.makeText(context, "name: "+electionList.get(pos).getName(), Toast.LENGTH_SHORT).show();

        }
    }
}
