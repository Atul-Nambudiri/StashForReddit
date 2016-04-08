package com.atulnambudiri.stashforreddit;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by atuln on 11/14/2015.
 */
public class LinkFragment extends Fragment {
    private String url;
    private WebView wView;

    public LinkFragment() {

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
        View v = inflater.inflate(R.layout.fragment_link, container, false);
        wView = (WebView) v.findViewById(R.id.web_view);

        wView.getSettings().setJavaScriptEnabled(true);
        wView.getSettings().setBuiltInZoomControls(true);
        wView.getSettings().setDisplayZoomControls(false);
        wView.getSettings().setLoadWithOverviewMode(true);
        wView.getSettings().setUseWideViewPort(true);
        wView.setWebViewClient(new WebViewClient());
        wView.loadUrl(url);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        wView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        wView.onPause();
    }

}
