package com.example.samples.onloadinglist;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import com.example.samples.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoLoadingListViewActivity extends Activity
        implements AutoLoadingListView.OnAutoLoadListener {


    private final String TAG = AutoLoadingListViewActivity.class.getName();
    private AutoLoadingListView mListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autoloadinglistview_activity);
        mListView = (AutoLoadingListView) findViewById(R.id.autoload_listview);



        String[] listitems = { "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
                "Acorn", "AdelostAdelostAdelostAdelostAdelostAdelostAdelostAdelostAdelostAdelostAdelostAdelostAdelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
                "Allgauer Emmentaler" };


        AutoLoadingListViewAdapter adapter =
                new AutoLoadingListViewAdapter(this.getApplicationContext());

        adapter.addAll(Arrays.asList(listitems));
        mListView.setAdapter(adapter);
        mListView.setAutoLoadListener(this);

    }

    @Override
    public void onAutoLoad(int totalItemCount) {
        mListView.addLoadingProgress();


        // Do load data
        AsyncTask<String, Void, List> task = new AsyncTask<String, Void, List>() {
            @Override
            protected List doInBackground(String ... params) {

                // Loading ...
                try {
                    Thread.sleep(3000 /* millisecond */);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String[] newData = {"new item1", "new item2"};
                return Arrays.asList(newData);

            }

            @Override
            protected void onPostExecute(List result) {

                // Finish loading
                mListView.removeLoadingProgress();
                mListView.addAll(result);

            };
        };

        task.execute("'");
    }
}
