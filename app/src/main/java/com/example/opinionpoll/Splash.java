package com.example.opinionpoll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

        private Animation fadeIn;
        private TextView text;
        private ImageView img;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);

            fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            text = findViewById(R.id.textView6);
            img = findViewById(R.id.img_batch);
            img.setAnimation(fadeIn);
            text.setAnimation(fadeIn);


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Splash.this,Login.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    finish();
                }
            },4000);

        }
    }