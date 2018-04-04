package edu.babarehner.android.dhprs;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import edu.babarehner.android.dhprs.data.RecordContract;

/**
 * Created by mike on 1/21/18.
 * This is used for what will be shown in ListView of all the records
 */


public class RecordCursorAdapter extends CursorAdapter {

    public RecordCursorAdapter(Context context, Cursor c)  {super(context, c /* flags*/);}

    /**
     * creates a new blank list item with no data
     * @param context   app context
     * @param c         cursor
     * @param parent    parent to which view is attached to
     * @return          the newly created list item view
     */

    @Override
    public View newView(Context context, Cursor c, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * Binds data to the empty list item
     * @param v View
     * @param context Context
     * @param c Cursor
     */

    @Override
    public void bindView(View v, Context context, Cursor c) {
        // find the id of the views to modify
        TextView dateTextView = (TextView) v.findViewById(R.id.list_item_date);
        TextView prac_typeTextView = (TextView) v.findViewById(R.id.list_item_prac_type);
        TextView prac_lenTextView = (TextView) v.findViewById(R.id.list_item_prac_len);


        int dateColIndex = c.getColumnIndex(RecordContract.RecordEntry.CDATE);
        int prac_typeColIndex = c.getColumnIndex(RecordContract.RecordEntry.CPRAC_TYPE);
        int prac_lenColIndex = c.getColumnIndex(RecordContract.RecordEntry.CPRAC_LEN);

        String date = c.getString(dateColIndex);
        String prac_type = c.getString(prac_typeColIndex);
        String prac_len = c.getString(prac_lenColIndex);

        dateTextView.setText(date);
        prac_typeTextView.setText(prac_type);
        // Adding text to Integer for display purposes only!!!!
        prac_lenTextView.setText(prac_len + " minutes");
    }

}
