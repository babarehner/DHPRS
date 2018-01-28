package edu.babarehner.android.dhprs.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
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
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
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

    public String getType(Uri uri){
        //TODO
        return "Dummy";
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int a[] = {0,1};
        Cursor c = a;
        return c;
    }

    public Uri insert(Uri uri, ContentValues values) {
        //TODO
        return uri;
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

