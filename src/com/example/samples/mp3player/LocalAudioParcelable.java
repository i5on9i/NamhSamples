package com.example.samples.mp3player;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 10. 17
 * Time: 오후 4:57
 * To change this template use File | Settings | File Templates.
 */
public class LocalAudioParcelable implements Parcelable, Media {

    private int mType;
    private String mPath;

    public LocalAudioParcelable(int type, String path) {
        mType = type;
        mPath = path;
    }


    @Override
    public MediaPlayer play(Context context) throws IOException {
        if (mPath == "") {
            // Tell the user to provide an audio file URL.
            Toast.makeText(
                    context,
                    "path is empty, why?",
                    Toast.LENGTH_LONG).show();

        }
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(mPath);
        mediaPlayer.prepare();
        mediaPlayer.start();

        return mediaPlayer;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mType);
        dest.writeString(mPath);
    }


    // example constructor that takes a Parcel and gives you an object populated with it's values
    private LocalAudioParcelable(Parcel in) {
        mType = in.readInt();
        mPath = in.readString();
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Creator<LocalAudioParcelable> CREATOR = new Creator<LocalAudioParcelable>() {
        public LocalAudioParcelable createFromParcel(Parcel in) {
            return new LocalAudioParcelable(in);
        }

        public LocalAudioParcelable[] newArray(int size) {
            return new LocalAudioParcelable[size];
        }
    };



}
