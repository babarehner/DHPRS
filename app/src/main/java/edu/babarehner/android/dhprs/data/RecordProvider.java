package edu.babarehner.android.dhprs.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import static edu.babarehner.android.dhprs.data.RecordContract.RecordEntry.TDHPRS;
import static edu.babarehner.android.dhprs.data.RecordDbHelper.DB_NAME;


/**
 * Created by mike on 1/18/18.
 */


public class RecordProvider {

    public static final String LOG_TAG = RecordProvider.class.getSimpleName();

    private RecordDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        //sUriMatcher.addURI(...);
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

