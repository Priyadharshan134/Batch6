package com.example.opinionpoll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Profile extends AppCompatActivity {

    private String user,photoUrl;
    private CardView card_create,card_live,card_result,card_share,card_logout;
    private TextView nameDis;
    private ImageView picDis;
    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        card_create = findViewById(R.id.card_create);
        card_live = findViewById(R.id.card_live);
        card_result = findViewById(R.id.card_result);
        card_share = findViewById(R.id.card_share);
        card_logout = findViewById(R.id.card_logout);
        picDis = findViewById(R.id.profile_pic);
        nameDis = findViewById(R.id.user_name);
        user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        photoUrl = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
        builder = new AlertDialog.Builder(this);

        nameDis.setText(user);
        Picasso.with(getApplicationContext())
                .load(photoUrl)
                .resize(70, 70)
                .transform(new CropCircleTransformation())
                .centerCrop()
                .into(picDis);

        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(
                this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if(pendingDynamicLinkData != null)
                            deepLink = pendingDynamicLinkData.getLink();

                        if(deepLink != null){
                            String link = deepLink.toString();
                            String[] id = link.split("/");
                            Intent intent = new Intent(getApplicationContext(),PollDisplay.class);
                            intent.putExtra("owner",id[4]);
                            intent.putExtra("id",id[5]);
                            startActivity(intent);
                        }
                    }
                }
        );

        card_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Create.class));
            }
        });

        card_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Live.class));
            }
        });

        card_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Result.class));
            }
        });

        card_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });

        card_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("Do you want to logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                signOut();
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

    private void share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,"Let me recommend you this application\n" + "https://opinion2020.github.io/opinionpoll/");
        startActivity(Intent.createChooser(shareIntent, "choose one"));
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(Profile.this, Login.class));

                    }
                });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
    }
}