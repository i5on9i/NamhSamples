package com.example.samples.progressui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.samples.R;
import com.example.samples.progressui.ProgressView;

public class ProgressViewActivity extends Activity implements View.OnClickListener {

    private Button mDownloadButton;
    private Button mCancelButton;



    private long mDownloadId;

    private int progress;
    private ProgressView pw_two;

    private Activity mActivity;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_view_activity);
        mActivity = this;

        pw_two = (ProgressView) findViewById(R.id.progressView);
        pw_two.setProgress(20);

        Button b = (Button) findViewById(R.id.button_start);
        b.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_start) {


            progress = 0;
            final Runnable r = new Runnable() {
                public void run() {

                    while (progress < 361) {


                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pw_two.setProgress(progress++);
                            }
                        });

                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }
            };
            Thread s = new Thread(r);
            s.start();

        }
    }


}



