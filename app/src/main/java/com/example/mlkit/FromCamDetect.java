package com.example.mlkit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;

public class FromCamDetect extends AppCompatActivity {
    CameraKitView cameraKitView;
    ImageView capImage,flipCam;
    boolean isFront;

    Animation forward,backward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_cam_detect);

        isFront=false;
        cameraKitView = findViewById(R.id.camera);
        capImage  = findViewById(R.id.capImage);
        flipCam = findViewById(R.id.flipCamera);

        forward = AnimationUtils.loadAnimation(this,R.anim.cam_rotate_forward);
        backward=AnimationUtils.loadAnimation(this,R.anim.cam_rotate_backward);

        Bundle bundle = getIntent().getExtras();
        int code = bundle.getInt("code");

        Toast.makeText(this,Integer.toString(code) , Toast.LENGTH_SHORT).show();

        capImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                capImage.startAnimation(forward);
                //capImage.startAnimation(backward);

                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, byte[] bytes) {

                        if(code==1){

                            Intent intent = new Intent(getApplicationContext(),FacialFeatures.class);
                            intent.putExtra("bytes",bytes);
                            intent.putExtra("width",cameraKitView.getWidth());
                            intent.putExtra("height",cameraKitView.getHeight());
                            startActivity(intent);

                        } else if(code==2){


                            Intent intent = new Intent(getApplicationContext(),ClassifiedText.class);
                            intent.putExtra("bytes",bytes);
                            intent.putExtra("width",cameraKitView.getWidth());
                            intent.putExtra("height",cameraKitView.getHeight());
                            startActivity(intent);


                        }



                    }
                });
            }
        });

        flipCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCam.startAnimation(forward);
                if(isFront){
                    cameraKitView.setFacing(CameraKit.FACING_BACK);
                    isFront=false;
                } else{
                    cameraKitView.setFacing(CameraKit.FACING_FRONT);
                    isFront = true;
                }
            }
        });










    }









    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}