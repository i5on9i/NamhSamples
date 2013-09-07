package com.example.samples.progressupdater;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.samples.R;
import com.example.samples.progressui.ProgressView;

public class ProgressUpdaterActivity extends Activity implements View.OnClickListener {
    private PinProgressButton mProgressButton;
    private Button mDownloadButton;
    private Button mCancelButton;

    private ProgressView pw_two;
    int progress = 0;


    private long mDownloadId;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mProgressButton = (PinProgressButton) findViewById(R.id.pin_progress);
        mDownloadButton = (Button) findViewById(R.id.button_download);
        mCancelButton = (Button) findViewById(R.id.button_cancel);

        mDownloadButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);


        ProgressUpdater.getInstance().init(getApplicationContext());


    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Action is added
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(mReceiver, filter);

    }

    private long doDownload(PinProgressButton pb, String fUrl) {

        String filename = "TestImage.jpg";

        // Do download with DownloadManager
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(fUrl))
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setDescription("ProcessUpdaterExmpale");


        if (android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED)) {
            request.setDestinationInExternalPublicDir(
                    android.os.Environment.DIRECTORY_DOWNLOADS, filename);
        }

        return ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);


    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();


            long downloadId = intent.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID, 0);


            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

                int status = getDownloadStateWhenCompleted(downloadId);
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    // when the download status is "Successful"
                } else if (status == DownloadManager.STATUS_FAILED) {
                    // When Canceled

                }

                // Initialize the Progress Button
                ProgressUpdater.getInstance().stop(downloadId);

            }

            if (action.equals(Intent.ACTION_SCREEN_ON)) {
                Log.i("[BroadcastReceiver]", "Screen ON");
            }

        }// end of onReceive
    };


    private int getDownloadStateWhenCompleted(long id) {

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor c = ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).query(query);
        if (c.moveToFirst()) {
            int statusColumn = c.getColumnIndex(DownloadManager.COLUMN_STATUS);

            return c.getInt(statusColumn);

        }
        return DownloadManager.STATUS_FAILED;

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_download) {
            String fUrl = "http://upload.wikimedia.org/wikipedia/commons/c/cf/Frog_on_river_4000x3000_26-09-2010_11-01am_2mb.jpg";

            mDownloadId = doDownload(mProgressButton, fUrl);

            ProgressUpdater.getInstance().execute(mProgressButton, mDownloadId);
        } else if (v.getId() == R.id.button_cancel) {
            ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).remove(mDownloadId);

        }


    }
}



