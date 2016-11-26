package mal.com.fragment;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import mal.com.DB.DBOperations;
import mal.com.Models.Movie;
import mal.com.Models.Trailer;
import mal.com.Utilis.NetworkHelper;
import mal.com.adapters.TrailersAdapter;
import mal.com.movieapp.R;
import mal.com.movieapp.ReviewsActivity;

/**
 * Created by sara on 10/21/2016.
 */
public class MovieDetailsFragment extends Fragment implements
        AdapterView.OnItemClickListener, View.OnClickListener {

    private ImageView imgV_movie, imgV_favourite;
    private TextView tv_title, tv_date, tv_overView, tv_vote;
    private static int movieId;
    private static String urlTrailers, urlReviews;
    private static ArrayList<Trailer> lst_trailer = new ArrayList<>();

    private ListView lstV_trailers;
    private static TrailersAdapter adapter_trailers;

    private Button btn_reviews;
    private boolean first = true;
    private static String title, date, overView, img;
    private static String img_url;
    private static double vote;
    private static boolean flag_fav;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Log.e("save", "true");
            first = savedInstanceState.getBoolean("first", true);
        } else {
            Log.e("save", "no");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        if (first) {
            SetData();
            lst_trailer = new ArrayList<>();
            flag_fav = DBOperations.getMovie(getActivity(), movieId);
            Log.e("favourite", flag_fav + "");

        }
        imgV_favourite = (ImageView) view.findViewById(R.id.img_fav);
        lstV_trailers = (ListView) view.findViewById(R.id.lstV_trailers);
        btn_reviews = (Button) view.findViewById(R.id.btn_reviews);
        imgV_movie = (ImageView) view.findViewById(R.id.imgV_movie_details);
        tv_title = (TextView) view.findViewById(R.id.tv_movie_title);
        tv_date = (TextView) view.findViewById(R.id.tv_movie_date);
        tv_overView = (TextView) view.findViewById(R.id.tv_movie_overView);
        tv_vote = (TextView) view.findViewById(R.id.tv_movie_vote);
        adapter_trailers = new TrailersAdapter(getActivity(), lst_trailer);
        imgV_favourite.setImageResource(flag_fav ? R.drawable.fav : R.drawable.unfav);


        lstV_trailers.setAdapter(adapter_trailers);
        // lstV_reviews.setAdapter(adapter_reviews);
        lstV_trailers.setOnItemClickListener(this);
        btn_reviews.setOnClickListener(this);
        imgV_favourite.setOnClickListener(this);
        setViewsData();


    }

    private void SetData() {


        Bundle bundle = getArguments();

        if (bundle != null) {

            movieId = (bundle.containsKey("id") ? bundle.getInt("id", 0) : 0);
            Log.e("movie_id", movieId + "");

            title = (bundle.containsKey("title") ? bundle.getString("title") : "");


            date = (bundle.containsKey("date") ? bundle.getString("date") : "");


            overView = (bundle.containsKey("overview") ? bundle.getString("overview") : "");


            vote = (bundle.containsKey("vote") ? bundle.getDouble("vote", 0) : 0);


            img = (bundle.containsKey("img") ? bundle.getString("img") : "");
            img_url = getResources().getString(R.string.url_img) + "w185/" +
                    img;


            buildURLS();
            if (NetworkHelper.isNetworkAvailable(getActivity()))
                new MovieInfo().execute();
            else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setViewsData() {
        tv_title.setText(title);
        tv_date.setText(date);
        tv_overView.setText(overView);
        tv_vote.setText(String.valueOf(vote));
        Picasso.with(getActivity()).load(img_url).into(imgV_movie);
    }

    private void buildURLS() {

        urlTrailers = getResources().getString(R.string.URL) + movieId + "/videos" +
                "?api_key=" +
                getResources().getString(R.string.API_KEY);

        urlReviews = getResources().getString(R.string.URL) + movieId + "/reviews" +
                "?api_key=" +
                getResources().getString(R.string.API_KEY);
        ;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        openYoutube(lst_trailer.get(position).getKey());

    }

    private void openYoutube(String id) {

        if (NetworkHelper.isNetworkAvailable(getActivity())) {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            try {
                startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                startActivity(webIntent);
            }
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn_reviews) {

            Bundle bundle = new Bundle();
            bundle.putString("url_reviews", urlReviews);
            Intent intent = new Intent(getActivity(), ReviewsActivity.class);
            intent.putExtra("bundle", bundle);
            startActivity(intent);

        } else if (v == imgV_favourite) {

            if (flag_fav) {
                flag_fav = false;
                DBOperations.removeMovie(getActivity(), movieId);
                imgV_favourite.setImageResource(R.drawable.unfav);
            } else {
                flag_fav = true;
                Movie movie = new Movie();
                movie.setId(movieId);
                movie.setTitle(title);
                movie.setPoster(img);
                movie.setOverview(overView);
                movie.setRelease_date(date);
                movie.setVote_average(vote);
                DBOperations.insertMovie(getActivity(), movie);
                imgV_favourite.setImageResource(R.drawable.fav);

            }
        }
    }

    private class MovieInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {


            parseTrailers(NetworkHelper.FetchMovieData(urlTrailers));

            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            adapter_trailers.AddALL(lst_trailer);

        }
    }

    private void parseTrailers(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray resultLst = jsonObject.getJSONArray("results");
            Trailer trailer;
            for (int i = 0; i < resultLst.length(); i++) {
                JSONObject obj = resultLst.getJSONObject(i);
                trailer = new Trailer();
                trailer.setName(obj.getString("name"));
                trailer.setKey(obj.getString("key"));
                lst_trailer.add(trailer);

            }
        } catch (Exception e) {

            Log.e("trailer_error", e + "");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("first", false);
        super.onSaveInstanceState(outState);
    }
}
