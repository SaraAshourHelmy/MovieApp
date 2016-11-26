package mal.com.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import mal.com.Models.Movie;

/**
 * Created by sara on 11/21/2016.
 */
public class DBOperations {

    public static final String DATABASE_NAME = "MovieDB";
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

    public static void CreateDB(Context context) {

        SQLiteDatabase db =
                context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public static void insertMovie(Context context, Movie movie) {
        try {
            SQLiteDatabase db = context.openOrCreateDatabase(DATABASE_NAME,
                    context.MODE_PRIVATE, null);

            String query = "insert or replace INTO " + TABLE_NAME + "(" + COLUMN_NAME_ID
                    + COMMA_SEP + COLUMN_NAME_TITLE + COMMA_SEP + COLUMN_NAME_OVERVIEW +
                    COMMA_SEP + COLUMN_NAME_POSTER + COMMA_SEP + COLUMN_NAME_DATE +
                    COMMA_SEP + COLUMN_NAME_VOTE + ") values(?,?,?,?,?,?)";

            SQLiteStatement insertStmt = db.compileStatement(query);
            insertStmt.clearBindings();
            insertStmt.bindLong(1, movie.getId());
            insertStmt.bindString(2, movie.getTitle());
            insertStmt.bindString(3, movie.getOverview());
            insertStmt.bindString(4, movie.getPoster());
            insertStmt.bindString(5, movie.getRelease_date());
            insertStmt.bindDouble(6, movie.getVote_average());
            insertStmt.executeInsert();
            Log.e("insert", "done");
        } catch (Exception e) {
            Log.e("insert", "error");
        }
    }

    public static ArrayList<Movie> getAllMovies(Context context) {

        ArrayList<Movie> lst_movie = new ArrayList<>();
        Movie movie;
        try {
            SQLiteDatabase db = context.openOrCreateDatabase(DATABASE_NAME,
                    context.MODE_PRIVATE, null);

            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
            while (cursor.moveToNext()) {
                Log.e("get", "true");
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_OVERVIEW)));
                movie.setPoster(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_POSTER)));
                movie.setRelease_date(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE)));
                movie.setVote_average(cursor.getDouble(cursor.getColumnIndex(COLUMN_NAME_VOTE)));
                lst_movie.add(movie);

            }
            db.close();
        } catch (Exception e) {

        }
        return lst_movie;
    }

    public static boolean getMovie(Context context, int id) {
        SQLiteDatabase db = context.openOrCreateDatabase(DATABASE_NAME,
                context.MODE_PRIVATE, null);
        try {


            String query = "select * from " + TABLE_NAME + " where " +
                    COLUMN_NAME_ID + " = " + id;
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.getCount() > 0 && cursor != null) {

                Log.e("movie", "exist");
                db.close();
                return true;


            } else {
                Log.e("movie", "not found");
                db.close();

            }
            return false;
        } catch (Exception e) {
            Log.e("get_error", e.getMessage());
            db.close();
            return false;
        }

    }

    public static void removeMovie(Context context, int id) {

        SQLiteDatabase db = context.openOrCreateDatabase(DATABASE_NAME,
                context.MODE_PRIVATE, null);
        try {

            String query = "delete  from " + TABLE_NAME + " where " + COLUMN_NAME_ID
                    + " = " + id;
            db.execSQL(query);
            Log.e("remove", "done");
        } catch (Exception e) {
            Log.e("remove_error", e.getMessage());
        }
        db.close();
    }


}
