package mal.com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mal.com.Models.Trailer;
import mal.com.movieapp.R;

/**
 * Created by sara on 10/21/2016.
 */
public class TrailersAdapter extends BaseAdapter {

    private ArrayList<Trailer> lst_trailer;
    private Context context;

    public TrailersAdapter(Context context, ArrayList<Trailer> lst_trailer) {
        this.context = context;
        this.lst_trailer = lst_trailer;
    }

    @Override
    public int getCount() {
        return lst_trailer.size();
    }

    @Override
    public Object getItem(int position) {
        return lst_trailer.get(position);
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
            convertView = inflater.inflate(R.layout.adapter_trailer, parent, false);
        }
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_trailer_name);
        tv_name.setText(lst_trailer.get(position).getName());
        return convertView;
    }

    public void ClearAdapter() {
        lst_trailer.clear();
        notifyDataSetChanged();
    }

    public void AddALL(ArrayList<Trailer> trailers) {
        lst_trailer.addAll(trailers);
        notifyDataSetChanged();
    }
}
