package mal.com.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import mal.com.Models.Review;
import mal.com.Utilis.NetworkHelper;
import mal.com.adapters.ReviewsAdapter;
import mal.com.movieapp.R;

/**
 * Created by sara on 11/21/2016.
 */
public class ReviewFragment extends Fragment {

    private ListView lstV_reviews;
    private TextView tv_noReview;
    private static ArrayList<Review> lst_reviews = new ArrayList<>();
    private static ReviewsAdapter adapter_reviews;
    private String urlReviews = "";
    private boolean first = true;
    private int pos = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Log.e("save", "true");
            first = savedInstanceState.getBoolean("first", true);
            pos = savedInstanceState.getInt("scroll", 0);
            Log.e("pos", pos + "");
        } else {
            Log.e("save", "no");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_review, container, false);
        initViews(view);
        return view;
    }


    private void initViews(View view) {

        if (first) {
            adapter_reviews = null;
            lst_reviews = new ArrayList<>();
        }

        lstV_reviews = (ListView) view.findViewById(R.id.lstV_reviews);
        tv_noReview = (TextView) view.findViewById(R.id.tv_noReviews);


        if (lst_reviews.size() == 0 && !first)
            tv_noReview.setVisibility(View.VISIBLE);
        else
            tv_noReview.setVisibility(View.GONE);

        if (adapter_reviews != null)
            lstV_reviews.setAdapter(adapter_reviews);

        lstV_reviews.setVerticalScrollbarPosition(pos);

        if (first) {

            Bundle bundle = getActivity().getIntent().getExtras();
            if (bundle.containsKey("url_reviews"))
                urlReviews = bundle.getString("url_reviews");

            new MovieReviews().execute();
        }
    }

    private class MovieReviews extends AsyncTask<Void, Void, Void> {

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
        protected Void doInBackground(Void... params) {

            parseReviews(NetworkHelper.FetchMovieData(urlReviews));
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            if (dialog != null)
                dialog.dismiss();
            adapter_reviews = new ReviewsAdapter(getActivity(), lst_reviews);
            lstV_reviews.setAdapter(adapter_reviews);

            if (lst_reviews.size() == 0)
                tv_noReview.setVisibility(View.VISIBLE);
            else
                tv_noReview.setVisibility(View.GONE);


        }
    }

    private void parseReviews(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray resultLst = jsonObject.getJSONArray("results");
            Review review;
            for (int i = 0; i < resultLst.length(); i++) {
                JSONObject obj = resultLst.getJSONObject(i);
                review = new Review();
                review.setAuthor(obj.getString("author"));
                review.setContent(obj.getString("content"));
                lst_reviews.add(review);

            }
        } catch (Exception e) {

            Log.e("trailer_error", e + "");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("first", false);
        outState.putInt("scroll", lstV_reviews.getFirstVisiblePosition());
        super.onSaveInstanceState(outState);
    }
}
