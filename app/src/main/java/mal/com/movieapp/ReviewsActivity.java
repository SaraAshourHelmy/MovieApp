package mal.com.movieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mal.com.fragment.ReviewFragment;

public class ReviewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        initViews();
    }

    private void initViews() {
        ReviewFragment reviewFragment = new ReviewFragment();
        reviewFragment.setArguments(getIntent().getBundleExtra("bundle"));
        getFragmentManager().beginTransaction().add(R.id.fl_fragmentReview,
                reviewFragment, "").commit();
    }
}
