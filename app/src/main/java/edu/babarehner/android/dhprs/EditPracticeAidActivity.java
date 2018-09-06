package edu.babarehner.android.dhprs;

import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;

import edu.babarehner.android.dhprs.data.RecordContract;

public class EditPracticeAidActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mCurrentRecordUri;
    static final int EXISTING_PRACTICE_AID_LOADER = 3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_edit_practice_aid);

        Intent intent = getIntent();
        mCurrentRecordUri = intent.getData();

        if (mCurrentRecordUri == null) {
            // setTitle(getString(R.string.practice_aid_activity_title_add_record));
        } else {
            // setTitle(getString(R.string.practice_aid_activity_edit_record));
            getLoaderManager().initLoader(EXISTING_PRACTICE_AID_LOADER, null, EditPracticeAidActivity.this);
        }
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {RecordContract.RecordEntry._IDPA,
                RecordContract.RecordEntry.CPRAC_AIDS};

        return new CursorLoader(this, mCurrentRecordUri, projection, null, null, null);
    }


    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }


    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
