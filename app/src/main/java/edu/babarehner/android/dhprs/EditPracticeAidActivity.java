package edu.babarehner.android.dhprs;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.babarehner.android.dhprs.data.RecordContract;

public class EditPracticeAidActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = RecordActivity.class.getSimpleName();

    private Uri mCurrentRecordUri;
    static final int EXISTING_PRACTICE_AID_LOADER = 3;

    private String mPracticeAid;
    private boolean mPracticeAidChanged = false;
    private EditText mPracticeAidEditText;

    // Touch listener to check if changes made to a record
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event){
            mPracticeAidChanged = true;
            return false;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_practice_aid);

        Intent intent = getIntent();
        mCurrentRecordUri = intent.getData();

        if (mCurrentRecordUri == null) {
            setTitle(getString(R.string.practice_aid_activity_title_add_record));
        } else {
            setTitle(getString(R.string.practice_aid_activity_edit_record));
            getLoaderManager().initLoader(EXISTING_PRACTICE_AID_LOADER, null, EditPracticeAidActivity.this);
        }

        mPracticeAidEditText = (EditText) findViewById(R.id.edit_practice_aid);

        mPracticeAidEditText.setOnTouchListener(mTouchListener);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {RecordContract.RecordEntry._IDPA,
                RecordContract.RecordEntry.CPRAC_AIDS};

        return new CursorLoader(this, mCurrentRecordUri, projection, null, null, null);
    }


    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor c) {
        // move to the only row in the cursor
        if (c.moveToFirst()){
            int practiceAidColIndex = c.getColumnIndex(RecordContract.RecordEntry.CPRAC_AIDS);
            // use the index to pull the data out
            mPracticeAid = c.getString(practiceAidColIndex);
            // update the text view
            mPracticeAidEditText.setText(mPracticeAid);
        }

    }


    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // if invalid loader clear data from input field
        mPracticeAidEditText.setText("");
    }


    // Options menu automatially called from onCreate I believe
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_practice_aid, menu);

        return true;
    }

    // Select from the options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem = item.getItemId();
        switch (menuItem) {
            case R.id.action_save_practice_aid:
                saveRecord();
                finish();       // exit activity
                return true;
            case R.id.action_delete_practice_aid:
                // Alert Dialog for deleting one record
                showDeleteConfirmationDialog();
                return true;
            // this is the <- button on the toolbar
            case android.R.id.home:
                // record has not changed
                if (!mPracticeAidChanged) {
                    NavUtils.navigateUpFromSameTask(EditPracticeAidActivity.this);
                    return true;
                }
                // set up dialog to warn user about unsaved changes
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                //user click discard. Navigate up to parent activity
                                NavUtils.navigateUpFromSameTask(EditPracticeAidActivity.this);
                            }
                        };
                // show user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveRecord() {

        String pracAid =  mPracticeAidEditText.getText().toString().trim();

        // if the date field is left blank do nothing
        if (mCurrentRecordUri == null & TextUtils.isEmpty(pracAid)) {
            Toast.makeText(this, getString(R.string.missing_practice_aid), Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(RecordContract.RecordEntry.CPRAC_AIDS, pracAid);

        if (mCurrentRecordUri == null) {
            // a new record
            Log.v(LOG_TAG, "in saveRecord " + RecordContract.RecordEntry.PRACAID_URI.toString() + "\n" + values);
            Uri newUri = getContentResolver().insert(RecordContract.RecordEntry.PRACAID_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.record_provider_insert_record_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.record_provider_insert_record_good),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // existing record so update with content URI and pass in ContentValues
            int rowsAffected = getContentResolver().update(mCurrentRecordUri, values, null, null);
            if (rowsAffected == 0) {
                // TODO Check db- Text Not Null does not seem to be working or entering
                // "" does not mean NOT Null- there must be an error message closer to the db!!!
                Toast.makeText(this, getString(R.string.edit_update_record_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.edit_update_record_success),
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

    // hide delete menu item when adding a new record
    @Override
    public boolean onPrepareOptionsMenu(Menu m) {
        super.onPrepareOptionsMenu(m);
        // if this is add a record, hide "delete" menu item
        if (mCurrentRecordUri == null) {
            MenuItem deleteItem = m.findItem(R.id.action_delete_practice_aid);
            deleteItem.setVisible(false);
        }

        return true;
    }

    // delete record from db
    private void deleteRecord(){
        if (mCurrentRecordUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentRecordUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.delete_record_failure),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_record_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    // Override the activity's normal back button. If record has changed create a
    // discard click listener that closed current activity.
    @Override
    public void onBackPressed() {
        if (!mPracticeAidChanged) {
            super.onBackPressed();
            return;
        }
        //otherwise if there are unsaved changes setup a dialog to warn the  user
        //handles the user confirming that changes should be made
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // user clicked "Discard" button, close the current activity
                        finish();
                    }
                };

        // show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showDeleteConfirmationDialog() {
        // Create and AlertDialog.Builder, set message and click
        // listeners for positive and negative buttons
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked delete so delete
                deleteRecord();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked cancel, dismiss dialog, continue editing
                if (dialog != null) {dialog.dismiss();}
            }
        });
        // Create and show dialog
        AlertDialog alertD = builder.create();
        alertD.show();
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener){
        // Create AlertDialogue.Builder amd set message and click listeners
        // for positive and negative buttons in dialogue.
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // user clicked the "keep eiditing" button. Dismiss dialog and keep editing
                if (dialog !=null) { dialog.dismiss();}
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
