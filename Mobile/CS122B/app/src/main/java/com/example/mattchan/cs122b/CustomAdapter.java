package com.example.mattchan.cs122b;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sam on 3/7/18.
 */

public class CustomAdapter extends ArrayAdapter<Movie> implements View.OnClickListener {
    private ArrayList<Movie> dataSet;
    Context ctxt;

    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView year;
        TextView director;
        TextView cast;
        TextView genres;

        ImageView info;
    }

    public CustomAdapter(ArrayList<Movie> data, Context context){
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.ctxt = context;
    }

    @Override
    public void onClick(View v){
        int position = (Integer)v.getTag();
        Object obj = getItem(position);
        Movie movie = (Movie) obj;

        switch(v.getId()){
            case R.id.item_info:
                Snackbar.make(v, movie.getTitle() + "\n" + movie.getYear(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Movie movie = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.year = convertView.findViewById(R.id.year);
            viewHolder.director = convertView.findViewById(R.id.director);
            viewHolder.cast = convertView.findViewById(R.id.cast);
            viewHolder.genres = convertView.findViewById(R.id.genres);

            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(ctxt, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        lastPosition = position;
        viewHolder.title.setText(movie.getTitle());
        viewHolder.year.setText(movie.getYear());
        viewHolder.director.setText(movie.getDirector());
        viewHolder.cast.setText(movie.getCast());
        viewHolder.genres.setText(movie.getGenres());

        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);

        return convertView;
    }
}
