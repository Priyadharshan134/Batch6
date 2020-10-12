package com.example.opinionpoll;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Create extends AppCompatActivity {

    private TextInputEditText edt_opt1, edt_opt2, edt_opt3, edt_opt4;
    private EditText edt_qstn;
    private Button btn_sbt;
    private TextView txtopt;
    private String get_question, get_option1, get_option2, get_option3, get_option4;
    private FirebaseDatabase rootnode;
    private RadioButton opt_2,opt_3,opt_4;
    private DatabaseReference questionref,resRef,timeref;
    private CardView cardCreateOption,cardsubmit;
    private String creation_date,creation_time;
    private AlertDialog.Builder builder;
    private TextInputLayout layoutop3,layoutop4;
    private static int optionNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        txtopt = findViewById(R.id.textView_opt);
        edt_qstn = findViewById(R.id.edt_qstn);
        edt_opt1 = findViewById(R.id.edt_opt1);
        edt_opt2 = findViewById(R.id.edt_opt2);
        edt_opt3 = findViewById(R.id.edt_opt3);
        edt_opt4 = findViewById(R.id.edt_opt4);
        layoutop3 = findViewById(R.id.inputOp3);
        layoutop4 = findViewById(R.id.inputOp4);
        opt_2 = findViewById(R.id.two_opt);
        opt_3 = findViewById(R.id.three_opt);
        opt_4 = findViewById(R.id.four_opt);
        btn_sbt = findViewById(R.id.btn_sbt);
        cardCreateOption = findViewById(R.id.card_create_option);
        cardsubmit = findViewById(R.id.card_btn_sbt);

        opt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardCreateOption.setVisibility(View.VISIBLE);
                layoutop3.setVisibility(View.GONE);
                layoutop4.setVisibility(View.GONE);
                cardsubmit.setVisibility(View.VISIBLE);
                txtopt.setVisibility(View.VISIBLE);
                optionNum=2;
            }
        });
        opt_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardCreateOption.setVisibility(View.VISIBLE);
                layoutop3.setVisibility(View.VISIBLE);
                layoutop4.setVisibility(View.GONE);
                cardsubmit.setVisibility(View.VISIBLE);
                txtopt.setVisibility(View.VISIBLE);
                optionNum=3;
            }
        });
        opt_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardCreateOption.setVisibility(View.VISIBLE);
                layoutop3.setVisibility(View.VISIBLE);
                layoutop4.setVisibility(View.VISIBLE);
                txtopt.setVisibility(View.VISIBLE);
                cardsubmit.setVisibility(View.VISIBLE);
                optionNum=4;
            }
        });

        builder = new AlertDialog.Builder(this);
        btn_sbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitted();
            }
        });
    }

    public void onSubmitted() {
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String mailID = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        get_question = edt_qstn.getText().toString().trim();
        get_option1 = edt_opt1.getText().toString().trim();
        get_option2 = edt_opt2.getText().toString().trim();
        get_option3 = edt_opt3.getText().toString().trim();
        get_option4 = edt_opt4.getText().toString().trim();

        rootnode = FirebaseDatabase.getInstance();
        questionref = rootnode.getReference("Question_master");
        resRef = rootnode.getReference("Result");
        timeref = rootnode.getReference("Timestamps");

        if (optionNum == 2){
            if ((!get_question.isEmpty()) && (!get_option1.isEmpty()) && (!get_option2.isEmpty())) {
                final String questionid = questionref.push().getKey();
                builder.setMessage("Do you want to create this poll ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                timeref.child(userID).child(questionid).setValue(ServerValue.TIMESTAMP);
                                timeref.child(userID).child(questionid).addValueEventListener(new ValueEventListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        long timestamp = Long.parseLong(snapshot.getValue().toString());
                                        creation_time = getTime(timestamp);
                                        creation_date = getDate(timestamp);

                                        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                                .setLink(Uri.parse("https://opinionpoll2020.github.io/opinionpoll/" + userID + "/" + questionid + "/"))
                                                .setDomainUriPrefix("https://poll2020.page.link")
                                                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.example.opinionpoll")
                                                        .setFallbackUrl(Uri.parse("https://opinion2020.github.io/opinionpoll/"))
                                                        .build())
                                                .buildDynamicLink();

                                        Uri dynamicLinkUri = dynamicLink.getUri();
                                        String link = dynamicLinkUri.toString();
                                        Helper helper = new Helper(get_question, get_option1, get_option2, mailID, userID, creation_date, creation_time, questionid, link, optionNum);
                                        questionref.child(userID).child(questionid).setValue(helper);
                                        ResultHelper resultHelper = new ResultHelper(get_question, get_option1, get_option2, questionid,creation_date, creation_time, mailID, userID,optionNum);
                                        resRef.child(userID).child(questionid).setValue(resultHelper);
                                        Intent intent = new Intent(getApplicationContext(), Live.class);
                                        startActivity(intent);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
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
                alert.setTitle(R.string.app_name);
                alert.show();
            }
            else {
                Toast.makeText(this, "Enter all Fields", Toast.LENGTH_SHORT).show();
            }
        }

       if (optionNum == 3){
            if ((!get_question.isEmpty()) && (!get_option1.isEmpty()) && (!get_option2.isEmpty()) && (!get_option3.isEmpty())) {
                final String questionid = questionref.push().getKey();
                builder.setMessage("Do you want to create this poll ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                timeref.child(userID).child(questionid).setValue(ServerValue.TIMESTAMP);
                                timeref.child(userID).child(questionid).addValueEventListener(new ValueEventListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        long timestamp = Long.parseLong(snapshot.getValue().toString());
                                        creation_time = getTime(timestamp);
                                        creation_date = getDate(timestamp);

                                        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                                .setLink(Uri.parse("https://opinionpoll2020.github.io/opinionpoll/" + userID + "/" + questionid + "/"))
                                                .setDomainUriPrefix("https://poll2020.page.link")
                                                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.example.opinionpoll")
                                                        .setFallbackUrl(Uri.parse("https://opinion2020.github.io/opinionpoll/"))
                                                        .build())
                                                .buildDynamicLink();

                                        Uri dynamicLinkUri = dynamicLink.getUri();
                                        String link = dynamicLinkUri.toString();
                                        Helper helper = new Helper(get_question, get_option1, get_option2, get_option3, mailID, userID, creation_date, creation_time, questionid, link,optionNum);
                                        questionref.child(userID).child(questionid).setValue(helper);
                                        ResultHelper resultHelper = new ResultHelper(get_question, get_option1, get_option2, get_option3, questionid,creation_date, creation_time, mailID, userID,optionNum);
                                        resRef.child(userID).child(questionid).setValue(resultHelper);
                                        Intent intent = new Intent(getApplicationContext(), Live.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
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
                alert.setTitle(R.string.app_name);
                alert.show();
            }
            else {
                Toast.makeText(this, "Enter all Fields", Toast.LENGTH_SHORT).show();
            }
        }

        if (optionNum == 4){
            if ((!get_question.isEmpty()) && (!get_option1.isEmpty()) && (!get_option2.isEmpty()) && (!get_option3.isEmpty()) && (!get_option4.isEmpty())) {
                final String questionid = questionref.push().getKey();
                builder.setMessage("Do you want to create this poll ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                timeref.child(userID).child(questionid).setValue(ServerValue.TIMESTAMP);
                                timeref.child(userID).child(questionid).addValueEventListener(new ValueEventListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        long timestamp = Long.parseLong(snapshot.getValue().toString());
                                        creation_time = getTime(timestamp);
                                        creation_date = getDate(timestamp);

                                        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                                .setLink(Uri.parse("https://opinionpoll2020.github.io/opinionpoll/" + userID + "/" + questionid + "/"))
                                                .setDomainUriPrefix("https://poll2020.page.link")
                                                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.example.opinionpoll")
                                                        .setFallbackUrl(Uri.parse("https://opinion2020.github.io/opinionpoll/"))
                                                        .build())
                                                .buildDynamicLink();

                                        Uri dynamicLinkUri = dynamicLink.getUri();
                                        String link = dynamicLinkUri.toString();
                                        Helper helper = new Helper(get_question, get_option1, get_option2,get_option3, get_option4, mailID, userID, creation_date, creation_time, questionid, link, optionNum);
                                        questionref.child(userID).child(questionid).setValue(helper);
                                        ResultHelper resultHelper = new ResultHelper(get_question, get_option1, get_option2, get_option3, get_option4, questionid,creation_date, creation_time, mailID, userID,optionNum);
                                        resRef.child(userID).child(questionid).setValue(resultHelper);
                                        Intent intent = new Intent(getApplicationContext(), Live.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
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
                alert.setTitle(R.string.app_name);
                alert.show();
            }
            else {
                Toast.makeText(this, "Enter all Fields", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String cDate = String.valueOf(simpleDateFormat.format(date));
        return cDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getTime(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss");
        String cTime = String.valueOf(simpleTimeFormat.format(date));
        cTime = LocalTime.parse(cTime).format(DateTimeFormatter.ofPattern("h:mma"));
        return cTime;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}