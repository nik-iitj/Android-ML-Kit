package com.example.mlkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    ImageView text,face,entity;
    Animation fabOpen,fabClose,rotateForward,rotateBackward;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.floatingBtn);
        text= findViewById(R.id.textPage);
        face = findViewById(R.id.facialPage);
        entity = findViewById(R.id.entityPage);

        fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotateForward= AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();

                Intent intent = new Intent(MainActivity.this,TextExtractorActivity.class);
                startActivity(intent);

            }
        });

        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FacialFeaturesActivity.class);
                startActivity(intent);

            }
        });

        entity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,EntityExtractorActivity.class);
                startActivity(intent);

            }
        });




    }



    private void animateFab(){

        if(isOpen){
            fab.startAnimation(rotateForward);
            text.startAnimation(fabClose);
            face.startAnimation(fabClose);
            entity.startAnimation(fabClose);
            entity.setClickable(false);
            text.setClickable(false);
            face.setClickable(false);
            isOpen=false;
        } else{

            fab.startAnimation(rotateBackward);
            entity.startAnimation(fabOpen);
            face.startAnimation(fabOpen);
            text.startAnimation(fabOpen);
            entity.setClickable(true);
            face.setClickable(true);
            text.setClickable(true);
            isOpen=true;




        }

    }
}