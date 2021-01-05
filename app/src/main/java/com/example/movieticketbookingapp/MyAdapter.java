package com.example.movieticketbookingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class MyAdapter extends ArrayAdapter {


    List<String> movie_name;
    List<String> theater_name;
    List<Integer> movie_image;
    Context context;


    public MyAdapter(@NonNull Context context, List<String> movie_name, List<String> theater_name, List<Integer> movie_image) {
        super(context, R.layout.movies, movie_name);

        this.movie_name = movie_name;
        this.theater_name = theater_name;
        this.movie_image = movie_image;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.movies, parent, false);
        ImageView imageView = view.findViewById(R.id.movie_image);
        TextView textView1 = view.findViewById(R.id.movie_name);
        TextView textView2 = view.findViewById(R.id.theater_name);

        textView1.setText(movie_name.get(position));
        textView2.setText(theater_name.get(position));
        //imageView.setImageResource(movie_image.get(position));



        return view;

    }
}
