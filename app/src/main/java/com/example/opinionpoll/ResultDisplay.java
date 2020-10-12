package com.example.opinionpoll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResultDisplay extends AppCompatActivity {

    private BarChart chart;
    private FirebaseDatabase database;
    private DatabaseReference timeRef,resultRef,qref;
    private int option1Count=0,option2Count=0,option3Count=0,option4Count=0;
    private Button deletepoll;
    private TextView disQuestion,LiveOrCompleted,total;
    private AlertDialog.Builder builder;
    private TextView resOp1,resOp2,resOp3,resOp4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);

        disQuestion = findViewById(R.id.question_dis);
        LiveOrCompleted = findViewById(R.id.liv_or_comp);
        resOp1 = findViewById(R.id.resultOp1);
        resOp2 = findViewById(R.id.resultOp2);
        resOp3 = findViewById(R.id.resultOp3);
        resOp4 = findViewById(R.id.resultOp4);
        deletepoll = findViewById(R.id.btn_delete);
        chart = findViewById(R.id.barchart);
        total = findViewById(R.id.total_votes);
        builder = new AlertDialog.Builder(this);

        Intent intent = getIntent();
        String question = intent.getExtras().getString("Ques");
        String questionId = intent.getExtras().getString("QuesID");
        String userId = intent.getExtras().getString("IDUser");

        disQuestion.setText("Question : "+question);

        database = FirebaseDatabase.getInstance();
        qref = database.getReference("Question_master").child(userId).child(questionId);
        timeRef = database.getReference("Timestamps").child(userId).child(questionId);
        qref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() == 0) {
                    LiveOrCompleted.setText("Completed");
                }
                else{
                    LiveOrCompleted.setText("Live");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


        resultRef = database.getReference("Result").child(userId).child(questionId);
        qref = database.getReference("Question_master").child(userId).child(questionId);
        resultRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String option1 = snapshot.child("option1").getValue().toString();
                String option2 = snapshot.child("option2").getValue().toString();
                resOp1.setText("1) "+option1);
                resOp2.setText("2) "+option2);
                String optionNum = snapshot.child("optionNum").getValue().toString();
                if(optionNum.contentEquals("2")){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if(option1.contentEquals(dataSnapshot.getValue().toString()))
                            option1Count++;
                        if(option2.contentEquals(dataSnapshot.getValue().toString()))
                            option2Count++;
                    }
                    option1Count--;
                    option2Count--;
                    if((option1Count == 0)&&(option2Count == 0)) {
                        chart.setVisibility(View.GONE);
                        total.setText("Total Votes : 0");
                    }
                    setData(option1Count,option2Count,0,0);
                }
                if(optionNum.contentEquals("3")){
                    String option3 = snapshot.child("option3").getValue().toString();
                    resOp3.setVisibility(View.VISIBLE);
                    resOp3.setText("3) "+option3);
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if(option1.contentEquals(dataSnapshot.getValue().toString()))
                            option1Count++;
                        if(option2.contentEquals(dataSnapshot.getValue().toString()))
                            option2Count++;
                        if(option3.contentEquals(dataSnapshot.getValue().toString()))
                            option3Count++;
                    }
                    option1Count--;
                    option2Count--;
                    option3Count--;
                    if((option1Count == 0)&&(option2Count == 0)&&(option3Count == 0)) {
                        chart.setVisibility(View.GONE);
                        total.setText("Total Votes : 0");
                    }
                    setData(option1Count,option2Count,option3Count,0);
                }
                if(optionNum.contentEquals("4")){
                    String option3 = snapshot.child("option3").getValue().toString();
                    String option4 = snapshot.child("option4").getValue().toString();
                    resOp3.setVisibility(View.VISIBLE);
                    resOp4.setVisibility(View.VISIBLE);
                    resOp3.setText("3) "+option3);
                    resOp4.setText("4) "+option4);
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if(option1.contentEquals(dataSnapshot.getValue().toString()))
                            option1Count++;
                        if(option2.contentEquals(dataSnapshot.getValue().toString()))
                            option2Count++;
                        if(option3.contentEquals(dataSnapshot.getValue().toString()))
                            option3Count++;
                        if(option4.contentEquals(dataSnapshot.getValue().toString()))
                            option4Count++;

                    }
                    option1Count--;
                    option2Count--;
                    option3Count--;
                    option4Count--;
                    if((option1Count == 0)&&(option2Count == 0)&&(option3Count == 0)&&(option4Count == 0)) {
                        chart.setVisibility(View.GONE);
                        total.setText("Total Votes : 0");
                    }
                    setData(option1Count,option2Count,option3Count,option4Count);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        deletepoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("Do you want to delete this poll ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                qref.removeValue();
                                resultRef.removeValue();
                                timeRef.removeValue();
                                startActivity(new Intent(getApplicationContext(),Result.class));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("OPINION POLL");
                alert.show();
            }
        });
    }

    private void setData(int option1Count, int option2Count, int option3Count, int option4Count) {
        XAxis xAxis = chart.getXAxis();
        YAxis Right = chart.getAxisRight();
        YAxis Left = chart.getAxisLeft();
        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

        chart.animateY(500);
        Left.setStartAtZero(true);
        Right.setStartAtZero(true);
        xAxis.setDrawGridLines(false);
        Description description = chart.getDescription();
        description.setEnabled(false);
        xAxis.setEnabled(false);

        float barWidth = 9f;
        float spaceForBar = 10f;
        int totalVotes = option1Count+option2Count+option3Count+option4Count;
        int range = totalVotes+1;

        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisLeft().setAxisMaximum(range);


        total.setText("Total Votes : "+totalVotes);

        BarDataSet set1,set2,set3,set4;
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();

        if(option1Count !=0 ) {
            ArrayList<BarEntry> values1 = new ArrayList<>();
            values1.add(new BarEntry(0 * spaceForBar, option1Count));
            set1 = new BarDataSet(values1, "option1");
            dataSets.add(set1);
            set1.setColor(Color.RED);
        }

        if(option2Count !=0 ) {
            ArrayList<BarEntry> values2 = new ArrayList<>();
            values2.add(new BarEntry(1 * spaceForBar, option2Count));
            set2 = new BarDataSet(values2, "option2");
            dataSets.add(set2);
            set2.setColor(Color.BLUE);
        }

        if(option3Count !=0 ) {
            ArrayList<BarEntry> values3 = new ArrayList<>();
            values3.add(new BarEntry(2 * spaceForBar, option3Count));
            set3 = new BarDataSet(values3, "option3");
            dataSets.add(set3);
            set3.setColor(Color.GREEN);
        }

        if(option4Count !=0 ) {
            ArrayList<BarEntry> values4 = new ArrayList<>();
            values4.add(new BarEntry(3 * spaceForBar, option4Count));
            set4 = new BarDataSet(values4, "option4");
            dataSets.add(set4);
            set4.setColor(Color.YELLOW);
        }

        BarData data = new BarData(dataSets);

        Left.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) Math.floor(value));
            }
        });
        Right.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) Math.floor(value));
            }
        });

        Right.setLabelCount(range);
        Right.setAxisMinimum(0f);
        Right.setAxisMaximum(range);
        Left.setLabelCount(range);
        Left.setAxisMinimum(0f);
        Left.setAxisMaximum(range);
        data.setBarWidth(barWidth);
        chart.setFitBars(true);
        chart.setData(data);
    }
}