package com.example.samples.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;


/**
 * The below source is written in the basis of Grokking Android source{@ref http://goo.gl/14Gkdx}
 */
public class DatabaseProvider extends ContentProvider {

    private DatabaseHelper mDbHelper;

    private static final int NOTE_LIST = 1;
    private static final int NOTE_ID = 2;
    private static final int DOWNLOAD_ID_LIST = 3;
    private static final int DOWNLOAD_ID = 4;


    private static final UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        mUriMatcher.addURI(Contract.AUTHORITY, "items", NOTE_LIST);
        mUriMatcher.addURI(Contract.AUTHORITY, "items/#", NOTE_ID);
        mUriMatcher.addURI(Contract.AUTHORITY, "download_ids", DOWNLOAD_ID_LIST);
        mUriMatcher.addURI(Contract.AUTHORITY, "download_ids/#", DOWNLOAD_ID);
    }

    private final ThreadLocal<Boolean> mIsInBatchMode = new ThreadLocal<Boolean>(); // works like a lock

    @Override
    public boolean onCreate() {
        mDbHelper = new DatabaseHelper(getContext());

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        boolean useAuthorityUri = false;

        int uriType = mUriMatcher.match(uri);
        switch (uriType) {
            case NOTE_LIST:
                useAuthorityUri = true;
                queryBuilder.setTables(DatabaseHelper.TABLE_NAME_NOTELIST);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = Contract.Note.SORT_ORDER_DEFAULT;
                }

                break;
            case NOTE_ID:
                useAuthorityUri = true;
                queryBuilder.setTables(DatabaseHelper.TABLE_NAME_NOTELIST);
                queryBuilder.appendWhere(Contract.Note._ID + " = " +
                        uri.getLastPathSegment());

                break;
            case DOWNLOAD_ID_LIST:
                useAuthorityUri = true;
                queryBuilder.setTables(DatabaseHelper.TABLE_NAME_DOWNLOAD_ID);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = Contract.DownloadIds.SORT_ORDER_DEFAULT;
                }

                break;
            case DOWNLOAD_ID:
                useAuthorityUri = true;
                queryBuilder.setTables(DatabaseHelper.TABLE_NAME_DOWNLOAD_ID);
                queryBuilder.appendWhere(Contract.DownloadIds._ID + " = " +
                        uri.getLastPathSegment());

                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);

        if (useAuthorityUri) {
            // Make sure that potential listeners are getting notified
            cursor.setNotificationUri(getContext().getContentResolver(),
                    Contract.CONTENT_URI);
        } else {
            cursor.setNotificationUri(getContext().getContentResolver(),
                    uri);
        }


        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case NOTE_LIST:
                return Contract.Note.CONTENT_TYPE;
            case NOTE_ID:
                return Contract.Note.CONTENT_ITEM_TYPE;
            case DOWNLOAD_ID_LIST:
                return Contract.DownloadIds.CONTENT_TYPE;
            case DOWNLOAD_ID:
                return Contract.DownloadIds.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        int uriType = mUriMatcher.match(uri);

        SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();

        long rowId = 0;
        switch (uriType) {
            case NOTE_LIST:
                rowId = sqlDB.insertOrThrow(DatabaseHelper.TABLE_NAME_NOTELIST, null, values);
                break;
            case DOWNLOAD_ID_LIST:
                rowId = sqlDB.insertOrThrow(DatabaseHelper.TABLE_NAME_DOWNLOAD_ID, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        return getUriForId(rowId, uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = mUriMatcher.match(uri);

        SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();
        int delCount = 0;
        String where;
        String idStr;

        switch (uriType) {
            case NOTE_LIST:
                delCount = sqlDB.delete(DatabaseHelper.TABLE_NAME_NOTELIST, selection,
                        selectionArgs);
                break;
            case NOTE_ID:
                delCount = sqlDB.delete(DatabaseHelper.TABLE_NAME_NOTELIST, selection,
                        selectionArgs);

                idStr = uri.getLastPathSegment();
                where = Contract.Note._ID + " = " + idStr;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }

                delCount = sqlDB.delete(DatabaseHelper.TABLE_NAME_NOTELIST,
                        where,
                        selectionArgs);
                break;
            case DOWNLOAD_ID_LIST:
                delCount = sqlDB.delete(DatabaseHelper.TABLE_NAME_DOWNLOAD_ID, selection,
                        selectionArgs);
                break;
            case DOWNLOAD_ID:
                delCount = sqlDB.delete(DatabaseHelper.TABLE_NAME_DOWNLOAD_ID, selection,
                        selectionArgs);

                idStr = uri.getLastPathSegment();
                where = Contract.DownloadIds._ID + " = " + idStr;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }

                delCount = sqlDB.delete(DatabaseHelper.TABLE_NAME_DOWNLOAD_ID,
                        where,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (delCount > 0 && !isInBatchMode()) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return delCount;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = mUriMatcher.match(uri);

        SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();
        int updateCount = 0;
        String id;
        String where;
        switch (uriType) {
            case NOTE_LIST:
                updateCount = sqlDB.update(DatabaseHelper.TABLE_NAME_NOTELIST,
                        values,
                        selection,
                        selectionArgs);
                break;
            case NOTE_ID:
                id = uri.getLastPathSegment();
                where = Contract.Note._ID + "=" + id;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }

                updateCount = sqlDB.update(DatabaseHelper.TABLE_NAME_NOTELIST,
                        values,
                        where,
                        selectionArgs);
                break;

            case DOWNLOAD_ID_LIST:
                updateCount = sqlDB.update(DatabaseHelper.TABLE_NAME_DOWNLOAD_ID,
                        values,
                        selection,
                        selectionArgs);
                break;
            case DOWNLOAD_ID:
                id = uri.getLastPathSegment();
                where = Contract.DownloadIds._ID + "=" + id;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }

                updateCount = sqlDB.update(DatabaseHelper.TABLE_NAME_DOWNLOAD_ID,
                        values,
                        where,
                        selectionArgs);
                break;


            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        if (updateCount > 0 && !isInBatchMode()) {
            getContext().getContentResolver().notifyChange(uri, null);
        }


        return updateCount;
    }


    private Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            if (!isInBatchMode()) {
                getContext().getContentResolver().notifyChange(itemUri, null);
            }
            return itemUri;
        }

        // s.th. went wrong:
        throw new SQLException("Problem while inserting into uri: " + uri);
    }

//    @Override
//    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
//            throws OperationApplicationException {
//
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//        mIsInBatchMode.set(true);
//        // the next line works because SQLiteDatabase
//        // uses a thread local SQLiteSession object for
//        // all manipulations
//        db.beginTransaction();
//        try {
//            final ContentProviderResult[] retResult = super.applyBatch(operations);
//            db.setTransactionSuccessful();
//            getContext().getContentResolver().notifyChange(Contract.CONTENT_URI, null);
//            return retResult;
//        }
//        finally {
//            mIsInBatchMode.remove();
//            db.endTransaction();
//        }
//    }

    private boolean isInBatchMode() {
        return mIsInBatchMode.get() != null && mIsInBatchMode.get();
    }

}
