package mal.com.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import mal.com.Interfaces.MovieInterface;
import mal.com.Models.Movie;
import mal.com.Utilis.NetworkHelper;
import mal.com.adapters.MovieAdapter;
import mal.com.movieapp.FavouriteActivity;
import mal.com.movieapp.MainActivity;
import mal.com.movieapp.R;

/**
 * Created by sara on 10/21/2016.
 */
public class MovieFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static String sort_type = "popular";
    private GridView grd_movies;
    private MovieAdapter movieAdapter;
    private static ArrayList<Movie> lst_movie = new ArrayList<>();
    private boolean first = true;
    private int pos = 0;
    private MovieInterface movieInterface;

    public void setMovieInterface(MovieInterface movieInterface) {
        this.movieInterface = movieInterface;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            Log.e("save", "true");
            first = savedInstanceState.getBoolean("first", true);
            pos = savedInstanceState.getInt("scroll", 0);
            Log.e("pos", pos + "");
        } else {
            Log.e("save", "no");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sort_menu, menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        initViews(view);

        return view;
    }

    private void initViews(View view) {

        if (first) {
            lst_movie = new ArrayList<>();
            //sort_type = "popular";
        }
        grd_movies = (GridView) view.findViewById(R.id.grd_movies);
        grd_movies.setOnItemClickListener(this);
        movieAdapter = new MovieAdapter(getActivity(), lst_movie);
        grd_movies.setAdapter(movieAdapter);
        grd_movies.smoothScrollToPosition(pos);

        if (first) {
            Log.e("first","true");
            callMovieData();
        }
        else
        {
            Log.e("first","false");
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        Intent intent;
        switch (item_id) {
            case R.id.popular:
                sort_type = "popular";
                intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.rated:
                sort_type = "top_rated";
                intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.favourite:
                intent = new Intent(getActivity(), FavouriteActivity.class);
                startActivity(intent);
                getActivity().finish();
            default:
                sort_type = "popular";
                break;


        }

        return true;
    }

    private String getURL() {

        String url = getResources().getString(R.string.URL) + sort_type + "?api_key=" +
                getResources().getString(R.string.API_KEY);
        return url;


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        movieInterface.sendMovie(lst_movie.get(position).getId(),
                lst_movie.get(position).getTitle(),
                lst_movie.get(position).getPoster(),
                lst_movie.get(position).getOverview(),
                lst_movie.get(position).getRelease_date(),
                lst_movie.get(position).getVote_average());
    }

    private class MovieData extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait ...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("do", params[0]);
            return FetchMovieData(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog != null)
                dialog.dismiss();
            lst_movie = ParseMovieJson(s);
            movieAdapter.ClearAdapter();
            movieAdapter.AddMovies(lst_movie);
        }
    }

    private String FetchMovieData(String url_txt) {
        String result = "";
        try {
            URL url = new URL(url_txt);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream stream = connection.getInputStream();
            if (stream == null) {
                Log.e("stream", "null");
                result = null;
            } else {
                StringBuffer stringBuffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = reader.readLine()) != null) {
                    //Log.e("line", reader.readLine());
                    stringBuffer.append(line + "\n");
                }

                result = stringBuffer.toString();
                Log.e("inside_res", stringBuffer + "");

                reader.close();

            }
            connection.disconnect();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("result", result);
        return result;
    }

    private ArrayList<Movie> ParseMovieJson(String jsonStr) {

        ArrayList<Movie> result = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray movie_lst = jsonObject.getJSONArray("results");
            Movie movie;
            for (int i = 0; i < movie_lst.length(); i++) {
                JSONObject movieJson = movie_lst.getJSONObject(i);
                movie = new Movie();
                movie.setId(movieJson.getInt("id"));
                movie.setTitle(movieJson.getString("original_title"));
                movie.setPoster(movieJson.getString("poster_path"));
                movie.setOverview(movieJson.getString("overview"));
                movie.setVote_average(movieJson.getDouble("vote_average"));
                movie.setRelease_date(movieJson.getString("release_date"));
                result.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void callMovieData() {

        if (NetworkHelper.isNetworkAvailable(getActivity()))
            new MovieData().execute(getURL());
        else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("first", false);
        outState.putInt("scroll", grd_movies.getFirstVisiblePosition());
        super.onSaveInstanceState(outState);
    }
}
