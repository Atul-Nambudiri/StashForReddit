package com.atulnambudiri.stashforreddit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PostFragment extends Fragment {
    private String url;
    private String title = "";
    private String selfText = "";
    private View view;
    private RequestQueue queue;
    private ArrayList<Comment> commentList;
    private RecyclerView myRecyclerView;
    private DetailCardAdapter myAdapter;

    public PostFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        url = args.getString("url", "url");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post, container, false);
        commentList = new ArrayList<Comment>();
        setupRecyclerView(view);
        openPost();
        return view;
    }



    public void openPost() {
        String postUrl = url + ".json";
        Log.v("url", postUrl);
        queue = VolleySingleton.getInstance(this.getActivity()).getRequestQueue();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, postUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("Volley String Request", response);
                        try {
                            JSONObject postInfo = new JSONArray(response).getJSONObject(0)
                                    .getJSONObject("data").getJSONArray("children")
                                    .getJSONObject(0).getJSONObject("data");
                            JSONArray comments = new JSONArray(response).getJSONObject(1)
                                    .getJSONObject("data").getJSONArray("children");
                            title = postInfo.getString("title");
                            String subreddit = postInfo.getString("subreddit");
                            String domain = postInfo.getString("domain");
                            int score = postInfo.getInt("score");
                            int num_comments = postInfo.getInt("num_comments");
                            if(postInfo.has("selftext")) {
                                selfText = postInfo.getString("selftext");
                            }
                            setTitle();
                            Comment titleC = new Comment(title, score, num_comments, subreddit, domain);
                            Comment postTextC = new Comment(0, selfText, "", 0);
                            commentList.add(titleC);
                            commentList.add(postTextC);
                            processChildren(comments, 0);
                            myAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Volley String Request", "Failed");
                        Log.v("Error:", error.getMessage());
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        Log.v("Volley String Request", "Done");
    }

    public void processChildren(JSONArray comments, int indent) {
        for(int i = 0; i < comments.length(); i++) {
            try {
                JSONObject object = comments.getJSONObject(i).getJSONObject("data");
                if(object.has("body")) {
                    Comment comment = new Comment(indent, object.getString("body"), object.getString("author"), object.getInt("score"));
                    commentList.add(comment);
                    if(object.has("replies") && !(object.getString("replies").equals(""))) {
                        JSONArray replies = object.getJSONObject("replies").getJSONObject("data").getJSONArray("children");
                        processChildren(replies, indent + 1);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void setupRecyclerView(View mainView) {
        myRecyclerView = (RecyclerView) mainView.findViewById(R.id.comment_recycler_view);

        final RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(this.getActivity());

        //Use a linear layout manager
        myRecyclerView.setLayoutManager(myLayoutManager);

        //Specify an Adapter
        myAdapter = new DetailCardAdapter(commentList, this.getActivity());
        myRecyclerView.setAdapter(myAdapter);
    }

    public void setTitle() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                Toast.makeText(getActivity(), "Back", Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
