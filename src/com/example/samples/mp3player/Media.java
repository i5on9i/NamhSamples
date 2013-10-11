package com.example.samples.mp3player;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 10. 17
 * Time: 오후 4:57
 * To change this template use File | Settings | File Templates.
 */
public interface Media {


    public MediaPlayer play(Context context) throws IOException;
}
