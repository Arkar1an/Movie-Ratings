package com.example.russell.movieratings;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.util.ArrayList;

/**
 * Created by russell on 12/8/17.
 */

public class AddEditActivity extends Activity implements OnEditorActionListener {

    private EditText titleEditText;
    private RatingBar ratingBar;
    private DatePicker datePicker;
    private CheckBox checkBox;

    private boolean editMode;
    private boolean pendingMode;
    private MovieListDB db;
    //private String currentTabName = "";
    private MovieList movie;
    private int size;
    final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        //get reference to widgets
        titleEditText = (EditText) findViewById(R.id.title_edit_text);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        checkBox = (CheckBox) findViewById(R.id.checkbox);

        titleEditText.setOnEditorActionListener(this);
        db = new MovieListDB(this);
        ArrayList<MovieList> sortedByRating = db.getMovieListsByRating();
        ArrayList<MovieList> sortedByDate = db.getMovieListsByDate();
        size = sortedByDate.size();

        Intent intent = getIntent();
        editMode = intent.getBooleanExtra("editMode", false);
        pendingMode = intent.getBooleanExtra("pendingMode",false);

        NotificationManager m = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        if (editMode){//edit a movie
            int movieID = intent.getIntExtra("movieID", -1);
            movie = db.getMovie(movieID);

            titleEditText.setText(movie.getTitle());
            ratingBar.setRating(movie.getRating());
            datePicker.updateDate(movie.getYear(),movie.getMonth()-1,movie.getDayOfMonth());
        }
        else if(pendingMode){//was a pending intent
            String title = intent.getStringExtra("title");
            int day = intent.getIntExtra("day",1);
            int month = intent.getIntExtra("month",0);
            int year = intent.getIntExtra("year",2017);
            int rating = intent.getIntExtra("rating",0);
            titleEditText.setText(title);
            ratingBar.setRating(rating);
            datePicker.updateDate(year,month,day);
            m.cancel(NOTIFICATION_ID);
        }
        else{//add a movie
            datePicker.updateDate(2017,11,9);
            ratingBar.setRating(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuSave:
                if(checkBox.isChecked()){
                    //pending intent and notification stuff
                    Intent notificationIntent = new Intent(this,AddEditActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    String title = titleEditText.getText().toString();
                    int rating = (int) ratingBar.getRating();
                    int day = datePicker.getDayOfMonth();
                    int month = datePicker.getMonth();
                    int year = datePicker.getYear();
                    notificationIntent.putExtra("title",title);
                    notificationIntent.putExtra("day",day);
                    notificationIntent.putExtra("month",month);
                    notificationIntent.putExtra("year",year);
                    notificationIntent.putExtra("rating",rating);
                    notificationIntent.putExtra("pendingMode",true);
                    int flag = PendingIntent.FLAG_UPDATE_CURRENT;
                    PendingIntent pendingIntent =
                            PendingIntent.getActivity(this,0,notificationIntent,flag);
                    int icon = R.drawable.ic_launcher;
                    CharSequence tickerText = "Don't forget to rate your movie!";
                    CharSequence contentTitle = getText(R.string.app_name);
                    CharSequence contentText = "Select to rate movie now";

                    Notification notification = new NotificationCompat.Builder(this)
                            .setSmallIcon(icon)
                            .setTicker(tickerText)
                            .setContentTitle(contentTitle)
                            .setContentText(contentText)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .build();
                    NotificationManager m = (NotificationManager)
                            getSystemService(NOTIFICATION_SERVICE);
                    m.notify(NOTIFICATION_ID,notification);
                }
                else{
                    saveToDB();
                }
                this.finish();
                break;
            case R.id.menuCancel:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveToDB(){
        // get data from widgets
        String title = titleEditText.getText().toString();
        int rating = (int) ratingBar.getRating();
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth()+1;
        int year = datePicker.getYear();

        if (title == null || title.equals("")){
            return;
        }
        if (editMode){
            movie.setDayOfMonth(day);
            movie.setMonth(month);
            movie.setYear(year);
            movie.setRating(rating);
            movie.setTitle(title);
            db.updateMovie(movie);
        }
        else {
            movie = new MovieList(size, title, day, month, year, rating);
            db.insertMovie(movie);
        }
    }



    @Override
    public boolean onEditorAction(TextView view, int i, KeyEvent keyEvent) {
        int keyCode = -1;
        if(keyEvent != null) {
            keyCode = keyEvent.getKeyCode();
        }
        if (i == EditorInfo.IME_ACTION_DONE || keyCode == KeyEvent.KEYCODE_ENTER) {
            //hide keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return true;
        }
        return false;
    }
}
