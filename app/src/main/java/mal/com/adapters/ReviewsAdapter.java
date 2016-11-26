package mal.com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mal.com.Models.Review;
import mal.com.movieapp.R;

/**
 * Created by sara on 11/21/2016.
 */
public class ReviewsAdapter extends BaseAdapter {

    private ArrayList<Review> lst_reviews;
    private Context context;

    public ReviewsAdapter(Context context, ArrayList<Review> lst_reviews) {
        this.context = context;
        this.lst_reviews = lst_reviews;
    }

    @Override
    public int getCount() {
        return lst_reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return lst_reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_review, parent, false);
        }

        TextView tv_author = (TextView) convertView.findViewById(R.id.tv_review_author);
        TextView tv_content = (TextView) convertView.findViewById(R.id.tv_review_content);

        tv_author.setText(lst_reviews.get(position).getAuthor());
        tv_content.setText(lst_reviews.get(position).getContent());

        return convertView;
    }

    public void AddALL(ArrayList<Review> reviews) {
        lst_reviews.addAll(reviews);
        notifyDataSetChanged();
    }
}
