package com.example.russell.movieratings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by russell on 12/8/17.
 */

public class MovieListDB {

    //database constants
    public static final String DB_NAME = "MovieList.db";
    public static final int DB_VERSION = 1;

    //MOVIE_LIST_TABLE constants
    public static final String MOVIE_LIST_TABLE = "Movie_List";

    public static final String ML_ID = "_id";
    public static final int ML_ID_COL = 0;

    public static final String ML_TITLE = "_title";
    public static final int ML_TITLE_COL = 1;

    public static final String ML_DATE = "_date";
    public static final int ML_DATE_COL = 2;

    public static final String ML_RATING = "_rating";
    public static final int ML_RATING_COL = 3;

    //CREATE and DROP TABLE statements
    public static final String CREATE_MOVIE_LIST_TABLE =
            "CREATE TABLE " + MOVIE_LIST_TABLE + " (" +
            ML_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ML_TITLE + " TEXT NOT NULL, " +
            ML_DATE + " DATE NOT NULL, " +
            ML_RATING + " INTEGER NOT NULL);";

    public static final String DROP_MOVIE_LIST_TABLE =
            "DROP TABLE IF EXISTS " + MOVIE_LIST_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_MOVIE_LIST_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_MOVIE_LIST_TABLE);
            onCreate(sqLiteDatabase);
        }
    }

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public MovieListDB(Context context) {
        dbHelper = new DBHelper(context,DB_NAME,null,DB_VERSION);
    }

    private void openReadableDB(){
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB(){
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB(){
        if (db != null){
            db.close();
        }
    }

    public ArrayList<MovieList> getMovieListsByRating(){
        this.openReadableDB();
        Cursor cursor = db.query(MOVIE_LIST_TABLE,null,null,null,null,null,ML_RATING+" DESC");
        cursor.moveToFirst();
        ArrayList<MovieList> ml = new ArrayList<>();
        //cant figure out why the first movie in the database wont be displayed
        while (cursor.moveToNext()){
            ml.add(getMovieFromCursor(cursor));
        }
        if (cursor != null){
            cursor.close();
        }
        this.closeDB();
        return ml;
    }

    public ArrayList<MovieList> getMovieListsByDate(){
        this.openReadableDB();
        Cursor cursor = db.query(MOVIE_LIST_TABLE,null,null,null,null,null,ML_DATE+" DESC");
        cursor.moveToFirst();
        ArrayList<MovieList> ml = new ArrayList<>();

        while (cursor.moveToNext()){
            ml.add(getMovieFromCursor(cursor));
        }
        if (cursor != null){
            cursor.close();
        }
        this.closeDB();
        return ml;
    }

    public MovieList getMovie(int id){
        String where = ML_ID + "= ?";
        String[] whereArgs = {Integer.toString(id)};

        this.openReadableDB();
        Cursor cursor = db.query(MOVIE_LIST_TABLE,
                null,where,whereArgs,null,null,null);
        cursor.moveToFirst();
        MovieList m = getMovieFromCursor(cursor);
        if (cursor != null){
            cursor.close();
        }
        this.closeDB();
        return m;
    }

    private static MovieList getMovieFromCursor(Cursor cursor){
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }

        else{
            try{
                //(int id, String title, int day, int month, int year, int rating)
                String dateString = cursor.getString(ML_DATE_COL);
                //date format is YYYY-MM-DD
                int day,month,year;
                year = Integer.parseInt(dateString.substring(0,4));
                month = Integer.parseInt(dateString.substring(5,7));
                day = Integer.parseInt(dateString.substring(8));

                MovieList m = new MovieList(
                    cursor.getInt(ML_ID_COL),
                    cursor.getString(ML_TITLE_COL),
                    day,month,year,
                    cursor.getInt(ML_RATING_COL));
                return m;
            }
            catch(Exception e){
                return null;
            }
        }
    }

    public long insertMovie(MovieList m){
        ContentValues cv = new ContentValues();
        cv.put(ML_TITLE, m.getTitle());
        cv.put(ML_DATE,m.getDateString());
        cv.put(ML_RATING,m.getRating());

        this.openWriteableDB();
        long rowId = db.insert(MOVIE_LIST_TABLE,null,cv);
        this.closeDB();

        return rowId;
    }

    public int updateMovie(MovieList m){
        ContentValues cv = new ContentValues();
        cv.put(ML_ID, m.getId());
        cv.put(ML_TITLE, m.getTitle());
        cv.put(ML_DATE,m.getDateString());
        cv.put(ML_RATING,m.getRating());

        String where = ML_ID + "= ?";
        String[] whereArgs = { String.valueOf(m.getId())};

        this.openWriteableDB();
        int rowCount = db.update(MOVIE_LIST_TABLE,cv,where,whereArgs);
        this.closeDB();

        return rowCount;
    }


}
