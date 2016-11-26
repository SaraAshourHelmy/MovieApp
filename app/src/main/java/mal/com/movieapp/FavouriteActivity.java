package mal.com.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import mal.com.Interfaces.MovieInterface;
import mal.com.fragment.FavouriteFragment;
import mal.com.fragment.MovieDetailsFragment;

public class FavouriteActivity extends AppCompatActivity implements MovieInterface {

    private static boolean isTowPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        initView(savedInstanceState);
    }

    private void initView(Bundle bundle) {
        FavouriteFragment favFragment;
        if (bundle == null) {
            isTowPane = false;
            favFragment = new FavouriteFragment();

            getFragmentManager().beginTransaction()
                    .add(R.id.fl_frgFav, favFragment, "favFragment").commit();

            if (null != findViewById(R.id.fl_frgDetails)) {
                isTowPane = true;
                Log.e("tablet", "true");
            }
        } else {
            favFragment = (FavouriteFragment) getFragmentManager().findFragmentByTag("favFragment");
        }
        favFragment.setMovieInterface(this);


    }

    @Override
    public void sendMovie(int id, String title, String img, String overView, String date, double vote) {

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("id", id);
        bundle.putString("img", img);
        bundle.putString("overview", overView);
        bundle.putString("date", date);
        bundle.putDouble("vote", vote);

        if (isTowPane) {
            MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
            detailsFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.fl_frgDetails, detailsFragment, "")
                    .commit();
        } else {

            Intent intent = new Intent(this, MovieDetailsActivity.class);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        }
    }
}
