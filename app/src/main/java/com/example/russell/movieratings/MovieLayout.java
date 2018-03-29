package com.example.russell.movieratings;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by russell on 12/8/17.
 */

public class MovieLayout extends LinearLayout implements OnClickListener {

    private RatingBar ratingBar;
    private TextView movieTitleTextView;
    private TextView dateTextView;

    private MovieList movie;
    private MovieListDB db;
    private Context context;

    public MovieLayout(Context context){
        super(context);
    }

    public MovieLayout(Context context, MovieList m){
        super(context);

        this.context = context;
        db = new MovieListDB(context);

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.movie_listview,this,true);

        //get references to widgets
        ratingBar = (RatingBar) findViewById(R.id.rating_staticListview);
        movieTitleTextView = (TextView) findViewById(R.id.movieTitleTextViewListView);
        dateTextView = (TextView) findViewById(R.id.dateTextViewListView);

        this.setOnClickListener(this);

        //set movie data on widgets
        setMovie(m);
    }

    public void setMovie(MovieList m){
        movie = m;
        movieTitleTextView.setText(movie.getTitle());
        dateTextView.setText(movie.getDateString());
        ratingBar.setRating(movie.getRating());
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, AddEditActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("movieID", movie.getId());
        intent.putExtra("editMode", true);
        context.startActivity(intent);
    }
}
