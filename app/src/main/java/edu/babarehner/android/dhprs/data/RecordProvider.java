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
import android.util.Log;

import edu.babarehner.android.dhprs.R;

import static edu.babarehner.android.dhprs.data.RecordContract.CONTENT_AUTHORITY;
import static edu.babarehner.android.dhprs.data.RecordContract.PATH_TPRAC_AIDS;
import static edu.babarehner.android.dhprs.data.RecordContract.PATH_TRECORDS;


/**
 * Created by mike on 1/18/18.
 */


public class RecordProvider extends ContentProvider {

    public static final String LOG_TAG = RecordProvider.class.getSimpleName();

    private static final int RECORDS = 100;
    private static final int RECORD_ID = 101;

    private static final int PRACTICEAIDS = 200;
    private static final int PRACTICEAIDS_ID = 205;

    private RecordDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_TRECORDS, RECORDS);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_TRECORDS + "/#", RECORD_ID);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_TPRAC_AIDS, PRACTICEAIDS);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_TPRAC_AIDS + "/#", PRACTICEAIDS_ID);
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
            case PRACTICEAIDS:
                c = db.query(RecordContract.RecordEntry.TPRAC_AIDS, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case PRACTICEAIDS_ID:
                selection = RecordContract.RecordEntry._IDPA + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                c = db.query(RecordContract.RecordEntry.TPRAC_AIDS, projection, selection, selectionArgs,
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
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match((uri));
        switch (match) {
            case RECORDS:
                return insertRecord(uri, values);
            case PRACTICEAIDS:
                return insertPracticeAid(uri, values);
            default: throw new IllegalArgumentException("Insertion is not supported for: " + uri);
        }
    }


    // Insert a record into the records table with the given content values. Return the new content uri
    // for that specific row in the database
    public Uri insertRecord(Uri uri, ContentValues values) {
        // Check the the practice type is not null
        String prac_type = values.getAsString(RecordContract.RecordEntry.CPRAC_TYPE);
        if (prac_type == null){
            throw new IllegalArgumentException("Practice Type required to insert new Record");
        }

        String recDate = values.getAsString(RecordContract.RecordEntry.CDATE);
        String recTime = values.getAsString(RecordContract.RecordEntry.CTIME);
        String recSympBefore = values.getAsString(RecordContract.RecordEntry.CSYMP_BEFORE);
        String recStressBefore = values.getAsString(RecordContract.RecordEntry.CSTRESS_BEFORE);
        String recPracType = values.getAsString(RecordContract.RecordEntry.CPRAC_TYPE);
        String recPracAid = values.getAsString(RecordContract.RecordEntry.CPRAC_AID);
        String recSympAfter = values.getAsString(RecordContract.RecordEntry.CSYMP_AFTER);
        String recSressAfter = values.getAsString(RecordContract.RecordEntry.CSTRESS_AFTER);
        String recPracLen = values.getAsString(RecordContract.RecordEntry.CPRAC_LEN);
        String recComment = values.getAsString(RecordContract.RecordEntry.CCOMMENTS);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(RecordContract.RecordEntry.TRECORDS, null, values);
        Log.v(LOG_TAG, "Record not entered");
        if (id == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // notify all listeners that the data has changed for the TRECORDS table
        getContext().getContentResolver().notifyChange(uri, null);
        // return the new Uri with the ID of the newly inserted row appended to the db
        return ContentUris.withAppendedId(uri, id);
    }


    // Insert a record into the records table with the given content values. Return the new content uri
    // for that specific row in the database
    public Uri insertPracticeAid(Uri uri, ContentValues values) {
        // Check the the practice type is not null
        String prac_aid = values.getAsString(RecordContract.RecordEntry.CPRAC_AIDS);
        if (prac_aid == null){
            throw new IllegalArgumentException("Practice Aid entry required to insert new Record");
        }

        String recPracAid = values.getAsString(RecordContract.RecordEntry.CPRAC_AIDS);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(RecordContract.RecordEntry.TPRAC_AIDS, null, values);
        Log.v(LOG_TAG, "Practice Aid not entered");
        if (id == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // notify all listeners that the data has changed for the TRECORDS table
        getContext().getContentResolver().notifyChange(uri, null);
        // return the new Uri with the ID of the newly inserted row appended to the db
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case RECORDS:
                return updateRecords(uri, values, selection, selectionArgs);
            case RECORD_ID:
                selection = RecordContract.RecordEntry._IDR + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateRecords(uri, values, selection, selectionArgs);
            case PRACTICEAIDS:
                return updatePracticeAids(uri, values, selection, selectionArgs);
            case PRACTICEAIDS_ID:
                selection = RecordContract.RecordEntry._IDPA + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updatePracticeAids(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for: " + uri);
        }
    }


    private int updateRecords(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        // if there are no values quit
        if (values.size() == 0) {return 0;}

        // check that the practice type is not empty
        if (values.containsKey(RecordContract.RecordEntry.CPRAC_TYPE)) {
            String prac_name = values.getAsString(RecordContract.RecordEntry.CPRAC_TYPE);
            // check again
            if (prac_name == null) {
                throw new IllegalArgumentException("Exercise requires a practice type- in updateRecords");
            }
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rows_updated = db.update(RecordContract.RecordEntry.TRECORDS, values, selection, selectionArgs);
        if (rows_updated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows_updated;
    }


    private int updatePracticeAids(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        // if there are no values quit
        if (values.size() == 0) {return 0;}

        // check that the practice aids field is not empty
        if (values.containsKey(RecordContract.RecordEntry.CPRAC_AIDS)) {
            String prac_aid = values.getAsString(RecordContract.RecordEntry.CPRAC_AIDS);
            // check again
            if (prac_aid == null) {
                throw new IllegalArgumentException("Update requires a practice aid- in updateRecords");
            }
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rows_updated = db.update(RecordContract.RecordEntry.TPRAC_AIDS, values, selection, selectionArgs);
        if (rows_updated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows_updated;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECORD_ID:
                selection = RecordContract.RecordEntry._IDR + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(RecordContract.RecordEntry.TRECORDS, selection, selectionArgs);
                break;
            case PRACTICEAIDS_ID:
                selection = RecordContract.RecordEntry._IDPA + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(RecordContract.RecordEntry.TPRAC_AIDS, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for: " + uri);
        }

        if (rowsDeleted != 0) {
            // Notify all listeners that the db has changed
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
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
            case PRACTICEAIDS:
                return RecordContract.RecordEntry.PRACAID_LIST_TYPE;
            case PRACTICEAIDS_ID:
                return RecordContract.RecordEntry.PRACAID_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown Uri: " + uri + "with match: " + match);
        }
    }
}

