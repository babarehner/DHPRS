package edu.babarehner.android.dhprs;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.widget.CursorAdapter;
import edu.babarehner.android.dhprs.data.RecordContract;

/**
 * Created by mike on 1/21/18.
 * This is used for what will be shown in ListView of all the records
 */

/*
public class RecordCursorAdapter extends CursorAdapter {

    public RecordCursorAdapter(Context context, Cursor c)  {super(context, c /* flags*///);}

    /**
     * creates a new blank list item with no data
     * @param context   app context
     * @param c         cursor
     * @param parent    parent to which view is attached to
     * @return          the newly created list item view



    @Override
    public View newView(Context context, Cursor c, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * Binds data to the empty lsit item
     * @param v
     * @param context
     * @param c

    @Override
    public void bindView(View v, Context context, Cursor c) {
        // find the id of the views to modify
        TextView titleTextView = (TextView) v.findViewById(R.id.title);
        TextView publishYearTextView = (TextView) v.findViewById(R.id.publish_year);

        int titleColIndex = c.getColumnIndex(RecordContract.RecordEntry.COL_TITLE);
        int publishYearColIndex = c.getColumnIndex(RecordContract.RecordEntry.COL_YEAR_PUBLISHED);

        String title = c.getString(titleColIndex);
        String publish_year = c.getString(publishYearColIndex);

        titleTextView.setText(title);
        publishYearTextView.setText(publish_year);
    }

}
*/