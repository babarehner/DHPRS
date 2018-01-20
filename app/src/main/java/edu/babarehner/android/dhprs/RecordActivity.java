package edu.babarehner.android.dhprs;

import android.app.LoaderManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by mike on 1/18/18.
 */

public class RecordActivity extends AppCompatActivity {

    //TODO implements LoaderManager.LoaderCallbacks<Cursor>

    public static final Integer[] RATINGS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    public static final CharSequence[] PRACTYPE ={"Other", "Phone App", "Recording"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        setTitle("Add Records");

        setUpSpinners();

    }

    // TODO wire up spinners to show up data
    // populate the spinner automatically instead of using arrays.xml
    private void setUpSpinners() {

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, RATINGS);
        // Specify the layout to use when the list of choices appear
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter); // apply the adapter to the spinner

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, RATINGS);
        // Specify the layout to use when the list of choices appear
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter); // apply the adapter to the spinner

        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<Integer> adapter3 = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, RATINGS);
        // Specify the layout to use when the list of choices appear
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter); // apply the adapter to the spinner

        Spinner spinner4 = (Spinner) findViewById(R.id.spinner4);
        ArrayAdapter<Integer> adapter4 = new ArrayAdapter<Integer>(this,
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

}
