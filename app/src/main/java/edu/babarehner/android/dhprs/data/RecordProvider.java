package edu.babarehner.android.dhprs.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.SweepGradient;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static edu.babarehner.android.dhprs.data.RecordContract.CONTENT_AUTHORITY;
import static edu.babarehner.android.dhprs.data.RecordContract.PATH_TRECORDS;


/**
 * Created by mike on 1/18/18.
 */


public class RecordProvider extends ContentProvider {

    public static final String LOG_TAG = RecordProvider.class.getSimpleName();

    private static final int RECORDS = 100;
    private static final int RECORD_ID = 101;

    private RecordDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_TRECORDS, RECORDS);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_TRECORDS + "/#", RECORD_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new RecordDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Create or open a database to write to it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor c;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case RECORDS:
                c = db.query(RecordContract.RecordEntry.TRECORDS, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case RECORD_ID:
                selection = RecordContract.RecordEntry._IDR +"=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                c = db.query(RecordContract.RecordEntry.TRECORDS, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI: " + uri);
        }

        // notify if the data at this URI changes, Then we need to update the cursor listener
        // attached is automatically notified with uri
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECORDS:
                return RecordContract.RecordEntry.CONTENT_LIST_TYPE;
            case RECORD_ID:
                return RecordContract.RecordEntry.CONTENT_ITEM_TYPE;
            default: throw new IllegalStateException("Unknown Uri: " + uri + "with match: " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match((uri));
        switch (match) {
            case RECORDS:
                return insertRecord(uri, values);
            default: throw new IllegalArgumentException("Insertion is not supported for: " + uri);
        }
    }


    // Insert a record into the records table with the given content values. Return the new content uri
    // for that specific row in the database
    public Uri insertRecord(Uri uri, ContentValues values) {
        //TODO
        return uri;
    }



    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

/*

    @Override
    public boolean onCreate(){
        mDbHelper = new RecordDbHelper(getContext());
        return true;
    }


    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int a[] = {0,1};
        Cursor c = a;
        return c;
    }



    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //TODO

        return 0;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //TODO
        return 0;
    }
    */
}

