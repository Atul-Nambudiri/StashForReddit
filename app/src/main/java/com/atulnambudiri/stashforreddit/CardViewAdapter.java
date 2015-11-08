package com.atulnambudiri.stashforreddit;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by atuln on 11/7/2015.
 */
public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
    private ArrayList<JSONObject> postList;
    ImageLoader imageLoader;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Each data item is a CardView
        public CardView myCardView;
        public ViewHolder(CardView v) {
            super(v);
            myCardView = v;
        }
    }

    //Constructor
    public CardViewAdapter(ArrayList<JSONObject> postList, Context c) {
        this.postList = postList;
        imageLoader = VolleySingleton.getInstance(c).getImageLoader();
    }

    //Create new views (invoked by the layout manager)
    @Override
    public CardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        View v = holder.myCardView;
        JSONObject post = postList.get(position);

        TextView score = (TextView) v.findViewById(R.id.score);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView comments = (TextView) v.findViewById(R.id.comments);
        TextView subreddit = (TextView) v.findViewById(R.id.subreddit);
        NetworkImageView thumbnail = (NetworkImageView) v.findViewById(R.id.thumbnail);

        String scoreString = "0";
        String titleString = "NULL";
        String commentsString = "0";
        String subredditString = "NULL";
        String thumbnailURL = "NULL";
        String domain;

        try {
            scoreString = post.getString("score");
            titleString = post.getString("title");
            commentsString = post.getString("num_comments") + " comments";
            subredditString = post.getString("subreddit");
            thumbnailURL = post.getString("thumbnail");
            domain = post.getString("domain");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        score.setText(scoreString);
        title.setText(titleString);
        comments.setText(commentsString);
        subreddit.setText(subredditString);
        thumbnail.setImageUrl(thumbnailURL, imageLoader);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return postList.size();
    }
}
