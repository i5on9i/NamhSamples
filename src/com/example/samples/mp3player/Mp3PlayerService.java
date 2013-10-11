/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.samples.mp3player;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.example.samples.R;

import java.io.IOException;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

/**
 * This is an example of implementing an application service that runs locally
 * in the same process as the application.
 *
 * <p/>
 * <p>Notice the use of the {@link android.app.NotificationManager} when interesting things
 * happen in the service.  This is generally how background services should
 * interact with the user, rather than doing something more disruptive such as
 * calling startActivity().
 */

public class Mp3PlayerService extends Service {

    private static final String TAG = "MediaPlayerDemo";

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.mp3player_service_started;

    public static final String MEDIA = "media";
    public static final String PATH = "path";

    public static final int MEDIA_TYPE_LOCAL_AUDIO = 1;
    public static final int MEDIA_TYPE_STREAM_AUDIO = 2;
    public static final int MEDIA_TYPE_RESOURCES_AUDIO = 3;
    public static final int MEDIA_TYPE_LOCAL_VIDEO = 4;
    public static final int MEDIA_TYPE_STREAM_VIDEO = 5;
    public static final int MEDIA_TYPE_DEFAULT = MEDIA_TYPE_LOCAL_AUDIO;

    private String path;

    public static final String EXTRA_PLAYLIST = "EXTRA_PLAYLIST";
    public static final String EXTRA_SHUFFLE = "EXTRA_SHUFFLE";
    private boolean isPlaying = false;
    private MediaPlayer mMediaPlayer;



    @Override
    public void onCreate() {
        super.onCreate();




    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);

        LocalAudioParcelable media = intent.getParcelableExtra(MEDIA);

        playAudio(media);

//        playAudio(intent.getIntExtra(MEDIA, MEDIA_TYPE_DEFAULT),
//                intent.getStringExtra(PATH));


        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            stopForeground(true);
        }

        // Cancel the persistent notification.
//        mNM.cancel(NOTIFICATION);


        // Tell the user we stopped.
        Toast.makeText(this, R.string.mp3player_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void playAudio(Media media) {
        try {
            mMediaPlayer = media.play(this);

            Notification noti = buildMp3PlayerNotification();
            startForeground(NOTIFICATION, noti);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Show a notification while this service is running.
     */
    private Notification buildMp3PlayerNotification() {
        CharSequence title = getText(R.string.mp3player_service_started);
        CharSequence text = getText(R.string.mp3player_service_label);


        Intent i = new Intent(this, Mp3Player.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, 0);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle()
                .addLine(
                        "M.Twain (Google+) Haiku is more than a cert...")
                .addLine("M.Twain Reminder")
                .addLine("M.Twain Lunch?")
                .addLine("M.Twain Revised Specs")
                .addLine("M.Twain ")
                .addLine(
                        "Google Play Celebrate 25 billion apps with Goo..")
                .addLine(
                        "Stack Exchange StackOverflow weekly Newsl...")
                .setBigContentTitle("6 new message")
                .setSummaryText("mtwain@android.com");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.stat_sample)
                .setContentTitle(title)
                .setContentText(text);
        //.setContentIntent(contentIntent);


        Notification noti = mBuilder.build();



        RemoteViews expandedView = new RemoteViews(this.getPackageName(), R.layout.mp3player_notification);
        expandedView.setOnClickPendingIntent(R.id.noti_image, contentIntent);
        expandedView.setOnClickPendingIntent(R.id.noti_button_play, contentIntent);

        noti.bigContentView = expandedView;
        noti.flags |= Notification.FLAG_NO_CLEAR;

        return noti;
    }

    // TODO : will be deleted
//    private void playAudio(Integer media, String path) {
//        try {
//            switch (media) {
//                case MEDIA_TYPE_LOCAL_AUDIO:
//
//                    if (path == "") {
//                        // Tell the user to provide an audio file URL.
//                        Toast.makeText(
//                                Mp3PlayerService.this,
//                                "path is empty, why?",
//                                Toast.LENGTH_LONG).show();
//
//                    }
//                    mMediaPlayer = new MediaPlayer();
//                    mMediaPlayer.setDataSource(path);
//                    mMediaPlayer.prepare();
//                    mMediaPlayer.start();
//
//                    Notification noti = buildMp3PlayerNotification();
//                    startForeground(NOTIFICATION, noti);
//                    break;
//                case MEDIA_TYPE_RESOURCES_AUDIO:
//                    /**
//                     * TODO: Upload a audio file to res/raw folder and provide
//                     * its resid in MediaPlayer.create() method.
//                     */
////                    mMediaPlayer = MediaPlayer.create(this, R.raw.test_cbr);
////                    mMediaPlayer.start();
//                    break;
//
//            }
//
//        } catch (Exception e) {
//            Log.e(TAG, "error: " + e.getMessage(), e);
//        }
//
//    }











}

