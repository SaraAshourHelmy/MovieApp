package mal.com.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import mal.com.Models.Movie;

/**
 * Created by sara on 11/21/2016.
 */
public class DbQueries {

    public static final String TABLE_NAME = "favourite";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_OVERVIEW = "overview";
    public static final String COLUMN_NAME_POSTER = "poster";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_VOTE = "vote";

    private static final String TEXT_TYPE = " TEXT";
    private static final String DOUBLE_TYPE = " DOUBLE";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_OVERVIEW + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_POSTER + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_VOTE + DOUBLE_TYPE + " )";

    public static void insertData(DBHelper mDbHelper, Movie movie) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TITLE, movie.getTitle());
        values.put(COLUMN_NAME_ID, movie.getId());
        values.put(COLUMN_NAME_OVERVIEW, movie.getOverview());
        values.put(COLUMN_NAME_POSTER, movie.getPoster());
        values.put(COLUMN_NAME_DATE, movie.getRelease_date());
        values.put(COLUMN_NAME_VOTE, movie.getVote_average());
        try {
            long newRowId = db.insert(TABLE_NAME, null, values);
            Log.e("new row", newRowId + "");
        } catch (Exception e) {

            long newRowId = db.replace(TABLE_NAME, null, values);
            Log.e("new row", newRowId + "");
        }


    }

    public static ArrayList<Movie> getData(DBHelper mDbHelper) {

        ArrayList<Movie> lst_movie = new ArrayList<>();
        Movie movie;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        while (cursor.moveToNext()) {

            movie = new Movie();
            movie.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_OVERVIEW)));
            movie.setPoster(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_POSTER)));
            movie.setRelease_date(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE)));
            movie.setVote_average(cursor.getDouble(cursor.getColumnIndex(COLUMN_NAME_VOTE)));
            lst_movie.add(movie);

        }
        return lst_movie;
    }

    public static boolean getMovie(DBHelper mDbHelper, int id) {
        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " +
                    COLUMN_NAME_ID + " = " + id, null);

            if (cursor.getCount() > 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            Log.e("db_error", e.getMessage());
            return false;
        }
    }

    public static void removeMovie(DBHelper mDbHelper, int id) {

        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            db.rawQuery("delete  from " + TABLE_NAME + " where " + COLUMN_NAME_ID
                    + " = " + id, null);
        } catch (Exception e) {
            Log.e("remove_error", e.getMessage());
        }
    }

}
