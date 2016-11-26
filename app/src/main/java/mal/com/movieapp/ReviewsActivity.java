package mal.com.movieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mal.com.fragment.ReviewFragment;

public class ReviewsActivity extends AppCompatActivity {

    private boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        /*if (savedInstanceState != null) {
            first = savedInstanceState.getBoolean("first", true);
        }*/
        //initViews();
    }

    private void initViews() {
        ReviewFragment reviewFragment;
        if (first) {
            reviewFragment = new ReviewFragment();

            getFragmentManager().beginTransaction().add(R.id.fl_fragmentReview,
                    reviewFragment, "reviewFragment").commit();
        } else {
            reviewFragment = (ReviewFragment)
                    getFragmentManager().findFragmentByTag("reviewFragment");
        }
        reviewFragment.setArguments(getIntent().getBundleExtra("bundle"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("first", false);
        super.onSaveInstanceState(outState);
    }
}
