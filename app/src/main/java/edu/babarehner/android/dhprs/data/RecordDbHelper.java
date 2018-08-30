package edu.babarehner.android.dhprs.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static edu.babarehner.android.dhprs.data.RecordContract.RecordEntry.TPRAC_AIDS;
import static edu.babarehner.android.dhprs.data.RecordContract.RecordEntry.TRECORDS;
import static edu.babarehner.android.dhprs.data.RecordContract.RecordEntry._IDPA;
import static edu.babarehner.android.dhprs.data.RecordContract.RecordEntry._IDR;

/**
 * Created by mike on 1/18/18.
 */


public class RecordDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = RecordDbHelper.class.getSimpleName();

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "RecSheet.db";

    // Create the db
    public RecordDbHelper(Context context) {super(context, DB_NAME, null, DB_VERSION); }

    /** provided by Android Studio- left it in for now
     * public RecordDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
     * super(context, name, factory, version);}
     **/

    // SQL statements to create the tables
    @Override
    public void onCreate(SQLiteDatabase db){
        String SQL_CREATE_DHPRS_TABLE = "CREATE TABLE " + TRECORDS + "("
                + _IDR + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RecordContract.RecordEntry.CDATE + " DATE_NOT_NULL, "
                + RecordContract.RecordEntry.CTIME + " TEXT_NOT_NULL, "
                + RecordContract.RecordEntry.CSYMP_BEFORE + " INTEGER, "
                + RecordContract.RecordEntry.CSTRESS_BEFORE + " INTEGER, "
                + RecordContract.RecordEntry.CPRAC_TYPE + " TEXT, "
                + RecordContract.RecordEntry.CPRAC_AID + " TEXT, "
                + RecordContract.RecordEntry.CSYMP_AFTER + " INTEGER, "
                + RecordContract.RecordEntry.CSTRESS_AFTER + " INTEGER,"
                + RecordContract.RecordEntry.CPRAC_LEN + " INTEGER, "
                + RecordContract.RecordEntry.CCOMMENTS + " TEXT);";

        db.execSQL(SQL_CREATE_DHPRS_TABLE);

        String SQL_CREATE_PRAC_AIDS_TABLE = "CREATE TABLE " + TPRAC_AIDS + "("
                + _IDPA +  " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RecordContract.RecordEntry.CPRAC_AIDS + " TEXT);";

        db.execSQL(SQL_CREATE_PRAC_AIDS_TABLE);


        // Load the Practice Aids Table with values the first time the db is created
        String[] practiceAids = { "None", "Paper", "Phone App", "Recording", "Thermistor" };
        for (String each : practiceAids){
            db.execSQL("INSERT INTO " + RecordContract.RecordEntry.TPRAC_AIDS
                    + " ( " + RecordContract.RecordEntry.CPRAC_AIDS + " ) "
                    + " VALUES( "
                    +   "'" + each + "'" +  "," + " );");
        }

    }

    // told this was required
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}

