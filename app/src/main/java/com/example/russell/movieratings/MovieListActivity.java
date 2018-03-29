package com.example.russell.movieratings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.google.tabmanager.TabManager;

import java.util.ArrayList;

/**
 * Created by russell on 12/8/17.
 */



public class MovieListActivity extends FragmentActivity{
    TabHost tabHost;
    TabManager tabManager;
    MovieListDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        //get tab manager

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabManager = new TabManager(this,tabHost,R.id.realtabcontent);

        //get the lists from the database
        db = new MovieListDB(this);
        ArrayList<MovieList> sortedByRating = db.getMovieListsByRating();
        ArrayList<MovieList> sortedByDate = db.getMovieListsByDate();

        //add a tab for each list
        String tab1 = "Most Recent";
        String tab2 = "Highest Rated";
        TabSpec tabSpec = tabHost.newTabSpec(tab1);
        TabSpec tabSpec2 = tabHost.newTabSpec(tab2);
        tabSpec.setIndicator(tab1);
        tabSpec2.setIndicator(tab2);
        tabManager.addTab(tabSpec,MovieListFragment.class,null);
        tabManager.addTab(tabSpec2,MovieListFragment.class,null);

        //set current tab to the last tab opened
        if(savedInstanceState != null){
            tabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab",tabHost.getCurrentTabTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_movie_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuAddMovie){
            Intent intent = new Intent(this,AddEditActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
