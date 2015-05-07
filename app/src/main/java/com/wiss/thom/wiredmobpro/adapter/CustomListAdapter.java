package com.wiss.thom.wiredmobpro.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiss.thom.wiredmobpro.R;
import com.wiss.thom.wiredmobpro.model.Post;

import java.util.List;

/**
 * Created by Thomas on 15.04.2015.
 */
public class CustomListAdapter extends ArrayAdapter<Post> {

    private List<Post> list;
    private Activity context;

    public CustomListAdapter(Activity context,List<Post> list){
        super(context, R.layout.list_single, list);
        this.list = list;
        this.context = context;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        View rowView = convertView;


        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_single, null);

            viewHolder = new ViewHolder();
            viewHolder.previewTextView = (TextView) rowView.findViewById(R.id.itemPreview_single);
            viewHolder.titleTextView = (TextView) rowView.findViewById(R.id.itemTitle_single);
            viewHolder.postImageView = (ImageView) rowView.findViewById(R.id.icon_single);
            viewHolder.postedDateView = (TextView) rowView.findViewById(R.id.postedDate_single);
            rowView.setTag(viewHolder);

        }
        // we've just avoided calling findViewById() on resource everytime just use the viewHolder
        ViewHolder holder = (ViewHolder) rowView.getTag();

        Post postInside = getItem(position);
        if (postInside != null) {
            holder.previewTextView.setText(postInside.getPreview());
            holder.titleTextView.setText(postInside.getTitle());
            String postedDate = postInside.getPostedDate();
            String[] parts = postedDate.split(",");
            if(parts[0] != null){
                holder.postedDateView.setText(parts[0]);
            }else{
                holder.postedDateView.setText(postInside.getPostedDate());
            }
            holder.postImageView.setImageBitmap(postInside.getImage());
        } else {
            Log.i("CustomListAdapter", "post is empty");
        }

        return rowView;

    }


    static class ViewHolder {
        private TextView previewTextView;
        private TextView titleTextView;
        private ImageView postImageView;
        private TextView postedDateView;
    }




}
