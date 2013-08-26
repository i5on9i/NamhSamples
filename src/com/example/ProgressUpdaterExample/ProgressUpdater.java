package com.example.ProgressUpdaterExample;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import com.example.ProgressUpdaterExample.progressui.PinProgressButton;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 8. 25
 * Time: 오후 3:15
 * To change this template use File | Settings | File Templates.
 */
public class ProgressUpdater {
    private Handler mUIHandler = new Handler();
    private int mInterval = 1000;
    private ExecutorService mExecutorService;

    private Map<PinProgressButton, Long> mCurrentDownloadIds
            = Collections.synchronizedMap(new HashMap<PinProgressButton, Long>());
    private Map<Long, ButtonUpdaterUiRunnable> mCurrentButtonUpdater
            = Collections.synchronizedMap(new HashMap<Long, ButtonUpdaterUiRunnable>());



    private volatile static ProgressUpdater instance;
    private Context mContext;

    /** Returns singleton class instance */
    public static ProgressUpdater getInstance() {
        if (instance == null) {
            synchronized (ProgressUpdater.class) {
                if (instance == null) {
                    instance = new ProgressUpdater();
                }
            }
        }
        return instance;
    }
    private ProgressUpdater() {
    }

    public synchronized void init(Context c){
        mContext = c;
        mExecutorService = Executors.newFixedThreadPool(1);
    }

    public void execute(PinProgressButton ppb, long dId){
        //mCurrentPinProgressButtons.put(ppb, progress);
        if(ppb == null || dId == -1)
        {
            return;
        }

        mCurrentDownloadIds.put(ppb, dId);
        Task t = new Task(ppb, dId);
        PButtonUpdater pbu = new PButtonUpdater(t);

        mExecutorService.submit(pbu);	// thread.execute()

    }




    public void stop(long dId){

        ButtonUpdaterUiRunnable bu = mCurrentButtonUpdater.get(dId);
        mUIHandler.removeCallbacks(bu);

        // Set Progress Button to 0
        ButtonInitUiRunnable binit = new ButtonInitUiRunnable(bu.mTask);
        mUIHandler.post(binit);

    }


    private class PButtonUpdater implements Runnable {
        Task task;


        PButtonUpdater(Task task){
            this.task = task;
        }

        @Override
        public void run() {

            if(viewCorrupted(task))
            {
                return;
            }
            ButtonUpdaterUiRunnable bu = new ButtonUpdaterUiRunnable(task);
            mCurrentButtonUpdater.put(task.downloadId, bu);

            mUIHandler.postDelayed(bu, mInterval);  // start updating
        }

    }


    private class ButtonUpdaterUiRunnable implements Runnable {
        Task mTask;

        public ButtonUpdaterUiRunnable(Task t){
            mTask = t;
        }

        @Override
        public void run()
        {

            if(viewCorrupted(mTask))
            {
                return;
            }
            mTask.ppb.setProgress(getProgress(mTask.downloadId));
            mUIHandler.postDelayed(this, mInterval);    // This makes Update do periodically.
        }

    }

    private class ButtonInitUiRunnable implements Runnable {
        Task mTask;

        public ButtonInitUiRunnable(Task t){
            mTask = t;
        }

        @Override
        public void run()
        {
            mTask.ppb.setProgress(0);
        }

    }


    private class Task
    {
        public PinProgressButton ppb;
        public long downloadId;

        public Task(PinProgressButton ppb, long dId){
            this.ppb = ppb;
            this.downloadId = dId;
        }
    }


    protected boolean viewCorrupted(Task task){
        Long dId = mCurrentDownloadIds.get(task.ppb);

        if(dId == null || dId != task.downloadId){
            return true;
        }
        return false;
    }


    private int getProgress(long dId){
        DownloadManager.Query q = new DownloadManager.Query();
        q.setFilterById(dId);

        Cursor cursor = ((DownloadManager)mContext
                .getSystemService(Context.DOWNLOAD_SERVICE)).query(q);
        if(cursor.getCount() <= 0)
        {
            cursor.close();
            return 0;
        }

        cursor.moveToFirst();
        int bytes_downloaded = cursor.getInt(cursor
                .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
        int downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
        cursor.close();

        if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL) {
            return 0;
        }

        return (int)((long)bytes_downloaded*100 / (long)bytes_total);
    }
}
