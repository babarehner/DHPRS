package edu.babarehner.android.dhprs;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.babarehner.android.dhprs.data.RecordContract;

public class PracticeAidCursorAdapter extends CursorAdapter {

    public PracticeAidCursorAdapter(Context context, Cursor c)  {super(context, c /* flags*/);}

    /**
     * creates a new blank list item with no data
     * @param context   app context
     * @param c         cursor
     * @param parent    parent to which view is attached to
     * @return          the newly created list item view
     */

    @Override
    public View newView(Context context, Cursor c, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.practice_aid_list_item, parent, false);
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
        TextView prac_aidTextView = (TextView) v.findViewById(R.id.list_item_prac_aid);

        int prac_aidColIndex = c.getColumnIndex(RecordContract.RecordEntry.CPRAC_AIDS);

        String prac_aid = c.getString(prac_aidColIndex);

        prac_aidTextView.setText(prac_aid);

    }

}
