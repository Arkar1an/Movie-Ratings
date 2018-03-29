package com.example.russell.movieratings;

import java.util.Calendar;

/**
 * Created by russell on 12/8/17.
 */

public class MovieList {

    private int id;
    private String title;
    private Calendar date;
    private int rating;

    public MovieList(int id, String title, int day, int month, int year, int rating) {
        this.id = id;
        this.title = title;
        date = Calendar.getInstance();
        date.set(year,month,day);
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDayOfMonth() {
        return date.get(Calendar.DAY_OF_MONTH);
    }

    public void setDayOfMonth(int dayOfMonth) {
        date.set(Calendar.DAY_OF_MONTH,dayOfMonth);
    }

    public int getMonth() {
        return date.get(Calendar.MONTH);
    }

    public void setMonth(int month) {
        date.set(Calendar.MONTH,month);
    }

    public int getYear() {
        return date.get(Calendar.YEAR);
    }

    public void setYear(int year) {
        date.set(Calendar.YEAR,year);
    }
    //format YYYY-MM-DD
    public String getDateString(){
        String month,day;

        if(this.getMonth() < 10) {
            month = "0" + Integer.toString(this.getMonth()) + "-";
        }
        else{
            month = Integer.toString(this.getMonth()) + "-";
        }
        if(this.getDayOfMonth() < 10){
            day = "0" + Integer.toString(this.getDayOfMonth());
        }
        else{
            day = Integer.toString(this.getDayOfMonth());
        }

        String dateString = Integer.toString(this.getYear()) + "-" + month + day;
        return dateString;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
