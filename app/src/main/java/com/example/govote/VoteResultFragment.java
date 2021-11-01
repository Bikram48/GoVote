package com.example.govote;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.govote.Adapter.VoteResultDisplayAdapter;
import com.example.govote.Adapter.VotingResultAdapter;
import com.example.govote.Model.User;
import com.example.govote.Model.VoteInfo;
import com.example.govote.Model.VoteResult;
import com.example.govote.Service.MyService;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;


public class VoteResultFragment extends Fragment implements View.OnClickListener{
    private PieChart pieChart;
    private LinearLayout election_result_layout;
    private AppCompatButton publishResultBtn;
    private DatabaseReference databaseReference;
    private DatabaseReference voterReference;
    private String voterName;
    private ArrayList<String> candidates = new ArrayList<>();
    private VoteResultDisplayAdapter voteResultDisplayAdapter;
    private static final String JOB_TAG="jobtag";
    private FirebaseJobDispatcher jobDispatcher;
    private ArrayList<VoteResult> voteResults;
    private int total;
    public VoteResultFragment() {
        // Required empty public constructor
        databaseReference=FirebaseDatabase.getInstance().getReference("Vote");
        voterReference=FirebaseDatabase.getInstance().getReference("Users");
    }

    public void startJob(){
        Job job=jobDispatcher.newJobBuilder().setService(MyService.class)
                .setLifetime(Lifetime.FOREVER).
                setRecurring(true)
                .setTag(JOB_TAG)
                .setTrigger(Trigger.executionWindow(10,15))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setReplaceCurrent(false)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
        jobDispatcher.mustSchedule(job);
        Toast.makeText(getContext(), "Job Started", Toast.LENGTH_SHORT).show();
    }

    public void stopJob(View view){
        jobDispatcher.cancel(JOB_TAG);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_vote_result, container, false);
        pieChart=view.findViewById(R.id.barChart);
        publishResultBtn=view.findViewById(R.id.publishResult);
        election_result_layout=view.findViewById(R.id.election_result_layout);
        voteResults=new ArrayList<>();
       // jobDispatcher=new FirebaseJobDispatcher(new GooglePlayDriver(getContext()));
        publishResultBtn.setOnClickListener(this::onClick);



        FirebaseDatabase.getInstance().getReference("VoteCount")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                        if(snapshot2.exists()){
                            for(DataSnapshot snapshot:snapshot2.getChildren()){
                                CardView newCard = new CardView(getActivity().getApplicationContext());
                                PieChart pieChart = new PieChart(getActivity().getApplicationContext());
                                pieChart.setId(View.generateViewId());
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(800, 500);
                                pieChart.setLayoutParams(layoutParams);
                                pieChart.getDescription().setEnabled(false);
                                pieChart.setRotationEnabled(true);
                                pieChart.setDragDecelerationFrictionCoef(0.9f);
                                pieChart.setRotationAngle(0);
                                pieChart.setHighlightPerTapEnabled(true);
                                //adding animation so the entries pop up from 0 degree
                                pieChart.animateY(1400, Easing.EaseInOutQuad);
                                //setting the color of the hole in the middle, default white
                                pieChart.setHoleColor(Color.parseColor("#0f0f0f"));
                                String label = "Candidates";
                                Map<String, Integer> typeAmountMap = new HashMap<>();
                                LinearLayout newLinearLayout = new LinearLayout(getActivity().getApplicationContext());
                                newLinearLayout.setOrientation(LinearLayout.VERTICAL);
                                newCard.setCardElevation(15);
                                newCard.setContentPadding(15,15,15,15);
                                newCard.setRadius(10);
                                newCard.setPreventCornerOverlap(true);
                                newCard.setUseCompatPadding(true);
                                newCard.setMaxCardElevation(20);
                                TextView electionNameTxt = new TextView(getActivity().getApplicationContext());

                                TextView candidateTxtView = new TextView(getActivity().getApplicationContext());
                                ArrayList<PieEntry> pieEntries=new ArrayList<PieEntry>();
                                for(DataSnapshot snapshot1:snapshot.getChildren()) {

                                    candidates.add(snapshot1.getKey().toString());
                                    String voteCount = snapshot1.child("count").getValue().toString();
                                    int counts = Integer.parseInt(voteCount);
                                    String election = snapshot.getKey().toString();
                                    String candidate = snapshot1.getKey();
                                    voteResults.add(new VoteResult(counts, election, candidate));
                                    pieEntries.add(new PieEntry(counts,snapshot1.getKey()));
                                    electionNameTxt.setText(election);
                                    electionNameTxt.setTextSize(30);
                                   // typeAmountMap.put(candidates.get(i),Integer.parseInt(votes));
                                    //collecting the entries with label name

                                }
                                newLinearLayout.addView(electionNameTxt);
                                PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
                                //setting text size of the value
                                pieDataSet.setValueTextSize(12f);
                                //providing color list for coloring different entries
                                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                //grouping the data set from entry to chart
                                PieData pieData = new PieData(pieDataSet);
                                //showing the value of the entries, default true if not set
                                pieData.setDrawValues(true);
                                pieChart.setData(pieData);
                                pieChart.invalidate();
                                candidateTxtView.setTextSize(20);
                                newLinearLayout.addView(candidateTxtView);
                                newLinearLayout.addView(pieChart);
                                newLinearLayout.setPadding(5,5,5,5);
                                newCard.addView(newLinearLayout);
                                newLinearLayout.setBackgroundColor(Color.parseColor("#ffc979"));
                                election_result_layout.addView(newCard);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        //getInfo();
        return view;
    }


    private List<VoteInfo> getInfo(){
        List<VoteInfo> voteInfoList=new ArrayList<>();
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        /*
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
        voteInfoList.add(new VoteInfo("president","bikram","mithun",2));
         */
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {

                    VoteInfo voteInfo=new VoteInfo();
                    FirebaseDatabase.getInstance().getReference("VoteCount")
                            .child(dataSnapshot.child("candidateName").getValue().toString())
                            .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            if(snapshot2.exists()){
                                String counter=snapshot2.child("count").getValue().toString();
                                voteInfo.setVoteCount(Integer.parseInt(counter));
                                Log.d("totalcount", "count: "+total);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return voteInfoList;
    }

    @Override
    public void onClick(View view) {
        BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(getActivity())
                .setTitle("Result Publish?")
                .setMessage("Are you sure want to publish the result?")
                .setCancelable(true)
                .setPositiveButton("Via Email ", R.drawable.ic_send, new BottomSheetMaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        startJob();
                        Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("To HomePage", R.drawable.ic_baseline_home_24, new BottomSheetMaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(getContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                })
                .build();
        // Show Dialog
        mBottomSheetDialog.show();

    }
}