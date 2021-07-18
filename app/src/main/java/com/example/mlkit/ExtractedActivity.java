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
                                assert dateTimeEntity != null;

                                if(!p.containsKey("Granularity")){
                                    p.put("Granularity",new ArrayList<String>());
                                }
                                Objects.requireNonNull(p.get("Granularity")).add(Integer.toString(dateTimeEntity.getDateTimeGranularity()));


                                if(!p.containsKey("Timestamp")){
                                    p.put("Timestamp",new ArrayList<String>());
                                }
                                Objects.requireNonNull(p.get("Timestamp")).add(Integer.toString((int) dateTimeEntity.getTimestampMillis()));
//                                k = k + "\n Granularity: " + dateTimeEntity.getDateTimeGranularity() +
//                                "\n" +"Timestamp: " + dateTimeEntity.getTimestampMillis();

                            case Entity.TYPE_FLIGHT_NUMBER:
                                FlightNumberEntity flightNumberEntity = entity.asFlightNumberEntity();
                                //assert flightNumberEntity != null;

                                if(!p.containsKey("Airline Code")){
                                    p.put("Airline Code",new ArrayList<String>());
                                }
                                Objects.requireNonNull(p.get("Airline Code")).add(flightNumberEntity.getAirlineCode());

                                if(!p.containsKey("Flight number")){
                                    p.put("Flight number",new ArrayList<String>());
                                }
                                Objects.requireNonNull(p.get("Flight number")).add(flightNumberEntity.getFlightNumber());

//                                k = k + "\n" + "Airline Code: " + flightNumberEntity.getAirlineCode()
//                                + "\n" + "Flight number: " + flightNumberEntity.getFlightNumber();

                            case Entity.TYPE_MONEY:
                                MoneyEntity moneyEntity = entity.asMoneyEntity();
                                assert moneyEntity != null;

                                if(!p.containsKey("Currency")){
                                    p.put("Currency",new ArrayList<String>());
                                }
                                Objects.requireNonNull(p.get("Currency")).add(moneyEntity.getUnnormalizedCurrency());

                                if(!p.containsKey("Integer Part")){
                                    p.put("Integer Part",new ArrayList<String>());
                                }
                                Objects.requireNonNull(p.get("Integer Part")).add(Integer.toString(moneyEntity.getIntegerPart()));

                                if(!p.containsKey("Fractional Part")){
                                    p.put("Fractional Part",new ArrayList<String>());
                                }
                                Objects.requireNonNull(p.get("Fractional Part")).add(Integer.toString(moneyEntity.getFractionalPart()));
//                                k=k+ "\n" + "Currency: " + moneyEntity.getUnnormalizedCurrency()
//                                        + "\n" + "Integer Part: " + moneyEntity.getIntegerPart()
//                                        + "\n" + "Fractional Part: " + moneyEntity.getFractionalPart();






                            default:
                                if(!p.containsKey("Entity")){
                                    p.put("Entity",new ArrayList<String>());
                                }
                                Objects.requireNonNull(p.get("Entity")).add(entity.toString());

                                //k = k + "\n" + "Entity: " + entity;




                        }








                    }





                    //textView.setText(k);




                }





            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(ExtractedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if(p.containsKey("Fractional Part")){

//                        m = p.get("Flight number");
//
//                        assert m != null;
//                        Toast.makeText(ExtractedActivity.this, m.get(0), Toast.LENGTH_SHORT).show();
            Toast.makeText(ExtractedActivity.this, "Flight", Toast.LENGTH_SHORT).show();

        }



        if(p.containsKey("Currency")){

//                        m = p.get("Currency");
//
//                        assert m != null;
//                        Toast.makeText(ExtractedActivity.this, m.get(0), Toast.LENGTH_SHORT).show();
            Toast.makeText(ExtractedActivity.this, "Currency", Toast.LENGTH_SHORT).show();

        }



    }



}