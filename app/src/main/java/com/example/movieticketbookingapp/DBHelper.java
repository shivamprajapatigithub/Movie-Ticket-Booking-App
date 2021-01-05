package com.example.movieticketbookingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    Context context;
    //table 1 with id, theater, city and movie
    String query1="create table "+MovieDetails.TABLI_NAME1+"("+
            " "+MovieDetails.ID+" integer primary key autoincrement,"+
            " "+MovieDetails.THEATER+" varchar not null,"+
            " "+MovieDetails.CITY+" varchar not null,"+
            " "+MovieDetails.MOVIE+" varchar not null"+
            ")";
    // table 2 with id, timing, seats and key(foreign key refering to id of table 1)
    String query2="create table "+MovieDetails.TABLI_NAME2+"("+
            " "+MovieDetails.ID+" integer primary key autoincrement,"+
            " "+MovieDetails.TIMING+" varchar not null,"+
            " "+MovieDetails.SEATS+" varchar not null,"+
            " "+MovieDetails.KEY+" varchar not null"+
            ")";

    public DBHelper(Context context){
        super(context,MovieDetails.DB_NAME,null,MovieDetails.DB_VERSION);
        this.context=context;

    }
    //add record in db. takes id, theater, city, movie, array of available timing, array of available seats of corresponding timings and reference to db
    public void addRecord(int id,String theater, String city, String movie,
                          String[] timing, String[] seats,SQLiteDatabase sd){
        ContentValues c= new ContentValues();
        c.put(MovieDetails.ID,id);
        c.put(MovieDetails.THEATER,theater);
        c.put(MovieDetails.CITY,city);
        c.put(MovieDetails.MOVIE,movie);
        sd.insert(MovieDetails.TABLI_NAME1,null,c);
        ContentValues c1=new ContentValues();
        c1.put(MovieDetails.KEY,id);
        for(int i=0;i<timing.length;i++) {
            c1.put(MovieDetails.TIMING, timing[i]);
            c1.put(MovieDetails.SEATS, seats[i]);
            sd.insert(MovieDetails.TABLI_NAME2, null, c1);
        }
    }

    //update seats for provided id with provided bitmap of seats
    public void updateSeats(String id, String seat, SQLiteDatabase sd){
        ContentValues c=new ContentValues();
        c.put(MovieDetails.SEATS,seat);
        sd.update(MovieDetails.TABLI_NAME2,c,MovieDetails.ID+"=?",new String[]{id});

    }
    //returns id, theater and movie for specific city
    public Cursor getForCity(String city,SQLiteDatabase sd){
        String []col={MovieDetails.ID,MovieDetails.THEATER,MovieDetails.MOVIE};
        return(sd.query(MovieDetails.TABLI_NAME1,col,
                MovieDetails.CITY+"=?",new String[]{city},null,null,null));
    }

    //returns unique list of cities
    public Cursor getCity(SQLiteDatabase sd){
        return(sd.query(true,MovieDetails.TABLI_NAME1,
                new String[]{MovieDetails.CITY},null,null,
                null,null,null,null));
    }

    //returns available timing and seating for provided key
    public Cursor getForKey(String key, SQLiteDatabase sd){
        String[] col={MovieDetails.ID,MovieDetails.TIMING,MovieDetails.SEATS};
        return(sd.query(MovieDetails.TABLI_NAME2,col,MovieDetails.KEY+"=?",
                new String[]{key}, null,null,null));
    }

    //creates Default SQL DB
    /*default db has 2 cities each with 2 different theater entry
    * one theater show 1 movie other 2 movies
    *total 6 entries in table 1: 2 citis each with 3 shows
    *
    * each show has 3 timing available
    * seats contain bitmap of available seats
    * 'a' corresponds to available seat and 'o' correspond to un-available or already booked seat*/

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(query1);
        sqLiteDatabase.execSQL(query2);
        String[] timing={"09:00am-12:00pm","01:00pm-04:00pm","05:00pm-08:00pm"};
        String[] seats={"a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,",
                        "a,o,a,o,a,o,a,o,a,o,a,o,a,o,a,o,a,o,a,o,",
                        "o,a,o,a,o,a,o,a,o,a,o,a,o,a,o,a,o,a,o,a,"};
        addRecord(1,"Novelty","Lucknow","Chhichhore",
                timing,seats,sqLiteDatabase);

        addRecord(2,"Inox","Lucknow","3-Idiots",
                timing,seats,sqLiteDatabase);
        addRecord(3,"Wave Cinema","Lucknow","Yariyaan",
                timing,seats,sqLiteDatabase);

        addRecord(4,"Z-Square","Kanpur","RAW",
                timing,seats,sqLiteDatabase);

        addRecord(5,"Carnival Cinemas","Kanpur","WAR",
                timing,seats,sqLiteDatabase);
        addRecord(6,"Jugal Palace Cinema","Kanpur","URI",
                timing,seats,sqLiteDatabase);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
