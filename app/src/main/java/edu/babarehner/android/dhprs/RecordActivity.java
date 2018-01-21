package edu.babarehner.android.dhprs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by mike on 1/18/18.
 */


public class RecordActivity extends AppCompatActivity {

    //TODO implements LoaderManager.LoaderCallbacks<Cursor>

    public static final Integer[] RATINGS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    public static final CharSequence[] PRACTYPE ={"Other", "Phone App", "Recording"};
    private TextView tvDate, tvTime;
    private Button pickDate, pickTime;
    int mYear, mMonth, mDay, mHour, mMinute;

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;

    //TODO wire up time button
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        setTitle("Add Records");

        setUpSpinners();

        getDate();
        getTime();



    }

    // get up date picker
    public void getDate() {

        tvDate = (TextView) findViewById(R.id.tvDate);
        pickDate = (Button) findViewById(R.id.pick_date);

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
    private void updateDisplay() {
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
                    updateDisplay();
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

        // display the current time
        updateTime();
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

    // populate the spinner automatically instead of using arrays.xml
    private void setUpSpinners() {

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, RATINGS);
        // Specify the layout to use when the list of choices appear
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter); // apply the adapter to the spinner

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, RATINGS);
        // Specify the layout to use when the list of choices appear
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter); // apply the adapter to the spinner

        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<Integer> adapter3 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, RATINGS);
        // Specify the layout to use when the list of choices appear
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter); // apply the adapter to the spinner

        Spinner spinner4 = (Spinner) findViewById(R.id.spinner4);
        ArrayAdapter<Integer> adapter4 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, RATINGS);
        // Specify the layout to use when the list of choices appear
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter); // apply the adapter to the spinner

        Spinner spinnerPracType = (Spinner) findViewById(R.id.prac_type);
        ArrayAdapter<CharSequence> adapter_pracType = ArrayAdapter.createFromResource(this,
                R.array.spinner_practype, android.R.layout.simple_spinner_item);
        adapter_pracType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPracType.setAdapter(adapter_pracType);

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

}
