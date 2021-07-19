package com.example.mlkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.entityextraction.DateTimeEntity;
import com.google.mlkit.nl.entityextraction.Entity;
import com.google.mlkit.nl.entityextraction.EntityAnnotation;
import com.google.mlkit.nl.entityextraction.EntityExtraction;
import com.google.mlkit.nl.entityextraction.EntityExtractionParams;
import com.google.mlkit.nl.entityextraction.EntityExtractor;
import com.google.mlkit.nl.entityextraction.EntityExtractorOptions;
import com.google.mlkit.nl.entityextraction.FlightNumberEntity;
import com.google.mlkit.nl.entityextraction.MoneyEntity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class ExtractedActivity extends AppCompatActivity {
    TextView textView;
    String classText;
    String k;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extracted);

        textView = findViewById(R.id.text);
        textView.setMovementMethod(new ScrollingMovementMethod());
        k="";
        Map<String,List<String>>p= new HashMap<String, List<String>>();







        Bundle bundle = getIntent().getExtras();
        classText=bundle.getString("string");

        //startClassification();

        EntityExtractor entityExtractor = EntityExtraction.getClient(new EntityExtractorOptions.Builder(EntityExtractorOptions.ENGLISH).build());

        entityExtractor
                .downloadModelIfNeeded()
                .addOnSuccessListener(
                        aVoid -> {
                            Toast.makeText(this, "Model Ready", Toast.LENGTH_SHORT).show();
                        })
                .addOnFailureListener(
                        exception -> {
                            Toast.makeText(this, "Model Extraction failed", Toast.LENGTH_SHORT).show();
                        });
        EntityExtractionParams params = new EntityExtractionParams.Builder(classText).setPreferredLocale(Locale.getDefault()).setReferenceTimeZone(TimeZone.getDefault())
                .build();

        entityExtractor.annotate(params).addOnSuccessListener(new OnSuccessListener<List<EntityAnnotation>>() {
            @Override
            public void onSuccess(@NonNull @NotNull List<EntityAnnotation> entityAnnotations) {

                for(EntityAnnotation entityAnnotation:entityAnnotations){

                    List<Entity> entities = entityAnnotation.getEntities();

                    for(Entity entity:entities){

                        switch (entity.getType()){

                            case Entity.TYPE_DATE_TIME:
                                DateTimeEntity dateTimeEntity = entity.asDateTimeEntity();
                                //assert dateTimeEntity != null;

                                if(dateTimeEntity!=null){

                                    k= k + "\n\n" + "Granularity : " + Integer.toString(dateTimeEntity.getDateTimeGranularity()) + "\n"
                                    + "Timestamp:" + Integer.toString((int) dateTimeEntity.getTimestampMillis()) +"\n";


                                    Toast.makeText(ExtractedActivity.this,Integer.toString(dateTimeEntity.getDateTimeGranularity()) , Toast.LENGTH_SHORT).show();
                                    Toast.makeText(ExtractedActivity.this, Integer.toString((int) dateTimeEntity.getTimestampMillis()), Toast.LENGTH_SHORT).show();

                                }




                            case Entity.TYPE_FLIGHT_NUMBER:
                                FlightNumberEntity flightNumberEntity = entity.asFlightNumberEntity();

                                if(flightNumberEntity!=null){

                                    k =k + "\n\n" + "Airline Code : " + flightNumberEntity.getAirlineCode() +"\n" + "Flight Number : "+
                                            flightNumberEntity.getFlightNumber() + "\n";


                                    Toast.makeText(ExtractedActivity.this, flightNumberEntity.getAirlineCode(), Toast.LENGTH_SHORT).show();

                                }




                            case Entity.TYPE_MONEY:
                                MoneyEntity moneyEntity = entity.asMoneyEntity();

                                if(moneyEntity!=null){
                                    Toast.makeText(ExtractedActivity.this, moneyEntity.getUnnormalizedCurrency(), Toast.LENGTH_SHORT).show();

                                    k = k+ "\n\n" + "Currency : " + moneyEntity.getUnnormalizedCurrency() + "\n" +
                                            "Integer Part : " + Integer.toString(moneyEntity.getIntegerPart()) + "\n" +
                                            "Fractional Part : " + Integer.toString(moneyEntity.getFractionalPart());


                                }






                            default:

                                Toast.makeText(ExtractedActivity.this, "Not found", Toast.LENGTH_SHORT).show();

                                k =k + "\n" + "Entity: " + entity;




                        }








                    }





                }

                if(k.isEmpty()){

                    textView.setText("No identifiable data extracted :( \nBut we'll improve in coming time :) ");

                } else{
                    textView.setText(k);

                }







            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(ExtractedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });





    }



}