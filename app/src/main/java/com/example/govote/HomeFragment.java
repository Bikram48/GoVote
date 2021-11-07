package com.example.govote;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.govote.Adapter.SliderAdapter;
import com.example.govote.Model.VoteResult;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {
    private ArrayList<String> candidates = new ArrayList<>();
    private ArrayList<VoteResult> voteResults;
    private TextView electionResultTitle;
    private HorizontalScrollView horizontalScrollView;
    private DatabaseReference voteReference,electionReference,voteCountReference;
    SliderView sliderView;
    private ArrayList<String>  elctionList,electionNameList;
    private String status;
    private LinearLayout publishedResultLayout;
    int[] images = {R.mipmap.voting1,
            R.mipmap.voting2};
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        voteResults=new ArrayList<>();
        elctionList=new ArrayList<>();
        electionNameList=new ArrayList<>();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            voteReference = FirebaseDatabase.getInstance().getReference("Vote")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            electionReference = FirebaseDatabase.getInstance().getReference("Election");
            voteCountReference = FirebaseDatabase.getInstance().getReference("VoteCount");
            if (getContext() != null) {
                voteReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                String electionName = snapshot1.getKey().toString();
                                Log.d("HomeFragment", "firstname: " + electionName);
                                elctionList.add(electionName);
                            }

                            electionReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                    for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                        String name = snapshot3.child("name").getValue().toString();
                                        Log.d("HomeFragment", "name: " + name);
                                        if (elctionList.contains(name)) {
                                            status = snapshot3.child("isEnded").getValue().toString();
                                        }

                                    }
                                    if (status.equals("Y")) {
                                        Log.d("HomeFragment", "Hello ");
                                        electionResult(context);
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
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);

        horizontalScrollView=view.findViewById(R.id.horizontalScrollView);
        publishedResultLayout=view.findViewById(R.id.published_result);
        electionResultTitle=view.findViewById(R.id.textView9);
        sliderView = view.findViewById(R.id.image_slider);

        SliderAdapter sliderAdapter = new SliderAdapter(images);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void electionResult(Context context){
        voteReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    publishedResultLayout.setVisibility(View.VISIBLE);
                    electionResultTitle.setVisibility(View.VISIBLE);
                    horizontalScrollView.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String electionName = snapshot1.getKey().toString();
                        Log.d("HomeFragment", "firstname: " + electionName);
                        electionNameList.add(electionName);
                    }
                    voteCountReference
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            String name = snapshot1.getKey().toString();
                                            if (electionNameList.contains(name)) {
                                                CardView newCard = new CardView(context);
                                                PieChart pieChart = new PieChart(context);
                                                pieChart.setId(View.generateViewId());
                                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(800, 300);
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
                                                String label = "";
                                                Map<String, Integer> typeAmountMap = new HashMap<>();
                                                LinearLayout newLinearLayout = new LinearLayout(context);
                                                newLinearLayout.setOrientation(LinearLayout.VERTICAL);
                                                newCard.setCardElevation(15);
                                                newCard.setContentPadding(15, 15, 15, 15);
                                                newCard.setRadius(10);
                                                newCard.setPreventCornerOverlap(true);
                                                newCard.setUseCompatPadding(true);
                                                newCard.setMaxCardElevation(20);
                                                TextView electionNameTxt = new TextView(context);
                                                electionNameTxt.setTextSize(12f);
                                                electionNameTxt.setGravity(Gravity.CENTER_HORIZONTAL);
                                                electionNameTxt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                                                electionNameTxt.setTypeface(null, Typeface.BOLD);
                                                TextView candidateTxtView = new TextView(context);
                                                TextView voteResultTxtView = new TextView(context);
                                                ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();

                                                for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                    candidates.add(snapshot2.getKey().toString());
                                                    String voteCount = snapshot2.child("count").getValue().toString();
                                                    int counts = Integer.parseInt(voteCount);
                                                    String election = snapshot1.getKey().toString();
                                                    String candidate = snapshot2.getKey();
                                                    voteResults.add(new VoteResult(counts, election, candidate));
                                                    pieEntries.add(new PieEntry(counts, snapshot2.getKey()));
                                                    electionNameTxt.setText(election);
                                                    //electionNameTxt.setTextSize(30);
                                                    // typeAmountMap.put(candidates.get(i),Integer.parseInt(votes));
                                                    //collecting the entries with label name

                                                }
                                                VoteResult max = Collections.max(voteResults);

                                                newLinearLayout.addView(electionNameTxt);
                                                PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
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
                                                candidateTxtView.setTextSize(12);
                                                voteResultTxtView.setText(max.getCandidate()+" has win the election by "+max.getVoteCount()+" votes");
                                                voteResultTxtView.setTextSize(10);
                                                voteResultTxtView.setGravity(Gravity.CENTER_HORIZONTAL);
                                                voteResultTxtView.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                                                voteResultTxtView.setTypeface(null, Typeface.BOLD);
                                                newLinearLayout.addView(candidateTxtView);
                                                newLinearLayout.addView(voteResultTxtView);
                                                newLinearLayout.addView(pieChart);
                                                newLinearLayout.setPadding(5, 5, 5, 5);
                                                newCard.addView(newLinearLayout);
                                                newLinearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                                                publishedResultLayout.addView(newCard);


                                            }
                                        }
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


    }

}