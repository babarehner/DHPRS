package edu.babarehner.android.dhprs;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.net.Uri;
import java.util.List;

import edu.babarehner.android.dhprs.data.RecordContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // identifier for loader to run on a separate thread
    private static final int RECORD_LOADER = 0;
    RecordCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a floating action button Need to add
        // compile 'com.android.support:design:26.1.0' to build gradle module
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });

        ListView recordsListView = (ListView) findViewById(R.id.list);
        // display the empty view
        View emptyView = findViewById(R.id.empty_view);
        recordsListView.setEmptyView(emptyView);

        mCursorAdapter = new RecordCursorAdapter(this, null);
        recordsListView.setAdapter(mCursorAdapter);
        recordsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                Uri currentMainUri = ContentUris.withAppendedId(
                        RecordContract.RecordEntry.CONTENT_URI, id);
                intent.setData(currentMainUri);
                startActivity(intent);

            }
        });

        getLoaderManager().initLoader(RECORD_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {RecordContract.RecordEntry._IDR,
                RecordContract.RecordEntry.CDATE,
                RecordContract.RecordEntry.CPRAC_TYPE,
                RecordContract.RecordEntry.CPRAC_LEN};

        return new CursorLoader(this,
                RecordContract.RecordEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
