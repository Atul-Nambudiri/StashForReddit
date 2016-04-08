package com.atulnambudiri.stashforreddit;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by atuln on 11/15/2015.
 */
public class DetailCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Comment> commentsList;
    Context c;
    int colors[];


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class CommentViewHolder extends RecyclerView.ViewHolder{
        //Each data item is a CardView
        public CardView myCardView;
        TextView body;
        TextView author;
        TextView score;
        //TextView sideBar;

        public CommentViewHolder(CardView v) {
            super(v);
            myCardView = v;
            body = ((TextView) v.findViewById(R.id.text));
            author = ((TextView) v.findViewById(R.id.author));
            score = ((TextView) v.findViewById(R.id.score));
            //sideBar = ((TextView) v.findViewById(R.id.side_bar));
        }
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder{
        //Each data item is a CardView
        public CardView myCardView;
        TextView title;
        TextView score;
        TextView comments;
        TextView subreddit;
        TextView domain;

        public TitleViewHolder(CardView v) {
            super(v);
            myCardView = v;
            title = ((TextView) v.findViewById(R.id.title));
            score = ((TextView) v.findViewById(R.id.score));
            comments = ((TextView) v.findViewById(R.id.comments));
            subreddit = ((TextView) v.findViewById(R.id.subreddit));
            domain = ((TextView) v.findViewById(R.id.domain));
        }
    }

    public class MainViewHolder extends RecyclerView.ViewHolder{
        //Each data item is a CardView
        public CardView myCardView;
        TextView body;

        public MainViewHolder(CardView v) {
            super(v);
            myCardView = v;
            body = ((TextView) v.findViewById(R.id.text));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position < 2) {
            return position;
        }
        else{
            return 2;
        }
    }

    //Constructor
    public DetailCardAdapter(ArrayList<Comment> commentsList, Context c) {
        this.c = c;
        this.commentsList = commentsList;
        colors = new int[] {c.getResources().getColor(R.color.red),
                            c.getResources().getColor(R.color.blue),
                            c.getResources().getColor(R.color.green),
                            c.getResources().getColor(R.color.purple),
                            c.getResources().getColor(R.color.orange)};
    }

    //Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_card_layout, parent, false);
        if(viewType == 1) {
            return new MainViewHolder(v);
        } else if(viewType == 0) {
            CardView v2 = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card_layout, parent, false);
            return new TitleViewHolder(v2);
        }
        else{
            return new CommentViewHolder(v);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.v("View Holder", "View Holder Has Been Bound");
        if(position == 0) {
            ((TitleViewHolder) holder).title.setText(commentsList.get(position).title);
            ((TitleViewHolder) holder).subreddit.setText(commentsList.get(position).subreddit);
            ((TitleViewHolder) holder).score.setText(Integer.toString(commentsList.get(position).score) + " points");
            ((TitleViewHolder) holder).domain.setText(commentsList.get(position).domain);
            ((TitleViewHolder) holder).comments.setText(Integer.toString(commentsList.get(position).comments) + " comments");
        }
        else if(position == 1) {
            TextView v = ((MainViewHolder) holder).body;
            v.setText(commentsList.get(position).body);
            final ViewGroup.MarginLayoutParams lpt =(ViewGroup.MarginLayoutParams)((MainViewHolder) holder).myCardView.getLayoutParams();
            float pxToDp = c.getResources().getDisplayMetrics().density;
            int margin = ((int) (5 * pxToDp));
            lpt.setMargins(2*margin, margin, 2*margin, margin);
        }
        else{
            ((CommentViewHolder) holder).body.setText(commentsList.get(position).body);
            ((CommentViewHolder) holder).author.setText(commentsList.get(position).author);
            ((CommentViewHolder) holder).score.setText(Integer.toString(commentsList.get(position).score) + " points");
            final ViewGroup.MarginLayoutParams lpt =(ViewGroup.MarginLayoutParams)((CommentViewHolder) holder).myCardView.getLayoutParams();
            int indent = commentsList.get(position).indent;
            if(indent > 0) {
                //((CommentViewHolder) holder).sideBar.setBackgroundColor(colors[indent % 5]);
                //((CommentViewHolder) holder).sideBar.setLayoutParams(new LinearLayout.LayoutParams(25, ((CommentViewHolder) holder).myCardView.getHeight()));
                if(indent > 0) {
                    lpt.setMargins(30 * commentsList.get(position).indent, 2, 0, 2);
                }
            }
            else {
                //((CommentViewHolder) holder).sideBar.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                lpt.setMargins(0, 2, 0, 2);
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return commentsList.size();
    }
}

