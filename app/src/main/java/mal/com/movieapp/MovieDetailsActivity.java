package mal.com.movieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mal.com.fragment.MovieDetailsFragment;

public class MovieDetailsActivity extends AppCompatActivity {

    private boolean isTowPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        initViews();
    }

    private void initViews() {

        MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
        detailsFragment.setArguments(getIntent().getBundleExtra("bundle"));
        //detailsFragment.setReviewInterface(this);
        getFragmentManager().beginTransaction().add(R.id.fl_frgDetails, detailsFragment, "")
                .commit();
        // if (null != findViewById(R.id.fl_frgReviews)) {
        //   isTowPane = true;

        //}
    }

}
