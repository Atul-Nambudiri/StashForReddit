package com.atulnambudiri.stashforreddit;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class SubredditFragment extends Fragment {
    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter myAdapter;
    private final RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(this.getActivity());

    private String currentPage = "https://www.reddit.com/";
    private String order = "hot";
    private String title;
    private RequestQueue queue;
    ArrayList<JSONObject> postList;

    public SubredditFragment() {
        title = "Front Page";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_main, container, false);
        postList = new ArrayList<JSONObject>();
        getPosts();
        setupRecyvlerView(myView);
        return myView;
    }

    public void setupRecyvlerView(View mainView) {
        myRecyclerView = (RecyclerView) mainView.findViewById(R.id.my_recylcer_view);

        //Use a linear layout manager
        myRecyclerView.setLayoutManager(myLayoutManager);

        //Specify an Adapter
        myAdapter = new CardViewAdapter(postList, this.getActivity());
        myRecyclerView.setAdapter(myAdapter);
    }

    public void getPosts() {
        queue = VolleySingleton.getInstance(this.getActivity()).getRequestQueue();
        String inputPage = currentPage + order + "/.json";
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, inputPage, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray posts = response.getJSONObject("data").getJSONArray("children");
                            postList.clear();
                            for(int i = 0 ; i < posts.length(); i ++) {
                                postList.add(posts.getJSONObject(i).getJSONObject("data"));
                            }
                            myAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Volley JSON Request", "Failed");
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
        Log.v("Volley JSON Request", "Done");
    }

    /**
     * Changes the subreddit
     * @param newSubreddit The new subreddit to go to
     */
    public void changeSubreddit(String newSubreddit) {
        String newPage;
        if(newSubreddit.equals("Front Page")) {
            newPage = "https://www.reddit.com/";
            title = "Front Page";
        }
        else if(!newSubreddit.equals("")) {
            newPage = "https://www.reddit.com/r/" + newSubreddit + "/";
            title = newSubreddit;
        }
        else {
            newPage = "https://www.reddit.com/";
            title = newSubreddit;
        }
        if(!newPage.equals(currentPage)) {   //Don't change the subreddit if the new subreddit is the same as the current one
            currentPage = newPage;
            setTitle();
            getPosts();
        }
    }

    /**
     * Changes the order of Posts
     * @param newOrder The new ordering to use
     */
    public void changeOrder(String newOrder) {
        if(!newOrder.equals(order)) {
            order = newOrder;
            getPosts();
        }

    }

    public void setTitle() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
    }
}
