package com.example.opinionpoll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Live extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private ListView qlistview;
    private ArrayList<Helper> qlist = new ArrayList<>();
    private Helper help;
    private String userID;
    private TextView defaultLive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        qlistview=findViewById(R.id.listview_live);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Question_master").child(userID);
        help = new Helper();

        questionDisplay();

    }
    private void questionDisplay() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final QuestionListAdapter adapter = new QuestionListAdapter(getApplicationContext(),R.layout.live_listview,qlist);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    help = ds.getValue(Helper.class);
                    if (userID.contentEquals(help.getOwnerUserID())) {
                        qlist.add(help);
                        qlistview.setAdapter(adapter);
                    }
                }
                if(qlist.isEmpty()){
                    defaultLive = findViewById(R.id.live_defaut);
                    defaultLive.setVisibility(View.VISIBLE);
                }
                qlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String question = ((TextView) view.findViewById(R.id.list_ques)).getText().toString();
                        String timing = ((TextView) view.findViewById(R.id.list_time)).getText().toString();
                        String quesId = ((TextView) view.findViewById(R.id.list_ques_id)).getText().toString();
                        String user_id = ((TextView) view.findViewById(R.id.list_user_id)).getText().toString();
                        String option1 = ((TextView) view.findViewById(R.id.list_option1)).getText().toString();
                        String option2 = ((TextView) view.findViewById(R.id.list_option2)).getText().toString();
                        String option3 = ((TextView) view.findViewById(R.id.list_option3)).getText().toString();
                        String option4 = ((TextView) view.findViewById(R.id.list_option4)).getText().toString();
                        String link = ((TextView) view.findViewById(R.id.list_link)).getText().toString();

                        Intent intent = new Intent(getApplicationContext(),Poll.class);
                        intent.putExtra("Q",question);
                        intent.putExtra("T",timing);
                        intent.putExtra("QID",quesId);
                        intent.putExtra("idUser",user_id);
                        intent.putExtra("O1",option1);
                        intent.putExtra("O2",option2);
                        intent.putExtra("O3",option3);
                        intent.putExtra("O4",option4);
                        intent.putExtra("Link",link);
                        startActivity(intent);
                    }
                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Profile.class));
    }
}