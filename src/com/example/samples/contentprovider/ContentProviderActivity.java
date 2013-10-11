package com.example.samples.contentprovider;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.samples.R;

import java.util.ArrayList;
import java.util.List;


public class ContentProviderActivity extends Activity
        implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {


    private static final int LOADER_ID = 1;




    private List<DownloadItem> mDownloadItemList = new ArrayList<DownloadItem>();
    private List<Note> mNoteList = new ArrayList<Note>();


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contentprovider_main);

        Button b = (Button) findViewById(R.id.button_backup);
        b.setOnClickListener(this);


        // test inputs
        mDownloadItemList.add(new DownloadItem("file1", 1));
        mDownloadItemList.add(new DownloadItem("file2", 2));
        mDownloadItemList.add(new DownloadItem("file3", 3));


    }


    private void backupData() throws RemoteException, OperationApplicationException {

        // Batch instruction
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        saveDownloadIds(ops);
        saveOnePageNoteList(ops);
        getContentResolver().applyBatch(Contract.AUTHORITY, ops);


    }

    private void saveDownloadIds(ArrayList<ContentProviderOperation> ops) {

        //
        // For withYieldAllowed(true),
        // {@link : https://www.grokkingandroid.com/androids-contentprovideroperation-withyieldallowed-explained/}
        //


        // delete all
        ops.add(ContentProviderOperation.newDelete(Contract.DownloadIds.CONTENT_URI)
                .withYieldAllowed(true).build());

        // add
        for (DownloadItem item : mDownloadItemList) {
            // add new items
            ContentValues values = new ContentValues();
            values.put(Contract.DownloadIds.FILENAME, item.filename);
            values.put(Contract.DownloadIds.DOWNLOAD_ID, item.downloadId);

            ops.add(ContentProviderOperation.newInsert(Contract.DownloadIds.CONTENT_URI)
                    .withValues(values).withYieldAllowed(true).build());
        }

    }

    private void saveOnePageNoteList(ArrayList<ContentProviderOperation> ops) {
        // delete all
        ops.add(ContentProviderOperation.newDelete(Contract.Note.CONTENT_URI)
                .withYieldAllowed(true).build());

        // add new items
        for (Note note : mNoteList) {
            ContentValues values = new ContentValues();
            values.put(Contract.Note.DATE, note.date);
            values.put(Contract.Note.NAME, note.name);
            values.put(Contract.Note.NOTE, note.note);

            ops.add(ContentProviderOperation.newInsert(Contract.Note.CONTENT_URI)
                    .withValues(values).withYieldAllowed(true).build());
        }
    }


    /////////////////////////////////////////////////////////////////////////////////
    ////
    ////    OnClickListener
    ////
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_backup:
                try {
                    backupData();
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button_retrieve:
                retreiveDownloadIds();
                break;
        }


    }

    private void retreiveDownloadIds() {
        ArrayList<DownloadItem> dList = new ArrayList<DownloadItem>();


        Cursor cursor = getContentResolver().query(Contract.DownloadIds.CONTENT_URI,
                Contract.DownloadIds.PROJECTION_ALL, null, null, null);

        if (cursor != null) {

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String filename = cursor.getString(cursor.getColumnIndex(Contract.DownloadIds.FILENAME));
                int did = cursor.getInt(cursor.getColumnIndex(Contract.DownloadIds.DOWNLOAD_ID));

                dList.add(new DownloadItem(filename, did));
                cursor.moveToNext();
            }

            cursor.close();
        }

        mDownloadItemList = dList;

    }


    /////////////////////////////////////////////////////////////////////////////////
    ///   LoaderManager.LoaderCallbacks<Cursor>
    ///

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new CursorLoader(this, Contract.Note.CONTENT_URI,
                Contract.Note.PROJECTION_ALL, null, null, null);


    }

    /**
     * Everytime the application is called, this method is invoked.
     *
     * @param cursorLoader
     * @param cursor
     */
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        initNoteList(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        Log.d("namh", "onLoaderReset()");

    }


    /////////////////////////////////////////////////////////////////////////////////
    ////    Private methods
    ////


    private void initNoteList(Cursor cursor) {
        mNoteList = generateListWithEntries(cursor);

        if (mNoteList == null) {
            mNoteList.add(new Note("2010-10-01", "name1", "note1"));
            mNoteList.add(new Note("2010-10-02", "name2", "note2"));
            mNoteList.add(new Note("2010-10-03", "name3", "note3"));
            mNoteList.add(new Note("2010-10-04", "name4", "note4"));

        }

    }

    private ArrayList<Note> generateListWithEntries(Cursor cursor) {
        ArrayList<Note> notelist = new ArrayList<Note>();

        if (cursor != null) {

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String date = cursor.getString(cursor.getColumnIndex(Contract.Note.DATE));
                String name = cursor.getString(cursor.getColumnIndex(Contract.Note.NAME));
                String note = cursor.getString(cursor.getColumnIndex(Contract.Note.NOTE));

                notelist.add(new Note(date, name, note));
                cursor.moveToNext();
            }

            // @notice
            // Do NOT close the cursor,
            //
            // because {@link onLoadFinished()} is invoked everytime resuming the application
            // but {@link onCreateLoader()} is invoked once.
        }
        return notelist;
    }


}
