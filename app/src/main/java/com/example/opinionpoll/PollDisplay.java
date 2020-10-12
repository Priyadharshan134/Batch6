package com.example.opinionpoll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PollDisplay extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference questioRref,resultRef;
    private TextView text_question;
    private RadioButton rad_op1,rad_op2,rad_op3,rad_op4;
    private Button submitVote;
    private FirebaseAuth mAuth;
    private AlertDialog.Builder dbuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_display);

        text_question = findViewById(R.id.dis_question);
        rad_op1 = findViewById(R.id.radio_option1);
        rad_op2 = findViewById(R.id.radio_option2);
        rad_op3 = findViewById(R.id.radio_option3);
        rad_op4 = findViewById(R.id.radio_option4);
        submitVote = findViewById(R.id.btn_vote);
        dbuilder = new AlertDialog.Builder(this);

        mAuth = FirebaseAuth.getInstance();
        final String user = mAuth.getCurrentUser().getUid();

        final Intent intent = getIntent();
        final String questionId = intent.getExtras().get("id").toString();
        final String owner = intent.getExtras().get("owner").toString();

        database = FirebaseDatabase.getInstance();
        questioRref = database.getReference("Result").child(owner).child(questionId);
        questioRref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int check = 0;

                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (user.contentEquals(ds.getKey().toString()))
                        check++;
                }

                if(check == 0){
                    String question = snapshot.child("question").getValue().toString();
                    String option1 = snapshot.child("option1").getValue().toString();
                    String option2 = snapshot.child("option2").getValue().toString();
                    String optionNum = snapshot.child("optionNum").getValue().toString();
                    if(optionNum.contentEquals("2")){
                        text_question.setText(question);
                        rad_op1.setText(option1);
                        rad_op2.setText(option2);
                        rad_op3.setVisibility(View.GONE);
                        rad_op4.setVisibility(View.GONE);
                    }
                    if(optionNum.contentEquals("3")) {
                        String option3 = snapshot.child("option3").getValue().toString();
                        text_question.setText(question);
                        rad_op1.setText(option1);
                        rad_op2.setText(option2);
                        rad_op3.setText(option3);
                        rad_op4.setVisibility(View.GONE);
                    }
                    if(optionNum.contentEquals("4")) {
                        String option3 = snapshot.child("option3").getValue().toString();
                        String option4 = snapshot.child("option4").getValue().toString();
                        text_question.setText(question);
                        rad_op1.setText(option1);
                        rad_op2.setText(option2);
                        rad_op3.setText(option3);
                        rad_op4.setText(option4);
                    }
                }
                else{
                    text_question.setVisibility(View.GONE);
                    rad_op2.setVisibility(View.GONE);
                    rad_op1.setVisibility(View.GONE);
                    rad_op3.setVisibility(View.GONE);
                    rad_op4.setVisibility(View.GONE);
                    submitVote.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"You have answered the poll",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        submitVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating dialog box
                AlertDialog alert = dbuilder.create();
                if ((rad_op1.isChecked()) || (rad_op2.isChecked()) || (rad_op3.isChecked()) || (rad_op4.isChecked())) {
                    dbuilder.setMessage("Do you want to submit this answer?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (rad_op1.isChecked())
                                        submitAnswer(rad_op1.getText().toString(), questionId, owner);
                                    else if (rad_op2.isChecked())
                                        submitAnswer(rad_op2.getText().toString(), questionId, owner);
                                    else if (rad_op3.isChecked())
                                        submitAnswer(rad_op3.getText().toString(), questionId, owner);
                                    else
                                        submitAnswer(rad_op4.getText().toString(), questionId, owner);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });

                    //Setting the title manually
                    alert.setTitle("OPINION POLL");
                    alert.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Choose an option", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void submitAnswer(String Option,String id,String owner) {
        String questionID = id;
        String chosenOption = Option;
        database = FirebaseDatabase.getInstance();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        resultRef = database.getReference("Result").child(owner).child(questionID);
        resultRef.child(userid).setValue(chosenOption);
        startActivity(new Intent(getApplicationContext(),Profile.class));

    }
}