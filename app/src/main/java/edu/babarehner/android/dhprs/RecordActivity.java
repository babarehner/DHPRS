package edu.babarehner.android.dhprs;

import android.app.LoaderManager;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by mike on 1/18/18.
 */

public class RecordActivity extends AppCompatActivity {

    //TODO implements LoaderManager.LoaderCallbacks<Cursor>

    public static final Integer[] RATINGS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    // populate the spinner automatically instead of using arrays.xml
    private void setUpSpinner() {

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, RATINGS);
        // Specify the layout to use when the list of choices appear
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter); // apply the adapter to the spinner
    }

}
