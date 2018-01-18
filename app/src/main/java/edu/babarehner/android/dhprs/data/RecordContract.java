package edu.babarehner.android.dhprs.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mike on 1/18/18.
 */

public final class RecordContract {

    // to prevent someone from accidentally instantiating the contract class
    // give it an empty constructor
    private RecordContract() {}

    /**
     * The Content Authority is a name for the entire content provider, similar to the
     * relationship between a domain name and its website. A convenient string to use for the
     * content authority is the package name for the app which is guaranteed to be unique on the device
     */
    public static final String CONTENT_AUTHORITY = "com.babarehner.record.dahprs";

    /**
     * Use CONTENT AUTHORITY to create the base of all URI's which apps will use to contact the
     * content provider. <parse().changes Str to URI
     */

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_TDHPRS = "DHPRS";
    public static final String PATH_TBP = "BP";

    /**
     * Inner class that defines constant values foe the record sheet database
     * Defines global table and column names
     */
    public static final class RecordEntry implements BaseColumns {

        // The MIME type of the (@Link #CONTENT_URI for the library database table
        // TODO implement content uri's


        // name of db table for Daily Home Practice Recording Sheet
        public final static String TDHPRS = "TDhprs";
        // Primary key to be autoincremaented. Required name for some Android APIs
        // Column names in TDHPRS
        public static final String _IDD = BaseColumns._ID;
        public static final String CDATE = "CDate";
        public static final String CTIME = "CTime";
        public static final String CSYMP_BEFORE = "CSympBefore";
        public static final String CSTRESS_BEFORE = "CStressBefore";
        public static final String CPRAC_TYPE = "CPracType";
        public static final String CPRAC_AID = "CPracAid";
        public static final String CSYMP_AFTER = "CSympAfter";
        public static final String CSTRESS_AFTER = "CStressAfter";
        public static final String CCOMMENTS = "CComments";

        // name of db table to hold blood pressure readings
        public static final String TBP = "TBP";
        // Column names in TBP
        public static final String _IDB = BaseColumns._ID;

    }





}
