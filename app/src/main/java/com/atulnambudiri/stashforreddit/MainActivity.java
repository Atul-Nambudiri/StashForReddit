package com.atulnambudiri.stashforreddit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawer;
    ListView drawerList;
    ActionBarDrawerToggle toggle;
    SubredditFragment frag;
    LinkFragment lFrag;
    PostFragment pFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupDrawer();
        frag = new SubredditFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, frag, "main")
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:
                return true;
            case R.id.action_choose:
                new ChooseSubredditDialog().show(getSupportFragmentManager(), "Choose Subreddit Dialog");
            case R.id.action_order:
                new ChooseOrderDialog().show(getSupportFragmentManager(), "Choose Order Dialog");
            case android.R.id.home:
                Toast.makeText(getApplication(), "Back", Toast.LENGTH_LONG).show();
                onBackPressed();
            default:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Opens up a new post as a detail fragment
     * @param domain The domain of the post, ie i.imgur, or self.reddit
     * @param url The URL to Open
     */
    public void openPost(String domain, String url) {
        Bundle args = new Bundle();
        args.putString("url", url);
        Log.v("Domain", domain);
        Log.v("URL", url);
        toggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(domain.substring(0, 5).equals("self.")) {
            pFrag = new PostFragment();
            pFrag.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, pFrag, "main").addToBackStack(null)
                    .commit();
        }
        else{
            lFrag = new LinkFragment();
            lFrag.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, lFrag, "main").addToBackStack(null)
                    .commit();
        }
    }

    public void onBackPressed()
    {
        FragmentManager f = this.getSupportFragmentManager();
        f.popBackStack();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(true);

    }

    public void setupDrawer() {
        getSupportActionBar().setTitle("Front Page");
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.subreddits)));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        toggle.syncState();
        drawer.setDrawerListener(toggle);
    }

    public class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            TextView t = (TextView) view.findViewById(android.R.id.text1); //The text in the ListView
            String account = t.getText().toString();
            frag.changeSubreddit(account);
            drawerList.setItemChecked(position, true);
            drawer.closeDrawer(drawerList);
        }
    }

    @SuppressLint("ValidFragment")
    public class ChooseSubredditDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Choose Subreddit");
            final EditText input = new EditText(getActivity());
            builder.setView(input)
                    .setPositiveButton(R.string.go, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String subreddit = input.getText().toString();
                            frag.changeSubreddit(subreddit);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ChooseSubredditDialog.this.getDialog().cancel();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    @SuppressLint("ValidFragment")
    public class ChooseOrderDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Order Posts By:");
            final String orders[] = getResources().getStringArray(R.array.orders);
            builder.setItems(orders, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int pos) {
                    frag.changeOrder(orders[pos].toLowerCase());
                }
            });
            return builder.create();
        }
    }
}
