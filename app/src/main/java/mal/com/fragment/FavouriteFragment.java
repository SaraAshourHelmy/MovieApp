package mal.com.fragment;

import android.app.Fragment;
import android.content.Intent;
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

import java.util.ArrayList;

import mal.com.DB.DBOperations;
import mal.com.Interfaces.MovieInterface;
import mal.com.Models.Movie;
import mal.com.adapters.MovieAdapter;
import mal.com.movieapp.FavouriteActivity;
import mal.com.movieapp.MainActivity;
import mal.com.movieapp.R;

/**
 * Created by sara on 11/21/2016.
 */
public class FavouriteFragment extends Fragment implements AdapterView.OnItemClickListener {

    private GridView grdV_favorite;
    private static ArrayList<Movie> lst_movies;
    private static MovieAdapter adapter;
    private boolean first = true;
    private int pos = 0;
    private MovieInterface movieInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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

    public void setMovieInterface(MovieInterface movieInterface) {
        this.movieInterface = movieInterface;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sort_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        Intent intent;
        switch (item_id) {
            case R.id.popular:
                MovieFragment.sort_type = "popular";
                intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.rated:
                MovieFragment.sort_type = "top_rated";
                intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.favourite:
                intent = new Intent(getActivity(), FavouriteActivity.class);
                startActivity(intent);
                getActivity().finish();
            default:
                MovieFragment.sort_type = "popular";
                break;


        }

        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        grdV_favorite = (GridView) view.findViewById(R.id.grd_favorite);
        if (first)
            lst_movies = DBOperations.getAllMovies(getActivity());
        Log.e("favourite_count", lst_movies.size() + "");

        adapter = new MovieAdapter(getActivity(), lst_movies);
        grdV_favorite.setAdapter(adapter);
        grdV_favorite.setOnItemClickListener(this);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("first", false);
        outState.putInt("scroll", grdV_favorite.getFirstVisiblePosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        movieInterface.sendMovie(lst_movies.get(position).getId(),
                lst_movies.get(position).getTitle(),
                lst_movies.get(position).getPoster(),
                lst_movies.get(position).getOverview(),
                lst_movies.get(position).getRelease_date(),
                lst_movies.get(position).getVote_average());
    }
}
