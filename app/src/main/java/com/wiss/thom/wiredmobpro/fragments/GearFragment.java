package com.wiss.thom.wiredmobpro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wiss.thom.wiredmobpro.R;
import com.wiss.thom.wiredmobpro.activities.DetailedArticle;
import com.wiss.thom.wiredmobpro.adapter.CustomListAdapter;
import com.wiss.thom.wiredmobpro.model.Categories;
import com.wiss.thom.wiredmobpro.model.Post;
import com.wiss.thom.wiredmobpro.model.PostORM;

import java.util.List;

/**
 * Created by Thomas on 20.04.2015.
 */
public class GearFragment extends ListFragment {

    private static final String TAG = "GearFragment";
    private List<Post> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gear_layout, container, false);
        list = PostORM.getAllPostsByCategory(getActivity(), Categories.gear);
        //
        //  Sort the list!!! comparator datestamp..
        //
        CustomListAdapter adapter = new CustomListAdapter(getActivity(), list);
        setListAdapter(adapter);
        return rootView;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        // ------ Startet an Position 0!!
        Post post = list.get(position);

        Intent intent = new Intent(getActivity(), DetailedArticle.class);
        intent.putExtra("link", post.getUrl());
        startActivity(intent);

        Log.d(TAG,"onListItemClick on position " + position + " with id " + id);
    }
}
