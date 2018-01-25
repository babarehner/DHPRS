package edu.babarehner.android.dhprs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;

import org.w3c.dom.Text;

import java.util.Calendar;

import edu.babarehner.android.dhprs.data.RecordContract;

/**
 * Created by mike on 1/18/18.
 */

//TODO save the data to the database
public class RecordActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // rating integers & strings to populate the spinners
    public static final CharSequence[] RATINGS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    public static final CharSequence[] PRAC_TYPE = {"Paper", "Phone App", "Recording"};
    
    private Uri mCurrentRecordsFileUri = null;
    private Uri mCurrentRecordUri;
    private TextView tvDate;
    private TextView tvTime;
    private Button pickDate, pickTime, butEnter ;
    private int mYear, mMonth, mDay, mHour, mMinute;

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;
    static final int EXISTING_RECORD_LOADER = 2;

    private Spinner sp1,sp2, spPracType, sp3,sp4; // the spinner
    //private String mSpPractType_val = "", mSp1_val = "", mSp2_val = "", mSp3_val = "", mSp4_val = ""; // the values from the spinner
    private String[] spin_val = {"", "", "", "", ""  }; // Array of values
    private EditText mCommentEditText;
    
    private boolean mRecordChanged = false; // When edit change made ot record row
    
    // Touch listener to check if changes made to a record
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event){
            mRecordChanged = true;
            return false;
        }
    };

    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // initialization required or it crashes- why doesn't it work when I initialize it above????
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);

        // get intent and get data from intent
        Intent intent = getIntent();
        mCurrentRecordUri = intent.getData();
        
        // if the intent does not contain a single item URI FAB has been cicked
        if (mCurrentRecordUri == null) {
            // set pager header to add record
            setTitle(getString(R.string.record_activity_title_add_record));
        } else {
            setTitle(getString(R.string.record_activity_title_edit_record));
            getLoaderManager().initLoader(EXISTING_RECORD_LOADER, null, RecordActivity.this);
        }
          
        // Find all input views to read from
        sp1 = (Spinner) findViewById(R.id.sp_1);
        sp2 = (Spinner) findViewById(R.id.sp_2);
        spPracType = (Spinner) findViewById(R.id.sp_pracType);
        sp3 = (Spinner) findViewById(R.id.sp_3);
        sp4 = (Spinner) findViewById(R.id.sp_4);
        mCommentEditText = (EditText) findViewById(R.id.comment);
        
        // Set up Touch listener on all the input fields to see if user touched a field
        tvDate.setOnTouchListener(mTouchListener);
        tvTime.setOnTouchListener(mTouchListener);
        sp1.setOnTouchListener(mTouchListener);
        sp2.setOnTouchListener(mTouchListener);
        spPracType.setOnTouchListener(mTouchListener);
        sp3.setOnTouchListener(mTouchListener);
        sp4.setOnTouchListener(mTouchListener);
        mCommentEditText.setOnTouchListener(mTouchListener);
        
        // Loads the spinners with strings from an array
        sp1 = getSpinnerVal(R.id.sp_1, RATINGS, 0);
        sp2 = getSpinnerVal(R.id.sp_2, RATINGS, 1);
        spPracType = getSpinnerVal(R.id.sp_pracType, PRAC_TYPE, 2);
        sp3 = getSpinnerVal(R.id.sp_3, RATINGS, 3);
        sp4 = getSpinnerVal(R.id.sp_4, RATINGS, 4);

        // setUpSpinners();
        
        //TODO these need to get moved into methods
        getDate();
        getTime();
    }


    // overide this abstract method
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // define a projection that contains all columns in TBooks
        String[] projection = {RecordContract.RecordEntry._IDD,
                RecordContract.RecordEntry.CDATE,
                RecordContract.RecordEntry.CTIME,
                RecordContract.RecordEntry.CSYMP_BEFORE,
                RecordContract.RecordEntry.CSTRESS_BEFORE,
                RecordContract.RecordEntry.CPRAC_TYPE,
                RecordContract.RecordEntry.CSTRESS_AFTER,
                RecordContract.RecordEntry.CSYMP_AFTER,
                RecordContract.RecordEntry.CCOMMENTS};

        // Start a new thread
        return new CursorLoader(this, mCurrentRecordUri, projection, null, null, null);
    }


    // override this abstarct method
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        // move to the only row in the Cursor
        if (c.moveToFirst()){
            // get the index of each column
            int dateColIndex=c.getColumnIndex(RecordContract.RecordEntry.CDATE);
            int timeColIndex=c.getColumnIndex(RecordContract.RecordEntry.CTIME);
            int symp_beforeColIndex=c.getColumnIndex(RecordContract.RecordEntry.CSYMP_BEFORE);
            int stress_beforeColIndex=c.getColumnIndex(RecordContract.RecordEntry.CSTRESS_BEFORE);
            int pract_typeColIndex = c.getColumnIndex(RecordContract.RecordEntry.CPRAC_TYPE);
            int symp_afterColIndex = c.getColumnIndex(RecordContract.RecordEntry.CSYMP_AFTER);
            int stress_afterColIndex = c.getColumnIndex(RecordContract.RecordEntry.CSTRESS_AFTER);
            int commentColIndex = c.getColumnIndex(RecordContract.RecordEntry.CCOMMENTS);

            // use the index to pull the data out
            String date=c.getString(dateColIndex);
            String time=c.getString(timeColIndex);
            int symp_before = c.getInt(symp_beforeColIndex);
            int stress_before = c.getInt(stress_beforeColIndex);
            int prac_type = c.getInt(pract_typeColIndex);
            int symp_after = c.getInt(symp_afterColIndex);
            int stress_after = c.getInt(stress_afterColIndex);
            int comment = c.getInt(commentColIndex);

            //Update the text views
            tvDate.setText(date);
            tvTime.setText(time);
            // Get the position of the rating from the spinner
            sp1.setSelection(symp_before);
            sp2.setSelection(stress_before);
            spPracType.setSelection(prac_type);
            sp3.setSelection(symp_before);
            sp4.setSelection(stress_after);
            mCommentEditText.setText(comment);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If invalid Loader clear data from input field
        tvDate.setText("");
        tvTime.setText("");
        sp1.setSelection(0);
        sp2.setSelection(0);
        spPracType.setSelection(0);
        sp3.setSelection(0);
        sp4.setSelection(0);
        mCommentEditText.setText("");
    }
    
    
    // get up date picker
    public void getDate() {

        tvDate = findViewById(R.id.tvDate);
        pickDate = findViewById(R.id.pick_date);

        // add a click listener to the button
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        //get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }



    //updates the date displayed in TextView
    private void updateDate() {
         tvDate.setText(
         new StringBuilder()
                 .append((mMonth + 1)).append("/")
                 .append(mDay).append("/")
                 .append(mYear).append(" "));
    }

    // the callback received when the user sets the date in the dialog
    private DatePickerDialog.OnDateSetListener DateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mYear = year;
                    mMonth = month;
                    mDay = dayOfMonth;
                    updateDate();
                }
            };


    public void getTime() {
        // get View elements
        tvTime = (TextView) findViewById(R.id.tvTime);
        pickTime = (Button) findViewById(R.id.pick_time);
        // add a click listener to the button
        pickTime.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        }));

        // get the current time
        final Calendar t = Calendar.getInstance();
        mHour = t.get(Calendar.HOUR_OF_DAY);
        mMinute = t.get(Calendar.MINUTE);

    }

    private void updateTime() {
        tvTime.setText(new StringBuilder().append(pad(mHour)).append(":").append(pad(mMinute)));
    }

    // the callback received when the user sets the time in the dialog
    private TimePickerDialog.OnTimeSetListener TimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mHour = hourOfDay;
                    mMinute = minute;
                    updateTime();
                }
            };



    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, DateSetListener, mYear, mMonth, mDay);
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, TimeSetListener, mHour, mMinute, false);
        }
        return null;
    }


    // puts a 0 in front of a single digit time number
    private static String pad(int c) {
        if (c >= 10) {
            return String.valueOf(c);
        }
        else {
            return "0" + String.valueOf(c);
        }
    }


    // need to clean this up and change/verifiy how/where I get data out after I can easily verify data
    private Spinner getSpinnerVal(int resourceID, final CharSequence[] a, final int i) {
        Log.v("RecordActivity", Integer.toString(resourceID));
        Spinner s = (Spinner)  findViewById(resourceID);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, a);
        // Specify the layout to use when the list of choices appear
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter); // apply the adapter to the spinner
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                Log.v("RecordActivity", selection);
                spin_val[i] = selection;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spin_val[0] = a[0].toString();
            }
        });
        return s;
    }


    private void saveRecord() {
        // read from input fields
        String dateString = tvDate.getText().toString();
        String timeString = tvTime.getText().toString();
        String symptomBeforeRating = spin_val[0];
        String stressBeforeRating = spin_val[1];
        String pracType = spin_val[2];
        String symptomAfterRating = spin_val[3];
        String stressAfterRating = spin_val[4];
        String comment = mCommentEditText.getText().toString().trim();

        // if the date field is left blank do nothing
        if (mCurrentRecordUri == null & TextUtils.isEmpty(dateString)) {
            Toast.makeText(this, getString(R.string.missing_date), Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(RecordContract.RecordEntry.CDATE, dateString);
        values.put(RecordContract.RecordEntry.CTIME, timeString);
        values.put(RecordContract.RecordEntry.CSYMP_BEFORE, symptomBeforeRating);
        values.put(RecordContract.RecordEntry.CSTRESS_BEFORE, stressBeforeRating);
        values.put(RecordContract.RecordEntry.CPRAC_TYPE, pracType);
        values.put(RecordContract.RecordEntry.CSYMP_AFTER, symptomAfterRating);
        values.put(RecordContract.RecordEntry.CSTRESS_AFTER, stressAfterRating);
        values.put(RecordContract.RecordEntry.CCOMMENTS, comment);

        if (mCurrentRecordUri == null) {
            // a new record
            Uri newUri = getContentResolver().insert(RecordContract.RecordEntry.CONTENT_URI, values);
        }
    }
}
