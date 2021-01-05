package com.example.movieticketbookingapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    DBHelper dbHelper;
    SQLiteDatabase sd;
    ListView movie_list;
    List<String> movie_name = new ArrayList();
    List<String> theater_name = new ArrayList();
    List<Integer> movie_image = new ArrayList();
    List<String> key=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movie_list = findViewById(R.id.movie_list);

        /*get readable database and get citis available and convert result to List*/
        dbHelper=new DBHelper(this);
        sd=dbHelper.getReadableDatabase();
        Cursor cursor=dbHelper.getCity(sd);
        List<String> citis=new ArrayList();
        if(cursor.moveToFirst()){
            do{
                citis.add(cursor.getString(cursor.getColumnIndex(MovieDetails.CITY)));
            }while(cursor.moveToNext());
        }
        //initalize spinner with citis list
        spinner = (Spinner) findViewById(R.id.city_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,citis);
        spinner.setAdapter(adapter);

        /*
        * on selecting city from spinner query db for theater and movie available in city and update list view with results*/
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor result=dbHelper.getForCity(citis.get(i),sd);
                update(result);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        //on selecting card move to next activity where timing and seating details will be showed
        movie_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),MovieConfirmation.class);
                intent.putExtra("movie",movie_name.get(i));
                intent.putExtra("theater",theater_name.get(i));
                intent.putExtra("key",key.get(i));
                startActivity(intent);
            }
        });

    }

    /*this converts result to list and update list view with results
    * */
    void update(Cursor c){
        movie_name=new ArrayList<>();
        theater_name=new ArrayList<>();
        key=new ArrayList<>();
        if(c.moveToFirst()){
            do{
                movie_name.add(c.getString(c.getColumnIndex(MovieDetails.MOVIE)));
                theater_name.add(c.getString(c.getColumnIndex(MovieDetails.THEATER)));
                key.add(c.getString(c.getColumnIndex(MovieDetails.ID)));
            }while(c.moveToNext());
        }
        else{
            Toast.makeText(this, "No theater Available", Toast.LENGTH_SHORT).show();
        }
        MyAdapter myAdapter = new MyAdapter(this, movie_name, theater_name, movie_image);
        movie_list.setAdapter(myAdapter);
    }



}