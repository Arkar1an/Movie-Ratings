package com.example.russell.movieratings;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;

/**
 * Created by russell on 12/8/17.
 */

public class MovieListFragment extends Fragment {

    private ListView movieListView;
    private String currentTabTag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        //inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_list,container,false);

        //get references to widgets
        movieListView = (ListView) view.findViewById(R.id.movieListView);

        //get the current tab
        TabHost tabHost = (TabHost) container.getParent().getParent();
        currentTabTag = tabHost.getCurrentTabTag();

        //refresh the task list view
        refreshMovieList();

        //return the view
        return view;
    }

    public void refreshMovieList(){
        //get Movie list for current tab from database
        Context context = getActivity().getApplicationContext();
        MovieListDB db = new MovieListDB(context);
        ArrayList<MovieList> movies;

        if(currentTabTag == "Most Recent"){
            movies = db.getMovieListsByDate();
            MovieListAdapter adapter1 = new MovieListAdapter(context,movies);
            movieListView.setAdapter(adapter1);
        }
        else if (currentTabTag == "Highest Rated"){
            movies = db.getMovieListsByRating();
            MovieListAdapter adapter2 = new MovieListAdapter(context,movies);
            movieListView.setAdapter(adapter2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshMovieList();
    }
}
