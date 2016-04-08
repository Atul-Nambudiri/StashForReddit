package com.atulnambudiri.stashforreddit;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by atuln on 11/7/2015.
 */
public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.MyViewHolder> {
    private ArrayList<JSONObject> postList;
    OnItemClickListener itemClickListener;
    ImageLoader imageLoader;

    public interface OnItemClickListener {
        void onItemClick(JSONObject object);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //Each data item is a CardView
        public CardView myCardView;

        public MyViewHolder(CardView v) {
            super(v);
            v.setOnClickListener(this);
            myCardView = v;
        }

        @Override
        public void onClick(View v) {
            String text = ((TextView) (v.findViewById(R.id.title))).getText().toString();
            Snackbar.make(v, "Clicked on " + text, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            if (itemClickListener != null) {
                itemClickListener.onItemClick(postList.get(getPosition()));
            }
        }
    }

    //Constructor
    public CardViewAdapter(ArrayList<JSONObject> postList, Context c) {
        this.postList = postList;
        imageLoader = VolleySingleton.getInstance(c).getImageLoader();
    }

    //Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card_layout, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        View v = holder.myCardView;
        JSONObject post = postList.get(position);

        TextView score = (TextView) v.findViewById(R.id.score);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView comments = (TextView) v.findViewById(R.id.comments);
        TextView subreddit = (TextView) v.findViewById(R.id.subreddit);
        TextView domain = (TextView) v.findViewById(R.id.domain);
        //NetworkImageView thumbnail = (NetworkImageView) v.findViewById(R.id.thumbnail);

        String scoreString = "0";
        String titleString = "NULL";
        String commentsString = "0";
        String subredditString = "NULL";
        String thumbnailURL = "NULL";
        String domainString = "NULL";

        try {
            scoreString = post.getString("score") + " points";
            titleString = post.getString("title");
            commentsString = post.getString("num_comments") + " comments";
            subredditString = post.getString("subreddit").trim();
            thumbnailURL = post.getString("thumbnail");
            domainString = post.getString("domain");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        score.setText(scoreString);
        title.setText(titleString);
        comments.setText(commentsString);
        subreddit.setText(subredditString);
        domain.setText(domainString);

        //thumbnail.setImageUrl(thumbnailURL, imageLoader);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return postList.size();
    }
}
