package com.example.opinionpoll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Poll extends AppCompatActivity {
    private TextView question,option1,option2,option3,option4;
    private FirebaseDatabase database;
    private DatabaseReference quesRef,resultRef,timeref;
    private Button delete,end,share;
    private AlertDialog.Builder builder;
    private CardView cardOption3,cardOption4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        question = findViewById(R.id.questionDis);
        option1 = findViewById(R.id.option1Dis);
        option2 = findViewById(R.id.option2Dis);
        option3 = findViewById(R.id.option3Dis);
        option4 = findViewById(R.id.option4Dis);
        cardOption3 = findViewById(R.id.card_option3);
        cardOption4 =findViewById(R.id.card_option4);
        delete = findViewById(R.id.delete);
        end = findViewById(R.id.end);
        share = findViewById(R.id.share);
        builder = new AlertDialog.Builder(this);

        Intent intent = getIntent();
        String pollQuestion = intent.getExtras().getString("Q");
        String pollTime = intent.getExtras().getString("T");
        String pollQId = intent.getExtras().getString("QID");
        String pollUserId = intent.getExtras().getString("idUser");
        String pollOption1 = intent.getExtras().getString("O1");
        String pollOption2 = intent.getExtras().getString("O2");
        String pollOption3 = intent.getExtras().getString("O3");
        String pollOption4 = intent.getExtras().getString("O4");
        final String pollLink = intent.getExtras().getString("Link");

        question.setText(pollQuestion);
        option1.setText(pollOption1);
        option2.setText(pollOption2);
        if(!pollOption4.isEmpty()){
            option3.setText(pollOption3);
            option4.setText(pollOption4);
        }
        else if(!pollOption3.isEmpty()) {
            option3.setText(pollOption3);
            cardOption4.setVisibility(View.GONE);
        }
        else{
            cardOption3.setVisibility(View.GONE);
            cardOption4.setVisibility(View.GONE);
        }


        database = FirebaseDatabase.getInstance();
        quesRef = database.getReference("Question_master").child(pollUserId).child(pollQId);
        resultRef = database.getReference("Result").child(pollUserId).child(pollQId);
        timeref = database.getReference("Timestamps").child(pollUserId).child(pollQId);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("Do you want to delete this poll ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                quesRef.removeValue();
                                resultRef.removeValue();
                                timeref.removeValue();
                                startActivity(new Intent(getApplicationContext(),Live.class));
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

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setMessage("Do you want to end this poll ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                quesRef.removeValue();
                                startActivity(new Intent(getApplicationContext(),Live.class));
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

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,"Answer the question by clicking the link\n" + pollLink);
                startActivity(Intent.createChooser(shareIntent, "Share using"));
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Live.class));
    }
}