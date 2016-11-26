package mal.com.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mal.com.Models.Movie;
import mal.com.movieapp.R;

/**
 * Created by sara on 10/21/2016.
 */
public class MovieAdapter extends BaseAdapter {

    private ArrayList<Movie> lst_movie;
    private Context context;

    public MovieAdapter(Context context, ArrayList<Movie> lst_movie) {
        this.context = context;
        this.lst_movie = lst_movie;
    }

    @Override
    public int getCount() {
        return lst_movie.size();
    }

    @Override
    public Object getItem(int position) {
        return lst_movie.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Log.e("position", position + "");
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_movie, parent, false);
        }
        ImageView imgV_poster = (ImageView) convertView.findViewById(R.id.imgV_poster);
        String img_url = context.getResources().getString(R.string.url_img) + "w185/" +
                lst_movie.get(position).getPoster();
        Log.e("img_url", img_url);
        Picasso.with(context).load(img_url).placeholder(R.drawable.loading).error(R.drawable.no_img)
                .into(imgV_poster);

        return convertView;
    }

    public void ClearAdapter() {
        lst_movie.clear();
        notifyDataSetChanged();
    }

    public void AddMovies(ArrayList<Movie> movies) {
        lst_movie.addAll(movies);
        notifyDataSetChanged();
    }
}
