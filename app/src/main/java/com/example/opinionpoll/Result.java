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

public class Result extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference resRef;
    private ListView reslistview;
    private ArrayList<ResultHelper> reslist = new ArrayList<>();
    private ResultHelper resultHelper = new ResultHelper();
    private String UserID,MailID;
    private TextView defaultResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mAuth = FirebaseAuth.getInstance();
        UserID = mAuth.getCurrentUser().getUid();
        MailID = mAuth.getCurrentUser().getEmail();
        reslistview=findViewById(R.id.listview_result);
        database = FirebaseDatabase.getInstance();
        resRef = database.getReference("Result").child(UserID);
        quesDisplay();
    }
    private void quesDisplay() {
        resRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ResultListAdapter adapter = new ResultListAdapter(getApplicationContext(),R.layout.result_listview,reslist);
                for(DataSnapshot ds: snapshot.getChildren()){
                    resultHelper = ds.getValue(ResultHelper.class);
                    if(MailID.contentEquals(resultHelper.getOwnerMailID()) || UserID.contentEquals(resultHelper.getOwnerUserID())){
                        reslist.add(resultHelper);
                        reslistview.setAdapter(adapter);
                    }
                }
                if(reslist.isEmpty()){
                    defaultResult = findViewById(R.id.result_default);
                    defaultResult.setVisibility(View.VISIBLE);
                }
                reslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String question = ((TextView) view.findViewById(R.id.res_question)).getText().toString();
                        String mail_id = ((TextView) view.findViewById(R.id.res_creation_time)).getText().toString();
                        String quesId = ((TextView) view.findViewById(R.id.res_ques_id)).getText().toString();
                        String user_id = ((TextView) view.findViewById(R.id.res_user_id)).getText().toString();

                        Intent intent = new Intent(getApplicationContext(),ResultDisplay.class);
                        intent.putExtra("Ques",question);
                        intent.putExtra("mail",mail_id);
                        intent.putExtra("QuesID",quesId);
                        intent.putExtra("IDUser",user_id);
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
}