package com.example.movieticketbookingapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MovieConfirmation extends AppCompatActivity implements View.OnClickListener {

    Spinner spinner;
    Button book_button;
    TextView movie,theater;
    int count_seats;
    String current_id,current_Timing,current_Movie,current_Theater;
    String all_seats;
    List<String>selected_seats;
    Button b[]=new Button[20];
    DBHelper dbHelper;
    SQLiteDatabase sq;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_confirmation);
        b[0]=findViewById(R.id.seat1);
        b[1]=findViewById(R.id.seat2);
        b[2]=findViewById(R.id.seat3);
        b[3]=findViewById(R.id.seat4);
        b[4]=findViewById(R.id.seat5);
        b[5]=findViewById(R.id.seat6);
        b[6]=findViewById(R.id.seat7);
        b[7]=findViewById(R.id.seat8);
        b[8]=findViewById(R.id.seat9);
        b[9]=findViewById(R.id.seat10);
        b[10]=findViewById(R.id.seat11);
        b[11]=findViewById(R.id.seat12);
        b[12]=findViewById(R.id.seat13);
        b[13]=findViewById(R.id.seat14);
        b[14]=findViewById(R.id.seat15);
        b[15]=findViewById(R.id.seat16);
        b[16]=findViewById(R.id.seat17);
        b[17]=findViewById(R.id.seat18);
        b[18]=findViewById(R.id.seat19);
        b[19]=findViewById(R.id.seat20);
        //get movie name from bundle and show it to user
        Bundle bun=getIntent().getExtras();
        movie=findViewById(R.id.movie_name);
        movie.setText(bun.getString("movie").toString());

        //get theater name from bundle and show it to user
        theater=findViewById(R.id.theater_name);
        theater.setText(bun.getString("theater").toString());
        String key=bun.getString("key");

        //get readable database
        dbHelper=new DBHelper(this);
        sq=dbHelper.getReadableDatabase();

        //query database for timing and seats by providing foreign it(key)
        Cursor c=dbHelper.getForKey(key,sq);

        //turn results to list for easier access
        List<String> timing=new ArrayList<>();
        List<String> seats=new ArrayList<>();
        List<String> id=new ArrayList<>();
        if(c.moveToFirst()){
            do{
                timing.add(c.getString(c.getColumnIndex(MovieDetails.TIMING)));
                seats.add(c.getString(c.getColumnIndex(MovieDetails.SEATS)));
                id.add(c.getString(c.getColumnIndex(MovieDetails.ID)));
            }while (c.moveToNext());
        }

        //update spinner with available timings
        Spinner spinner = (Spinner) findViewById(R.id.timings);
        ArrayAdapter adapter=new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,timing);
        spinner.setAdapter(adapter);

        //register spinner for onItemClick Listner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            /*for selected timing get seats,
            initalize buttons according to seat status stored in db
            set bg color White and prefix A for available seatss
            set bg color Red and prefix U for unavailable seats
            * */
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                all_seats=seats.get(i);
                //Toast.makeText(MovieConfirmation.this, all_seats, Toast.LENGTH_SHORT).show();
                for(int j=0;j<all_seats.length();j+=2){
                    int k=j/2;
                    if(all_seats.charAt(j)=='a') {
                        b[k].setBackgroundColor(Color.WHITE);
                        b[k].setText("A_"+(k + 1));
                    }
                    else{
                            b[k].setBackgroundColor(Color.RED);
                            b[k].setText("U_"+(k + 1));
                        }
                    }
                current_id=id.get(i);
                current_Timing=timing.get(i);
                count_seats=0;
                selected_seats =new ArrayList<>();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(MovieConfirmation.this, "Slect Timing", Toast.LENGTH_SHORT).show();
            }
        });
        //register all buttons for onClickListner
        b[0].setOnClickListener(this);
        b[1].setOnClickListener(this);
        b[2].setOnClickListener(this);
        b[3].setOnClickListener(this);
        b[4].setOnClickListener(this);
        b[5].setOnClickListener(this);
        b[6].setOnClickListener(this);
        b[7].setOnClickListener(this);
        b[8].setOnClickListener(this);
        b[9].setOnClickListener(this);
        b[10].setOnClickListener(this);
        b[11].setOnClickListener(this);
        b[12].setOnClickListener(this);
        b[13].setOnClickListener(this);
        b[14].setOnClickListener(this);
        b[15].setOnClickListener(this);
        b[16].setOnClickListener(this);
        b[17].setOnClickListener(this);
        b[18].setOnClickListener(this);
        b[19].setOnClickListener(this);

        // BOOK BUTTON

        book_button = findViewById(R.id.book_button);
        book_button.setOnClickListener(new View.OnClickListener() {
            /*on click
            if seat is empty
                toast is printed
            * else
                get writable database,
                updated_seats contain bitmap string of avaiable seats update this variable using for loop
                after loop update database by passing id and updated_seats

                move to FinalActivity.java and pass booked seats, timng, movie name and theater name using bundles
            * */
            @Override
            public void onClick(View view) {
                if(selected_seats.isEmpty()==true){
                    Toast.makeText(MovieConfirmation.this, "Please select seat", Toast.LENGTH_SHORT).show();
                }
                else {
                    String updated_seats=all_seats;
                    String final_seats=" ";
                    sq=dbHelper.getWritableDatabase();
                    for(String j:selected_seats){
                        final_seats=final_seats+j+" ";
                        int k=Integer.parseInt(j);
                        k=(k-1)*2;
                        updated_seats=updated_seats.substring(0,k)+"o"+updated_seats.substring(k+1);
                    }
                    dbHelper.updateSeats(current_id,updated_seats,sq);

                    Intent intent = new Intent(MovieConfirmation.this, FinalActivity.class);
                    intent.putExtra("booked",final_seats);
                    intent.putExtra("timing",current_Timing);
                    intent.putExtra("movie",movie.getText().toString());
                    intent.putExtra("theater",theater.getText().toString());
                    startActivity(intent);
                }
            }
        });


    }
        /*OnClick listner for buttons
        if butten has prefix 'U"
            it means seat is unavailable or booked by someone else
            in that case just inform user by toast
        if button has prefix 'A'
            it means seat is available for booking
            check if selected seats are more than max limit then toast user for max limit
            else
                turn button green
                mark it as occupied(suffix O)
                add this seat to selected_seat list
                increase cout of seat
        if button has prefix 'O'
            it means seat is already selected by user
            check for any inconsistency
            then
                turn button white
                mark it as available (suffix A)
                remove it from selected_seat list
                decrease count

        * */

    @Override
    public void onClick(View view) {
        Button but=(Button)view;
        String text=but.getText().toString();
        switch (text.substring(0,1)){
            case "U":
                Toast.makeText(this, "Seat un-available", Toast.LENGTH_SHORT).show();
                break;
            case "A":
                if(count_seats>=5)
                    Toast.makeText(this, "Max seat Selected", Toast.LENGTH_SHORT).show();
                else{
                    but.setBackgroundColor(Color.GREEN);
                    but.setText("O"+text.substring(1));
                    selected_seats.add(text.substring(2));
                    count_seats++;
                }
                break;
            case "O":
                if(count_seats<=0||selected_seats.isEmpty())
                    Toast.makeText(this, "Error: No seat selected", Toast.LENGTH_SHORT).show();
                else{
                    but.setBackgroundColor(Color.WHITE);
                    but.setText("A"+text.substring(1));
                    selected_seats.remove(text.substring(2));
                    count_seats--;
                }
                break;
            default:
                Toast.makeText(this, text.substring(0,1), Toast.LENGTH_SHORT).show();
        }
    }
}