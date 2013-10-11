package com.example.samples.mp3player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import com.example.samples.R;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 10. 17
 * Time: 오후 4:57
 * To change this template use File | Settings | File Templates.
 */
public class Mp3Player extends Activity {
//    private static final String MEDIA = "media";
    private static final int LOCAL_AUDIO = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp3player_main);

        // Watch for button clicks.
        Button button = (Button)findViewById(R.id.button_start);
        button.setOnClickListener(mStartListener);
        button = (Button)findViewById(R.id.button_stop);
        button.setOnClickListener(mStopListener);
    }



    private View.OnClickListener mStartListener = new View.OnClickListener() {
        public void onClick(View v) {
            // Make sure the service is started.  It will continue running
            // until someone calls stopService().  The Intent we use to find
            // the service explicitly specifies our service component, because
            // we want it running in our own process and don't want other
            // applications to replace it.


            String path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS).getPath()
                    + File.separator
                    + "a.mp3";

            Parcelable parcelable = new LocalAudioParcelable(Mp3PlayerService.MEDIA_TYPE_LOCAL_AUDIO,path);

            Intent intent =
                    new Intent(Mp3Player.this.getApplication(),
                            Mp3PlayerService.class);
            intent.putExtra(Mp3PlayerService.MEDIA, parcelable);

            startService(intent);

        }
    };

    private View.OnClickListener mStopListener = new View.OnClickListener() {
        public void onClick(View v) {
            // Cancel a previous call to startService().  Note that the
            // service will not actually stop at this point if there are
            // still bound clients.
            stopService(new Intent(Mp3Player.this, Mp3PlayerService.class));
        }
    };
}
