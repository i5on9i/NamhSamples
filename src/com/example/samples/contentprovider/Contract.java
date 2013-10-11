package com.example.samples.contentprovider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Constants for the Note table of provider
 */
public final class Contract {

    /**
     * The authority of the provider.
     *
     * @Notice
     * You must add provider on {@ref: AndroidManifest.xml} with same AUTHORITY.
     */
    public static final String AUTHORITY =
            "com.contentprovider";
    /**
     * The content URI for the top-level provider authority.
     */
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    /**
     * Constants for the Note TABLE of the provider.
     */
    public static final class Note implements CommonColumns {
        /**
         * The content URI for this table.
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(Contract.CONTENT_URI, "items");
        /**
         * The mime type of a directory of items.
         */
        public static final String CONTENT_TYPE =ContentResolver.CURSOR_DIR_BASE_TYPE + "/notes_items";
        /**
         * The mime type of a single item.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/notes_items";

        /**
         * The data columns of this download_id.
         */
        public static final String DATE = "date";
        public static final String NAME = "name";
        public static final String NOTE = "note";

        /**
         * A projection of all columns
         * in the items table.
         */
        public static final String[] PROJECTION_ALL ={ NAME, DATE, NOTE};
        /**
         * The default sort order for
         * queries containing NAME fields.
         */
        public static final String SORT_ORDER_DEFAULT = "rowid ASC";
    }


    public static final class DownloadIds implements CommonColumns {
        /**
         * The Content URI for this table.
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(Contract.CONTENT_URI, "download_ids");
        /**
         * The mime type of a directory of download_ids
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/download_ids";
        /**
         * The mime type of a single download_id.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/download_ids";
        /**
         * The data columns of this download_id.
         */
        public static final String FILENAME = "filename";
        public static final String DOWNLOAD_ID = "download";
        /**
         * A projection of all columns in the download_id table.
         */
        public static final String[] PROJECTION_ALL = {FILENAME, DOWNLOAD_ID};

        public static final String SORT_ORDER_DEFAULT = FILENAME + " ASC";
    }


    /**
     * This interface defines common columns
     * found in multiple tables.
     */
    public static interface CommonColumns
            extends BaseColumns {

    }
}