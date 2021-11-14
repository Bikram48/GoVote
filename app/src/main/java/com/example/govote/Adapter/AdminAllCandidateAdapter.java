package com.example.govote.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.govote.Model.Candidate;
import com.example.govote.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminAllCandidateAdapter extends RecyclerView.Adapter<AdminAllCandidateAdapter.ViewHolder> {
    private Context context;
    private List<Candidate> candidateList;
    public AdminAllCandidateAdapter(Context context, List<Candidate> candidateList){
        this.context=context;
        this.candidateList=candidateList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_all_candidate_item,parent,false);
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
        holder.deleteBtn.setSelected(candidate.isClicked());
        holder.deleteBtn.setTag(position);
    }

    @Override
    public int getItemCount() {
        return candidateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private MaterialButton deleteBtn;
        private TextView candidateName,candidateDescription;
        private CircleImageView candidatePicture;
        public ViewHolder(@NonNull View listItemView) {
            super(listItemView);
            deleteBtn=itemView.findViewById(R.id.candidateDeleteBtn);
            candidateName = (TextView) listItemView.findViewById(R.id.candidate_name);
            candidateDescription= (TextView) listItemView.findViewById(R.id.candidate_description);
            candidatePicture=(CircleImageView) listItemView.findViewById(R.id.candidatePicture);
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
            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseDatabase.getInstance().getReference("Candidate")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot snapshot1:snapshot.getChildren()){

                                        if(snapshot1.child("name").getValue().toString().equals(candidateList.get(pos).getName())){
                                            Log.d("kamaal", "onDataChange: "+snapshot1.getKey());
                                            FirebaseDatabase.getInstance().getReference("Candidate")
                                                    .child(snapshot1.getKey()).removeValue();

                                        }
                                    }
                                    candidateList.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    notifyItemRangeChanged(getAdapterPosition(),candidateList.size());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            });
            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
    }
}
