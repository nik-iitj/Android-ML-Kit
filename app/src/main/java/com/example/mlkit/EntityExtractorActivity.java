package com.example.mlkit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EntityExtractorActivity extends AppCompatActivity {
    EditText editText;
    Button extract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_extractor);

        editText = findViewById(R.id.edit_des_text);
        extract = findViewById(R.id.extract);





        extract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!TextUtils.isEmpty(editText.getText())){

                    Intent intent = new Intent(EntityExtractorActivity.this,ExtractedActivity.class);
                    intent.putExtra("string", editText.getText().toString());
                    startActivity(intent);



                } else{

                    Toast.makeText(EntityExtractorActivity.this, "Can't leave blank !", Toast.LENGTH_SHORT).show();


                }




            }
        });



    }
}