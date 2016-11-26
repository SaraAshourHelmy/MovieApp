package mal.com.movieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mal.com.fragment.MovieDetailsFragment;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        initViews(savedInstanceState);
    }

    private void initViews(Bundle bundle) {

        if (bundle == null) {
            MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
            detailsFragment.setArguments(getIntent().getBundleExtra("bundle"));
            getFragmentManager().beginTransaction().add(R.id.fl_frgDetails, detailsFragment, "")
                    .commit();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("first", false);
        super.onSaveInstanceState(outState);
    }
}
