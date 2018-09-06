package edu.babarehner.android.dhprs;

import android.app.Activity;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import edu.babarehner.android.dhprs.data.RecordContract;

public class PracticeAidActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // identifier for loader to run on separate thread
    private static final int PRACTICE_AID_LOADER = 7;
    PracticeAidCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_aid);

        setTitle("Practice Aids");

        // Create a floating action button Need to add
        // compile 'com.android.support:design:26.1.0' to build gradle module
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.pa_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PracticeAidActivity.this, EditPracticeAidActivity.class);
                startActivity(intent);
            }
        });


        ListView practiceAidListView = (ListView) findViewById(R.id.practice_aid_list);
        // display the empty view
        View emptyView = findViewById(R.id.practice_aid_empty_view);
        practiceAidListView.setEmptyView(emptyView);

        mCursorAdapter = new PracticeAidCursorAdapter(this, null);
        practiceAidListView.setAdapter(mCursorAdapter);
        practiceAidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                Intent intent = new Intent(PracticeAidActivity.this, EditPracticeAidActivity.class);
                Uri currentPracticeAidUri = ContentUris.withAppendedId(
                        RecordContract.RecordEntry.PRACAID_URI, id);
                intent.setData(currentPracticeAidUri);
                startActivity(intent);

            }
        });


        getLoaderManager().initLoader(PRACTICE_AID_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Define a projections with the columns we are interested in
        // CursorAdapter requires column title of "_Id"
        String[] projection = {RecordContract.RecordEntry._IDPA,
                RecordContract.RecordEntry.CPRAC_AIDS};

        // This loader will execute the ContentProvider's query  method on a background thread
        return new CursorLoader(this,                           // parent activity context
                RecordContract.RecordEntry.PRACAID_URI,       // ProviderContent URI
                projection,
                null,
                null,
                RecordContract.RecordEntry.CPRAC_AIDS + " ASC");
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // update(@link LibraryCursorAdapter) with this new cursor containing update Libary data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted- use null
        mCursorAdapter.swapCursor(null);
    }

}
