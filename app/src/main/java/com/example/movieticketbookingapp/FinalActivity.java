package com.example.movieticketbookingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinalActivity extends AppCompatActivity {

    Button home_button;
    TextView tv;

    /*show user his confirm ticket information and home button leads to mainactivity.java*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        tv=findViewById(R.id.tv2);
        Bundle bun=getIntent().getExtras();
        tv.setText("Your ticket for\n"+"'"+bun.getString("movie")+"'"+"\n"+"in"+"\n "+"'"+bun.getString("theater")+"'"
        +"\n has been booked.\n \nShow time: "+bun.getString("timing")+"\n Seat Details:\n "+bun.getString("booked"));
        home_button = findViewById(R.id.home_button);
        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new
                        Intent(FinalActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


}