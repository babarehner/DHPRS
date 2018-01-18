package edu.babarehner.android.dhprs.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;


/**
 * Created by mike on 1/18/18.
 */


public class RecordProvider extends ContentProvider {

    public static final String LOG_TAG = RecordProvider.class.getSimpleName();

    private RecordDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        //sUriMatcher.addURI(...);
    }



    @Override
    public boolean onCreate(){
        mDbHelper = new RecordDbHelper(getContext());
        return true;
    }

    public String getType(Uri uri){
        //TODO
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

    }

    public Uri insert(Uri uri, ContentValues values) {
        //TODO
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //TODO
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //TODO
    }
}

